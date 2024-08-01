package net.foxyas.changedaddon.procedures;

import com.mojang.math.Vector3f;
import net.ltxprogrammer.changed.entity.variant.LatexVariant;
import net.ltxprogrammer.changed.init.ChangedRegistry;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.*;

import javax.annotation.Nullable;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class PlayerUtilProcedure {

	public static void TransfurPlayer(Entity entity, LatexVariant<?> latexVariant){
		LivingEntity livingEntity = (LivingEntity) entity;
		ProcessTransfur.transfur(livingEntity,entity.getLevel(),latexVariant,true);
	}

	public static void TransfurPlayer(Entity entity, String id){
		ResourceLocation form = null;
		try {
			form = new ResourceLocation(id);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		LivingEntity livingEntity = (LivingEntity) entity;
		if (form != null && LatexVariant.PUBLIC_LATEX_FORMS.contains(form)) {
		LatexVariant<?> latexVariant = ChangedRegistry.LATEX_VARIANT.get().getValue(form);
		ProcessTransfur.transfur(livingEntity,entity.getLevel(),latexVariant,true);
		}
	}

	public static void UnTransfurPlayer(Entity entity){
		Player player = (Player) entity;
		ProcessTransfur.ifPlayerLatex(player, (variant) -> {
			variant.unhookAll(player);
			ProcessTransfur.setPlayerLatexVariant(player, null);
			ProcessTransfur.setPlayerTransfurProgress(player, new ProcessTransfur.TransfurProgress(0.0F, LatexVariant.FALLBACK_VARIANT));
		});
	}

	public static Entity getEntityPlayerLookingAt(Player player, double range) {
		Level world = player.level;
		Vec3 startVec = player.getEyePosition(1.0F); // Player's eye position
		Vec3 lookVec = player.getLookAngle(); // Player's look direction
		Vec3 endVec = startVec.add(lookVec.scale(range)); // End point of the line of sight

		Entity closestEntity = null;
		double closestDistance = range;

		// Iterate over all entities within range
		for (Entity entity : world.getEntities(player, player.getBoundingBox().expandTowards(lookVec.scale(range)).inflate(1.0D))) {
			// Ignore entities in spectator mode
			if (entity.isSpectator()) {
				continue;
			}

			AABB entityBoundingBox = entity.getBoundingBox().inflate(entity.getPickRadius());

			// Check if the line of sight intersects the entity's bounding box
			if (entityBoundingBox.contains(startVec) || entityBoundingBox.clip(startVec, endVec).isPresent()) {
				double distanceToEntity = startVec.distanceTo(entity.position());

				if (distanceToEntity < closestDistance) {
					closestEntity = entity;
					closestDistance = distanceToEntity;
				}
			}
		}

		return closestEntity; // Return the closest entity the player is looking at
	}

	public static Entity getEntityPlayerLookingAtType2(Entity entity, Entity player, double entityReach) {
		double distance = entityReach * entityReach;
		Vec3 eyePos = player.getEyePosition(1.0f);
		HitResult hitResult = entity.pick(entityReach, 1.0f, false);

		if (hitResult != null && hitResult.getType() != HitResult.Type.MISS) {
			distance = hitResult.getLocation().distanceToSqr(eyePos);
			double blockReach = 5;

			if (distance > blockReach * blockReach) {
				Vec3 pos = hitResult.getLocation();
				hitResult = BlockHitResult.miss(pos, Direction.getNearest(eyePos.x, eyePos.y, eyePos.z), new BlockPos(pos));
			}
		}

		Vec3 viewVec = player.getViewVector(1.0F);
		Vec3 toVec = eyePos.add(viewVec.x * entityReach, viewVec.y * entityReach, viewVec.z * entityReach);
		AABB aabb = entity.getBoundingBox().expandTowards(viewVec.scale(entityReach)).inflate(1.0D, 1.0D, 1.0D);
		EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(player, eyePos, toVec, aabb, (p_234237_) -> !p_234237_.isSpectator(), distance);

		if (entityHitResult != null) {
			Entity targetEntity = entityHitResult.getEntity();
			Vec3 targetPos = entityHitResult.getLocation();
			double distanceToTarget = eyePos.distanceToSqr(targetPos);

			if (distanceToTarget > distance || distanceToTarget > entityReach * entityReach) {
				hitResult = BlockHitResult.miss(targetPos, Direction.getNearest(viewVec.x, viewVec.y, viewVec.z), new BlockPos(targetPos));
			} else if (distanceToTarget < distance) {
				hitResult = entityHitResult;
			}
		}

		if (hitResult.getType() == HitResult.Type.ENTITY) {
			return ((EntityHitResult) hitResult).getEntity();
		}

		return null;
	}

	public static class GlobalEntityUtil {
		@Nullable
		public static Entity getEntityByUUID(LevelAccessor world, String uuid) {
			try {
				Stream<Entity> entities;

				if (world instanceof ClientLevel clientLevel) {
					entities = StreamSupport.stream(clientLevel.entitiesForRendering().spliterator(), false);
				} else if (world instanceof ServerLevel serverLevel) {
					entities = StreamSupport.stream(serverLevel.getAllEntities().spliterator(), false);
				} else {
					return null;
				}

				return entities.filter(entity -> entity.getStringUUID().equals(uuid)).findFirst().orElse(null);
			} catch (Exception e) {
				e.printStackTrace(); // Log the exception for debugging purposes
				return null;
			}
		}
	}

	public static class ParticlesUtil {

		public static void sendColorTransitionParticles(Level level, double x, double y, double z,
														float redStart, float greenStart, float blueStart,
														float redEnd, float greenEnd, float blueEnd,
														float size,float XV,float YV,float ZV, int count,float speed) {

			// Criar a opção de partícula para transição de cor usando Vector3f
			Vector3f startColor = new Vector3f(redStart, greenStart, blueStart);
			Vector3f endColor = new Vector3f(redEnd, greenEnd, blueEnd);
			DustColorTransitionOptions particleOptions = new DustColorTransitionOptions(startColor, endColor, size);

			// Enviar as partículas
			if(level instanceof ServerLevel serverLevel){
			serverLevel.sendParticles(particleOptions,
					x, y+1, z, count, XV, YV, ZV, speed);
			}
		}
	}
}
