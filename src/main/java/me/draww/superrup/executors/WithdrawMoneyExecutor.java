package me.draww.superrup.executors;

import me.draww.superrup.Main;
import me.draww.superrup.Rank;
import me.draww.superrup.api.Executor;
import me.draww.superrup.api.annotations.ActionField;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class WithdrawMoneyExecutor implements Executor<Player> {

    @ActionField(type = "id")
    private String id;

    @ActionField(type = "queue")
    private Integer queue;

    @ActionField(type = "rank")
    private Rank rank;

    @ActionField(type = "value", required = true, custom = true)
    private Double money;

    @Override
    public void run(Player player) {
        Main.INSTANCE.getVaultEconomy().withdrawPlayer(player, money);
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public Integer queue() {
        return queue;
    }

    @Override
    public Rank rank() {
        return rank;
    }
}
