package cz.larkyy.ccrates.commands;

import cz.larkyy.ccrates.CCrates;
import cz.larkyy.ccrates.runnables.animations.CSGOAnimation;
import cz.larkyy.ccrates.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MainCommand implements CommandExecutor {

    private final CCrates main;
    private Utils utils;
    private CommandSender sender;

    public MainCommand(CCrates main) {
        this.main = main;
        this.utils = main.getUtils();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        this.sender = sender;

        if (args == null || args.length == 0)
            return false;

        switch (args[0]) {
            case "crate":
                new CrateCommand(sender, this, args);
                break;
            case "key":
                new KeyCommand(sender, this, args);
                break;
            case "calc":
                double arg = Double.parseDouble(args[1]);
                double result = Math.round(arg / 7) * 7;
                sender.sendMessage("Result is: " + result);
                break;
            case "reload":
                main.reloadCfg(true);
                break;
            case "csgo":
                Inventory gui = Bukkit.createInventory(null, 27, "CSGO Test");
                Player p = (Player) sender;
                p.openInventory(gui);
                new CSGOAnimation(7, mkItems(), gui).runTaskTimer(main, 0, 1);
        }

        return false;
    }

    private List<ItemStack> mkItems() {
        List<ItemStack> items = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            ItemStack is;
            if (i % 2 == 0) {
                is = new ItemStack(Material.STONE);
            } else if (i % 3 == 0)
                is = new ItemStack(Material.DIAMOND);
            else
                is = new ItemStack(Material.DIRT);
            ItemMeta im = is.getItemMeta();
            im.setDisplayName("Reward " + i);
            is.setItemMeta(im);
            items.add(is);
        }
        return items;
    }

    public CCrates getMain() {
        return main;
    }

    public boolean isPlayerSender() {
        return (sender instanceof Player);
    }
}
