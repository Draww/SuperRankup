package me.draww.superrup;

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
                config.getConfig().getInt("menu.size"));
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

        public MenuSettings(String title, Integer size) {
            this.title = title;
            this.size = size;
        }

        public String getTitle() {
            return title;
        }

        public Integer getSize() {
            return size;
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
