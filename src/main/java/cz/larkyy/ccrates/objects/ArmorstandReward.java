package cz.larkyy.ccrates.objects;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ArmorstandReward {

    private UUID uuid;
    private ArmorStand as;
    private Reward reward;
    private PlacedCrate crate;
    private Player p;

    public ArmorstandReward(ArmorStand as, Reward reward, Player p, PlacedCrate crate) {
        this.uuid = as.getUniqueId();
        this.as = as;
        this.reward = reward;
        this.p = p;
        this.crate = crate;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Reward getReward() {
        return reward;
    }

    public ArmorStand getAs() {
        return as;
    }

    public Player getPlayer() {
        return p;
    }

    public PlacedCrate getCrate() {
        return crate;
    }
}
