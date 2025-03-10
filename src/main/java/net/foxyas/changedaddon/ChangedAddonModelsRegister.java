package net.foxyas.changedaddon;

import com.google.common.collect.ImmutableMap;
import net.foxyas.changedaddon.client.model.*;
import net.foxyas.changedaddon.client.renderer.SnowLeopardPartialRenderer;
import net.foxyas.changedaddon.configuration.ChangedAddonClientConfigsConfiguration;
import net.foxyas.changedaddon.entity.SnowLeopardPartialEntity;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.Map;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = {Dist.CLIENT})
public class ChangedAddonModelsRegister {
	@SubscribeEvent
	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(ModelSnowFox.LAYER_LOCATION, ModelSnowFox::createBodyLayer);
		event.registerLayerDefinition(ModelFemaleSnowFox.LAYER_LOCATION, ModelFemaleSnowFox::createBodyLayer);
		event.registerLayerDefinition(DazedLatexModel.LAYER_LOCATION, DazedLatexModel::createBodyLayer);
		event.registerLayerDefinition(PuroKindModel.LAYER_LOCATION, PuroKindModel::createBodyLayer);
		event.registerLayerDefinition(PuroKindFemaleModel.LAYER_LOCATION, PuroKindFemaleModel::createBodyLayer);
		event.registerLayerDefinition(BunyModel.LAYER_LOCATION, BunyModel::createBodyLayer);
		event.registerLayerDefinition(BioSynthSnowLeopardMaleModel.LAYER_LOCATION, BioSynthSnowLeopardMaleModel::createBodyLayer);
		event.registerLayerDefinition(OrganicSnowLeopardMaleModel.LAYER_LOCATION, OrganicSnowLeopardMaleModel::createBodyLayer);
		event.registerLayerDefinition(BioSynthSnowLeopardFemaleModel.LAYER_LOCATION, BioSynthSnowLeopardFemaleModel::createBodyLayer);
		event.registerLayerDefinition(OrganicSnowLeopardFemaleModel.LAYER_LOCATION, OrganicSnowLeopardFemaleModel::createBodyLayer);
		event.registerLayerDefinition(KetModel.LAYER_LOCATION, KetModel::createBodyLayer);
		event.registerLayerDefinition(ModelMirrorWhiteTiger.LAYER_LOCATION, ModelMirrorWhiteTiger::createBodyLayer);
		event.registerLayerDefinition(Experiment10Model.LAYER_LOCATION, Experiment10Model::createBodyLayer);
		event.registerLayerDefinition(MaleExp2Model.LAYER_LOCATION, MaleExp2Model::createBodyLayer);
		event.registerLayerDefinition(FemaleExp2Model.LAYER_LOCATION, FemaleExp2Model::createBodyLayer);
		event.registerLayerDefinition(WolfyModel.LAYER_LOCATION, WolfyModel::createBodyLayer);
		event.registerLayerDefinition(SnowLeopardPartialModel.LAYER_LOCATION_HUMAN, () -> SnowLeopardPartialModel.createHumanLayer(false));
		event.registerLayerDefinition(SnowLeopardPartialModel.LAYER_LOCATION_LATEX, () -> SnowLeopardPartialModel.createLatexLayer(false));
		event.registerLayerDefinition(SnowLeopardPartialModel.LAYER_LOCATION_HUMAN_SLIM, () -> SnowLeopardPartialModel.createHumanLayer(true));
		event.registerLayerDefinition(SnowLeopardPartialModel.LAYER_LOCATION_LATEX_SLIM, () -> SnowLeopardPartialModel.createLatexLayer(true));
		event.registerLayerDefinition(GrapeSnowLeopardModel.LAYER_LOCATION, GrapeSnowLeopardModel::createBodyLayer);
		event.registerLayerDefinition(ReynModel.LAYER_LOCATION, ReynModel::createBodyLayer);
	}

	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(ChangedAddonEntitys.SNOW_LEOPARD_PARTIAL.get(), SnowLeopardPartialRenderer::new);
	}

	/*
	private static Map<String, EntityRenderer<? extends SnowLeopardPartialEntity>> partialRenderers = ImmutableMap.of();
	
	@Nullable
	public static <T extends Entity> EntityRenderer<? super T> getRenderer(T entity) {
		if (entity instanceof SnowLeopardPartialEntity partial) {
			String s = partial.getModelName();
			EntityRenderer<? extends SnowLeopardPartialEntity> entityrenderer = partialRenderers.get(s);
			return (EntityRenderer) (entityrenderer != null ? entityrenderer : partialRenderers.get("default"));
		}
			return null; // Default to registered renderer
	}

		public static void registerComplexRenderers(EntityRendererProvider.Context context) {
		partialRenderers = new ImmutableMap.Builder<String, EntityRenderer<? extends SnowLeopardPartialEntity>>()
				.put("default", new SnowLeopardPartialRenderer(context, false))
				.put("slim", new SnowLeopardPartialRenderer(context, true)).build();
	}*/
}