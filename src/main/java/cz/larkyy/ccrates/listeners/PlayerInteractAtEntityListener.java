package cz.larkyy.ccrates.listeners;

import cz.larkyy.ccrates.objects.ArmorstandReward;
import cz.larkyy.ccrates.objects.PlacedCrate;
import cz.larkyy.ccrates.utils.AnimationHandler;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import java.util.List;
import java.util.Map;

public class PlayerInteractAtEntityListener implements Listener {

    private final AnimationHandler animationHandler;

    public PlayerInteractAtEntityListener(AnimationHandler animationHandler) {
        this.animationHandler = animationHandler;
    }

    @EventHandler
    public void onArmorStandClick(PlayerInteractAtEntityEvent e) {
        if (animationHandler.getPlayingAnimations().isEmpty())
            return;

        if (!(e.getRightClicked() instanceof ArmorStand)) {
            return;
        }

        ArmorStand as = (ArmorStand) e.getRightClicked();

        for (Map.Entry<PlacedCrate, List<ArmorstandReward>> pair : animationHandler.getPlayingAnimations().entrySet()) {
            for (ArmorstandReward asR : pair.getValue()) {
                if (asR.getAs().equals(as))
                    e.setCancelled(true);
            }
        }
    }
}
