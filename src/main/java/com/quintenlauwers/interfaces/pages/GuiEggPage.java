package com.quintenlauwers.interfaces.pages;

import com.quintenlauwers.backend.DnaProperties;
import com.quintenlauwers.backend.inventory.RestrictedSlot;
import com.quintenlauwers.backend.inventory.TakeOnlySlot;
import com.quintenlauwers.backend.network.slotcontents.SlotContentsToServerPackage;
import com.quintenlauwers.backend.util.UtilDna;
import com.quintenlauwers.entity.chicken.EntityDnaChicken;
import com.quintenlauwers.interfaces.GuiDnaMain;
import com.quintenlauwers.interfaces.custombuttons.GuiImageButton;
import com.quintenlauwers.item.dnaSyringe;
import com.quintenlauwers.main.TestMod;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityList;
import net.minecraft.init.Items;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import java.util.List;

import static java.lang.Math.abs;

/**
 * Created by quinten on 20/08/16.
 */
public class GuiEggPage extends GuiEditDna {

    public static ResourceLocation CONTAINERBACKGROUND = new ResourceLocation("testmod:textures/gui/background.png");
    public static ResourceLocation UPCODON = new ResourceLocation("testmod:textures/gui/upCodon.png");
    public static ResourceLocation DOWNCODON = new ResourceLocation("testmod:textures/gui/downCodon.png");
    public static ResourceLocation GENEEDITTEXTURE = new ResourceLocation("testmod:textures/gui/dnaButtonEdit.png");

    GuiButton createEggButton;
    GuiButton cloneButton;
    byte[] dnaData;
    byte[] dnaData2;
    String[] possibleCodons;
    int possibleCodonIndex = 0;

    int codonButtonWiddth = 30;
    GuiButton nextCodon;
    GuiButton previousCodon;

    public GuiEggPage(GuiDnaMain screen) {
        super(screen);
        this.yDNARowPosition = 92;
        this.yChromosomePosition = 30;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (isDnaVisible) {
            super.drawScreen(mouseX, mouseY, partialTicks);
        }
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
        return buttonList;
    }

    @Override
    public void initGui() {
        super.initGui();
        for (GuiButton button : buttonList) {
            button.visible = true;
        }
        this.buttonList.add(
                this.createEggButton = new GuiButton(5, toWorldx((this.xWindowSize) / 2), toWorldy(5),
                        this.xWindowSize / 2 - 5, 20, I18n.format("gui.makeEgg")));
        this.buttonList.add(
                this.cloneButton = new GuiButton(8, toWorldx(5), toWorldy(5),
                        this.xWindowSize / 2 - 5, 20, I18n.format("gui.clone")));
        this.buttonList.add(
                this.nextCodon = new GuiImageButton(6, toWorldx((this.xWindowSize - codonButtonWiddth) / 2),
                        toWorldy(yNucleobasePosition - 12), 0, 0, this.codonButtonWiddth, 10, this.codonButtonWiddth, 30, UPCODON, "", 10));
        this.buttonList.add(
                this.previousCodon = new GuiImageButton(7, toWorldx((this.xWindowSize - codonButtonWiddth) / 2),
                        toWorldy(yNucleobasePosition + 10), 0, 0, this.codonButtonWiddth, 10, this.codonButtonWiddth, 30, DOWNCODON, "", 10));
        this.nextCodon.visible = false;
        this.previousCodon.visible = false;
    }

    @Override
    public void commingFromOtherTab() {
        if (getContainer().getOutputSlot() != null
                && getContainer().getOutputSlot().getHasStack()
                && getContainer().getOutputSlot().getStack().getItem() instanceof ItemMonsterPlacer) {
            this.createEggButton.displayString = I18n.format("gui.remakeEgg");
            this.createEggButton.visible = true;
            this.cloneButton.visible = true;
            this.dnaData = getContainer().getOutputSlot().getStack().getTagCompound()
                    .getCompoundTag("EntityTag").getByteArray("dnaData");
            makeDnaVisible();
        } else {
            if (canCreateEgg()) {
                this.createEggButton.displayString = I18n.format("gui.makeEgg");
                this.createEggButton.visible = true;
                this.cloneButton.visible = true;
                this.codonIsActive = false;
            } else if (canCloneDna()) {
                this.createEggButton.visible = false;
                this.cloneButton.visible = true;
                this.codonIsActive = false;
            } else {
                this.createEggButton.visible = false;
                this.cloneButton.visible = false;
                this.codonIsActive = false;
            }
            makeDnaInvisible();
        }
    }

