package com.quintenlauwers.interfaces.pages;

import com.quintenlauwers.backend.inventory.RestrictedSlot;
import com.quintenlauwers.backend.network.slotcontents.SlotContentsToServerPackage;
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

import java.util.List;

/**
 * Created by quinten on 20/08/16.
 */
public class GuiEggPage extends GuiEditDna {

    public static ResourceLocation CONTAINERBACKGROUND = new ResourceLocation("testmod:textures/gui/background.png");
    GuiButton createEgg;
    byte[] dnaData;

    public GuiEggPage(GuiDnaMain screen) {
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
        return buttonList;
    }

    @Override
    public void initGui() {
        super.initGui();
        for (GuiButton button : buttonList) {
            button.visible = true;
        }
        this.buttonList.add(this.createEgg = new GuiButton(0, toWorldx((this.xWindowSize - 100) / 2), toWorldy(10), 100, 20, I18n.format("gui.makeEgg")));
    }

    @Override
    public void commingFromOtherTab() {
        if (getContainer().getOutputSlot() != null
                && getContainer().getOutputSlot().getHasStack()
                && getContainer().getOutputSlot().getStack().getItem() instanceof ItemMonsterPlacer) {
            this.createEgg.displayString = I18n.format("gui.remakeEgg");
            this.createEgg.visible = true;
            this.dnaData = getContainer().getOutputSlot().getStack().getTagCompound()
                    .getCompoundTag("EntityTag").getByteArray("dnaData");
            makeDnaVisible();
        } else {
            if (canCreateEgg()) {
                this.createEgg.displayString = I18n.format("gui.makeEgg");
                this.createEgg.visible = true;
            } else {
                this.createEgg.visible = false;
            }
            makeDnaInvisible();
        }
    }

    public void makeDnaVisible() {
        System.out.println("Making dna visible.");
        this.prevChromosome.visible = true;
        this.nextChromosome.visible = true;
        drawChromosomes();
    }

    public void makeDnaInvisible() {
        this.prevChromosome.visible = false;
        this.nextChromosome.visible = false;
        this.buttonList.removeAll(visibleChromosomes);
    }

    @Override
    public void actionPerformed(GuiButton button) {
        if (this.createEgg != null && button == this.createEgg) {
            System.out.println("Create egg");
            RestrictedSlot[] slots = getContainer().getCombinedInputSlots();
            if (slots != null && slots[0] != null && slots[0].getHasStack()) {
                ItemStack syringe = slots[0].getStack();
                if (syringe.getItem() instanceof dnaSyringe && syringe.hasTagCompound()) {
                    NBTTagCompound syringeTag = syringe.getTagCompound();
                    if (!syringeTag.hasKey("dnaData")) {
                        return;
                    }
                    writeToEgg(syringeTag.getByteArray("dnaData"));
                    this.createEgg.displayString = I18n.format("gui.remakeEgg");
                    makeDnaVisible();

                }
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
        TestMod.network.sendToServer(new SlotContentsToServerPackage(resultStack, 102));
        this.dnaData = dnaData;
        // TODO: don't put result directly in player inventory.
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
}
