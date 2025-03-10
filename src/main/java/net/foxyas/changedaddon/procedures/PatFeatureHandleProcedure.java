package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.entity.Exp2FemaleEntity;
import net.foxyas.changedaddon.entity.Exp2MaleEntity;
import net.foxyas.changedaddon.entity.Experiment10Entity;
import net.foxyas.changedaddon.entity.KetExperiment009Entity;
import net.foxyas.changedaddon.init.ChangedAddonModMobEffects;
import net.foxyas.changedaddon.network.ChangedAddonModVariables;
import net.foxyas.changedaddon.variants.AddonLatexVariant;
import net.ltxprogrammer.changed.entity.LatexEntity;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Iterator;

public class PatFeatureHandleProcedure {
	//Thanks gengyoubo for the code
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null) return;

		Entity targetEntity = getEntityLookingAt(entity, 4);
		if (targetEntity == null) return;

		if (isInSpectatorMode(entity)) return;

		if (targetEntity instanceof Experiment10Entity || targetEntity instanceof KetExperiment009Entity) {
			handleSpecialEntities(entity, targetEntity);
		} else if (targetEntity instanceof LatexEntity) {
			handleLatexEntity(entity, targetEntity, world);
		} else if (targetEntity instanceof Player) {
			handlePlayerEntity(entity, (Player) targetEntity, world);
		} else if (targetEntity.getType().is(TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation("changed_addon:patable_entitys")))) {
			handlePatableEntity(entity, targetEntity, world);
		}
	}

	private static Entity getEntityLookingAt(Entity entity, double reach) {
		double distance = reach * reach;
		Vec3 eyePos = entity.getEyePosition(1.0f);
		HitResult hitResult = entity.pick(reach, 1.0f, false);

		if (hitResult != null && hitResult.getType() != HitResult.Type.MISS) {
			distance = hitResult.getLocation().distanceToSqr(eyePos);
		}

		Vec3 viewVec = entity.getViewVector(1.0F);
		Vec3 toVec = eyePos.add(viewVec.x * reach, viewVec.y * reach, viewVec.z * reach);
		AABB aabb = entity.getBoundingBox().expandTowards(viewVec.scale(reach)).inflate(1.0D, 1.0D, 1.0D);

		EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(entity, eyePos, toVec, aabb, e -> !e.isSpectator(), distance);

		if (entityHitResult != null) {
			Entity hitEntity = entityHitResult.getEntity();
			if (eyePos.distanceToSqr(entityHitResult.getLocation()) <= reach * reach) {
				return hitEntity;
			}
		}
		return null;
	}

	private static boolean isInSpectatorMode(Entity entity) {
		if (entity instanceof ServerPlayer serverPlayer) {
			return serverPlayer.gameMode.getGameModeForPlayer() == GameType.SPECTATOR;
		} else if (entity.level.isClientSide() && entity instanceof Player player) {
			return Minecraft.getInstance().getConnection().getPlayerInfo(player.getGameProfile().getId()).getGameMode() == GameType.SPECTATOR;
		}
		return false;
	}

	private static void handleSpecialEntities(Entity player, Entity target) {
		if (!isInCreativeMode(player)) return;

		if (isHandEmpty(player, InteractionHand.MAIN_HAND) || isHandEmpty(player, InteractionHand.OFF_HAND)) {
			if (player instanceof Player) {
				((Player) player).swing(getSwingHand(player), true);
			}
			if (player instanceof Player p && !p.level.isClientSide()) {
				p.displayClientMessage(new TranslatableComponent("key.changed_addon.pat_message", target.getDisplayName().getString()), true);
			}
		}
	}

	private static void handleLatexEntity(Entity player, Entity target, LevelAccessor world) {
		boolean isPlayerTransfur = player.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables()).transfur;
		boolean isPlayerTransfurInExp2 = (ProcessTransfur.getPlayerLatexVariant((Player) player) != null && ((ProcessTransfur.getPlayerLatexVariant((Player) player)).is(AddonLatexVariant.EXP2.male()) || ProcessTransfur.getPlayerLatexVariant((Player) player).is(AddonLatexVariant.EXP2.female())));
		//if (!isPlayerTransfur) return;

		if (!(target instanceof Experiment10Entity) && !(target instanceof KetExperiment009Entity) && isHandEmpty(player, InteractionHand.MAIN_HAND) || isHandEmpty(player, InteractionHand.OFF_HAND)) {
			if (player instanceof Player) {
				((Player) player).swing(getSwingHand(player), true);
				if(target instanceof Exp2MaleEntity || target instanceof Exp2FemaleEntity && isPlayerTransfur){
					if(!isPlayerTransfurInExp2 && isPlayerTransfur)
					((Player) player).addEffect(new MobEffectInstance(ChangedAddonModMobEffects.TRANSFUR_SICKNESS.get(), 2400, 0, false, false));
				}
			}
			if (world instanceof ServerLevel serverLevel) {
				serverLevel.sendParticles(ParticleTypes.HEART, target.getX(), target.getY() + 1, target.getZ(), 7, 0.3, 0.3, 0.3, 1);
			}
			if (player instanceof Player p && !p.level.isClientSide()) {
				p.displayClientMessage(new TranslatableComponent("key.changed_addon.pat_message", target.getDisplayName().getString()), true);
			}
		}
	}

	private static void handlePlayerEntity(Entity player, Player target, LevelAccessor world) {
		boolean isPlayerTransfur = player.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables()).transfur;
		boolean isTargetTransfur = target.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables()).transfur;
		boolean isPlayerTransfurInExp2 = (ProcessTransfur.getPlayerLatexVariant((Player) player) != null && ((ProcessTransfur.getPlayerLatexVariant((Player) player)).is(AddonLatexVariant.EXP2.male()) || ProcessTransfur.getPlayerLatexVariant((Player) player).is(AddonLatexVariant.EXP2.female())));
		boolean isTargetTransfurInExp2 = (ProcessTransfur.getPlayerLatexVariant(target) != null && (ProcessTransfur.getPlayerLatexVariant(target).is(AddonLatexVariant.EXP2.male()) || ProcessTransfur.getPlayerLatexVariant(target).is(AddonLatexVariant.EXP2.female())));

		
		if ((isPlayerTransfur || !isPlayerTransfur) && (!isTargetTransfur || isTargetTransfur) && isHandEmpty(player, InteractionHand.MAIN_HAND) || isHandEmpty(player, InteractionHand.OFF_HAND)) {
			if (!isPlayerTransfur && !isTargetTransfur){return;}//Don't Be Able to Pet if at lest one is Transfur :P

			if (player instanceof Player player1) {
				((Player) player).swing(getSwingHand(player), true);
				if(isPlayerTransfur && isTargetTransfur) { //Add The Effect if is Transfur is Exp2
				 if (isPlayerTransfurInExp2 && !isTargetTransfurInExp2){
					 target.addEffect(new MobEffectInstance(ChangedAddonModMobEffects.TRANSFUR_SICKNESS.get(), 2400, 0, false, false));
					} else if (!isPlayerTransfurInExp2 && isTargetTransfurInExp2) {
					 ((Player) player).addEffect(new MobEffectInstance(ChangedAddonModMobEffects.TRANSFUR_SICKNESS.get(), 2400, 0, false, false));
				 	} else if (isPlayerTransfurInExp2 && isTargetTransfurInExp2){
					 return;//Exp2 Can't give Exp2 Transfur Sickness
				 	}
				}
			}



			if (isTargetTransfur && world instanceof ServerLevel serverLevel) {
				serverLevel.sendParticles(ParticleTypes.HEART, target.getX(), target.getY() + 1, target.getZ(), 7, 0.3, 0.3, 0.3, 1);
				if(serverLevel.random.nextFloat(100) <= 2.5f){
					target.heal(6f);
					GivePatAdvancement(player);
				}
			}

			if (player instanceof Player p && !p.level.isClientSide()) {
				p.displayClientMessage(new TranslatableComponent("key.changed_addon.pat_message", target.getDisplayName().getString()), true);
				target.displayClientMessage(new TranslatableComponent("key.changed_addon.pat_received", player.getDisplayName().getString()), true);
			}
		}
	}


	private static void handlePatableEntity(Entity player, Entity target, LevelAccessor world) {
		if (isHandEmpty(player, InteractionHand.MAIN_HAND) || isHandEmpty(player, InteractionHand.OFF_HAND)) {
			if (player instanceof Player) {
				((Player) player).swing(getSwingHand(player), true);
			}
			if (world instanceof ServerLevel serverLevel) {
				serverLevel.sendParticles(ParticleTypes.HEART, target.getX(), target.getY() + 1, target.getZ(), 7, 0.3, 0.3, 0.3, 1);
			}
			if (player instanceof Player p && !p.level.isClientSide()) {
				p.displayClientMessage(new TranslatableComponent("key.changed_addon.pat_message", target.getDisplayName().getString()), true);
			}
		}
	}

	private static boolean isInCreativeMode(Entity entity) {
		if (entity instanceof ServerPlayer serverPlayer) {
			return serverPlayer.gameMode.getGameModeForPlayer() == GameType.CREATIVE;
		} else if (entity.level.isClientSide() && entity instanceof Player player) {
			return Minecraft.getInstance().getConnection().getPlayerInfo(player.getGameProfile().getId()).getGameMode() == GameType.CREATIVE;
		}
		return false;
	}

	private static boolean isHandEmpty(Entity entity, InteractionHand hand) {
		return entity instanceof LivingEntity livingEntity && livingEntity.getItemInHand(hand).getItem() == Blocks.AIR.asItem();
	}

	private static InteractionHand getSwingHand(Entity entity) {
		return isHandEmpty(entity, InteractionHand.MAIN_HAND) ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
	}

	public static void GivePatAdvancement(Entity entity){
		if (entity instanceof ServerPlayer _player) {
			Advancement _adv = _player.server.getAdvancements().getAdvancement(new ResourceLocation("changed_addon:pat_advancement"));
            assert _adv != null;
            AdvancementProgress _ap = _player.getAdvancements().getOrStartProgress(_adv);
			if (!_ap.isDone()) {
                for (String string : _ap.getRemainingCriteria()) {
                    _player.getAdvancements().award(_adv, string);
                }
			}
		}

	}
}
