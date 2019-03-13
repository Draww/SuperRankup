package me.draww.superrup.utils;

import me.draww.superrup.Config;
import me.draww.superrup.Main;
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

    public static ItemStack deserializeItemStack(ConfigurationSection section) { //TODO: remove the return null effects
        if (section.contains("template") && section.isString("template")) {
            ConfigurationSection templateSection = Main.getInstance().getTemplateConfig().getConfigurationSection(section.getString("template"));
            return deserializeItemStack(templateSection);
        }
        if (!section.contains("material")) return null;
        String materialStr = section.getString("material");
        if (materialStr.isEmpty()) return null;
        String[] splitMaterial = materialStr.split(":");
        Material material = null;
        Integer damage = 0;
        int quantity = 1;
        if (splitMaterial.length >= 1) {
            material = Material.matchMaterial(splitMaterial[0]);
            if (splitMaterial.length == 2) damage = Integer.valueOf(splitMaterial[1]);
        }
        if (section.contains("amount") && section.isInt("amount")) quantity = section.getInt("amount");
        if (material == null) return null;
        ItemStack createdItem = new ItemStack(material, quantity, damage.shortValue());
        ItemMeta controlMeta = createdItem.getItemMeta();
        if (controlMeta instanceof FireworkMeta) {
            FireworkMeta fireworkMeta = (FireworkMeta) controlMeta;
            List<org.bukkit.Color> colors = new ArrayList<>();
            List<org.bukkit.Color> fade = new ArrayList<>();
            FireworkEffect.Type type = null;
            boolean trail = false, flicker = false;
            int power = 1;
            if (section.contains("meta.power") && section.isInt("meta.power")) power = section.getInt("meta.power");
            if (!section.contains("meta.type")) return null;
            if (section.isString("meta.type")) {
                type = FireworkEffect.Type.valueOf(section.getString("meta.type").toUpperCase());
            }
            if (type == null) return null;
            if (section.contains("meta.colors")) {
                if (section.isList("meta.colors")) {
                    for (String colorStr : section.getStringList("meta.colors")) {
                        colors.add(Color.valueOf(colorStr.toUpperCase()).getBukkitColor());
                    }
                } else if (section.isString("meta.colors")) {
                    String color = section.getString("meta.colors");
                    //TODO: "," control
                    colors.add(Color.valueOf(color.toUpperCase()).getBukkitColor());
                }
            }
            if (section.contains("meta.fade")) {
                if (section.isList("meta.fade")) {
                    for (String colorStr : section.getStringList("meta.fade")) {
                        fade.add(Color.valueOf(colorStr.toUpperCase()).getBukkitColor());
                    }
                } else if (section.isString("meta.fade")) {
                    String color = section.getString("meta.fade");
                    //TODO: "," control
                    fade.add(Color.valueOf(color.toUpperCase()).getBukkitColor());
                }
            }
            if (section.contains("meta.trail") && section.isBoolean("meta.trail")) {
                trail = section.getBoolean("meta.trail");
            }
            if (section.contains("meta.flicker") && section.isBoolean("meta.flicker")) {
                flicker = section.getBoolean("meta.flicker");
            }
            FireworkEffect.Builder fireworkEffectBuilder = FireworkEffect.builder()
                    .with(type)
                    .trail(trail)
                    .flicker(flicker);
            if (!colors.isEmpty()) fireworkEffectBuilder.withColor(colors);
            if (!fade.isEmpty()) fireworkEffectBuilder.withFade(fade);
            fireworkMeta.setPower(power);
            fireworkMeta.addEffect(fireworkEffectBuilder.build());
            createdItem.setItemMeta(fireworkMeta);
        } else if (controlMeta instanceof BookMeta) {
            BookMeta bookMeta = (BookMeta) controlMeta;
            if (section.contains("meta.title") && section.isString("meta.title")) {
                bookMeta.setTitle(Text.colorize(section.getString("meta.title")));
            }
            if (section.contains("meta.author") && section.isString("meta.author")) {
                bookMeta.setAuthor(section.getString("meta.author"));
            }
            createdItem.setItemMeta(bookMeta);
        } else if (controlMeta instanceof PotionMeta) {
            PotionMeta potionMeta = (PotionMeta) controlMeta;
            if (section.contains("meta.effects") && section.contains("meta.base") && section.isString("meta.base")) {
                potionMeta.setBasePotionData(new PotionData(PotionType.valueOf(section.getString(section.getString("meta.base").toUpperCase()))));
                if (section.isConfigurationSection("meta.effects")) {
                    ConfigurationSection effectSection = section.getConfigurationSection("meta.effects");
                    for (String effectKey : effectSection.getKeys(false)) {
                        PotionEffectType potionEffectType;
                        Integer power = 1; //-127 to 128
                        Integer duration = 1; // seconds
                        boolean ambient = false, particles = false, overwrite = false;
                        org.bukkit.Color color = null;
                        if (!effectSection.contains(effectKey + ".effect")) continue;
                        potionEffectType = PotionEffectType.getByName(effectSection.getString(effectKey + ".effect").toUpperCase());
                        if (effectSection.contains(effectKey + ".power") && effectSection.isInt(effectKey + ".power")) {
                            power = effectSection.getInt(effectKey + ".power");
                        }
                        if (effectSection.contains(effectKey + ".duration") && effectSection.isInt(effectKey + ".duration")) {
                            duration = effectSection.getInt(effectKey + ".duration");
                        }
                        if (effectSection.contains(effectKey + ".ambient") && effectSection.isBoolean(effectKey + ".ambient")) {
                            ambient = effectSection.getBoolean(effectKey + ".ambient");
                        }
                        if (effectSection.contains(effectKey + ".particles") && effectSection.isBoolean(effectKey + ".particles")) {
                            particles = effectSection.getBoolean(effectKey + ".particles");
                        }
                        if (effectSection.contains(effectKey + ".overwrite") && effectSection.isBoolean(effectKey + ".overwrite")) {
                            overwrite = effectSection.getBoolean(effectKey + ".overwrite");
                        }
                        if (effectSection.contains(effectKey + ".color") && effectSection.isString(effectKey + ".color")) {
                            color = Color.valueOf(effectSection.getString(effectKey + ".color").toUpperCase()).getBukkitColor();
                        }
                        if (color == null) {
                            potionMeta.addCustomEffect(new PotionEffect(potionEffectType, duration, power, ambient, particles), overwrite);
                        } else {
                            potionMeta.addCustomEffect(new PotionEffect(potionEffectType, duration, power, ambient, particles, color), overwrite);
                        }
                    }
                }
            }
            createdItem.setItemMeta(potionMeta);
        } else if (controlMeta instanceof LeatherArmorMeta) {
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
            controlMeta.setDisplayName(Text.colorize(section.getString("name")));
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
