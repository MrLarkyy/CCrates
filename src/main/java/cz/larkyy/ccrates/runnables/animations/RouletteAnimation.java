package cz.larkyy.ccrates.runnables.animations;

import cz.larkyy.ccrates.CCrates;
import cz.larkyy.ccrates.objects.ArmorstandReward;
import cz.larkyy.ccrates.objects.PlacedCrate;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class RouletteAnimation extends BukkitRunnable {

    private int tick;
    private final Location center;
    private final float radius;
    private final float radPerTick;
    private final List<ArmorstandReward> armorstands;
    private final double space;
    private final String direction;
    private final int length;
    private final PlacedCrate crate;
    private final CCrates main;
    private ArmorstandReward asR;

    public RouletteAnimation(List<ArmorstandReward> armorstands, float radius, float radPerSec, double space, PlacedCrate crate, CCrates main, int length) {
        this.center = armorstands.get(0).getAs().getLocation();
        this.radius = radius;
        this.armorstands = armorstands;
        this.radPerTick = radPerSec / 20f;
        this.space = space;
        this.direction = crate.getDirection();
        this.length = length + 1;
        this.crate = crate;
        this.main = main;
    }

    @Override
    public void run() {

        if (tick <= length) {
            teleportAs();
        }
        if (tick == length) {
            asR = main.getAnimationHandler().chooseReward(crate);

            Location particleLoc = asR.getAs().getLocation().clone();
            particleLoc.add(0, 1.8, 0);

            particleLoc.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, particleLoc, 7, 0.325, 0.325, 0, 325);
        } else if (tick > length) {
            rotateChoosenAs();
        }

        if (tick > length + 50) {
            removeAs();
            main.getAnimationHandler().finish(asR);
            cancel();
        }
        ++tick;
    }

    private void rotateChoosenAs() {
        ArmorStand as = asR.getAs();
        Location loc = as.getLocation();
        loc.setYaw(as.getLocation().getYaw() + 10);

        as.teleport(loc);
    }

    private void teleportAs() {
        Location loc;
        int armorstand = 0;
        double angle = calculateAngle();
        for (ArmorstandReward asR : armorstands) {
            ArmorStand as = asR.getAs();

            loc = getLocationAroundCircle(center, radius, angle + space * armorstand);

            as.teleport(loc);
            armorstand++;
        }
    }

    private void removeAs() {
        for (ArmorstandReward asR : armorstands) {
            ArmorStand as = asR.getAs();

            as.remove();
        }
    }

    private double calculateAngle() {
        double angleInRad;
        if (tick > length - 40) {
            float percent = ((tick - (length - 40f)) / 80f);
            float minus = (radPerTick * (tick - (length - 40f))) * percent;
            angleInRad = radPerTick * tick - minus;
        } else
            angleInRad = radPerTick * tick;

        return angleInRad;
    }

    private Location getLocationAroundCircle(Location center, double radius, double angleInRadian) {

        double x;
        double y;
        double z;

        Location loc;

        switch (direction) {
            case "W":
                x = center.getX();
                z = center.getZ() + radius * Math.cos(angleInRadian);
                y = center.getY() + radius * Math.sin(angleInRadian);
                loc = new Location(center.getWorld(), x, y, z);
                loc.setYaw(loc.getYaw() - 90);
                return loc;
            case "N":
                x = center.getX() + radius * Math.sin(angleInRadian);
                z = center.getZ();
                y = center.getY() + radius * Math.cos(angleInRadian);
                return new Location(center.getWorld(), x, y, z);
            case "E":
                x = center.getX();
                z = center.getZ() + radius * Math.cos(-angleInRadian);
                y = center.getY() + radius * Math.sin(-angleInRadian);
                loc = new Location(center.getWorld(), x, y, z);
                loc.setYaw(loc.getYaw() + 90);
                return loc;
            default:
                x = center.getX() + radius * Math.sin(-angleInRadian);
                z = center.getZ();
                y = center.getY() + radius * Math.cos(-angleInRadian);
                loc = new Location(center.getWorld(), x, y, z);
                loc.setYaw(loc.getYaw() + 180);
                return loc;
        }
    }
}
