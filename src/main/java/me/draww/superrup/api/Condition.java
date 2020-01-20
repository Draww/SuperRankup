package me.draww.superrup.api;

import org.bukkit.command.CommandSender;

public interface Condition<T extends CommandSender> extends Action {

    boolean test(T t);

}
