package net.foxyas.changedaddon.ability;

import net.foxyas.changedaddon.procedures.PlayerUtilProcedure;
import net.foxyas.changedaddon.variants.AddonLatexVariant;
import net.ltxprogrammer.changed.ability.IAbstractLatex;
import net.ltxprogrammer.changed.ability.SimpleAbility;
import net.ltxprogrammer.changed.entity.beast.LightLatexCentaur;
import net.ltxprogrammer.changed.entity.variant.LatexVariant;
import net.ltxprogrammer.changed.init.ChangedRegistry;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.Objects;

public class CarryAbility extends SimpleAbility {

    public CarryAbility() {
        super();
    }

    @Override
    public TranslatableComponent getDisplayName(IAbstractLatex entity) {
        return new TranslatableComponent("changed_addon.ability.carry");
    }

    @Override
    public ResourceLocation getTexture(IAbstractLatex entity) {
        return new ResourceLocation("changed_addon:textures/screens/carry_ability.png");
    }

    @Override
    public UseType getUseType(IAbstractLatex entity) {
        return UseType.INSTANT;
    }

    @Override
    public int getCoolDown(IAbstractLatex entity) {
        return 5;
    }

    @Override
    public boolean canUse(IAbstractLatex entity) {
        return (Objects.requireNonNull(entity.getLatexVariantInstance()).getParent().is(AddonLatexVariant.EXP2.female())
                || Objects.requireNonNull(entity.getLatexVariantInstance()).getParent().is(AddonLatexVariant.EXP2.male())
                || Objects.requireNonNull(entity.getLatexVariantInstance()).getParent().is(AddonLatexVariant.ORGANIC_SNOW_LEOPARD.female())
                || Objects.requireNonNull(entity.getLatexVariantInstance()).getParent().is(AddonLatexVariant.ORGANIC_SNOW_LEOPARD.male())
                || Objects.requireNonNull(entity.getLatexVariantInstance()).getParent().is(AddonLatexVariant.ADDON_PURO_KIND.female())
                || Objects.requireNonNull(entity.getLatexVariantInstance()).getParent().is(AddonLatexVariant.ADDON_PURO_KIND.male())
                || Objects.requireNonNull(entity.getLatexVariantInstance()).getParent().is(TagKey.create(ChangedRegistry.LATEX_VARIANT.get().getRegistryKey(), new ResourceLocation("changed_addon:able_to_carry"))))
                && !Spectator(entity.getEntity());
    }

    @Override
    public void startUsing(IAbstractLatex entity) {
        super.startUsing(entity);
        Run(entity.getEntity());
    }

    @Override
    public void onRemove(IAbstractLatex entity) {
        super.onRemove(entity);
        SafeRemove(entity.getEntity());
    }

    public static boolean Spectator(Entity entity) {
        if (entity instanceof Player player1) {
            return player1.isSpectator();
        }
        return true;
    }

    private static void Run(Entity mainEntity) {
        if (mainEntity instanceof Player player) {
            // If the player is already carrying someone, make them dismount
            if (player.getFirstPassenger() != null && player.isShiftKeyDown()) {
                Entity localEntity = player.getFirstPassenger();
                if (localEntity instanceof Player playerEntity){
                    playerEntity.stopRiding();
                    syncMount(player);
                    syncMount(playerEntity);
                } else {
                    player.getFirstPassenger().stopRiding();
                    syncMount(player);
                }
                return;
            } else if (player.getFirstPassenger() != null && !player.isShiftKeyDown()) {
                Entity localEntity = player.getFirstPassenger();
                if (localEntity instanceof Player playerEntity){
                    playerEntity.stopRiding();
                    syncMount(player);
                    syncMount(playerEntity);
                    if (!player.level.isClientSide()){
                        playerEntity.setDeltaMovement(player.getLookAngle().scale(1.05));
                    }
                } else {
                localEntity.stopRiding();
                syncMount(player);
                localEntity.setDeltaMovement(player.getLookAngle().scale(1.05));
                localEntity.hasImpulse = true;
                    if (!player.level.isClientSide()) {
                        ((ServerLevel) player.level).getChunkSource().broadcast(localEntity, new ClientboundSetEntityMotionPacket(localEntity));
                    }
                }
                return;
            }

            // If the player is a spectator, do nothing
            if (player.isSpectator()) {
                return;
            }

            // Get the entity the player is looking at
            Entity carryTarget = PlayerUtilProcedure.getEntityPlayerLookingAt(player, 4);

            // If no entity is being looked at, exit
            if (carryTarget == null) {
                return;
            }

            // If the looked at entity is a player
            if (carryTarget instanceof Player carryPlayer) {
                // Creative players cannot be carried by non-creative players
                if (carryPlayer.isCreative() && !player.isCreative()) {
                    return;
                }

                // Check if the player has the light latex centaur variant and prevent them from being carried
                if (ProcessTransfur.getPlayerLatexVariant(carryPlayer) != null && ProcessTransfur.getPlayerLatexVariant(carryPlayer).is(LatexVariant.LIGHT_LATEX_CENTAUR)) {
                    player.displayClientMessage(new TranslatableComponent("changedaddon.warn.cant_carry", carryPlayer.getDisplayName()), true);
                    return;
                }

                // Start carrying the player
                if (carryPlayer.startRiding(player, true)) {
                    // Synchronize mount with clients
                    syncMount(player);
                    syncMount(carryPlayer);
                }

            } else {
                // If the looked at entity is a light latex centaur, prevent them from being carried
                if (carryTarget instanceof LightLatexCentaur) {
                    player.displayClientMessage(new TranslatableComponent("changedaddon.warn.cant_carry", carryTarget.getDisplayName()), true);
                    return;
                }

                // If the looked at entity is in the humanoid tag or can be carried
                if (carryTarget.getType().is(ChangedTags.EntityTypes.HUMANOIDS) || carryTarget.getType().is(TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation("changed_addon:can_carry")))) {
                    if (carryTarget.startRiding(player, true)) {
                        // Synchronize mount with clients
                        syncMount(player);
                    }
                }
            }
        }
    }

    public static void SoundPlay(Player player) {
        // Play the sound on the server side to ensure it is properly synchronized
        player.level.playSound(null, player.blockPosition(), ChangedSounds.BOW2, SoundSource.PLAYERS, 2.5f, 1.0f);
    }

    private static void syncMount(Player player) {
        // Send the mount synchronization packet to all tracking players
        if (!player.level.isClientSide) {
            ((ServerLevel) player.level).getChunkSource().broadcast(player, new ClientboundSetPassengersPacket(player));
            SoundPlay(player); // Play the sound on the server for correct synchronization
        }
    }


    public static void SafeRemove(Entity MainEntity) {
        if (MainEntity.getFirstPassenger() != null) {
            MainEntity.getFirstPassenger().stopRiding();
        }
    }
}
