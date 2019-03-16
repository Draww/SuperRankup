package me.draww.superrup;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import me.draww.superrup.utils.Text;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("rank")
public class RankCommand extends BaseCommand {

    @Default
    @CommandPermission("superrup.menu")
    public void onOpen(Player player) {
        new RankMenu(player);
    }

    @Subcommand("reload")
    @CommandPermission("superrup.reload")
    public void onReload(CommandSender sender) {
        Main.getInstance().reload();
        sender.sendMessage(Text.colorize("&aSuperRankup has been successfully reloaded!"));
    }


}
