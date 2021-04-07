package cz.larkyy.ccrates.utils;

import com.Zrips.CMI.CMI;
import com.Zrips.CMI.Modules.Holograms.CMIHologram;
import cz.larkyy.ccrates.CCrates;
import cz.larkyy.ccrates.objects.PlacedCrate;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

public class HologramHandler {

    private HashMap<PlacedCrate, CMIHologram> cmiholograms = new HashMap<>();
    private final CCrates main;
    private final HologramHook hologramHook;

    public enum HologramHook {
        CMI, HolographicDisplays
    }

    public HologramHandler(CCrates main, HologramHook hologramHook) {
        this.main = main;
        this.hologramHook = hologramHook;
    }


    public void loadHolograms() {
        for (Map.Entry<Location, PlacedCrate> pair : main.getCrateHandler().getCrates().entrySet()) {
            mkHologram(pair.getValue());
        }

//        for (Map.Entry<Location,PlacedCrate> pair : main.getCrateHandler().getCrates().entrySet()) {
//
//            Hologram hologram = new Hologram(
//                    pair.getKey(),
//                    pair.getValue().getCrateInfo().getHologram(),
//                    main
//            );
//
//            holograms.put(
//                    pair.getValue(),hologram);
//        }
    }

    public void spawnHolograms() {
        for (Map.Entry<PlacedCrate, CMIHologram> pair : cmiholograms.entrySet()) {
            pair.getValue().enable();
        }
    }

    public void removeHolograms() {
        for (Map.Entry<PlacedCrate, CMIHologram> pair : cmiholograms.entrySet()) {
            pair.getValue().remove();
        }
        cmiholograms = new HashMap<>();
    }

    public void removeHologram(PlacedCrate crate) {
        cmiholograms.get(crate).remove();
        cmiholograms.remove(crate);
    }

    public void spawnHologram(PlacedCrate crate) {
        mkHologram(crate);
    }

    public void respawnHolograms() {
        removeHolograms();
        loadHolograms();
        spawnHolograms();
    }

    private void mkHologram(PlacedCrate crate) {
        Location loc = crate.getLoc().clone();

        CMIHologram holo = new CMIHologram(loc.getWorld().getName() + "," + loc.getX() + "," + loc.getY() + "," + loc.getZ(), loc.add(0.5, 1.8 + crate.getCrateInfo().getHologram().size() * 0.2, 0.5));
        holo.setLines(crate.getCrateInfo().getHologram());
        CMI.getInstance().getHologramManager().addHologram(holo);

        cmiholograms.put(crate, holo);
    }
}
