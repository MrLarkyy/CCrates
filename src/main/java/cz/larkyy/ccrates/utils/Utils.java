package cz.larkyy.ccrates.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import cz.larkyy.ccrates.CCrates;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    private final CCrates main;
    private final Pattern pattern = Pattern.compile("#[a-fA-f0-9]{6}");

    public Utils(CCrates main) {
        this.main = main;
    }

    public void sendConsoleMsg(String msg) {
        Bukkit.getConsoleSender().sendMessage(format(msg));
    }

    public String format(String msg) {
        if (msg != null) {
            if (Bukkit.getVersion().contains("1.16")) {
                Matcher match = pattern.matcher(msg);
                while (match.find()) {
                    String color = msg.substring(match.start(), match.end());
                    msg = msg.replace(color, net.md_5.bungee.api.ChatColor.of(color) + "");
                    match = pattern.matcher(msg);
                }
            }
            return ChatColor.translateAlternateColorCodes('&', msg);
        }
        return msg;
    }

    public void sendMsg(Player p, String msg) {
        p.sendMessage(format(msg));
    }

    public int getRandomBetween(int min, int max) {
        Random rand = new Random();
        return rand.nextInt(max + 1 - min) + min;
    }

    public String getDirection(Player player) {
        final float yaw = player.getLocation().getYaw();

        if (yaw == Math.abs(yaw)) {
            if (270 + 45 <= yaw || yaw <= 45) return "S";
            else if (270 - 45 <= yaw && yaw <= 270 + 45) return "E";
            else if (90 - 45 <= yaw && yaw <= 90 + 45) return "W";
            else if (180 - 45 <= yaw && yaw <= 180 + 45) return "N";
        } else {
            if (-360 + 45 >= yaw || yaw >= -45) return "S";
            else if (-270 - 45 <= yaw && yaw <= -270 + 45) return "W";
            else if (-90 - 45 <= yaw && yaw <= -90 + 45) return "E";
            else if (-180 - 45 <= yaw && yaw <= -180 + 45) return "N";
        }
        return null;
    }

    public ItemStack mkItem(Material material, String displayName, String localizedName, List<String> lore, String texture) {
        ItemStack is = new ItemStack(material);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(format(displayName));
        im.setLocalizedName(localizedName);
        if (lore != null)
            im.setLore(formatLore(lore));
        is.setItemMeta(im);

        if (texture != null && material.equals(Material.PLAYER_HEAD))
            setSkullItemSkin(is, texture);

        return is;
    }

    public void setSkullItemSkin(ItemStack is, String texture) {

        ItemMeta meta = is.getItemMeta();

        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", texture));
        Field field;
        try {
            field = meta.getClass().getDeclaredField("profile");
            field.setAccessible(true);
            field.set(meta, profile);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException x) {
            x.printStackTrace();
        }
        is.setItemMeta(meta);

    }

    public List<String> formatLore(List<String> lore) {
        List<String> result = new ArrayList<>();
        for (String str : lore) {
            result.add(format(str));
        }
        return result;
    }

}
