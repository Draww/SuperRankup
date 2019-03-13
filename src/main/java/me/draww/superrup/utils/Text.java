package me.draww.superrup.utils;

import net.kyori.text.Component;
import net.kyori.text.TextComponent;
import net.kyori.text.adapter.bukkit.TextAdapter;
import net.kyori.text.serializer.ComponentSerializers;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Utilities for working with {@link Component}s and formatted text strings.
 */
@SuppressWarnings("deprecation")
public final class Text {

    public static final char SECTION_CHAR = '\u00A7'; // §
    public static final char AMPERSAND_CHAR = '&';

    public static String joinNewline(String... strings) {
        return joinNewline(Arrays.stream(strings));
    }

    public static String joinNewline(Stream<String> strings) {
        return strings.collect(Collectors.joining("\n"));
    }

    public static TextComponent fromLegacy(String input, char character) {
        return ComponentSerializers.LEGACY.deserialize(input, character);
    }

    public static TextComponent fromLegacy(String input) {
        return ComponentSerializers.LEGACY.deserialize(input);
    }

    public static String toLegacy(Component component, char character) {
        return ComponentSerializers.LEGACY.serialize(component, character);
    }

    public static String toLegacy(Component component) {
        return ComponentSerializers.LEGACY.serialize(component);
    }

    public static void sendMessage(CommandSender sender, Component message) {
        TextAdapter.sendComponent(sender, message);
    }

    public static void sendMessage(Iterable<CommandSender> senders, Component message) {
        TextAdapter.sendComponent(senders, message);
    }

    public static String colorize(String s) {
        return s == null ? null : translateAlternateColorCodes(AMPERSAND_CHAR, SECTION_CHAR, s);
    }

    public static List<String> colorizeList(List<String> list) {
        if (list == null) return null;
        List<String> newList = new ArrayList<>();
        for (String value : list) {
            newList.add(translateAlternateColorCodes(AMPERSAND_CHAR, SECTION_CHAR, value));
        }
        return newList;
    }

    public static String decolorize(String s) {
        return s == null ? null : translateAlternateColorCodes(SECTION_CHAR, AMPERSAND_CHAR, s);
    }

    public static String translateAlternateColorCodes(char from, char to, String textToTranslate) {
        char[] b = textToTranslate.toCharArray();
        for (int i = 0; i < b.length - 1; i++) {
            if (b[i] == from && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[i+1]) > -1) {
                b[i] = to;
                b[i+1] = Character.toLowerCase(b[i+1]);
            }
        }
        return new String(b);
    }

    private Text() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }

}
