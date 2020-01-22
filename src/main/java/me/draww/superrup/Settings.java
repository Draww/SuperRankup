package me.draww.superrup;

import me.draww.superrup.utils.ItemUtil;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@SuppressWarnings("WeakerAccess")
public class Settings {

    private Config config;

    private String permissionProvider;
    private CustomProviderSettings customSettings;
    private List<String> disabledGroups;
    private UnitSettings unitSettings;
    private MenuSettings menuSettings;

    public Settings(Main main) {
        this.config = main.getMainConfig();
        this.permissionProvider = config.getConfig().getString("permission_provider");
        this.customSettings = new CustomProviderSettings(config.getConfig().getString("settings.type"),
                config.getConfig().getString("settings.listener_priority"));
        this.disabledGroups = config.getConfig().getStringList("disabled_groups");
        this.unitSettings = new UnitSettings(config.getConfig().getString("units.day"),
                config.getConfig().getString("units.days"),
                config.getConfig().getString("units.hour"),
                config.getConfig().getString("units.hours"),
                config.getConfig().getString("units.minute"),
                config.getConfig().getString("units.minutes"),
                config.getConfig().getString("units.second"),
                config.getConfig().getString("units.seconds"));
        this.menuSettings = new MenuSettings(config.getConfig().getString("menu.title"),
                config.getConfig().isString("menu.size") && config.getConfig().getString("menu.size").equalsIgnoreCase("DYNAMIC") ? -1 : config.getConfig().getInt("menu.size"),
                config.getConfig().getString("menu.directionArrowsPosition"),
                new MenuSettings.EmptyFillSetting(config.getConfig().getBoolean("menu.empty_fill.rank_container.status", false),
                        ItemUtil.deserializeItemStack(config.getConfigurationSection("menu.empty_fill.rank_container.icon"), null)),
                new MenuSettings.EmptyFillSetting(config.getConfig().getBoolean("menu.empty_fill.arrow_container.status", false),
                        ItemUtil.deserializeItemStack(config.getConfigurationSection("menu.empty_fill.arrow_container.icon"), null)),
                ItemUtil.deserializeItemStack(config.getConfigurationSection("menu.elements.down_arrow"), null),
                ItemUtil.deserializeItemStack(config.getConfigurationSection("menu.elements.up_arrow"), null));
    }

    public static class CustomProviderSettings {

        private String type;
        private String listenerPriority;

        public CustomProviderSettings(String type, String listenerPriority) {
            this.type = type;
            this.listenerPriority = listenerPriority;
        }

        public String getType() {
            return type;
        }

        public String getListenerPriority() {
            return listenerPriority;
        }
    }

    public static class UnitSettings {

        private String day;
        private String days;
        private String hour;
        private String hours;
        private String minute;
        private String minutes;
        private String second;
        private String seconds;

        public UnitSettings(String day, String days, String hour, String hours, String minute, String minutes, String second, String seconds) {
            this.day = day;
            this.days = days;
            this.hour = hour;
            this.hours = hours;
            this.minute = minute;
            this.minutes = minutes;
            this.second = second;
            this.seconds = seconds;
        }

        public String getDay() {
            return day;
        }

        public String getDays() {
            return days;
        }

        public String getHour() {
            return hour;
        }

        public String getHours() {
            return hours;
        }

        public String getMinute() {
            return minute;
        }

        public String getMinutes() {
            return minutes;
        }

        public String getSecond() {
            return second;
        }

        public String getSeconds() {
            return seconds;
        }
    }

    public static class MenuSettings {

        private String title;
        private Integer size;
        private String directionArrowsPosition;
        private EmptyFillSetting emptyRankContainer;
        private EmptyFillSetting emptyArrowContainer;
        private ItemStack elementDownArrow;
        private ItemStack elementUpArrow;

        public MenuSettings(String title, Integer size, String directionArrowsPosition, EmptyFillSetting emptyRankContainer, EmptyFillSetting emptyArrowContainer, ItemStack elementDownArrow, ItemStack elementUpArrow) {
            this.title = title;
            this.size = size;
            this.directionArrowsPosition = directionArrowsPosition;
            this.emptyRankContainer = emptyRankContainer;
            this.emptyArrowContainer = emptyArrowContainer;
            this.elementDownArrow = elementDownArrow;
            this.elementUpArrow = elementUpArrow;
        }

        public String getTitle() {
            return title;
        }

        public Integer getSize() {
            return size;
        }

        public String getDirectionArrowsPosition() {
            return directionArrowsPosition;
        }

        public EmptyFillSetting getEmptyRankContainer() {
            return emptyRankContainer;
        }

        public EmptyFillSetting getEmptyArrowContainer() {
            return emptyArrowContainer;
        }

        public ItemStack getElementDownArrow() {
            return elementDownArrow;
        }

        public ItemStack getElementUpArrow() {
            return elementUpArrow;
        }

        public static class EmptyFillSetting {

            private boolean status;
            private ItemStack icon;

            public EmptyFillSetting(boolean status, ItemStack icon) {
                this.status = status;
                this.icon = icon;
            }

            public boolean isStatus() {
                return status;
            }

            public ItemStack getIcon() {
                return icon;
            }
        }

    }

    public Config getConfig() {
        return config;
    }

    public String getPermissionProvider() {
        return permissionProvider;
    }

    public CustomProviderSettings getCustomSettings() {
        return customSettings;
    }

    public List<String> getDisabledGroups() {
        return disabledGroups;
    }

    public UnitSettings getUnitSettings() {
        return unitSettings;
    }

    public MenuSettings getMenuSettings() {
        return menuSettings;
    }
}
