package cz.larkyy.ccrates.listeners;

import cz.larkyy.ccrates.CCrates;
import cz.larkyy.ccrates.objects.CrateInfo;
import cz.larkyy.ccrates.objects.PlacedCrate;
import cz.larkyy.ccrates.utils.CrateHandler;
import cz.larkyy.ccrates.utils.HologramHandler;
import cz.larkyy.ccrates.utils.StorageUtils;
import cz.larkyy.ccrates.utils.Utils;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;

public class BlockPlaceListener implements Listener {

    private final CCrates main;
    private final Utils utils;
    private final StorageUtils storageUtils;
    private final CrateHandler crateHandler;
    private final HologramHandler hologramHandler;

    public BlockPlaceListener(CCrates main) {
        this.main = main;
        this.utils = main.getUtils();
        this.storageUtils = main.getStorageUtils();
        this.crateHandler = main.getCrateHandler();
        this.hologramHandler = main.getHologramHandler();
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) throws IOException {
        Player p = e.getPlayer();
        ItemStack is = e.getItemInHand();
        ItemMeta im = is.getItemMeta();
        Block b = e.getBlockPlaced();

        if (im == null)
            return;

        if (im.getLocalizedName().contains("crate")) {
            String crateNameString = im.getLocalizedName().split(" ")[0];

            if (!crateHandler.isCrateName(crateNameString)) {
                return;
            }

            p.sendMessage("Placed crate " + crateNameString);
            CrateInfo crateInfo = crateHandler.getCrateInfo(crateNameString);
            String direction = utils.getDirection(p);
            PlacedCrate crate = new PlacedCrate(b.getLocation(), direction, crateInfo);

            crateHandler.addCrate(b.getLocation(), crate);
            hologramHandler.spawnHologram(crate);
        }
    }
}
