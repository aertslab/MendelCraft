package com.quintenlauwers.main;

import com.quintenlauwers.item.ModItems;

public class ServerProxy extends  CommonProxy{
    @Override
    public void registerRenderInfo() {
        ModItems.registerRenders();
    }
}
