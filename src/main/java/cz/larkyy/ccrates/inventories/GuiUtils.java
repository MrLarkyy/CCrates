package cz.larkyy.ccrates.inventories;

import cz.larkyy.ccrates.CCrates;

public class GuiUtils {

    private final CCrates main;

    public GuiUtils(CCrates main) {
        this.main = main;
    }

    public boolean isMoreSlots(String invType, String itemType) {
        return main.getCfg().getConfiguration().getConfigurationSection("inventories." + invType + ".items." + itemType).getKeys(true).contains("slots");
    }

}
