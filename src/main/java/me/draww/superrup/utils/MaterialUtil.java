package me.draww.superrup.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Acrobot
 * @edited Draww
 * Original https://github.com/ChestShop-authors/ChestShop-3/blob/master/src/main/java/com/Acrobot/Breeze/Utils/MaterialUtil.java
 */
public class MaterialUtil {

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
