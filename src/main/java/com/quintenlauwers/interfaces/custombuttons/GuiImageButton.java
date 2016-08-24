package com.quintenlauwers.interfaces.custombuttons;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

/**
 * Created by quinten on 5/08/16.
 */
public class GuiImageButton extends GuiButton implements holdable {


    private ResourceLocation imageLocation;
    private boolean inHold = false;
    private float u;
    private float v;
    private float textureWidth;
    private float textureHeight;

    public GuiImageButton(int buttonId, int x, int y, ResourceLocation image) {
        this(buttonId, x, y, 200, 20, image);
    }

    public GuiImageButton(int buttonId, int x, int y, int widthIn, int heightIn, ResourceLocation image) {
        this(buttonId, x, y, 0, 0, widthIn, heightIn, widthIn, 60, image, "");
    }

    public GuiImageButton(int buttonId, int x, int y, float u, float v, int widthIn, int heightIn, float textureWidth, float textureHeight, ResourceLocation image, String text) {
        super(buttonId, x, y, widthIn, heightIn, text);
        this.imageLocation = image;
        this.u = u;
        this.v = v;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY)
    {
        if (this.visible)
        {
            FontRenderer fontrenderer = mc.fontRendererObj;
            mc.getTextureManager().bindTexture(this.imageLocation);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            int i = this.getHoverState(this.hovered);
//            GlStateManager.enableBlend();
//            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
//            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            drawModalRectWithCustomSizedTexture(this.xPosition, this.yPosition, u, v + (i) * 20, this.width, this.height, this.textureWidth, this.textureHeight);
//            drawModalRectWithCustomSizedTexture(this.xPosition + this.width / 2, this.yPosition, 50 - this.width / 2, (i) * 20, this.width / 2, this.height, 50, 60);
            this.mouseDragged(mc, mouseX, mouseY);
        }
        if (this.inHold) {
            FontRenderer fontrenderer = mc.fontRendererObj;
            mc.getTextureManager().bindTexture(this.imageLocation);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
//            GlStateManager.enableBlend();
//            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
//            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            drawModalRectWithCustomSizedTexture(this.xPosition, this.yPosition, u, v, this.width, this.height, this.textureWidth, this.textureHeight);
//            drawModalRectWithCustomSizedTexture(this.xPosition + this.width / 2, this.yPosition, 50 - this.width / 2, 0, this.width / 2, this.height, 50, 60);
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