    @Override
    public void makeDnaInvisible() {
        this.previousCodon.visible = false;
        this.nextCodon.visible = false;
//        this.properties = null;
        super.makeDnaInvisible();
    }

    @Override
    protected void drawDNAText() {
    }

    @Override
    public void actionPerformed(GuiButton button) {
        if (this.cloneButton != null && button == this.cloneButton) {
            cloneSyringe();
        }
        if (this.createEggButton != null && button == this.createEggButton) {
            createEgg();
        }
        if (button == this.nextCodon && this.properties != null) {
            changeCodon(codonChangeDirection.UP);
        }
        if (button == this.previousCodon && this.properties != null) {
            changeCodon(codonChangeDirection.DOWN);
        }
        if (this.visibleCodons.contains(button) && button != this.activeCodonButton) {
            this.possibleCodons = null;
        }
        if (isDnaVisible) {
            super.actionPerformed(button);
        }
    }

    protected void drawCodonRow(int chromosomeNumber, List<GuiButton> codonButtonList, int row) {
        int endIndex = Math.min(TestMod.dnaConfig.getNbOfCodonsInChromosome(chromosomeNumber), codonIndex + nbVisibleCodons);
        drawGeneMarker(endIndex);
        GuiImageButton tempButton;
        int j = 0;
        for (int i = codonIndex; i < endIndex; i++) {
            int xPosition = toWorldx(20 + xButtonSize * j);
            int yPosition = toWorldy(yDNARowPosition) - 13 + 13 * row;
            if (this.properties.isEditablePosition(TestMod.dnaConfig.positionFromCodonIndex(i))) {
                tempButton = new GuiImageButton(i, xPosition, yPosition, xButtonSize, yButtonSize, GENEEDITTEXTURE);
            } else {
                tempButton = new GuiImageButton(i, xPosition, yPosition, xButtonSize, yButtonSize, GENECOLORTEXTURE);
            }
            this.buttonList.add(tempButton);
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

    private void changeCodon(codonChangeDirection direction) {
        int codonPosition = TestMod.dnaConfig.getCodonIndex(this.activeChromosome, this.activeCodon);
        if (codonPosition >= dnaData.length) {
            return;
        }
        if (this.possibleCodons == null) {
            int[] position = TestMod.dnaConfig.positionFromCodonIndex(codonPosition);
            // TODO: review, seems inefficient.
            possibleCodons = this.properties.getPossibleCodons(position);
        }
        if (possibleCodons == null) {
            return;
        }
        changeCodonIndex(direction);
        byte[] tempDna = this.dnaData;
        if (dnaString == 2) {
            tempDna = this.dnaData2;
        }
        byte dnaChange = UtilDna.stringNucleobaseToByte(possibleCodons[this.possibleCodonIndex]);
        if (dnaChange == tempDna[codonPosition]) {
            changeCodonIndex(direction);
            dnaChange = UtilDna.stringNucleobaseToByte(possibleCodons[this.possibleCodonIndex]);
        }
        tempDna[codonPosition] = dnaChange;
        writeToEgg(this.dnaData, this.dnaData2);
    }


    private void changeCodonIndex(codonChangeDirection direction) {
        if (codonChangeDirection.UP.equals(direction)) {
            this.possibleCodonIndex = abs(++possibleCodonIndex) % possibleCodons.length;
        }
        if (codonChangeDirection.DOWN.equals(direction)) {
            this.possibleCodonIndex = abs(--possibleCodonIndex % possibleCodons.length);
        }
    }

    public void cloneSyringe() {
        RestrictedSlot[] slots = getContainer().getCombinedInputSlots();
        if (slots != null && slots[0] != null && slots[0].getHasStack()) {
            ItemStack syringe = slots[0].getStack();
            if (syringe.getItem() instanceof dnaSyringe && syringe.hasTagCompound()) {
                NBTTagCompound syringeTag = syringe.getTagCompound();
                if (!syringeTag.hasKey("dnaData")) {
                    return;
                }
                byte[] dnaData = syringeTag.getByteArray("dnaData").clone();
                byte[] dnaData2 = null;
                String animal = "chicken";
                if (syringeTag.hasKey("animal")) {
                    animal = syringeTag.getString("animal");
                }
                if (syringeTag.hasKey("dnaData2")) {
                    dnaData2 = syringeTag.getByteArray("dnaData2").clone();
                    this.properties = new DnaProperties(animal, dnaData, dnaData2);
                } else {
                    this.properties = new DnaProperties(animal, dnaData);
                }
                writeToEgg(dnaData, dnaData2);
                this.createEggButton.displayString = I18n.format("gui.remakeEgg");
                makeDnaVisible();

            }
        }
    }

    public void createEgg() {
        RestrictedSlot[] slots = getContainer().getCombinedInputSlots();
        if (canCreateEgg()) {
            ItemStack syringe1 = slots[0].getStack();
            ItemStack syringe2 = slots[1].getStack();
            if (syringe1.getItem() instanceof dnaSyringe && syringe1.hasTagCompound()) {
                NBTTagCompound syringeTag1 = syringe1.getTagCompound();
                NBTTagCompound syringeTag2 = syringe2.getTagCompound();
                if (syringeTag1 == null || syringeTag2 == null) {
                    return;
                }
                byte[] dnaData11 = getDna1FromTag(syringeTag1);
                byte[] dnaData12 = getDna2FromTag(syringeTag1);
                String animal1 = getAnimalFromTag(syringeTag1);
                byte[] dnaData21 = getDna1FromTag(syringeTag2);
                byte[] dnaData22 = getDna2FromTag(syringeTag2);
                String animal2 = getAnimalFromTag(syringeTag2);
                if (animal1 == null || !animal1.equals(animal2)) {
                    return;
                }
                byte[] dnaFinal1;
                byte[] dnaFinal2 = null;
                if (TestMod.dnaConfig.isDiploid()) {
                    dnaFinal1 = TestMod.dnaConfig.reduceToSingleDnaString(dnaData11, dnaData12);
                    dnaFinal2 = TestMod.dnaConfig.reduceToSingleDnaString(dnaData21, dnaData22);
                    if (dnaFinal1 == null || dnaFinal2 == null) {
                        return;
                    }
                    this.properties = new DnaProperties(animal1, dnaFinal1, dnaFinal2);
                } else {
                    dnaFinal1 = TestMod.dnaConfig.reduceToSingleDnaString(dnaData11, dnaData21);
                    if (dnaFinal1 == null) {
                        return;
                    }
                    this.properties = new DnaProperties(animal1, dnaFinal1);
                }
                writeToEgg(dnaFinal1, dnaFinal2);
                this.createEggButton.displayString = I18n.format("gui.remakeEgg");
                makeDnaVisible();

            }
        }
    }

    private byte[] getDna1FromTag(NBTTagCompound tag) {
        if (!tag.hasKey("dnaData")) {
            return null;
        }
        return tag.getByteArray("dnaData").clone();
    }

    private byte[] getDna2FromTag(NBTTagCompound tag) {
        if (!tag.hasKey("dnaData2")) {
            return null;
        }
        return tag.getByteArray("dnaData2").clone();
    }

    private String getAnimalFromTag(NBTTagCompound tag) {
        if (!tag.hasKey("animal")) {
            return null;
        }
        return tag.getString("animal");
    }

    /**
     * Creates automaticly an egg if no egg exists yet.
     *
     * @param dnaData
     */
    public void writeToEgg(byte[] dnaData, byte[] dnaData2) {
        if (dnaData == null) {
            return;
        }
        String entityName = EntityList.getEntityStringFromClass(EntityDnaChicken.class);
        ItemStack resultStack = new ItemStack(Items.SPAWN_EGG);
        net.minecraft.item.ItemMonsterPlacer.applyEntityIdToItemStack(resultStack, entityName);
        resultStack.getTagCompound().getCompoundTag("EntityTag").setByteArray("dnaData", dnaData);
        if (dnaData2 != null) {
            resultStack.getTagCompound().getCompoundTag("EntityTag").setByteArray("dnaData2", dnaData2);
        }
        // TODO: make this work if the server client delay is bigger.
        TestMod.network.sendToServer(new SlotContentsToServerPackage(resultStack, 2));
        this.dnaData = dnaData;
        this.dnaData2 = dnaData2;
        this.properties.setDna(dnaData);
        this.properties.setDna2(dnaData2);
    }

    public boolean canCreateEgg() {
        RestrictedSlot[] slots = getContainer().getCombinedInputSlots();
        if (slots != null && slots[1] != null && slots[1].getHasStack()) {
            if (slots[1].getStack().hasTagCompound() && slots[1].getStack().getTagCompound().hasKey("dnaData") && canCloneDna()) {
                NBTTagCompound tag1 = slots[0].getStack().getTagCompound();
                NBTTagCompound tag2 = slots[0].getStack().getTagCompound();
                return (tag1.hasKey("animal") && tag2.hasKey("animal") && tag1.getString("animal") == tag2.getString("animal"));
            }
        }
        return false;
    }

    public boolean canCloneDna() {
        RestrictedSlot[] slots = getContainer().getCombinedInputSlots();
        if (slots != null && slots[0] != null && slots[0].getHasStack()) {
            if (slots[0].getStack().hasTagCompound() && slots[0].getStack().getTagCompound().hasKey("dnaData")) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected String getCodonAsString() {
        String nubleobases = "";
        int currentCodonIndex = TestMod.dnaConfig.getCodonIndex(this.activeChromosome, this.activeCodon);
        byte[] dnaData = this.getRawDna(this.dnaString);
        if (dnaData != null && dnaData.length > currentCodonIndex) {
            nubleobases = UtilDna.byteNucleobaseToString(dnaData[currentCodonIndex]);
            if (properties == null) {
                if (TestMod.dnaConfig.isDiploid()) {
                    byte[] dnaData2;
                    if (this.dnaString < 2) {
                        dnaData2 = getRawDna(2);
                    } else {
                        dnaData2 = dnaData;
                        dnaData = getRawDna(1);
                    }
                    properties = new DnaProperties(getAnimalName(), dnaData, dnaData2);

                } else {
                    properties = new DnaProperties(getAnimalName(), dnaData);
                }
            }
            if (properties.isEditablePosition(TestMod.dnaConfig.positionFromCodonIndex(currentCodonIndex))) {
                this.previousCodon.visible = true;
                this.nextCodon.visible = true;
            } else {
                this.previousCodon.visible = false;
                this.nextCodon.visible = false;
            }
        }
        return nubleobases;
    }

    @Override
    protected byte[] getRawDna(int dnaString) {
        TakeOnlySlot slot = getContainer().getOutputSlot();
        if (slot != null && slot.getHasStack() && slot.getStack().getItem() instanceof ItemMonsterPlacer) {
            ItemStack stack = slot.getStack();
            if (stack.hasTagCompound() && stack.getTagCompound().hasKey("EntityTag")) {
                NBTTagCompound entityTag = stack.getTagCompound().getCompoundTag("EntityTag");
                if (dnaString == 1) {
                    if (entityTag.hasKey("dnaData")) {
                        return entityTag.getByteArray("dnaData");
                    }
                }
                if (dnaString == 2) {
                    if (entityTag.hasKey("dnaData2")) {
                        return entityTag.getByteArray("dnaData2");
                    }
                }
            }
        }
        return null;
    }

    private String getAnimalName() {
        TakeOnlySlot slot = getContainer().getOutputSlot();
        if (slot != null && slot.getHasStack() && slot.getStack().getItem() instanceof ItemMonsterPlacer) {
            ItemStack stack = slot.getStack();
            if (stack.hasTagCompound() && stack.getTagCompound().hasKey("EntityTag")) {
                NBTTagCompound entityTag = stack.getTagCompound().getCompoundTag("EntityTag");
                if (entityTag.hasKey("animal")) {
                    return entityTag.getString("animal");
                }
            }
        }
        return null;
    }


    enum codonChangeDirection {
        UP, DOWN
    }
}
