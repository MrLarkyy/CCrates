package cz.larkyy.ccrates.objects;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Reward {

    private String displayName;
    private ItemStack displayItem;
    private double chance;
    private List<String> commands;

    public Reward(String displayName, ItemStack displayItem, double chance, List<String> commands) {
        this.displayItem = displayItem;
        this.displayName = displayName;
        this.chance = chance;
        this.commands = commands;
    }

    public double getChance() {
        return chance;
    }

    public String getDisplayName() {
        return displayName;
    }

    public ItemStack getDisplayItem() {
        return displayItem;
    }

    public List<String> getCommands() {
        return commands;
    }
}
