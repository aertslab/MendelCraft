package com.quintenlauwers.interfaces.pages;


import com.quintenlauwers.backend.DNAData;
import com.quintenlauwers.backend.DnaProperties;
import com.quintenlauwers.backend.inventory.RestrictedSlot;
import com.quintenlauwers.backend.util.UtilDna;
import com.quintenlauwers.interfaces.GuiDnaMain;
import com.quintenlauwers.interfaces.custombuttons.GuiImageButton;
import com.quintenlauwers.interfaces.helpers.StoredRect;
import com.quintenlauwers.item.dnaSyringe;
import com.quintenlauwers.main.TestMod;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by quinten on 18/08/16.
 */
public class GuiEditDna extends GuiPage {
    DNAData DNA = new DNAData("chicken");

    protected List<GuiButton> buttonList = new ArrayList<GuiButton>();
    protected List<StoredRect> rectangleList = new ArrayList<StoredRect>();

    protected GuiButton prevGene;
    protected GuiButton nextGene;
    protected GuiButton prevChromosome;
    protected GuiButton nextChromosome;
    protected GuiButton doneBtn;

    /**
     * The current active chromosme.
     */
    protected int activeChromosome;
    private GuiImageButton activeChromosomeButton = null;
    /**
     * The current active gene.
     */
    protected boolean codonIsActive = false;
    protected int activeCodon;
    protected int dnaString = 1;
    protected GuiImageButton activeCodonButton = null;

    /**
     * The Y position of the first chomosome row in pixels.
     */
    protected int yChromosomePosition = 20;
    /**
     * The Y position of the first DNA row in pixels.
     */
    protected int yDNARowPosition = 90;
    /**
     * The Y posoition of the nucleobase row in pixels.
     */
    protected int yNucleobasePosition = 142;
    /**
     * The X size of the chromosome button in pixels.
     */
    private int xChromosomeSize = 20;
    /**
     * The Y size of the chromosome button in pixels.
     */
    private int yChromosomeSize = 20;
    /**
     * The maximum number of genes showed in a row at once.
     */
    protected int nbVisibleCodons;
    /**
     * The first shown codon.
     */
    protected int codonIndex = 0;
    private int lastCodonIndex = 1;
    /**
     * The maximum number of chromosomes showed in a row at once.
     */
    private int nbVisibleChromosomes;
    /**
     * The first shown chromosome.
     */
    private int chromosomeIndex = 0;
    protected int lastChromosomeIndex = 1;
    /**
     * Is there a valid syringe in the item slot
     */
    protected boolean isDnaVisible;
    /**
     * The properties of this animal.
     */
    protected DnaProperties properties;

    private GuiTextField text = null;


    protected ArrayList<GuiButton> visibleChromosomes = new ArrayList<GuiButton>();
    protected ArrayList<GuiButton> visibleCodons = new ArrayList<GuiButton>();
    protected ArrayList<GuiButton> visibleCodons2 = new ArrayList<GuiButton>();

    protected ArrayList<GuiTextField> textboxList = new ArrayList<GuiTextField>();

    /**
     * Textures used.
     */
    public static ResourceLocation BACKGROUNDTEXTURE = new ResourceLocation("testmod:textures/gui/background.png");
    public static ResourceLocation CHROMOSOMETEXTURE = new ResourceLocation("testmod:textures/gui/chromosomeButtons.png");
    public static ResourceLocation GENECOLORTEXTURE = new ResourceLocation("testmod:textures/gui/dnaButton.png");
    public static ResourceLocation GENEBACKGROUND = new ResourceLocation("testmod:textures/gui/geneBackgroundOrange.png");

    public static int NUMBEROFCHROMOSOMES = 30;

    public GuiEditDna(GuiDnaMain container) {

        super(container);
    }


