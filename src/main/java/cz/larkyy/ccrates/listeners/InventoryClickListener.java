package cz.larkyy.ccrates.listeners;

import cz.larkyy.ccrates.inventories.PreviewInventoryHolder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player))
            return;

        Player p = (Player) e.getWhoClicked();

        if (e.getInventory().getHolder() instanceof PreviewInventoryHolder) {
            e.setCancelled(true);
        }
    }
}
