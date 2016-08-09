package com.quintenlauwers.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

/**
 * Created by quinten on 9/08/16.
 */
public class ModRenderFactory implements IRenderFactory<EntityDnaChicken> {

    private ModelBase model;
    private float shadowSize;

    ModRenderFactory(ModelBase model, float shadowSize) {
        this.model = model;
        this.shadowSize = shadowSize;
    }

    public Render<? super EntityDnaChicken> createRenderFor(RenderManager manager) {
        return new RenderDnaChicken(manager, this.model, this.shadowSize);
    }
}
