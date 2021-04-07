package cz.larkyy.ccrates.inventories;

import cz.larkyy.ccrates.CCrates;
import cz.larkyy.ccrates.objects.PlacedCrate;
import cz.larkyy.ccrates.objects.Reward;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PreviewInventoryHolder implements InventoryHolder {

    private Inventory gui;
    private final CCrates main;
    private final GuiUtils guiUtils;
    private final PlacedCrate crate;
    private int page;

    public PreviewInventoryHolder(CCrates main, PlacedCrate crate, int page) {
        this.main = main;
        this.guiUtils = main.getGuiUtils();
        this.crate = crate;
        this.page = page;
    }

    @Override
    public @NotNull Inventory getInventory() {

        gui = Bukkit.createInventory(this, main.getCfg().getInt("inventories.preview.size", 45), main.getUtils().format(main.getCfg().getString("inventories.preview.title", "&8Preview Menu")));
        addItems();

        return gui;
    }

    private void addItems() {
        for (String itemType : main.getCfg().getConfiguration().getConfigurationSection("inventories.preview.items").getKeys(false)) {
            switch (itemType) {
                case "prevPage":
                    loadItem(itemType, "prevPage");
                    break;
                case "nextPage":
                    loadItem(itemType, "nextPage");
                    break;
                case "close":
                    loadItem(itemType, "close");
                    break;
                case "rewards":
                    loadRewards();
                    break;
                default:
                    loadItem(itemType, null);
                    break;
            }
        }
    }

    private void loadRewards() {
        List<ItemStack> rewardItems = new ArrayList<>();
        for (Reward reward : crate.getCrateInfo().getRewards()) {
            rewardItems.add(reward.getDisplayItem());
        }
        List<Integer> slots = main.getCfg().getConfiguration().getIntegerList("inventories.preview.items.rewards.slots");
        int first = slots.size() * page;

        for (int i : slots) {
            try {
                gui.setItem(i, rewardItems.get(first));
            } catch (IndexOutOfBoundsException ex) {
                continue;
            }
            first++;
        }

    }

    private void loadItem(String itemType, String localizedName) {
        if (!isMoreSlots(itemType)) {
            int slot = main.getCfg().getConfiguration().getInt("inventories.preview.items." + itemType + ".slot");
            if (slot != -1) {
                gui.setItem(slot, mkItem(itemType, localizedName));
            }

        } else {
            ItemStack is = mkItem(itemType, localizedName);
            for (int i : main.getCfg().getConfiguration().getIntegerList("inventories.preview.items." + itemType + ".slots")) {
                gui.setItem(i, is);
            }
        }
    }

    private boolean isMoreSlots(String itemType) {
        return guiUtils.isMoreSlots("preview", itemType);
    }

    private ItemStack mkItem(String itemType, String localizedName) {
        return main.getUtils().mkItem(
                //MATERIAL
                Material.valueOf(main.getCfg().getConfiguration().getString("inventories.preview.items." + itemType + ".material", "STONE")),
                //NAME
                main.getUtils().format(main.getCfg().getConfiguration().getString("inventories.preview.items." + itemType + ".name", null)),
                //LOCALIZEDNAME
                localizedName,
                //LORE
                main.getUtils().formatLore(main.getCfg().getConfiguration().getStringList("inventories.preview.items." + itemType + ".lore")),
                //TEXTURE
                main.getCfg().getConfiguration().getString("inventories.preview.items." + itemType + ".texture", null));
    }
}
