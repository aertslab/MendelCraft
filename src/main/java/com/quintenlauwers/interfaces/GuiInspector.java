package com.quintenlauwers.interfaces;

import com.quintenlauwers.backend.DnaProperties;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

/**
 * Created by quinten on 27/08/16.
 */
public class GuiInspector extends GuiScreen {
    public final int xWindowSize = 176;
    public final int yWindowSize = 166;

    private DnaProperties properties;

    public static ResourceLocation BACKGROUNDTEXTURE = new ResourceLocation("testmod:textures/gui/background.png");

    GuiInspector(DnaProperties properties) {
        this.properties = properties;
    }

    @Override
    public void initGui() {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawBackground();
        String text = I18n.format("gui.animal." + properties.getAnimal());
        this.fontRendererObj.drawString(text, (this.width - this.fontRendererObj.getStringWidth(text)) / 2, (this.height - yWindowSize) / 2 + 10, 0x999999);
        int j = 2;
        for (String prop : properties.getPossibleProperties()) {
            String left = I18n.format("gui.prop." + prop);
            this.fontRendererObj.drawString(left, (this.width - this.xWindowSize) / 2 + 10, (this.height - yWindowSize) / 2 + 10 * j, 0x999999);
            String value = I18n.format("gui.prop.value." + properties.getStringProperty(prop));
            this.fontRendererObj.drawString(value, (this.width + this.xWindowSize) / 2 - 10 - fontRendererObj.getStringWidth(value), (this.height - yWindowSize) / 2 + 10 * j, 0x000000);
            j++;
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    protected void drawBackground() {
        int BackgroundRosterX = (this.width - this.xWindowSize) / 2;
        int BackgroundRosterY = (this.height - this.yWindowSize) / 2;
        this.mc.getTextureManager().bindTexture(BACKGROUNDTEXTURE);
        drawModalRectWithCustomSizedTexture(BackgroundRosterX, BackgroundRosterY, 0, 0, xWindowSize,
                yWindowSize, xWindowSize, yWindowSize);
    }
}
