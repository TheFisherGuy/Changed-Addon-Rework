
package net.foxyas.changedaddon.client.renderer;

import net.foxyas.changedaddon.client.model.ModelSnowFox;
import net.ltxprogrammer.changed.client.renderer.LatexHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.CustomEyesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.LatexParticlesLayer;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexMaleWolfModel;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexWolfModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import net.foxyas.changedaddon.entity.LatexSnowFoxEntity;
import net.foxyas.changedaddon.client.model.ModelFoxyasModel;

public class LatexSnowFoxRenderer extends LatexHumanoidRenderer<LatexSnowFoxEntity, ModelSnowFox, ArmorLatexMaleWolfModel<LatexSnowFoxEntity>> {
	public LatexSnowFoxRenderer(EntityRendererProvider.Context context) {
		super(context, new ModelSnowFox(context.bakeLayer(ModelSnowFox.LAYER_LOCATION)),ArmorLatexMaleWolfModel::new,ArmorLatexMaleWolfModel.INNER_ARMOR,ArmorLatexMaleWolfModel.OUTER_ARMOR, 0.5f);
		this.addLayer(new LatexParticlesLayer<>(this, getModel()));
		this.addLayer(new CustomEyesLayer<>(this, context.getModelSet()));
	}

	@Override
	public ResourceLocation getTextureLocation(LatexSnowFoxEntity entity) {
		return new ResourceLocation("changed_addon:textures/entities/latex_snowfox_male_new.png");
	}
}