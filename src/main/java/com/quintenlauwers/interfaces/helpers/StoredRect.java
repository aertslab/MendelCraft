package com.quintenlauwers.interfaces.helpers;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

/**
 * Created by quinten on 31/08/16.
 */
public class StoredRect {
    private int x, y, width, height;
    private float u, v, textureWidth, textureHeight;
    private ResourceLocation texture;


    public StoredRect(int x, int y, float u, float v, int width, int height, float textureWidth,
                      float textureHeight, ResourceLocation texture) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.u = u;
        this.v = v;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.texture = texture;
    }

    public void drawRect(TextureManager manager) {
        manager.bindTexture(texture);
        GuiScreen.drawModalRectWithCustomSizedTexture(x, y, u, v, width, height, textureWidth, textureHeight);
    }
}
