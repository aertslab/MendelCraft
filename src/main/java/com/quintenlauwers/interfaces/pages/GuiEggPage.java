package com.quintenlauwers.interfaces.pages;

import com.quintenlauwers.backend.DnaProperties;
import com.quintenlauwers.backend.inventory.RestrictedSlot;
import com.quintenlauwers.backend.inventory.TakeOnlySlot;
import com.quintenlauwers.backend.network.slotcontents.SlotContentsToServerPackage;
import com.quintenlauwers.backend.util.UtilDna;
import com.quintenlauwers.entity.EntityDnaChicken;
import com.quintenlauwers.interfaces.GuiDnaMain;
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

import java.util.Arrays;
import java.util.List;

import static java.lang.Math.abs;

/**
 * Created by quinten on 20/08/16.
 */
public class GuiEggPage extends GuiEditDna {

    public static ResourceLocation CONTAINERBACKGROUND = new ResourceLocation("testmod:textures/gui/background.png");
    GuiButton createEggButton;
    boolean isDnaVisible;
    byte[] dnaData;
    String[] possibleCodons;
    int possibleCodonIndex = 0;

    int codonButtonWiddth = 30;
    GuiButton nextCodon;
    GuiButton previousCodon;
    DnaProperties properties;

    public GuiEggPage(GuiDnaMain screen) {
        super(screen);
        this.yDNARowPosition = 90;
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
                this.createEggButton = new GuiButton(5, toWorldx((this.xWindowSize - 100) / 2), toWorldy(10),
                        100, 20, I18n.format("gui.makeEgg")));
        this.buttonList.add(
                this.nextCodon = new GuiButton(6, toWorldx((this.xWindowSize - codonButtonWiddth) / 2),
                        toWorldy(yNucleobasePosition - 10), this.codonButtonWiddth, 10, "^"));
        this.buttonList.add(
                this.previousCodon = new GuiButton(7, toWorldx((this.xWindowSize - codonButtonWiddth) / 2),
                        toWorldy(yNucleobasePosition + 10), this.codonButtonWiddth, 10, "v"));
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
            this.dnaData = getContainer().getOutputSlot().getStack().getTagCompound()
                    .getCompoundTag("EntityTag").getByteArray("dnaData");
            makeDnaVisible();
        } else {
            if (canCreateEgg()) {
                this.createEggButton.displayString = I18n.format("gui.makeEgg");
                this.createEggButton.visible = true;
                this.codonIsActive = false;
            } else {
                this.createEggButton.visible = false;
                this.codonIsActive = false;
            }
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
        this.previousCodon.visible = false;
        this.nextCodon.visible = false;
        this.buttonList.removeAll(visibleChromosomes);
        this.properties = null;
        visibleChromosomes.clear();
        isDnaVisible = false;
        codonIsActive = false;
        lastChromosomeIndex = -1;
    }

    @Override
    public void actionPerformed(GuiButton button) {
        if (this.createEggButton != null && button == this.createEggButton) {
            createEgg();
        }
        if (button == this.nextCodon && this.properties != null) {
            System.out.println("Next codon");
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

    private void changeCodon(codonChangeDirection direction) {
        int codonPosition = TestMod.dnaConfig.getCodonIndex(this.activeChromosome, this.activeCodon);
        System.out.println("Codonindex is " + codonPosition);
        if (codonPosition >= dnaData.length) {
            return;
        }
        if (this.possibleCodons == null) {
            int[] position = TestMod.dnaConfig.positionFromCodonIndex(codonPosition);
            // TODO: review, seems inefficient.
            possibleCodons = this.properties.getPossibleCodons(position);
        }
        System.out.println(Arrays.toString(possibleCodons));
        if (possibleCodons == null) {
            return;
        }
        changeCodonIndex(direction);
        System.out.println("String index is " + possibleCodonIndex);
        byte dnaChange = UtilDna.stringNucleobaseToByte(possibleCodons[this.possibleCodonIndex]);
        if (dnaChange == dnaData[codonPosition]) {
            changeCodonIndex(direction);
            dnaChange = UtilDna.stringNucleobaseToByte(possibleCodons[this.possibleCodonIndex]);
        }
        dnaData[codonPosition] = dnaChange;
        writeToEgg(dnaData);
    }


    private void changeCodonIndex(codonChangeDirection direction) {
        System.out.println("changing direction" + direction);
        if (codonChangeDirection.UP.equals(direction)) {
            System.out.println("up");
            this.possibleCodonIndex = abs(possibleCodonIndex++) % possibleCodons.length;
        }
        if (codonChangeDirection.DOWN.equals(direction)) {
            this.possibleCodonIndex = abs(possibleCodonIndex-- % possibleCodons.length);
        }
    }

    public void createEgg() {
        System.out.println("Create egg");
        RestrictedSlot[] slots = getContainer().getCombinedInputSlots();
        if (slots != null && slots[0] != null && slots[0].getHasStack()) {
            ItemStack syringe = slots[0].getStack();
            if (syringe.getItem() instanceof dnaSyringe && syringe.hasTagCompound()) {
                NBTTagCompound syringeTag = syringe.getTagCompound();
                if (!syringeTag.hasKey("dnaData")) {
                    return;
                }
                byte[] dnaData = syringeTag.getByteArray("dnaData").clone();
                this.properties = new DnaProperties("chicken", dnaData);
                writeToEgg(dnaData);
                this.createEggButton.displayString = I18n.format("gui.remakeEgg");
                // TODO: make universal for any animal.
                makeDnaVisible();

            }
        }
    }

    /**
     * Creates automaticly an egg if no egg exists yet.
     *
     * @param dnaData
     */
    public void writeToEgg(byte[] dnaData) {
        if (dnaData == null) {
            return;
        }
        String entityName = EntityList.getEntityStringFromClass(EntityDnaChicken.class);
        ItemStack resultStack = new ItemStack(Items.SPAWN_EGG);
        net.minecraft.item.ItemMonsterPlacer.applyEntityIdToItemStack(resultStack, entityName);
        resultStack.getTagCompound().getCompoundTag("EntityTag").setByteArray("dnaData", dnaData);
        // TODO: make this work if the server client delay is bigger.
        TestMod.network.sendToServer(new SlotContentsToServerPackage(resultStack, 38));
        this.dnaData = dnaData;
        this.properties.setDna(dnaData);
    }

    public boolean canCreateEgg() {
        RestrictedSlot[] slots = getContainer().getCombinedInputSlots();
        if (slots != null && slots[0] != null && slots[0].getHasStack()) {
            if (slots[0].getStack().hasTagCompound() && slots[0].getStack().getTagCompound().hasKey("dnaData")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void drawNucleobasesOfCodon() {
        String nubleobases = "";
        int codonIndex = TestMod.dnaConfig.getCodonIndex(this.activeChromosome, this.activeCodon);
        TakeOnlySlot slot = getContainer().getOutputSlot();
        if (slot != null && slot.getHasStack() && slot.getStack().getItem() instanceof ItemMonsterPlacer) {
            ItemStack stack = slot.getStack();
            if (stack.hasTagCompound() && stack.getTagCompound().hasKey("EntityTag")) {
                NBTTagCompound entityTag = stack.getTagCompound().getCompoundTag("EntityTag");
                if (entityTag.hasKey("dnaData")) {
                    byte[] dnaData = entityTag.getByteArray("dnaData");
                    if (dnaData != null && dnaData.length > codonIndex) {
                        nubleobases = UtilDna.byteNucleobaseToString(dnaData[codonIndex]);
                        if (properties == null) {
                            properties = new DnaProperties("chicken", dnaData);
                            // TODO: Make universal for any animal.
                        }
                        this.previousCodon.visible = true;
                        this.nextCodon.visible = true;
                    } else {
                        System.err.println("Dnadata null our trying to acces out of range.");
                    }
                } else {
                    System.err.println("Has no dnaData.");
                }
            } else {
                System.err.println("has no entitytag");
            }
        } else {
            System.err.println("Item is gone");
        }
        int yPosition = toWorldy(this.yNucleobasePosition);
        int xBegin = toWorldx((this.xWindowSize - 10 * nubleobases.length() - 10 * (nubleobases.length() - 1)) / 2);
        for (int i = 0; i < nubleobases.length(); i++) {
            String letter = Character.toString(nubleobases.charAt(i));
            int xPosition = xBegin + i * 20;
            getContainer().getFontRendererObj().drawString(letter, xPosition, yPosition, 0x000000);
        }
    }

    enum codonChangeDirection {
        UP, DOWN
    }
}
