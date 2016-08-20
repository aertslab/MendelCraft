package com.quintenlauwers.interfaces.pages;

import com.quintenlauwers.interfaces.GuiDnaMain;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

@SideOnly(Side.CLIENT)
public abstract class GuiPage {

    /**
     * The X size of the DNA window in pixels.
     */
    protected int xWindowSize = 176;
    /**
     * The Y size of the DNA window in pixels.
     */
    protected int yWindowSize = 166;
    /**
     * The X size of the DNA button in pixels.
     */
    protected int xButtonSize = 34;
    /**
     * The Y size of the DNA button in pixels.
     */
    protected int yButtonSize = 20;
    /**
     * The Y position of the first DNA row in pixels.
     */
    protected int yDNARowPosition = 70;
    /**
     * The Y posoition of the nucleobase row in pixels.
     */
    protected int yNucleobasePosition = 140;
    /**
     * The X size of the chromosome button in pixels.
     */
    protected int xChromosomeSize = 20;
    /**
     * The Y size of the chromosome button in pixels.
     */
    protected int yChromosomeSize = 20;
    /**
     * The maximum number of genes showed in a row at once.
     */
    protected int nbVisibleGenes;
    /**
     * The first shown gene.
     */
    protected int geneIndex = 0;
    protected int lastGeneIndex = 1;
    /**
     * The maximum number of chromosomes showed in a row at once.
     */
    protected int nbVisibleChromosomes;
    /**
     * inventorySlotsIn
     * The first shown chromosome.
     */
    protected int chromosomeIndex = 0;
    protected int lastChromosomeIndex = 1;
    /**
     * The number of ticks since the last mouse movement.
     */
    protected float ticksSinceMovement = 0;
    /**
     * The last position of the mouse.
     */
    protected int lastMouseX = 0;
    protected int lastMouseY = 0;

    public static ResourceLocation BACKGROUNDTEXTURE = new ResourceLocation("testmod:textures/gui/background.png");

    protected GuiDnaMain container;

    GuiPage(GuiDnaMain container) {
        this.container = container;
    }

    public GuiDnaMain getContainer() {
        return this.container;
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    }

    public void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        int BackgroundRosterX = (getContainer().getWidth() - this.xWindowSize) / 2;
        int BackgroundRosterY = (getContainer().getHeight() - this.yWindowSize) / 2;
        getContainer().drawRectWithCustomSizedTexture(BackgroundRosterX, BackgroundRosterY, 0, 0, xWindowSize,
                yWindowSize, xWindowSize, yWindowSize, BACKGROUNDTEXTURE);
    }

    public abstract List<GuiButton> getButtonList();

    public abstract void initGui();

    public void commingFromOtherTab() {
    }

    public abstract void actionPerformed(GuiButton button);

    /**
     * Convert the given coordinates in the DNA window to world coordinates. Dna coordinates are given as:
     * 0----------------> x
     * |      DNA
     * |
     * |
     * v
     * y
     *
     * @param xCoordinate The x coordinate in DNA window coordinates.
     * @param yCoordinate The y coordinate in DNA window coordinates.
     * @return The world coordinates as {xCoordinate, yCoordinate}.
     */
    public int[] toWorldCoordinates(int xCoordinate, int yCoordinate) {
        int[] coordinates = {toWorldx(xCoordinate), toWorldy(yCoordinate)};
        return coordinates;
    }

    public int toWorldx(int xCoordinate) {
        return (getContainer().width - this.xWindowSize) / 2 + xCoordinate;
    }

    public int toWorldy(int yCoordinate) {
        return (getContainer().height - this.yWindowSize) / 2 + yCoordinate;
    }

    public int[] toWorldCoordinates(int[] coordinates) {
        return toWorldCoordinates(coordinates[0], coordinates[1]);
    }
}
