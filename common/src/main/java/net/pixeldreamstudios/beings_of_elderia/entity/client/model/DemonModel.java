package net.pixeldreamstudios.beings_of_elderia.entity.client.model;

import mod.azure.azurelib.constant.DataTickets;
import mod.azure.azurelib.core.animatable.model.CoreGeoBone;
import mod.azure.azurelib.core.animation.AnimationState;
import mod.azure.azurelib.model.GeoModel;
import mod.azure.azurelib.model.data.EntityModelData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.pixeldreamstudios.beings_of_elderia.BeingsOfElderia;
import net.pixeldreamstudios.beings_of_elderia.entity.DemonEntity;

public class DemonModel extends GeoModel<DemonEntity> {

    @Override
    public ResourceLocation getModelResource(DemonEntity object) {
        return new ResourceLocation(BeingsOfElderia.MOD_ID, "geo/entity/demon.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(DemonEntity object) {
        return new ResourceLocation(BeingsOfElderia.MOD_ID, "textures/entity/demon.png");
    }

    @Override
    public ResourceLocation getAnimationResource(DemonEntity animatable) {
        return new ResourceLocation(BeingsOfElderia.MOD_ID, "animations/entity/demon.animation.json");
    }

    @Override
    public void setCustomAnimations(DemonEntity animatable, long instanceId, AnimationState<DemonEntity> animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("head");

        if (head != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            head.setRotX(entityData.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
        }
    }
}
