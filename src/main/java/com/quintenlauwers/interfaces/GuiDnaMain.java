package com.quintenlauwers.interfaces;

import com.quintenlauwers.item.ObsidianStick;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.List;

/**
 * Created by quinten on 18/08/16.
 */
public class GuiDnaMain extends GuiContainer {

    GuiPage currentPage;

    Tab[] tabs;
    Tab activeTab;

    public GuiDnaMain(ContainerDna dna) {
        super(dna);
        tabs = new Tab[3];
        tabs[0] = new Tab(0, new GuiContainerPage(this));
        tabs[1] = new Tab(1, new GuiEditDna(this));
        tabs[2] = new Tab(2, new GuiEditDna(this));
        activeTab = tabs[0];
        this.currentPage = activeTab.getDisplayPage();
    }

    List<GuiButton> getButtonList() {
        return this.buttonList;
    }


    @Override
    public void initGui() {
        for (Tab tab : tabs) {
            if (tab != null && tab.getDisplayPage() != null) {
                tab.getDisplayPage().initGui();
            }
        }
        super.initGui();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        for (Tab tab : this.tabs) {
            if (tab != this.activeTab) {
                this.drawTab(tab);
            }
        }
        currentPage.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (currentPage instanceof GuiContainerPage) {
            super.drawScreen(mouseX, mouseY, partialTicks);
            currentPage.drawScreen(mouseX, mouseY, partialTicks);
        } else {
            this.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
            currentPage.drawScreen(mouseX, mouseY, partialTicks);
        }
        drawButtons(mouseX, mouseY, partialTicks);
        drawTab(this.activeTab);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        currentPage.actionPerformed(button);
        super.actionPerformed(button);
    }

    void drawButtons(int mouseX, int mouseY, float partialTicks) {
        for (int i = 0; i < this.buttonList.size(); ++i) {
            this.buttonList.get(i).drawButton(this.mc, mouseX, mouseY);
        }
    }


    /**
     * Draws the given tab and its background, deciding whether to highlight the tab or not based off of the selected
     * index.
     */
    protected void drawTab(Tab tab) {
        this.mc.getTextureManager().bindTexture(new ResourceLocation("minecraft:textures/gui/container/creative_inventory/tabs.png"));
        boolean isactive = tab == this.activeTab;
        boolean inFirstRow = true;
        int i = tab.getTabColumn();
        int j = i * 28;
        int k = 0;
        int l = this.guiLeft + 28 * i;
        int i1 = this.guiTop;
        int j1 = 32;

        if (isactive) {
            k += 32;
        }

        if (i == 5) {
            l = this.guiLeft + this.xSize - 28;
        } else if (i > 0) {
            l += i;
        }

        if (inFirstRow) {
            i1 = i1 - 28;
        } else {
            k += 64;
            i1 = i1 + (this.ySize - 4);
        }

        GlStateManager.disableLighting();
        GlStateManager.color(1F, 1F, 1F); //Forge: Reset color in case Items change it.
        GlStateManager.enableBlend(); //Forge: Make sure blend is enabled else tabs show a white border.
        this.drawTexturedModalRect(l, i1, j, k, 28, 32);
        this.zLevel = 100.0F;
        this.itemRender.zLevel = 100.0F;
        l = l + 6;
        i1 = i1 + 8 + (inFirstRow ? 1 : -1);
        GlStateManager.enableLighting();
        GlStateManager.enableRescaleNormal();
        ItemStack itemstack = tab.getIconItemStack();
        this.itemRender.renderItemAndEffectIntoGUI(itemstack, l, i1);
        this.itemRender.renderItemOverlays(this.fontRendererObj, itemstack, l, i1);
        GlStateManager.disableLighting();
        this.itemRender.zLevel = 0.0F;
        this.zLevel = 0.0F;
    }

    /**
     * Called when a mouse button is released.
     */
    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        if (state == 0) {
            int i = mouseX - this.guiLeft;
            int j = mouseY - this.guiTop;

            for (Tab tab : this.tabs) {
                if (tab != null && this.isMouseOverTab(tab, i, j)) {
                    this.activeTab = tab;
                    this.currentPage = tab.getDisplayPage();
                    buttonList = currentPage.getButtonList();
                    return;
                }
            }
        }

        super.mouseReleased(mouseX, mouseY, state);
    }

    /**
     * Checks if the mouse is over the given tab. Returns true if so.
     */
    protected boolean isMouseOverTab(Tab tab, int mouseX, int mouseY) {
        int i = tab.getTabColumn();
        int j = 28 * i;
        int k = -32;

        if (i == 5) {
            j = this.xSize - 28 + 2;
        } else if (i > 0) {
            j += i;
        }


        return mouseX >= j && mouseX <= j + 28 && mouseY >= k && mouseY <= k + 32;
    }

    void pubDrawHoveringText(List<String> textLines, int x, int y) {
        super.drawHoveringText(textLines, x, y);
    }

    int getStringWidth(String text) {
        return this.fontRendererObj.getStringWidth(text);
    }

    void drawString(String text, int x, int y, int color) {
        this.fontRendererObj.drawString(text, x, y, color);
    }

    void drawRectWithCustomSizedTexture(int x, int y, float u, float v, int width, int height, float textureWidth,
                                        float textureHeight, ResourceLocation texture) {
        this.mc.getTextureManager().bindTexture(texture);
        drawModalRectWithCustomSizedTexture(x, y, u, v, width, height, textureWidth, textureHeight);
    }

    int getWidth() {
        return this.width;
    }

    int getHeight() {
        return this.height;
    }

    /**
     * Do not use unless you know what you are doing!
     *
     * @return this.fontRenderObj
     */
    FontRenderer getFontRendererObj() {
        return this.fontRendererObj;
    }
}

class Tab {
    private ItemStack iconItemStack;
    private int column;
    private GuiPage page;

    Tab(int column, GuiPage displayPage) {
        this.column = column;
        this.page = displayPage;
    }

    GuiPage getDisplayPage() {
        return this.page;
    }


    @SideOnly(Side.CLIENT)
    ItemStack getIconItemStack() {
        {
            if (this.iconItemStack == null) {
                this.iconItemStack = new ItemStack(this.getTabIconItem(), 1, 0);
            }

            return this.iconItemStack;
        }
    }

    @SideOnly(Side.CLIENT)
    public Item getTabIconItem() {
        return ObsidianStick.oStick;
    }

    int getTabColumn() {
        return this.column;
    }
}
