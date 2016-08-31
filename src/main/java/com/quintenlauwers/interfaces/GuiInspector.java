package com.quintenlauwers.interfaces;

import com.google.common.collect.Lists;
import com.quintenlauwers.backend.DnaProperties;
import com.quintenlauwers.lib.RefStrings;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by quinten on 27/08/16.
 */
public class GuiInspector extends GuiScreen {
    public final int xWindowSize = 176;
    public final int yWindowSize = 166;

    private DnaProperties properties;

    private String[] allProperties;
    private int pageIndex = 0;

    private List<List<String>> pageProperties;

    private GuiButton nextButton;
    private GuiButton prevButton;

    public static ResourceLocation BACKGROUNDTEXTURE = new ResourceLocation(RefStrings.MODID + ":textures/gui/background.png");

    GuiInspector(DnaProperties properties) {
        this.properties = properties;
    }

    @Override
    public void initGui() {
        this.buttonList.add(this.nextButton = new GuiButton(0,
                (this.width + this.xWindowSize) / 2 - 35, (this.height + this.yWindowSize) / 2 - 30, 25, 20, ">"));
        this.buttonList.add(this.prevButton = new GuiButton(1,
                (this.width - this.xWindowSize) / 2 + 10, (this.height + this.yWindowSize) / 2 - 30, 25, 20, "<"));
        this.prevButton.visible = false;


    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawBackground();
        String text = I18n.format("gui.animal." + properties.getAnimal());
        this.fontRendererObj.drawString(text, (this.width - this.fontRendererObj.getStringWidth(text)) / 2, (this.height - yWindowSize) / 2 + 10, 0x999999);
        int j = 2;
        if (this.allProperties == null) {
            this.allProperties = properties.getPossibleProperties();
            pageProperties = Lists.partition(Arrays.asList(allProperties), 10);
        }
        for (String prop : pageProperties.get(pageIndex)) {
            String left = I18n.format("gui.prop." + prop);
            this.fontRendererObj.drawString(left, (this.width - this.xWindowSize) / 2 + 10, (this.height - yWindowSize) / 2 + 10 * j, 0x999999);
            String value = I18n.format("gui.prop.value." + properties.getStringProperty(prop));
            this.fontRendererObj.drawString(value, (this.width + this.xWindowSize) / 2 - 10 - fontRendererObj.getStringWidth(value), (this.height - yWindowSize) / 2 + 10 * j, 0x000000);
            j++;
        }
        this.prevButton.visible = pageIndex > 0;
        this.nextButton.visible = pageIndex < this.pageProperties.size() - 1;
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    protected void drawBackground() {
        int BackgroundRosterX = (this.width - this.xWindowSize) / 2;
        int BackgroundRosterY = (this.height - this.yWindowSize) / 2;
        this.mc.getTextureManager().bindTexture(BACKGROUNDTEXTURE);
        drawModalRectWithCustomSizedTexture(BackgroundRosterX, BackgroundRosterY, 0, 0, xWindowSize,
                yWindowSize, xWindowSize, yWindowSize);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button == prevButton) {
            if (pageIndex > 0) {
                pageIndex--;
            }
        }
        if (button == nextButton) {
            if (pageIndex < this.pageProperties.size() - 1) {
                pageIndex++;
            }
        }
        super.actionPerformed(button);
    }
}
