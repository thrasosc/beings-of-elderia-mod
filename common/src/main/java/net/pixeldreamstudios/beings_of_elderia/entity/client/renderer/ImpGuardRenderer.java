package net.pixeldreamstudios.beings_of_elderia.entity.client.renderer;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.pixeldreamstudios.beings_of_elderia.BeingsOfElderia;
import net.pixeldreamstudios.beings_of_elderia.entity.ImpGuardEntity;
import net.pixeldreamstudios.beings_of_elderia.entity.client.model.ImpGuardModel;
import net.pixeldreamstudios.beings_of_elderia.entity.client.renderer.abstraction.HeldItemRenderer;

public class ImpGuardRenderer extends HeldItemRenderer<ImpGuardEntity> {
    public ImpGuardRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new ImpGuardModel());
        this.shadowRadius = 0.5f;
    }

    @Override
    public ResourceLocation getTextureLocation(ImpGuardEntity animatable) {
        return new ResourceLocation(BeingsOfElderia.MOD_ID, "textures/entity/imp_guard.png");
    }
}