package cz.larkyy.ccrates.objects;

import org.bukkit.Location;

public class PlacedCrate {

    private Location loc;
    private String direction;
    private CrateInfo crateInfo;

    public PlacedCrate(Location loc, String direction, CrateInfo crateInfo) {
        this.loc = loc;
        this.direction = direction;
        this.crateInfo = crateInfo;
    }

    public String getDirection() {
        return direction;
    }

    public Location getLoc() {
        return loc;
    }

    public CrateInfo getCrateInfo() {
        return crateInfo;
    }
}
