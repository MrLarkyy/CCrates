package cz.larkyy.ccrates.utils;

import cz.larkyy.ccrates.CCrates;
import cz.larkyy.ccrates.objects.ArmorstandReward;
import cz.larkyy.ccrates.objects.PlacedCrate;
import cz.larkyy.ccrates.objects.Reward;
import cz.larkyy.ccrates.runnables.animations.RouletteAnimation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AnimationHandler {

    private final CCrates main;
    private final CrateHandler crateHandler;
    private final Utils utils;
    private HashMap<PlacedCrate, List<ArmorstandReward>> playingAnimations = new HashMap<>();

    public AnimationHandler(CCrates main) {
        this.main = main;
        this.crateHandler = main.getCrateHandler();
        this.utils = main.getUtils();
    }


    public void playRoulette(PlacedCrate crate, Player p) {

        main.getHologramHandler().removeHologram(crate);

        int max = main.getCfg().getInt("Roulette.rewardAmount", 10);
        float radius = (float) main.getCfg().getConfiguration().getDouble("Roulette.radius", 3);
        int length = main.getUtils().getRandomBetween(
                main.getCfg().getInt("Roulette.length.min", 80),
                main.getCfg().getInt("Roulette.length.max", 120));

        List<ArmorstandReward> armorstandRewards = new ArrayList<>();
        for (int i = 0; i < max; i++) {
            ArmorStand as = spawnAs(crate);

            Reward reward = crateHandler.getRandomReward(crate.getCrateInfo());
            as.setItem(EquipmentSlot.HEAD, reward.getDisplayItem());
            as.setCustomName(utils.format(reward.getDisplayName()));

            armorstandRewards.add(new ArmorstandReward(as, reward, p, crate));
        }
        playingAnimations.put(crate, armorstandRewards);
        new RouletteAnimation(playingAnimations.get(crate), radius, 2.5F, ((2 * Math.PI) / max), crate, main, length).runTaskTimer(main, 0, 1);
    }

    private ArmorStand spawnAs(PlacedCrate crate) {

        Location asLoc = crate.getLoc().clone();
        asLoc.add(0.5, 3, 0.5);

        switch (crate.getDirection()) {
            case "E":
                asLoc.setYaw(asLoc.getYaw() + 90);
                break;
            case "S":
                asLoc.setYaw(asLoc.getYaw() + 180);
                break;
            case "W":
                asLoc.setYaw(asLoc.getYaw() - 90);
                break;
            default:
                break;
        }

        ArmorStand as = (ArmorStand) asLoc.getWorld().spawnEntity(asLoc, EntityType.ARMOR_STAND);
        as.setGravity(false);
        as.setInvulnerable(false);
        as.setBasePlate(false);
        as.setVisible(false);

        as.setCustomNameVisible(true);

        return as;
    }

    public void finish(ArmorstandReward asR) {
        Player p = asR.getPlayer();
        Reward reward = asR.getReward();
        List<String> commands = reward.getCommands();

        for (String cmd : commands) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("%player%", p.getName()));
        }
        playingAnimations.remove(asR.getCrate());
        main.getHologramHandler().spawnHologram(asR.getCrate());
    }

    public boolean isOpening(PlacedCrate crate) {
        return playingAnimations.containsKey(crate);
    }

    public ArmorstandReward chooseReward(PlacedCrate crate) {
        Location loc = crate.getLoc().clone();
        double distance = -1.0;
        ArmorstandReward asR = null;

        List<ArmorstandReward> armorstandRewards = playingAnimations.get(crate);
        for (ArmorstandReward armorstandReward : armorstandRewards) {
            if (distance == -1) {
                loc.add(0.5, 0.5, 0.5);

                distance = loc.distance(armorstandReward.getAs().getLocation());
                asR = armorstandReward;
            } else if (distance > loc.distance(armorstandReward.getAs().getLocation())) {
                distance = loc.distance(armorstandReward.getAs().getLocation());
                asR = armorstandReward;
            }
        }
        asR.getPlayer().sendMessage("You won " + asR.getReward().getDisplayName());
        return asR;

    }

    public HashMap<PlacedCrate, List<ArmorstandReward>> getPlayingAnimations() {
        return playingAnimations;
    }
}
