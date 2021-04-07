package cz.larkyy.ccrates;

import cz.larkyy.ccrates.commands.MainCommand;
import cz.larkyy.ccrates.inventories.GuiUtils;
import cz.larkyy.ccrates.listeners.*;
import cz.larkyy.ccrates.utils.*;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class CCrates extends JavaPlugin {

    private final Utils utils = new Utils(this);
    private final StorageUtils storageUtils = new StorageUtils(this);
    private final CrateHandler crateHandler = new CrateHandler(this);
    private final AnimationHandler animationHandler = new AnimationHandler(this);
    private HologramHandler hologramHandler;
    private final DataUtils config = new DataUtils(this, "config.yml");
    private final DataUtils crates = new DataUtils(this, "crates.yml");
    private final DataUtils cratesData = new DataUtils(this, "locations.yml");
    private final GuiUtils guiUtils = new GuiUtils(this);

    @Override
    public void onEnable() {
        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&6[cCrates]&f Plugin has been &aenabled&f!"));

        if (!getDataFolder().exists())
            getDataFolder().mkdir();

        config.load();
        crates.load();
        cratesData.load();

        crateHandler.loadCrateInfos();
        crateHandler.loadCrates();

        hologramHandler = new HologramHandler(this, HologramHandler.HologramHook.CMI);
        hologramHandler.loadHolograms();

        getCommand("ccrates").setExecutor(new MainCommand(this));

        getServer().getPluginManager().registerEvents(new BlockPlaceListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(this), this);
        getServer().getPluginManager().registerEvents(new BlockBreakListener(this), this);
        getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractAtEntityListener(animationHandler), this);
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&6[cCrates]&f Plugin has been &cdisabled&f!"));
        hologramHandler.removeHolograms();
    }

    public Utils getUtils() {
        return utils;
    }

    public DataUtils getCfg() {
        return config;
    }

    public DataUtils getCratesCfg() {
        return crates;
    }

    public DataUtils getCratesData() {
        return cratesData;
    }

    public StorageUtils getStorageUtils() {
        return storageUtils;
    }

    public void reloadCfg() {
        config.load();
    }

    public void reloadCfg(boolean crates) {
        if (crates)
            reloadCratesCfg();
        config.load();
        crateHandler.loadCrateInfos();
        crateHandler.loadCrates();
        hologramHandler.respawnHolograms();
    }

    public void reloadCratesCfg() {
        crates.load();
    }

    public CrateHandler getCrateHandler() {
        return crateHandler;
    }

    public AnimationHandler getAnimationHandler() {
        return animationHandler;
    }

    public HologramHandler getHologramHandler() {
        return hologramHandler;
    }

    public GuiUtils getGuiUtils() {
        return guiUtils;
    }
}
