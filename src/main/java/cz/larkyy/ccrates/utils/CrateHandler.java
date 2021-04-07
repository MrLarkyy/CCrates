package cz.larkyy.ccrates.utils;

import cz.larkyy.ccrates.CCrates;
import cz.larkyy.ccrates.objects.CrateInfo;
import cz.larkyy.ccrates.objects.PlacedCrate;
import cz.larkyy.ccrates.objects.Reward;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrateHandler {

    private final CCrates main;
    private final Utils utils;
    private HologramHandler hologramHandler;

    private HashMap<String, CrateInfo> crateInfos = new HashMap<>();
    private HashMap<Location, PlacedCrate> crates = new HashMap<>();

    public CrateHandler(CCrates main) {
        this.main = main;
        this.utils = main.getUtils();
        this.hologramHandler = main.getHologramHandler();
    }

    public void loadCrateInfos() {
        HashMap<String, CrateInfo> loadedCrateInfos = new HashMap<>();
        if (getCratesConfig().getConfiguration().getConfigurationSection("crates") == null) {
            crateInfos = loadedCrateInfos;
            return;
        }

        for (String crateName : getCratesConfig().getConfiguration().getConfigurationSection("crates").getKeys(false)) {

            CrateInfo.CrateType crateType = CrateInfo.CrateType.valueOf(getCratesConfig().getConfiguration().getString("crates." + crateName + ".type"));
            List<Reward> rewards = loadRewards(crateName);

            List<String> hologram = utils.formatLore(getCratesConfig().getStringList("crates." + crateName + ".hologram", new ArrayList<>()));

            CrateInfo crateInfo = new CrateInfo(crateName, crateType, rewards, loadKey(crateName), loadCrateItem(crateName), hologram);

            loadedCrateInfos.put(crateName, crateInfo);
        }
        this.crateInfos = loadedCrateInfos;
    }

    public void loadCrates() {
        HashMap<Location, PlacedCrate> loadedCrates = new HashMap<>();

        for (String nonSplitted : getCratesData().getConfiguration().getStringList("locations")) {
            String[] splitted = nonSplitted.split("\\|\\|");

            if (Bukkit.getWorld(splitted[0]) != null && crateInfos.containsKey(splitted[5])) {
                World w = Bukkit.getWorld(splitted[0]);
                double x = Double.parseDouble(splitted[1]);
                double y = Double.parseDouble(splitted[2]);
                double z = Double.parseDouble(splitted[3]);
                String direction = splitted[4];
                String crateName = splitted[5];

                Location loc = new Location(w, x, y, z);
                CrateInfo crateInfo = crateInfos.get(crateName);
                PlacedCrate crate = new PlacedCrate(loc, direction, crateInfo);

                loadedCrates.put(loc, crate);
            }
            this.crates = loadedCrates;
        }
    }

    public void saveCrates() throws IOException {
        List<String> cratesToSave = new ArrayList<>();

        for (Map.Entry<Location, PlacedCrate> pair : crates.entrySet()) {
            PlacedCrate crate = pair.getValue();

            String data = crate.getLoc().getWorld().getName() + "||" + crate.getLoc().getX() + "||" + crate.getLoc().getY() + "||" + crate.getLoc().getZ() + "||" + crate.getDirection() + "||" + crate.getCrateInfo().getCrateName();

            cratesToSave.add(data);
        }
        getCratesData().getConfiguration().set("locations", cratesToSave);
        getCratesData().save();
    }

    private List<Reward> loadRewards(String crateName) {
        if (getCratesConfig().getConfiguration().getConfigurationSection("crates." + crateName + ".rewards") == null) {
            return new ArrayList<>();
        }
        List<Reward> loadedRewards = new ArrayList<>();
        for (String rewardString : getCratesConfig().getConfiguration().getConfigurationSection("crates." + crateName + ".rewards").getKeys(false)) {
            Bukkit.broadcastMessage("Loading reward " + rewardString);
            Reward reward = new Reward(
                    getCratesConfig().getString("crates." + crateName + ".rewards." + rewardString + ".displayName", null),
                    utils.mkItem(
                            Material.valueOf(getCratesConfig().getString("crates." + crateName + ".rewards." + rewardString + ".material", "STONE")),
                            getCratesConfig().getString("crates." + crateName + ".rewards." + rewardString + ".displayName", null),
                            rewardString,
                            getCratesConfig().getStringList("crates." + crateName + ".rewards." + rewardString + ".lore", null),
                            getCratesConfig().getConfiguration().getString("crates." + crateName + ".rewards." + rewardString + ".texture", null)
                    ),
                    getCratesConfig().getConfiguration().getDouble("crates." + crateName + ".rewards." + rewardString + ".chance", 100),
                    getCratesConfig().getConfiguration().getStringList("crates." + crateName + ".rewards." + rewardString + ".commands")

            );
            loadedRewards.add(reward);
            utils.sendConsoleMsg("Reward " + rewardString + " has been loaded!");
            Bukkit.broadcastMessage("Loaded reward");
        }
        return loadedRewards;

    }

    private ItemStack loadKey(String crateName) {
        return utils.mkItem(
                Material.valueOf(getCratesConfig().getConfiguration().getString("crates." + crateName + ".key.material", "STONE")),
                getCratesConfig().getConfiguration().getString("crates." + crateName + ".key.displayName"),
                crateName + " key",
                getCratesConfig().getConfiguration().getStringList("crates." + crateName + ".key.lore"),
                getCratesConfig().getConfiguration().getString("crates." + crateName + ".key.texture", null)
        );
    }

    private ItemStack loadCrateItem(String crateName) {
        return utils.mkItem(
                Material.valueOf(getCratesConfig().getConfiguration().getString("crates." + crateName + ".crate.material", "STONE")),
                getCratesConfig().getConfiguration().getString("crates." + crateName + ".crate.displayName"),
                crateName + " crate",
                getCratesConfig().getConfiguration().getStringList("crates." + crateName + ".crate.lore"),
                getCratesConfig().getConfiguration().getString("crates." + crateName + ".crate.texture", null)
        );


    }

    private DataUtils getCratesConfig() {
        return main.getCratesCfg();
    }

    private DataUtils getCratesData() {
        return main.getCratesData();
    }

    public HashMap<String, CrateInfo> getCrateInfos() {
        return crateInfos;
    }

    public HashMap<Location, PlacedCrate> getCrates() {
        return crates;
    }

    public boolean isCrateName(String crateName) {
        return (crateInfos.containsKey(crateName));
    }

    public CrateInfo getCrateInfo(String crateName) {
        return crateInfos.get(crateName);
    }

    public void addCrate(Location loc, PlacedCrate crate) throws IOException {
        crates.put(loc, crate);
        saveCrates();
    }

    public void removeCrate(PlacedCrate crate) throws IOException {
        hologramHandler = main.getHologramHandler();
        hologramHandler.removeHologram(crate);
        crates.remove(crate.getLoc());
        saveCrates();
    }

    public PlacedCrate getCrate(Location loc) {
        return crates.get(loc);
    }

    public boolean isPlacedCrate(Location loc) {
        return (crates.containsKey(loc));
    }

    public Reward getRandomReward(CrateInfo crateInfo) {
        if (crateInfo.getTotalPercentage() > 0) {
            List<Reward> rewards = crateInfo.getRewards();
            // Compute the total weight of all items together
            double totalWeight = 0.0d;
            for (Reward winning1 : rewards) {
                totalWeight += winning1.getChance();
            }

            // Now choose a random item
            int randomIndex = -1;
            double random = Math.random() * totalWeight;
            for (int i = 0; i < rewards.size(); ++i) {
                random -= rewards.get(i).getChance();
                if (random <= 0.0d) {
                    randomIndex = i;
                    break;
                }
            }
            return rewards.get(randomIndex);
        }
        return null;
    }
}
