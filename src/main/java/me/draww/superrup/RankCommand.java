package me.draww.superrup;

import me.draww.superrup.menus.MainMenu;
import me.draww.superrup.utils.Text;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RankCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0 && sender.hasPermission("superrup.menu") && sender instanceof Player) {
            new MainMenu((Player) sender);
        } else if (args.length > 0) {
            if (args[0].equals("reload") && sender.hasPermission("superrup.reload")) {
                Main.INSTANCE.reload();
                sender.sendMessage(Text.colorize("&aSuperRankup has been successfully reloaded!"));
            }
        }
        return true;
    }
}
