package cz.larkyy.ccrates.listeners;

import cz.larkyy.ccrates.CCrates;
import cz.larkyy.ccrates.inventories.PreviewInventoryHolder;
import cz.larkyy.ccrates.objects.PlacedCrate;
import cz.larkyy.ccrates.utils.CrateHandler;
import cz.larkyy.ccrates.utils.Utils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerInteractListener implements Listener {

    private CCrates main;
    private Utils utils;
    private CrateHandler crateHandler;

    public PlayerInteractListener(CCrates main) {
        this.main = main;
        this.utils = main.getUtils();
        this.crateHandler = main.getCrateHandler();
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        Block b = e.getClickedBlock();


        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (b == null || b.getType().equals(Material.AIR))
                return;

            if (!crateHandler.isPlacedCrate(b.getLocation())) {
                return;
            }

            e.setCancelled(true);
            PlacedCrate crate = crateHandler.getCrate(b.getLocation());

            ItemStack is = p.getInventory().getItemInMainHand();
            ItemStack key = crate.getCrateInfo().getKey();

            if (!is.isSimilar(key)) {
                p.sendMessage("Doesn't have key");
                return;
            }

            if (main.getAnimationHandler().isOpening(crate)) {
                p.sendMessage("Already opening");
                return;
            }

            p.sendMessage("Have key");
            p.getInventory().removeItem(key);
            main.getAnimationHandler().playRoulette(crate, p);
        }

        if (e.getAction().equals(Action.LEFT_CLICK_BLOCK) && !p.isSneaking()) {

            if (!crateHandler.isPlacedCrate(b.getLocation())) {
                return;
            }

            e.setCancelled(true);
            PlacedCrate crate = crateHandler.getCrate(b.getLocation());

            p.openInventory(new PreviewInventoryHolder(main, crate, 0).getInventory());
        }
    }
}
