package com.quintenlauwers.main;

import com.quintenlauwers.item.ObsidianStick;

public class ServerProxy extends  CommonProxy{
    @Override
    public void registerRenderInfo() {
        ObsidianStick.registerRenders();
    }
}
