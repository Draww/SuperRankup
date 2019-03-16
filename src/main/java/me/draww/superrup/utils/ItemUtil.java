package me.draww.superrup.utils;

import me.draww.superrup.Config;
import me.draww.superrup.Main;
import me.draww.superrup.Rank;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemUtil {

    public static ItemStack redesignPlaceholderItemStack(Player player, ItemStack item, String... variables) {
        ItemStack newItem = item.clone();
        ItemMeta meta = newItem.getItemMeta();
        ItemStackBuilder stackBuilder = ItemStackBuilder.of(newItem);
        String oldName = meta.getDisplayName();
        if (oldName != null) {
            String newName = StringUtil.replacePlayerPlaceholders(player, oldName);
            if (variables.length > 1) {
                for (int i = 0; i < variables.length; i += 2) {
                    newName = newName.replace(variables[i], variables[i + 1]);
                }
            }
            stackBuilder.name(Text.colorize(newName));
        }
        List<String> oldLores = meta.getLore();
        if (oldLores != null) {
            List<String> newLores = new ArrayList<>();
            for (String lore : oldLores) {
                String newLore = StringUtil.replacePlayerPlaceholders(player, lore);
                if (variables.length > 1) {
                    for (int i = 0; i < variables.length; i += 2) {
                        newLore = newLore.replace(variables[i], variables[i + 1]);
                    }
                }
                newLores.add(Text.colorize(newLore));
            }
            stackBuilder.newLore(newLores);
        }
        return stackBuilder.build();
    }

    public static ItemStack deserializeItemStack(ConfigurationSection section, Rank rank) {
        if (section.contains("template") && section.isString("template")) {
            ConfigurationSection templateSection = Main.getInstance().getTemplateConfig().getConfigurationSection("icons." + section.getString("template"));
            return deserializeItemStack(templateSection, rank);
        }
        if (!section.contains("material")) return null;
        String materialStr = section.getString("material");
        if (materialStr.isEmpty()) return null;
        String[] splitMaterial = materialStr.split(":");
        Material material = null;
        Integer data = 0;
        int quantity = 1;
        if (splitMaterial.length >= 1) {
            material = Material.matchMaterial(splitMaterial[0]);
            if (splitMaterial.length == 2) data = Integer.valueOf(splitMaterial[1]);
        }
        if (section.contains("amount") && section.isInt("amount")) quantity = section.getInt("amount");
        if (material == null) return null;
        ItemStack createdItem = new ItemStack(material, quantity, data.shortValue());
        ItemMeta controlMeta = createdItem.getItemMeta();
        if (controlMeta instanceof LeatherArmorMeta) {
            LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) controlMeta;
            if (section.contains("meta.color") && section.isList("meta.color")) {
                List<String> listColor = section.getStringList("meta.color");
                if (!listColor.isEmpty() && listColor.size() == 3) {
                    leatherArmorMeta.setColor(org.bukkit.Color.fromRGB(
                            Integer.valueOf(listColor.get(0)),
                            Integer.valueOf(listColor.get(1)),
                            Integer.valueOf(listColor.get(2))
                    ));
                }
            }
            createdItem.setItemMeta(leatherArmorMeta);
        }
        if (section.contains("name") && section.isString("name")) {
            String name = section.getString("name");
            if (rank != null) {
                name = name
                        .replace("%rank%", rank.getId())
                        .replace("%rank_group%", rank.getGroup());
            }
            controlMeta.setDisplayName(Text.colorize(name));
        }
        if (section.contains("lores") && section.isList("lores")) {
            controlMeta.setLore(Text.colorizeList(section.getStringList("lores")));
        }
        createdItem.setItemMeta(controlMeta);
        return createdItem;
    }

}
