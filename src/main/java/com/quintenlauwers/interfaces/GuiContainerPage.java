package com.quintenlauwers.interfaces;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by quinten on 18/08/16.
 */
public class GuiContainerPage extends GuiPage {

    public static ResourceLocation CONTAINERBACKGROUND = new ResourceLocation("testmod:textures/gui/background.png");


    public GuiContainerPage(GuiDnaMain screen) {
        super(screen);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

    }

    @Override
    public void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        int BackgroundRosterX = (getContainer().getWidth() - this.xWindowSize) / 2;
        int BackgroundRosterY = (getContainer().getHeight() - this.yWindowSize) / 2;
        getContainer().drawRectWithCustomSizedTexture(BackgroundRosterX, BackgroundRosterY, 0, 0, xWindowSize,
                yWindowSize, xWindowSize, yWindowSize, CONTAINERBACKGROUND);
    }

    @Override
    public List<GuiButton> getButtonList() {
        return new ArrayList<GuiButton>();
    }

    @Override
    public void initGui() {

    }

    @Override
    public void actionPerformed(GuiButton button) {

    }


}
