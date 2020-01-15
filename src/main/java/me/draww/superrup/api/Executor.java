package me.draww.superrup.api;

import org.bukkit.command.CommandSender;

public interface Executor<T extends CommandSender> extends Action {

    void run(T t);
}
