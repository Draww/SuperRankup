package me.draww.superrup.utils;

import me.draww.superrup.Config;
import me.draww.superrup.Main;
import me.draww.superrup.Rank;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
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

    public static ItemStack deserializeItemStack(ConfigurationSection section, Rank rank) { //TODO: remove the return null effects
        if (section.contains("template") && section.isString("template")) {
            ConfigurationSection templateSection = Main.getInstance().getTemplateConfig().getConfigurationSection(section.getString("template"));
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
                        .replace("{rank}", rank.getId());
            }
            controlMeta.setDisplayName(Text.colorize(name));
        }
        if (section.contains("lores") && section.isList("lores")) {
            controlMeta.setLore(Text.colorizeList(section.getStringList("lores")));
        }
        createdItem.setItemMeta(controlMeta);
        return createdItem;
    }

    public static Map<?, ?> serializeItemStack(ItemStack item) {
        Map<Object, Object> serializeMap = new HashMap<>();
        return serializeMap;
    }

    enum Color {
        WHITE(org.bukkit.Color.WHITE),
        SILVER(org.bukkit.Color.SILVER),
        GRAY(org.bukkit.Color.GRAY),
        BLACK(org.bukkit.Color.BLACK),
        RED(org.bukkit.Color.RED),
        MAROON(org.bukkit.Color.MAROON),
        YELLOW(org.bukkit.Color.YELLOW),
        OLIVE(org.bukkit.Color.OLIVE),
        LIME(org.bukkit.Color.LIME),
        GREEN(org.bukkit.Color.GREEN),
        AQUA(org.bukkit.Color.AQUA),
        TEAL(org.bukkit.Color.TEAL),
        BLUE(org.bukkit.Color.BLUE),
        NAVY(org.bukkit.Color.NAVY),
        FUCHSIA(org.bukkit.Color.FUCHSIA),
        PURPLE(org.bukkit.Color.PURPLE),
        ORANGE(org.bukkit.Color.ORANGE);

        private org.bukkit.Color bukkitColor;

        Color(org.bukkit.Color bukkitColor) {
            this.bukkitColor = bukkitColor;
        }

        public org.bukkit.Color getBukkitColor() {
            return bukkitColor;
        }
    }

}
