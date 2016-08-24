package com.quintenlauwers.interfaces.pages;


import com.quintenlauwers.backend.DNAData;
import com.quintenlauwers.backend.inventory.RestrictedSlot;
import com.quintenlauwers.backend.util.UtilDna;
import com.quintenlauwers.interfaces.GuiDnaMain;
import com.quintenlauwers.interfaces.custombuttons.GuiClickableImage;
import com.quintenlauwers.interfaces.custombuttons.GuiImageButton;
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

    protected GuiButton prevGene;
    protected GuiButton nextGene;
    protected GuiButton prevChromosome;
    protected GuiButton nextChromosome;
    protected GuiButton doneBtn;

    /**
     * The current active chromosme.
     */
    protected int activeChromosome;
    private GuiClickableImage activeChromosomeButton = null;
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
    protected int yNucleobasePosition = 140;
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
    private int nbVisibleCodons;
    /**
     * The first shown codon.
     */
    private int codonIndex = 0;
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

    private GuiTextField text = null;


    protected ArrayList<GuiButton> visibleChromosomes = new ArrayList<GuiButton>();
    protected ArrayList<GuiButton> visibleCodons = new ArrayList<GuiButton>();
    protected ArrayList<GuiButton> visibleCodons2 = new ArrayList<GuiButton>();

    protected ArrayList<GuiTextField> textboxList = new ArrayList<GuiTextField>();

    /**
     * Textures used.
     */
    public static ResourceLocation BACKGROUNDTEXTURE = new ResourceLocation("testmod:textures/gui/background.png");
    public static ResourceLocation CHROMOSOMETEXTURE = new ResourceLocation("testmod:textures/gui/chromosomes.png");
    public static ResourceLocation GENECOLORTEXTURE = new ResourceLocation("testmod:textures/gui/DNAcolor.png");

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
        if (!chromosomeIsVisilbe(chromosomeNumber))
            return tempButton;
        int j = chromosomeNumber - this.chromosomeIndex;
        int xPosition = toWorldx(22 + 28 * (j % this.nbVisibleChromosomes));
        int yPosition = toWorldy(this.yChromosomePosition + 25 * (j / this.nbVisibleChromosomes));
        int xInTexture = (chromosomeNumber % NUMBEROFCHROMOSOMES) * 20;
        this.buttonList.add(tempButton = new GuiClickableImage(chromosomeNumber, xPosition, yPosition, xInTexture, 0, xChromosomeSize, yChromosomeSize, 600, 40, CHROMOSOMETEXTURE));
        if (this.activeChromosomeButton != null) {
            if (this.activeChromosome == chromosomeNumber) {
                this.activeChromosomeButton = (GuiClickableImage) tempButton;
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
    public boolean chromosomeIsVisilbe(int chromosomeNumber) {
        return chromosomeNumber >= this.chromosomeIndex && chromosomeNumber <= Math.min(TestMod.dnaConfig.getNbOfChromosomes(), chromosomeIndex + 2 * nbVisibleChromosomes);
    }

//    @Override
//    public void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
//        this.drawDNABackground();
//    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDNAText();
        this.drawChromosomes();
        this.hoverText(mouseX, mouseY, partialTicks);
        if (this.codonIsActive)
            drawNucleobasesOfCodon();
//        for (GuiTextField textbox : textboxList) {
//            textbox.drawTextBox();
//        }
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
    private void drawDNAText() {
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
        this.activeChromosomeButton = (GuiClickableImage) button;
        this.activeChromosomeButton.hold();
        this.drawCodonsOfChromosome(activeChromosome);
        drawTextbox();
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


    public void drawCodonsOfChromosome(int chromosomeNumber) {
        this.nbVisibleCodons = Math.min(4, TestMod.dnaConfig.getNbOfCodonsInChromosome(chromosomeNumber));
        this.prevGene.visible = true;
        this.nextGene.visible = true;
        if (codonIndex == lastCodonIndex) {
            return;
        }
        this.buttonList.removeAll(this.visibleCodons);
        this.visibleCodons.clear();

        if (!TestMod.dnaConfig.isDiploid()) {
            drawCodonRow(chromosomeNumber, this.visibleCodons, 1);
        } else {
            drawCodonRow(chromosomeNumber, this.visibleCodons, 0);
            drawCodonRow(chromosomeNumber, this.visibleCodons2, 2);
        }
        this.lastCodonIndex = this.codonIndex;
    }

    private void drawCodonRow(int chromosomeNumber, List<GuiButton> codonButtonList, int row) {
        int endIndex = Math.min(TestMod.dnaConfig.getNbOfCodonsInChromosome(chromosomeNumber), codonIndex + nbVisibleCodons);
        GuiButton tempButton;
        int j = 0;
        for (int i = codonIndex; i < endIndex; i++) {
            int xPosition = toWorldx(20 + xButtonSize * j);
            int yPosition = toWorldy(yDNARowPosition) - 13 + 13 * row;
            this.buttonList.add(tempButton = new GuiImageButton(i, xPosition, yPosition, xButtonSize, yButtonSize, GENECOLORTEXTURE));
            codonButtonList.add(tempButton);
            if (this.activeCodonButton != null) {
                if (this.activeChromosome == chromosomeNumber && this.activeCodon == i
                        && ((this.dnaString == 1 && row < 2) || (this.dnaString == row))) {
                    this.activeCodonButton = (GuiImageButton) tempButton;
                    this.activeCodonButton.hold();
                }
            }
            j++;
        }
    }


    public void drawNucleobasesOfCodon() {
        String nubleobases = getDnaString();
        int yPosition = toWorldy(this.yNucleobasePosition);
        int xBegin = toWorldx((this.xWindowSize - 10 * nubleobases.length() - 10 * (nubleobases.length() - 1)) / 2);
        for (int i = 0; i < nubleobases.length(); i++) {
            String letter = Character.toString(nubleobases.charAt(i));
            int xPosition = xBegin + i * 20;
            getContainer().getFontRendererObj().drawString(letter, xPosition, yPosition, 0x000000);
        }
    }

    private String getDnaString() {
        String nubleobases = "";
        int currentCodonIndex = TestMod.dnaConfig.getCodonIndex(this.activeChromosome, this.activeCodon);
        byte[] dnaData = this.getRawDna();
        if (dnaData != null && dnaData.length > currentCodonIndex) {
            nubleobases = UtilDna.byteNucleobaseToString(dnaData[currentCodonIndex]);
        }
        return nubleobases;
    }

    private byte[] getRawDna() {
        RestrictedSlot slot = getContainer().getInputSlot();
        if (slot != null && slot.getHasStack() && slot.getStack().getItem() instanceof dnaSyringe) {
            dnaSyringe dnaSample = (dnaSyringe) slot.getStack().getItem();
            if (this.dnaString == 1) {
                return dnaSample.getDnaData(slot.getStack());
            }
            if (this.dnaString == 2) {
                return dnaSample.getDnaData2(slot.getStack());
            }
        }
        return null;
    }


    protected void mouseClicked(int x, int y, int btn) throws IOException {
        for (GuiTextField textbox : this.textboxList) {
            textbox.mouseClicked(x, y, btn);
        }
    }


}
