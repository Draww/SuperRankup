package me.draww.superrup.utils;

import me.draww.superrup.Settings;
import me.draww.superrup.api.SuperRankupAPI;

import java.util.concurrent.TimeUnit;

public class TimeUtil {

    public static String to(long secondsx) {
        if (secondsx == 0) return "0 second";

        long days = TimeUnit.SECONDS.toDays(secondsx);
        long hours = TimeUnit.SECONDS.toHours(secondsx) - TimeUnit.DAYS.toHours(days);
        long minutes = TimeUnit.SECONDS.toMinutes(secondsx) - TimeUnit.HOURS.toMinutes(hours)
                - TimeUnit.DAYS.toMinutes(days);
        long seconds = TimeUnit.SECONDS.toSeconds(secondsx) - TimeUnit.MINUTES.toSeconds(minutes)
                - TimeUnit.HOURS.toSeconds(hours) - TimeUnit.DAYS.toSeconds(days);

        Settings.UnitSettings unitSettings = SuperRankupAPI.INSTANCE.getSettings().getUnitSettings();

        StringBuilder time = new StringBuilder();
        if (days != 0) {
            time.append(days);
        }
        if (days == 1) time.append(unitSettings.getDay());
        else if (days > 1) time.append(unitSettings.getDays());

        if (hours != 0) {
            time.append(hours);
        }
        if (hours == 1) time.append(unitSettings.getHour());
        else if (hours > 1) time.append(unitSettings.getHours());

        if (minutes != 0) {
            time.append(minutes);
        }
        if (minutes == 1) time.append(unitSettings.getMinute());
        else if (minutes > 1) time.append(unitSettings.getMinutes());

        if (seconds != 0) {
            time.append(seconds);
        }
        if (seconds == 1) time.append(unitSettings.getSecond());
        else if (seconds > 1) time.append(unitSettings.getSeconds());

        return time.toString();
    }

}
