package net.pixeldreamstudios.beings_of_elderia.entity.client.model;

import mod.azure.azurelib.constant.DataTickets;
import mod.azure.azurelib.core.animatable.model.CoreGeoBone;
import mod.azure.azurelib.core.animation.AnimationState;
import mod.azure.azurelib.model.GeoModel;
import mod.azure.azurelib.model.data.EntityModelData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.pixeldreamstudios.beings_of_elderia.BeingsOfElderia;
import net.pixeldreamstudios.beings_of_elderia.entity.ImpGuardEntity;

public class ImpGuardModel extends GeoModel<ImpGuardEntity> {

    @Override
    public ResourceLocation getModelResource(ImpGuardEntity object) {
        return new ResourceLocation(BeingsOfElderia.MOD_ID, "geo/entity/imp_guard.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ImpGuardEntity object) {
        return new ResourceLocation(BeingsOfElderia.MOD_ID, "textures/entity/imp_guard.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ImpGuardEntity animatable) {
        return new ResourceLocation(BeingsOfElderia.MOD_ID, "animations/entity/imp_guard.animation.json");
    }

    @Override
    public void setCustomAnimations(ImpGuardEntity animatable, long instanceId, AnimationState<ImpGuardEntity> animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("head");

        if (head != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            head.setRotX(entityData.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
        }
    }
}
