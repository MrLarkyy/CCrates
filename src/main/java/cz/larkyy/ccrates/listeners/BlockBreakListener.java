package cz.larkyy.ccrates.listeners;

import cz.larkyy.ccrates.CCrates;
import cz.larkyy.ccrates.objects.PlacedCrate;
import cz.larkyy.ccrates.utils.AnimationHandler;
import cz.larkyy.ccrates.utils.CrateHandler;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.io.IOException;

public class BlockBreakListener implements Listener {

    private final CCrates main;
    private final CrateHandler crateHandler;
    private final AnimationHandler animationHandler;

    public BlockBreakListener(CCrates main) {
        this.main = main;
        this.crateHandler = main.getCrateHandler();
        this.animationHandler = main.getAnimationHandler();
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) throws IOException {
        Player p = e.getPlayer();
        Location loc = e.getBlock().getLocation();

        if (!crateHandler.isPlacedCrate(loc)) {
            return;
        }

        if (!p.isSneaking()) {
            return;
        }

        PlacedCrate crate = crateHandler.getCrate(loc);

        if (animationHandler.getPlayingAnimations().containsKey(crate)) {
            return;
        }

        crateHandler.removeCrate(crate);
        p.sendMessage("Crate has been removed!");
    }
}
