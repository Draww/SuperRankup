package me.draww.superrup.utils;

import me.draww.superrup.Config;
import me.draww.superrup.Main;
import me.draww.superrup.Rank;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.SplashPotion;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.potion.*;

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
            String newName = Text.replacePlayerPlaceholders(player, oldName);
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
                String newLore = Text.replacePlayerPlaceholders(player, lore);
                if (variables.length > 1) {
                    for (int i = 0; i < variables.length; i += 2) {
                        newLore = newLore.replace(variables[i], variables[i + 1]);
                    }
                }
                newLores.add(Text.colorize(newLore));
            }
            stackBuilder.newLore(newLores);
        }
        stackBuilder.transformMeta(controlMeta -> {
           if (controlMeta instanceof BookMeta) {
               BookMeta bookMeta = (BookMeta) controlMeta;
               if (bookMeta.getPages() != null) {
                   List<String> oldPages = new ArrayList<>();
                   List<String> newPages = new ArrayList<>();
                   for (String oldPage : oldPages) {
                       newPages.add(Text.colorize(Text.replacePlayerPlaceholders(player, oldPage)));
                   }
                   if (!newPages.isEmpty()) bookMeta.setPages(newPages);
               }
           }
        });
        return stackBuilder.build();
    }

    public static ItemStack deserializeItemStack(ConfigurationSection section, Rank rank) {
        if (section.contains("template") && section.isString("template")) {
            ConfigurationSection templateSection = Main.INSTANCE.getTemplateConfig().getConfigurationSection("icons." + section.getString("template"));
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
        if (controlMeta instanceof FireworkMeta) {
            if (section.contains("meta.type")) {
                FireworkMeta fireworkMeta = (FireworkMeta) controlMeta;
                List<org.bukkit.Color> colors = new ArrayList<>();
                List<org.bukkit.Color> fade = new ArrayList<>();
                FireworkEffect.Type type = null;
                boolean trail = false, flicker = false;
                int power = 1;
                if (section.contains("meta.power") && section.isInt("meta.power")) power = section.getInt("meta.power");
                if (section.isString("meta.type")) {
                    type = FireworkEffect.Type.valueOf(section.getString("meta.type").toUpperCase());
                }
                if (type != null) {
                    if (section.contains("meta.colors")) {
                        if (section.isList("meta.colors")) {
                            for (String colorStr : section.getStringList("meta.colors")) {
                                colors.add(Color.valueOf(colorStr.toUpperCase()).getBukkitColor());
                            }
                        } else if (section.isString("meta.colors")) {
                            String color = section.getString("meta.colors");
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
                }
            }
        } else if (controlMeta instanceof BookMeta) {
            BookMeta bookMeta = (BookMeta) controlMeta;
            if (section.contains("meta.title") && section.isString("meta.title")) {
                bookMeta.setTitle(Text.colorize(section.getString("meta.title")));
            }
            if (section.contains("meta.author") && section.isString("meta.author")) {
                bookMeta.setAuthor(section.getString("meta.author"));
            }
            if (section.contains("meta.pages") && section.isList("meta.pages")) {
                List<String> pages = section.getStringList("meta.pages");
                for (String page : pages) {
                    if (rank != null) {
                        page = page
                                .replace("%rank%", rank.getId())
                                .replace("%rank_group%", rank.getGroup());
                    }
                    bookMeta.addPage(Text.colorize(page));
                }
            }
            createdItem.setItemMeta(bookMeta);
        } else if (controlMeta instanceof PotionMeta) {
            if (section.contains("meta.effects") && section.contains("meta.base") && section.isString("meta.base")) {
                PotionMeta potionMeta = (PotionMeta) controlMeta;
                potionMeta.setBasePotionData(new PotionData(PotionType.valueOf(section.getString("meta.base").toUpperCase())));
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
                createdItem.setItemMeta(potionMeta);
            }
        } else if (controlMeta instanceof LeatherArmorMeta) {
            if (section.contains("meta.color") && section.isList("meta.color")) {
                LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) controlMeta;
                List<String> listColor = section.getStringList("meta.color");
                if (!listColor.isEmpty() && listColor.size() == 3) {
                    leatherArmorMeta.setColor(org.bukkit.Color.fromRGB(
                            Integer.valueOf(listColor.get(0)),
                            Integer.valueOf(listColor.get(1)),
                            Integer.valueOf(listColor.get(2))
                    ));
                }
                createdItem.setItemMeta(leatherArmorMeta);
            }
        } else if (controlMeta instanceof BannerMeta) {
            if (section.contains("meta.patterns") && section.isConfigurationSection("meta.patterns")) {
                BannerMeta bannerMeta = (BannerMeta) controlMeta;
                ConfigurationSection patternSection = section.getConfigurationSection("meta.patterns");
                for (String patternKey : patternSection.getKeys(false)) {
                    if (!patternSection.contains(patternKey + ".type")
                            || !patternSection.isString(patternKey + ".type")
                            || !patternSection.contains(patternKey + ".color")
                            || !patternSection.isString(patternKey + ".color")) continue;
                    PatternType patternType = PatternType.valueOf(patternSection.getString(patternKey + ".type").toUpperCase());
                    DyeColor color = DyeColor.valueOf(patternSection.getString(patternKey + ".color").toUpperCase());
                    if (patternType == null || color == null) continue;
                    bannerMeta.addPattern(new Pattern(color, patternType));
                }
                createdItem.setItemMeta(bannerMeta);
            }
        } else if (createdItem.getType().equals(Material.SHIELD)) {
            if (section.contains("meta.base_color") && section.isString("meta.base_color")
                    && section.contains("meta.patterns") && section.isConfigurationSection("meta.patterns")) {
                DyeColor baseColor = DyeColor.valueOf(section.getString("meta.base_color").toUpperCase());
                if (baseColor != null) {
                    BlockStateMeta stateMeta = (BlockStateMeta) controlMeta;
                    Banner banner = (Banner) stateMeta.getBlockState();
                    banner.setBaseColor(baseColor);
                    ConfigurationSection patternSection = section.getConfigurationSection("meta.patterns");
                    for (String patternKey : patternSection.getKeys(false)) {
                        if (!patternSection.contains(patternKey + ".type")
                                || !patternSection.isString(patternKey + ".type")
                                || !patternSection.contains(patternKey + ".color")
                                || !patternSection.isString(patternKey + ".color")) continue;
                        PatternType patternType = PatternType.valueOf(patternSection.getString(patternKey + ".type").toUpperCase());
                        DyeColor color = DyeColor.valueOf(patternSection.getString(patternKey + ".color").toUpperCase());
                        if (patternType == null || color == null) continue;
                        banner.addPattern(new Pattern(color, patternType));
                    }
                    banner.update();
                    stateMeta.setBlockState(banner);
                    createdItem.setItemMeta(stateMeta);
                }
            }

        } else if (controlMeta instanceof EnchantmentStorageMeta) {
            if (section.contains("meta.enchants") && section.isConfigurationSection("meta.enchants")) {
                EnchantmentStorageMeta enchantmentStorageMeta = (EnchantmentStorageMeta) controlMeta;
                ConfigurationSection enchantSection = section.getConfigurationSection("meta.enchants");
                for (String enchantKey : enchantSection.getKeys(false)) {
                    if (!enchantSection.contains(enchantKey + ".type")
                            || !enchantSection.isString(enchantKey + ".type")) continue;
                    Enchantment enchantment = Enchantment.getByName(enchantSection.getString(enchantKey + ".type"));
                    if (enchantment == null) continue;
                    Integer level = 1;
                    if (enchantSection.contains(enchantKey + ".level") && !enchantSection.isInt(enchantKey + ".level")) {
                        level = enchantSection.getInt(enchantKey + ".level");
                    }
                    enchantmentStorageMeta.addStoredEnchant(enchantment, level, true);
                }
                createdItem.setItemMeta(enchantmentStorageMeta);
            }
        } else if (controlMeta instanceof SkullMeta) {
            if (section.contains("meta.author") && section.isString("meta.author")) {
                SkullMeta skullMeta = (SkullMeta) controlMeta;
                skullMeta.setOwner(section.getString("meta.author"));
                createdItem.setItemMeta(skullMeta);
            }
        } else if (controlMeta instanceof SpawnEggMeta) {
            if (section.contains("meta.entity") && section.isString("meta.entity")) {
                SpawnEggMeta spawnEggMeta = (SpawnEggMeta) controlMeta;
                spawnEggMeta.setSpawnedType(EntityType.valueOf(section.getString("meta.entity")));
                createdItem.setItemMeta(spawnEggMeta);
            }
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
        if (section.contains("enchants") && section.isList("enchants")) {
            List<String> enchants = section.getStringList("enchants");
            for (String enchant : enchants) {
                String[] splitEnchant = enchant.split(":");
                Enchantment enchantment = Enchantment.getByName(splitEnchant[0]);
                if (enchantment == null) continue;
                Integer level = 1;
                if (splitEnchant.length == 2) level = Integer.valueOf(splitEnchant[1]);
                controlMeta.addEnchant(enchantment, level, true);
            }
        }
        if (section.contains("flags") && section.isList("flags")) {
            List<String> flags = section.getStringList("flags");
            for (String flagStr : flags) {
                ItemFlag flag = ItemFlag.valueOf(flagStr);
                if (flag == null) continue;
                controlMeta.addItemFlags(flag);
            }
        }
        createdItem.setItemMeta(controlMeta);
        return createdItem;
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

    public static boolean isSimilar(ItemStack item1, ItemStack item2) {
        if (item1.isSimilar(item2)) {
            return true;
        }
        return false;
    }

    public static boolean is(ItemStack item1, ItemStack item2) {
        if (item1.equals(item2)) {
            return true;
        }
        return false;
    }

    public static boolean isType(ItemStack item1, ItemStack item2) {
        if (item1.getType().equals(item2.getType())) {
            return true;
        }
        return false;
    }

    /**
     * Checks if the itemStack is empty or null
     *
     * @param item Item to check
     * @return Is the itemStack empty?
     */
    public static boolean isEmpty(ItemStack item) {
        return item == null || item.getType() == Material.AIR;
    }

    /**
     * Checks if the itemStacks are equal, ignoring their amount
     *
     * @param one first itemStack
     * @param two second itemStack
     * @return Are they equal?
     */
    public static boolean equals(ItemStack one, ItemStack two) {
        if (one == null || two == null) {
            return one == two;
        }
        if (one.isSimilar(two)) {
            return true;
        }

        // Special check for banners as they might include the deprecated base color
        if (one.getType() == two.getType()
                && one.getType() == Material.BANNER
                && one.getDurability() == two.getDurability()) {
            Map<String, Object> m1 = new HashMap<>(one.getItemMeta().serialize());
            Map<String, Object> m2 = new HashMap<>(two.getItemMeta().serialize());
            Object c1 = m1.remove("base-color");
            Object c2 = m2.remove("base-color");
            return (one.getData().equals(two.getData()) || c1.equals(c2)) && m1.equals(m2);
        }

        // Special check for books as their pages might change when serialising (See SPIGOT-3206)
        return one.getType() == two.getType()
                && one.getDurability() == two.getDurability()
                && one.getData().equals(two.getData())
                && one.hasItemMeta() && two.hasItemMeta()
                && one.getItemMeta() instanceof BookMeta && two.getItemMeta() instanceof BookMeta
                && one.getItemMeta().serialize().equals(two.getItemMeta().serialize());
    }

}
