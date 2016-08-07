package com.quintenlauwers.interfaces;

import java.io.IOException;
import java.util.ArrayList;

import com.quintenlauwers.backend.DNAData;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiTutorial extends GuiScreen {

    DNAData DNA = new DNAData();
    private GuiButton prevGene;
    private GuiButton nextGene;
    private GuiButton prevChromosome;
    private GuiButton nextChromosome;
    private GuiButton c;
    private GuiButton d;

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
     * The first shown chromosome.
     */
    protected int chromosomeIndex = 0;
    protected int lastChromosomeIndex = 1;
    /**
     * The number of ticks since the last mouse movement.
     */
    private float ticksSinceMovement = 0;
    /**
     * The last position of the mouse.
     */
    private int lastMouseX = 0;
    private int lastMouseY = 0;
    /**
     * The current active chromosme
     */
    private int activeChromosome;
    private GuiClickableImage activeChromosomeButton = null;


    protected ArrayList<GuiButton> visibleChromosomes = new ArrayList<GuiButton>();
    protected ArrayList<GuiButton> visibleGenes = new ArrayList<GuiButton>();

    /**
     * Textures used.
     */
    public static ResourceLocation BACKGROUNDTEXTURE = new ResourceLocation("testmod:textures/gui/background.png");
    public static ResourceLocation CHROMOSOMETEXTURE = new ResourceLocation("testmod:textures/gui/chromosomes.png");
    public static ResourceLocation GENECOLORTEXTURE = new ResourceLocation("testmod:textures/gui/DNAcolor.png");

    public static int NUMBEROFCHROMOSOMES = 30;

    @Override
    public void initGui() {
        this.nbVisibleChromosomes = Math.min(5, DNA.getNbOfChromosomes());
        this.lastChromosomeIndex = this.chromosomeIndex + 1;
        drawChromosomes();
        this.nbVisibleGenes = Math.min(4, DNA.getNbOfGenes(0));

        this.buttonList.add(this.prevGene = new GuiButton(0, toWorldx(10), toWorldy(this.yDNARowPosition), 10, this.yButtonSize, "<"));
        this.buttonList.add(this.nextGene = new GuiButton(1, toWorldx(this.xWindowSize - 20), toWorldy(this.yDNARowPosition), 10, this.yButtonSize, ">"));

        this.buttonList.add(this.prevChromosome = new GuiButton(0, toWorldx(10), toWorldy(30), 10, this.yButtonSize, "<"));
        this.buttonList.add(this.nextChromosome = new GuiButton(1, toWorldx(this.xWindowSize - 20), toWorldy(30), 10, this.yButtonSize, ">"));

        this.prevGene.visible = false;
        this.nextGene.visible = false;
    }


    /**
     * Draw all the chromosome buttons.
     */
    public void drawChromosomes() {
        if (chromosomeIndex == lastChromosomeIndex) {
            return;
        }
        GuiButton tempButton;
        this.buttonList.removeAll(this.visibleChromosomes);
        this.visibleChromosomes.clear();
        int endIndex = Math.min(DNA.getNbOfChromosomes(), chromosomeIndex + 2 * nbVisibleChromosomes);
        for (int i = chromosomeIndex; i < endIndex; i++) {
            tempButton = drawChromosomeButton(i);
            if (tempButton != null) {
                this.visibleChromosomes.add(tempButton);
            }
        }
        this.lastChromosomeIndex = this.chromosomeIndex;
    }

    /**
     * Draw the button of the given chromosomeNumber if it should be visible.
     * @param chromosomeNumber
     */
    public GuiButton drawChromosomeButton(int chromosomeNumber) {
        GuiButton tempButton = null;
        if (!chromosomeIsVisilbe(chromosomeNumber))
            return tempButton;
        int j = chromosomeNumber - this.chromosomeIndex;
        int xPosition = toWorldx(22 + 28 * (j % this.nbVisibleChromosomes));
        int yPosition = toWorldy(20 + 25 * (j / this.nbVisibleChromosomes));
        int xInTexture = (chromosomeNumber % NUMBEROFCHROMOSOMES) * 20;
        this.buttonList.add(tempButton = new GuiClickableImage(chromosomeNumber, xPosition, yPosition, xInTexture, 0, xChromosomeSize, yChromosomeSize, 600, 40, CHROMOSOMETEXTURE));
        return tempButton;
    }

    /**
     * Function to check if the button of the given chromosomeNumber should be visible.
     * @param chromosomeNumber
     * @return Chromosome button should be visible
     */
    public boolean chromosomeIsVisilbe(int chromosomeNumber) {
        return chromosomeNumber >= this.chromosomeIndex && chromosomeNumber <= Math.min(DNA.getNbOfChromosomes(), chromosomeIndex + 2 * nbVisibleChromosomes);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawDNABackground();
        this.drawChromosomes();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.hoverText(mouseX, mouseY, partialTicks);
    }

    /**
     * Draw the background of the DNA window, currently a gray rectangle with the title DNA.
     */
    private void drawDNABackground() {
        this.drawDNAWindow();
        this.drawDNAText();
    }

    /**
     * Draw the name of a chromosome if the mouse stays still for over 10 ticks over a chromosomeButton.
     * @param mouseX The x position of the mouse.
     * @param mouseY The y position of the mouse.
     * @param partialTicks The number of ticks passed since last calling this method.
     */
    private void hoverText(int mouseX, int mouseY, float partialTicks) {
        if (mouseX == this.lastMouseX && mouseY == this.lastMouseY) {
            this.ticksSinceMovement += partialTicks;
        } else {
            this.lastMouseX = mouseX;
            this.lastMouseY = mouseY;
            this.ticksSinceMovement = 0;
            return;
        }
        if (this.ticksSinceMovement > 10) {
            for (GuiButton button : visibleChromosomes) {
                if (button.isMouseOver()) {
                    System.out.println(button.id);
                    String text = DNA.getChromosmeDescription(button.id);
                    ArrayList<String> stringList = new ArrayList<String>();
                    stringList.add(text);
                    this.drawHoveringText(stringList, mouseX, mouseY);
                }
            }
        }
    }

    /**
     * Draw the title of the DNA window.
     */
    private void drawDNAText() {
        String DNAText = "DNA";
        int[] DNApositionTemp = this.toWorldCoordinates(this.xWindowSize / 2, 5);
        int[] DNAposition = {DNApositionTemp[0] - this.fontRendererObj.getStringWidth(DNAText) / 2, DNApositionTemp[1]};
        this.fontRendererObj.drawString(DNAText, DNAposition[0], DNAposition[1], 0x999999);
    }

    /**
     * Draw the gray background rectangle.
     */
    private void drawDNAWindow() {
        this.mc.getTextureManager().bindTexture(BACKGROUNDTEXTURE);
        int BackgroundRosterX = (this.width - this.xWindowSize) / 2;
        int BackgroundRosterY = (this.height - this.yWindowSize) / 2;
        drawModalRectWithCustomSizedTexture(BackgroundRosterX, BackgroundRosterY, 0, 0, xWindowSize,
                yWindowSize, xWindowSize, yWindowSize);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return true;
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button == this.nextChromosome) {
            //Main.packetHandler.sendToServer(...);
            if ((this.chromosomeIndex + 2 * this.nbVisibleChromosomes) <= DNA.getNbOfChromosomes()) {
                this.chromosomeIndex++;
            }
        }
        if (button == this.prevChromosome) {
            if (this.chromosomeIndex > 0) {
                this.chromosomeIndex--;
            }
        }
        if (button == this.prevGene) {
            if (this.geneIndex > 0) {
                this.geneIndex--;
                this.drawGenesOfChromosome(activeChromosome);
            }
        }
        if (button == this.nextGene) {
            if ((this.geneIndex + this.nbVisibleGenes) <= DNA.getNbOfGenes(this.activeChromosome)) {
                this.geneIndex++;
                this.drawGenesOfChromosome(activeChromosome);
            }
        }
        if (visibleChromosomes.contains(button)) {
            if (this.activeChromosomeButton != null) {
                this.activeChromosomeButton.releaseHold();
            }
            this.geneIndex = 0;
            this.lastGeneIndex = this.geneIndex + 1;
            this.activeChromosome = button.id;
            this.activeChromosomeButton = (GuiClickableImage) button;
            this.activeChromosomeButton.hold();
            this.drawGenesOfChromosome(activeChromosome);
        }
        if (visibleGenes.contains(button)){
        }
    }

    public void drawGenesOfChromosome(int chromosomeNumber) {
        this.nbVisibleGenes = Math.min(4, DNA.getNbOfGenes(chromosomeNumber));
        this.prevGene.visible = true;
        this.nextGene.visible = true;
        if (geneIndex == lastGeneIndex) {
            return;
        }
        GuiButton tempButton;
        this.buttonList.removeAll(this.visibleGenes);
        this.visibleGenes.clear();
        int endIndex = Math.min(DNA.getNbOfGenes(chromosomeNumber), geneIndex + nbVisibleGenes);
        int j = 0;
        for (int i = geneIndex; i < endIndex; i++) {
            int xPosition = toWorldx(20 + xButtonSize * j);
            int yPosition = toWorldy(yDNARowPosition);
            this.buttonList.add(tempButton = new GuiImageButton(i, xPosition, yPosition, xButtonSize, yButtonSize, GENECOLORTEXTURE));
            this.visibleGenes.add(tempButton);
            j++;
        }
        this.lastGeneIndex = this.geneIndex;
    }

    public void drawNucleobasesOfGene(int chromosomeNumber, int geneNumber) {

    }

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
        return (this.width - this.xWindowSize) / 2 + xCoordinate;
    }

    public int toWorldy(int yCoordinate) {
        return (this.height - this.yWindowSize) / 2 + yCoordinate;
    }

    public int[] toWorldCoordinates(int[] coordinates) {
        return toWorldCoordinates(coordinates[0], coordinates[1]);
    }
}
