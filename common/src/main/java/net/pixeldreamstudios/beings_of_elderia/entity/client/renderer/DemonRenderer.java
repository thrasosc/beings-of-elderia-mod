package net.pixeldreamstudios.beings_of_elderia.entity.client.renderer;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.pixeldreamstudios.beings_of_elderia.BeingsOfElderia;
import net.pixeldreamstudios.beings_of_elderia.entity.demons.DemonEntity;
import net.pixeldreamstudios.beings_of_elderia.entity.client.model.DemonModel;
import net.pixeldreamstudios.beings_of_elderia.entity.client.renderer.abstraction.HeldItemRenderer;

public class DemonRenderer extends HeldItemRenderer<DemonEntity> {
    public DemonRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new DemonModel());
        this.shadowRadius = 0.75f;
    }

    @Override
    public ResourceLocation getTextureLocation(DemonEntity animatable) {
        return new ResourceLocation(BeingsOfElderia.MOD_ID, "textures/entity/demon.png");
    }
}