package com.quintenlauwers.interfaces;


import com.quintenlauwers.backend.DNAData;
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

    private List<GuiButton> buttonList = new ArrayList<GuiButton>();

    private GuiButton prevGene;
    private GuiButton nextGene;
    private GuiButton prevChromosome;
    private GuiButton nextChromosome;
    private GuiButton doneBtn;

    /**
     * The current active chromosme.
     */
    private int activeChromosome;
    private GuiClickableImage activeChromosomeButton = null;
    /**
     * The current active gene.
     */
    private boolean geneIsActive = false;
    private int activeGene;
    private GuiImageButton activeGeneButton = null;

    private GuiTextField text = null;


    private ArrayList<GuiButton> visibleChromosomes = new ArrayList<GuiButton>();
    private ArrayList<GuiButton> visibleGenes = new ArrayList<GuiButton>();

    private ArrayList<GuiTextField> textboxList = new ArrayList<GuiTextField>();

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
     *
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
        return chromosomeNumber >= this.chromosomeIndex && chromosomeNumber <= Math.min(DNA.getNbOfChromosomes(), chromosomeIndex + 2 * nbVisibleChromosomes);
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
        if (this.geneIsActive)
            drawNucleobasesOfGene();
        for (GuiTextField textbox : textboxList) {
            textbox.drawTextBox();
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
                    String text = DNA.getChromosmeDescription(button.id);
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
        if (button == this.doneBtn) {
            handleDoneButton(button);
        }
        if (visibleChromosomes.contains(button)) {
            handleChromosomeButton(button);
        }
        if (visibleGenes.contains(button)) {
            handleGeneButton(button);
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
            if (this.activeGeneButton != null) {
                this.activeGeneButton.releaseHold();
                geneIsActive = false;
            }
        }
        this.geneIndex = 0;
        this.lastGeneIndex = this.geneIndex + 1;
        this.activeChromosome = button.id;
        this.activeChromosomeButton = (GuiClickableImage) button;
        this.activeChromosomeButton.hold();
        this.drawGenesOfChromosome(activeChromosome);
        drawTextbox();
    }

    private void drawTextbox() {
        if (this.text == null) {
            this.textboxList.add(this.text = new GuiTextField(0, getContainer().getFontRendererObj(), toWorldx((this.xWindowSize - 130) / 2), yNucleobasePosition, 130, 20));
            this.text.setMaxStringLength(23);
            this.text.setText(DNA.getChromosmeDescription(this.activeChromosome));
            this.text.setFocused(true);
            this.buttonList.add(this.doneBtn = new GuiButton(0, toWorldx((this.xWindowSize - 100) / 2), yNucleobasePosition + 25, 100, 20, I18n.format("gui.write")));
        } else {
            this.text.setText(DNA.getChromosmeDescription(this.activeChromosome));
        }
    }

    /**
     * Highlights the proper gene button and activates the nucleobases.
     *
     * @param button The gene button pressed.
     */
    private void handleGeneButton(GuiButton button) {
        this.removeTextBox();
        if (this.activeGeneButton != null) {
            this.activeGeneButton.releaseHold();
        }
        this.activeGeneButton = (GuiImageButton) button;
        this.geneIsActive = true;
        this.activeGene = button.id;
        this.activeGeneButton.hold();
    }

    private void handleDoneButton(GuiButton button) {
        DNA.setChromosomeDescription(this.activeChromosome, this.text.getText());
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

    public void drawNucleobasesOfGene() {
        String nubleobases = DNA.getNucleobase(this.activeChromosome, this.activeGene);
        int yPosition = toWorldy(this.yNucleobasePosition - 20);
        int xBegin = toWorldx((this.xWindowSize - 10 * nubleobases.length() - 10 * (nubleobases.length() - 1)) / 2);
        for (int i = 0; i < nubleobases.length(); i++) {
            String letter = Character.toString(nubleobases.charAt(i));
            int xPosition = xBegin + i * 20;
            getContainer().getFontRendererObj().drawString(letter, xPosition, yPosition, 0x000000);
        }
    }


    protected void mouseClicked(int x, int y, int btn) throws IOException {
        for (GuiTextField textbox : this.textboxList) {
            textbox.mouseClicked(x, y, btn);
        }
    }


}
