package com.quintenlauwers.interfaces;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

/**
 * Created by quinten on 6/08/16.
 */
public class GuiClickableImage extends GuiButton implements holdable {
    private ResourceLocation imageLocation;

    private float u;
    private float v;
    private float textureWidth;
    private float textureHeight;
    private boolean inHold = false;

    public GuiClickableImage(int buttonId, int x, int y, float u, float v, int width, int height, float textureWidth, float textureHeight, ResourceLocation image) {
        super(buttonId, x, y, width, height, "");
        this.imageLocation = image;
        this.u = u;
        this.v = v;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            FontRenderer fontrenderer = mc.fontRendererObj;
            mc.getTextureManager().bindTexture(this.imageLocation);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            int i = this.getHoverState(this.hovered);
            drawModalRectWithCustomSizedTexture(this.xPosition, this.yPosition, this.u, this.v + (i - 1) * this.height, this.width, this.height, this.textureWidth, this.textureHeight);
            this.mouseDragged(mc, mouseX, mouseY);
        }
        if (this.inHold){
            FontRenderer fontrenderer = mc.fontRendererObj;
            mc.getTextureManager().bindTexture(this.imageLocation);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            drawModalRectWithCustomSizedTexture(this.xPosition, this.yPosition, this.u, this.v + this.height, this.width, this.height, this.textureWidth, this.textureHeight);
        }
    }

    @Override
    public void hold() {
        this.visible = false;
        this.inHold = true;
        this.hovered = false;
    }

    @Override
    public void releaseHold() {
        this.visible = true;
        this.inHold = false;
    }
}
