package net.pixeldreamstudios.beings_of_elderia.entity.client.renderer;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.pixeldreamstudios.beings_of_elderia.BeingsOfElderia;
import net.pixeldreamstudios.beings_of_elderia.entity.ImpEntity;
import net.pixeldreamstudios.beings_of_elderia.entity.client.model.ImpModel;
import net.pixeldreamstudios.beings_of_elderia.entity.client.renderer.abstraction.HeldItemRenderer;

public class ImpRenderer extends HeldItemRenderer<ImpEntity> {
    public ImpRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new ImpModel());
        this.shadowRadius = 0.5f;
    }

    @Override
    public ResourceLocation getTextureLocation(ImpEntity animatable) {
        return new ResourceLocation(BeingsOfElderia.MOD_ID, "textures/entity/imp.png");
    }
}