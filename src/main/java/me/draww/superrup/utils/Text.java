package me.draww.superrup.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import me.draww.superrup.Main;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Text {

    public static final char SECTION_CHAR = '\u00A7'; // §
    public static final char AMPERSAND_CHAR = '&';

    public static String joinNewline(String... strings) {
        return joinNewline(Arrays.stream(strings));
    }

    public static String joinNewline(Stream<String> strings) {
        return strings.collect(Collectors.joining("\n"));
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

    public static String replacePlayerPlaceholders(Player paramPlayer, String paramString) {
        if ((paramString == null) || (paramString.isEmpty())) {
            return paramString;
        }
        paramString = Text.colorize(paramString);
        if (!paramString.contains("%")) {
            return paramString;
        }
        if (Main.INSTANCE.controlPlaceholderAPI()) {
            paramString = PlaceholderAPI.setPlaceholders(paramPlayer, paramString);
        }
        return paramString;
    }

    private Text() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }

}
