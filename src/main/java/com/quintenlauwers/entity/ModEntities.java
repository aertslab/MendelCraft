package com.quintenlauwers.entity;

import com.quintenlauwers.main.TestMod;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;

/**
 * Created by quinten on 9/08/16.
 */
public class ModEntities {

    public static void init() {

        RenderingRegistry.registerEntityRenderingHandler(EntityDnaChicken.class, new ModRenderFactory(new ModelDnaChicken(), 0.3F));
        EntityRegistry.registerModEntity(EntityDnaChicken.class, "dnaChicken", 1351, TestMod.instance, 12, 1, false, 230, 78 );
    }
}
