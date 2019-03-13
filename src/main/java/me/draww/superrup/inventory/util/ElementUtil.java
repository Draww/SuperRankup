package me.draww.superrup.inventory.util;

import me.blackness.black.Element;
import me.blackness.black.element.BasicElement;
import me.blackness.black.event.ElementBasicEvent;
import me.blackness.black.target.BasicTarget;
import org.bukkit.inventory.ItemStack;

public class ElementUtil {

    public static Element emptyElement(final ItemStack item) {
        return new BasicElement(item, new BasicTarget(ElementBasicEvent::cancel));
    }

}
