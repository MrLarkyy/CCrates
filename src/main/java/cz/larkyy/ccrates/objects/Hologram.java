package cz.larkyy.ccrates.objects;

import cz.larkyy.ccrates.CCrates;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

public class Hologram {

    private final CCrates main;
    private List<ArmorStand> armorStands = new ArrayList<>();
    private Location loc;
    private List<String> messages;

    public Hologram(Location loc, List<String> messages, CCrates main) {
        this.loc = loc;
        this.messages = messages;
        this.main = main;
    }

    public void spawn() {
        remove();
        double space = 0.3;
        Location asLoc = loc.clone();
        asLoc = asLoc.add(0.5, messages.size() * space, 0.5);

        for (String message : messages) {

            ArmorStand as = (ArmorStand) asLoc.getWorld().spawnEntity(asLoc, EntityType.ARMOR_STAND);

            setAsMeta(as);
            as.setCustomName(message);
            armorStands.add(as);
            asLoc = asLoc.add(0, -space, 0);
        }
    }

    private void setAsMeta(ArmorStand as) {
        as.setVisible(false);
        as.setGravity(false);
        as.setBasePlate(false);
        as.setCanMove(false);
        as.setInvulnerable(true);
        as.setCustomNameVisible(true);
    }

    public void remove() {
        for (ArmorStand as : armorStands) {
            as.remove();
        }
        armorStands = new ArrayList<>();
    }

}
