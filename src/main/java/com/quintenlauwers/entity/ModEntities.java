package com.quintenlauwers.entity;

import com.quintenlauwers.entity.chicken.EntityDnaChicken;
import com.quintenlauwers.entity.chicken.ModRenderFactoryChicken;
import com.quintenlauwers.entity.chicken.ModelDnaChicken;
import com.quintenlauwers.main.TestMod;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by quinten on 9/08/16.
 */
public class ModEntities {

    public static void init() {

        EntityRegistry.registerModEntity(EntityDnaChicken.class, "dnaChicken", 121, TestMod.instance, 12, 1, false, 230, 78 );
    }

    @SideOnly(Side.CLIENT)
    public static void registerRenderers() {
        RenderingRegistry.registerEntityRenderingHandler(EntityDnaChicken.class, new ModRenderFactoryChicken(new ModelDnaChicken(), 0.3F));
    }
}
