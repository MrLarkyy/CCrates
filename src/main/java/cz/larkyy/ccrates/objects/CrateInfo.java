package cz.larkyy.ccrates.objects;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CrateInfo {

    private String crateName;
    private ItemStack key;
    private ItemStack crateItem;
    private CrateType crateType;
    private List<Reward> rewards;
    private List<String> hologram;
    private double totalPercentage;

    public enum CrateType {
        ROULETTE, CLASSIC
    }

    public CrateInfo(String crateName, CrateType crateType, List<Reward> rewards, ItemStack key, ItemStack crateItem, List<String> hologram) {
        this.crateName = crateName;
        this.crateType = crateType;
        this.key = key;
        this.crateItem = crateItem;
        this.rewards = rewards;
        this.totalPercentage = loadTotalPercentage();
        this.hologram = hologram;
    }

    private double loadTotalPercentage() {
        double total = 0;
        for (Reward reward : rewards) {
            total += reward.getChance();
        }
        return total;
    }

    public String getCrateName() {
        return crateName;
    }

    public ItemStack getCrateItem() {
        return crateItem;
    }

    public ItemStack getKey() {
        return key;
    }

    public CrateType getCrateType() {
        return crateType;
    }

    public double getTotalPercentage() {
        return totalPercentage;
    }

    public List<Reward> getRewards() {
        return rewards;
    }

    public List<String> getHologram() {
        return hologram;
    }
}