    @Override
    public void initGui() {
        this.nbVisibleChromosomes = Math.min(5, TestMod.dnaConfig.getNbOfChromosomes());
        this.lastChromosomeIndex = this.chromosomeIndex + 1;
        drawChromosomes();
        this.nbVisibleCodons = Math.min(4, TestMod.dnaConfig.getNbOfCodonsInChromosome(0));

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
        int endIndex = Math.min(TestMod.dnaConfig.getNbOfChromosomes(), chromosomeIndex + 2 * nbVisibleChromosomes);
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
     *
     * @param chromosomeNumber
     */
    public GuiButton drawChromosomeButton(int chromosomeNumber) {
        GuiButton tempButton = null;
        if (!chromosomeIsVisible(chromosomeNumber))
            return tempButton;
        int j = chromosomeNumber - this.chromosomeIndex;
        int xPosition = toWorldx(22 + 28 * (j % this.nbVisibleChromosomes));
        int yPosition = toWorldy(this.yChromosomePosition + 25 * (j / this.nbVisibleChromosomes));
        int xInTexture = (chromosomeNumber % NUMBEROFCHROMOSOMES) * 20;
        this.buttonList.add(
                tempButton = new GuiImageButton(chromosomeNumber, xPosition, yPosition, xInTexture,
                        0, xChromosomeSize, yChromosomeSize, 600, 60, CHROMOSOMETEXTURE, ""));
        if (this.activeChromosomeButton != null) {
            if (this.activeChromosome == chromosomeNumber) {
                this.activeChromosomeButton = (GuiImageButton) tempButton;
                this.activeChromosomeButton.hold();
            }
        }
        return tempButton;
    }

    /**
     * Function to check if the button of the given chromosomeNumber should be visible.
     *
     * @param chromosomeNumber
     * @return Chromosome button should be visible
     */
    public boolean chromosomeIsVisible(int chromosomeNumber) {
        return chromosomeNumber >= this.chromosomeIndex && chromosomeNumber <= Math.min(
                TestMod.dnaConfig.getNbOfChromosomes(), chromosomeIndex + 2 * nbVisibleChromosomes);
    }

//    @Override
//    public void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
//        this.drawDNABackground();
//    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (!isDnaVisible) {
            return;
        }
        this.drawDNAText();
        this.drawChromosomes();
        this.drawRectangles();
//        this.hoverText(mouseX, mouseY, partialTicks);
        if (this.codonIsActive)
            drawNucleobasesOfCodon();
//        for (GuiTextField textbox : textboxList) {
//            textbox.drawTextBox();
//        }
    }

    public void drawRectangles() {
        for (StoredRect rect : rectangleList) {
            if (rect != null) {
                getContainer().drawRectWithCustomSizedTexture(rect);
            }
        }
    }

    @Override
    public List<GuiButton> getButtonList() {
        return this.buttonList;
    }


    public void updateScreen() {
        getContainer().updateScreen();
        for (GuiTextField textbox : textboxList) {
            textbox.updateCursorCounter();
        }
    }

    /**
     * Draw the name of a chromosome if the mouse stays still for over 10 ticks over a chromosomeButton.
     *
     * @param mouseX       The x position of the mouse.
     * @param mouseY       The y position of the mouse.
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
                    String text = "TestText"; // DNA.getChromosmeDescription(button.id);
                    ArrayList<String> stringList = new ArrayList<String>();
                    stringList.add(text);
                    getContainer().pubDrawHoveringText(stringList, mouseX, mouseY);
                }
            }
        }
    }

    /**
     * Draw the title of the DNA window.
     */
    protected void drawDNAText() {
        String DNAText = "DNA";
        int[] DNApositionTemp = this.toWorldCoordinates(this.xWindowSize / 2, 5);
        int[] DNAposition = {DNApositionTemp[0] - getContainer().getStringWidth(DNAText) / 2, DNApositionTemp[1]};
        getContainer().drawString(DNAText, DNAposition[0], DNAposition[1], 0x999999);
    }

    /**
     * Draw the gray background rectangle.
     */
    private void drawDNAWindow() {
        int BackgroundRosterX = (getContainer().getWidth() - this.xWindowSize) / 2;
        int BackgroundRosterY = (getContainer().getHeight() - this.yWindowSize) / 2;
        getContainer().drawRectWithCustomSizedTexture(BackgroundRosterX, BackgroundRosterY, 0, 0, xWindowSize,
                yWindowSize, xWindowSize, yWindowSize, BACKGROUNDTEXTURE);
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        for (GuiTextField textbox : textboxList) {
            textbox.textboxKeyTyped(typedChar, keyCode);
        }
    }

    public boolean doesGuiPauseGame() {
        return true;
    }

    @Override
    public void actionPerformed(GuiButton button) {
        if (!isDnaVisible) {
            return;
        }
        if (button == this.nextChromosome) {
            //Main.packetHandler.sendToServer(...);
            if ((this.chromosomeIndex + 2 * this.nbVisibleChromosomes) <= TestMod.dnaConfig.getNbOfChromosomes()) {
                this.chromosomeIndex++;
            }
        }
        if (button == this.prevChromosome) {
            if (this.chromosomeIndex > 0) {
                this.chromosomeIndex--;
            }
        }
        if (button == this.prevGene) {
            if (this.codonIndex > 0) {
                this.codonIndex--;
                this.drawCodonsOfChromosome(activeChromosome);
            }
        }
        if (button == this.nextGene) {
            if ((this.codonIndex + this.nbVisibleCodons) <= TestMod.dnaConfig.getNbOfCodonsInChromosome(this.activeChromosome)) {
                this.codonIndex++;
                this.drawCodonsOfChromosome(activeChromosome);
            }
        }
        if (button == this.doneBtn) {
            handleDoneButton(button);
        }
        if (visibleChromosomes.contains(button)) {
            handleChromosomeButton(button);
        }
        if (visibleCodons.contains(button)) {
            this.dnaString = 1;
            handleCodonButton(button);
        }
        if (visibleCodons2.contains(button)) {
            this.dnaString = 2;
            handleCodonButton(button);
        }

    }

    /**
     * Handles the proper chromosome button and activates the genes.
     *
     * @param button The chromosome button pressed.
     */
    private void handleChromosomeButton(GuiButton button) {
        if (this.activeChromosomeButton != null) {
            this.activeChromosomeButton.releaseHold();
            if (this.activeCodonButton != null) {
                this.activeCodonButton.releaseHold();
                codonIsActive = false;
            }
        }
        this.codonIndex = 0;
        this.lastCodonIndex = this.codonIndex + 1;
        this.activeChromosome = button.id;
        this.activeChromosomeButton = (GuiImageButton) button;
        this.activeChromosomeButton.hold();
        this.drawCodonsOfChromosome(activeChromosome);
//        drawTextbox();
    }

    private void drawTextbox() {
        if (this.text == null) {
            this.textboxList.add(this.text = new GuiTextField(0, getContainer().getFontRendererObj(), toWorldx((this.xWindowSize - 130) / 2), yNucleobasePosition + 20, 130, 20));
            this.text.setMaxStringLength(23);
            this.text.setText("Temp"); //DNA.getChromosmeDescription(this.activeChromosome));
            this.text.setFocused(true);
            this.buttonList.add(this.doneBtn = new GuiButton(0, toWorldx((this.xWindowSize - 100) / 2), yNucleobasePosition + 45, 100, 20, I18n.format("gui.write")));
        } else {
            this.text.setText("Othertest"); // DNA.getChromosmeDescription(this.activeChromosome));
        }
    }

    /**
     * Highlights the proper gene button and activates the nucleobases.
     *
     * @param button The gene button pressed.
     */
    private void handleCodonButton(GuiButton button) {
        this.removeTextBox();
        if (this.activeCodonButton != null) {
            this.activeCodonButton.releaseHold();
        }
        this.activeCodonButton = (GuiImageButton) button;
        this.codonIsActive = true;
        this.activeCodon = button.id;
        this.activeCodonButton.hold();
    }

    private void handleDoneButton(GuiButton button) {
        // DNA.setChromosomeDescription(this.activeChromosome, this.text.getText());
        this.text.setFocused(false);
    }

    private void removeTextBox() {
        if (this.text != null) {
            this.text.setFocused(false);
            this.textboxList.remove(this.text);
            this.text = null;
        }
        if (this.doneBtn != null) {
            this.buttonList.remove(doneBtn);
            this.doneBtn = null;
        }
    }

    @Override
    public void commingFromOtherTab() {
        if (getContainer().getInputSlot() != null) {
            System.out.println(getContainer().getInputSlot());
            if (getContainer().getInputSlot() != null) {
                System.out.println(getContainer().getInputSlot().getStack());
            }
        }
        if (getContainer().getInputSlot() != null
                && getContainer().getInputSlot().getHasStack()
                && getContainer().getInputSlot().getStack().getItem() instanceof dnaSyringe
                && this.getRawDna(1) != null) {
            makeDnaVisible();
            this.properties = new DnaProperties(this.getAnimalName(), this.getRawDna(1), this.getRawDna(2));
        } else {
            this.codonIsActive = false;
            makeDnaInvisible();
        }
    }

    public void makeDnaVisible() {
        this.prevChromosome.visible = true;
        this.nextChromosome.visible = true;
        isDnaVisible = true;
        drawChromosomes();
    }

    public void makeDnaInvisible() {
        this.prevChromosome.visible = false;
        this.nextChromosome.visible = false;
        this.prevGene.visible = false;
        this.nextGene.visible = false;
        this.buttonList.removeAll(visibleChromosomes);
        this.buttonList.removeAll(visibleCodons);
        this.buttonList.removeAll(visibleCodons2);
        this.rectangleList.clear();
        this.properties = null;
        this.activeCodonButton = null;
        this.activeChromosomeButton = null;
        visibleChromosomes.clear();
        visibleCodons.clear();
        visibleCodons2.clear();
        isDnaVisible = false;
        codonIsActive = false;
        lastChromosomeIndex = -1;
    }


    public void drawCodonsOfChromosome(int chromosomeNumber) {
        this.nbVisibleCodons = Math.min(4, TestMod.dnaConfig.getNbOfCodonsInChromosome(chromosomeNumber));
        this.prevGene.visible = true;
        this.nextGene.visible = true;
        if (codonIndex == lastCodonIndex) {
            return;
        }
        this.buttonList.removeAll(this.visibleCodons);
        this.buttonList.removeAll(this.visibleCodons2);
        this.rectangleList.clear();
        this.visibleCodons.clear();
        this.visibleCodons2.clear();

        if (!TestMod.dnaConfig.isDiploid()) {
            drawCodonRow(chromosomeNumber, this.visibleCodons, 1);
        } else {
            drawCodonRow(chromosomeNumber, this.visibleCodons, 0);
            drawCodonRow(chromosomeNumber, this.visibleCodons2, 2);
        }
        this.lastCodonIndex = this.codonIndex;
    }

    protected void drawCodonRow(int chromosomeNumber, List<GuiButton> codonButtonList, int row) {
        int endIndex = Math.min(TestMod.dnaConfig.getNbOfCodonsInChromosome(chromosomeNumber), codonIndex + nbVisibleCodons);
        drawGeneMarker(endIndex);
        GuiImageButton tempButton;
        int j = 0;
        for (int i = codonIndex; i < endIndex; i++) {
            int xPosition = toWorldx(20 + xButtonSize * j);
            int yPosition = toWorldy(yDNARowPosition) - 13 + 13 * row;
            this.buttonList.add(tempButton = new GuiImageButton(i, xPosition, yPosition, xButtonSize, yButtonSize, GENECOLORTEXTURE));
            codonButtonList.add(tempButton);
            if (this.codonIsActive) {
                if (this.activeChromosome == chromosomeNumber && this.activeCodon == i
                        && ((this.dnaString == 1 && row < 2) || (this.dnaString == row))) {
                    this.activeCodonButton = tempButton;
                    this.activeCodonButton.hold();
                }
            }
            j++;
        }
    }

    protected void drawGeneMarker(int endIndex) {
        int[] posBegin = TestMod.dnaConfig.positionFromCodonIndex(codonIndex);
        int[] posEnd = TestMod.dnaConfig.positionFromCodonIndex(endIndex);
        if (posEnd[0] > posBegin[0]) {
            posEnd[1] = TestMod.dnaConfig.positionFromCodonIndex(endIndex - 1)[1] + 1;
        }
        for (int geneNb = posBegin[1]; geneNb <= posEnd[1]; geneNb++) {
            System.out.println("Drawing gene starts.");
            int geneBegin = TestMod.dnaConfig.getCodonIndex(posBegin[0], geneNb, 0);
            int geneEnd = TestMod.dnaConfig.getCodonIndex(posBegin[0], geneNb + 1, 0) - 1;
            if (geneBegin >= codonIndex) {
                if (geneBegin < endIndex) {
                    System.out.println("start should be visible. " + geneBegin + "    " + codonIndex);
                    rectangleList.add(new StoredRect(
                            toWorldx(20 + xButtonSize * (geneBegin - codonIndex)),
                            toWorldy(yDNARowPosition - 15),
                            0,
                            0,
                            4,
                            50,
                            10,
                            50,
                            GENEBACKGROUND));
                }
            } else { // The gene begins before the first visible codon.
                rectangleList.add(new StoredRect(
                        toWorldx(20),
                        toWorldy(yDNARowPosition - 15),
                        8,
                        0,
                        4,
                        50,
                        20,
                        50,
                        GENEBACKGROUND));
            }
            if (geneEnd < endIndex) {
                rectangleList.add(new StoredRect(
                        toWorldx(20 + xButtonSize * (geneEnd - codonIndex + 1) - 4),
                        toWorldy(yDNARowPosition - 15),
                        6,
                        0,
                        4,
                        50,
                        10,
                        50,
                        GENEBACKGROUND));
            } else { // The ends after the last visible codon.
                if (geneBegin < endIndex) {
                    rectangleList.add(new StoredRect(
                            toWorldx(20 + xButtonSize * (endIndex - codonIndex) - 4),
                            toWorldy(yDNARowPosition - 15),
                            8,
                            0,
                            4,
                            50,
                            20,
                            50,
                            GENEBACKGROUND));
                }
            }
            int beginFiller = Math.max(geneBegin, codonIndex);
            int endFiller = Math.min(geneEnd + 1, endIndex);
            if (beginFiller < endIndex) {
                rectangleList.add(new StoredRect(
                        toWorldx(20 + xButtonSize * (beginFiller - codonIndex) + 4),
                        toWorldy(yDNARowPosition - 15),
                        800,
                        0,
                        xButtonSize * (endFiller - beginFiller) - 8,
                        50,
                        2000,
                        50,
                        GENEBACKGROUND));
            }
        }
    }


    public void drawNucleobasesOfCodon() {
        String nubleobases = getCodonAsString();
        int yPosition = toWorldy(this.yNucleobasePosition);
        int xBegin = toWorldx((this.xWindowSize - 10 * nubleobases.length() - 10 * (nubleobases.length() - 1)) / 2);
        for (int i = 0; i < nubleobases.length(); i++) {
            String letter = Character.toString(nubleobases.charAt(i));
            int xPosition = xBegin + i * 20 + 2;
            getContainer().getFontRendererObj().drawString(letter, xPosition, yPosition, 0x000000);
        }
    }

    protected String getCodonAsString() {
        String nubleobases = "";
        int currentCodonIndex = TestMod.dnaConfig.getCodonIndex(this.activeChromosome, this.activeCodon);
        byte[] dnaData = this.getRawDna(this.dnaString);
        if (dnaData != null && dnaData.length > currentCodonIndex) {
            nubleobases = UtilDna.byteNucleobaseToString(dnaData[currentCodonIndex]);
        }
        return nubleobases;
    }

    protected byte[] getRawDna(int dnaString) {
        RestrictedSlot slot = getContainer().getInputSlot();
        if (slot != null && slot.getHasStack() && slot.getStack().getItem() instanceof dnaSyringe) {
            dnaSyringe dnaSample = (dnaSyringe) slot.getStack().getItem();
            if (dnaString == 1) {
                return dnaSample.getDnaData(slot.getStack());
            }
            if (dnaString == 2) {
                return dnaSample.getDnaData2(slot.getStack());
            }
        }
        return null;
    }

    private String getAnimalName() {
        RestrictedSlot slot = getContainer().getInputSlot();
        if (slot != null && slot.getHasStack() && slot.getStack().getItem() instanceof dnaSyringe
                && slot.getStack().hasTagCompound() && slot.getStack().getTagCompound().hasKey("animal")) {
            return slot.getStack().getTagCompound().getString("animal");
        }
        return "";
    }


    protected void mouseClicked(int x, int y, int btn) throws IOException {
        for (GuiTextField textbox : this.textboxList) {
            textbox.mouseClicked(x, y, btn);
        }
    }


}
