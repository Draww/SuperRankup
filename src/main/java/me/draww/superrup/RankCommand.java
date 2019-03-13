package me.draww.superrup;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import org.bukkit.entity.Player;

@CommandAlias("rank")
public class RankCommand extends BaseCommand {

    @Default
    public void onOpen(Player player) {
        new RankMenu(player).showTo(player);
    }

}
