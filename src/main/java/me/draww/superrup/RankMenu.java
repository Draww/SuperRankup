package me.draww.superrup;

import me.blackness.black.Element;
import me.blackness.black.element.BasicElement;
import me.blackness.black.pane.BasicPane;
import me.blackness.black.target.BasicTarget;
import me.draww.superrup.condition.ConditionProvider;
import me.draww.superrup.executor.ExecutorProvider;
import me.draww.superrup.inventory.builder.NormalMenu;
import me.draww.superrup.inventory.util.ElementUtil;
import me.draww.superrup.utils.ItemStackBuilder;
import me.draww.superrup.utils.ItemUtil;
import me.draww.superrup.utils.Text;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class RankMenu extends NormalMenu {

    private final FileConfiguration config;

    private Player player;
    private Integer currentPage;

    public RankMenu(Player player) {
        super("Rank", Text.colorize(Main.getInstance().getMainConfig().getConfig().getString("menu.title")),
                Main.getInstance().getMainConfig().getConfig().getInt("menu.size") * 9);
        this.config = Main.getInstance().getMainConfig().getConfig();
        this.player = player;
        this.currentPage = 1;
        String group = Main.getInstance().getGroupManager().getPlayerPrimaryGroup(player);
        if (Main.getInstance().getMainConfig().getConfig().getStringList("disabled_groups").contains(group)) {
            player.sendMessage(Text.colorize(Main.getInstance().getLanguageConfig().getConfig().getString("disable_group")));
            return;
        }
        add("ranks");
        add("updowninfo");
        addPane("ranks", new BasicPane(config.getInt("menu.panes.rank.x"),
                config.getInt("menu.panes.rank.y"),
                config.getInt("menu.panes.rank.height"),
                config.getInt("menu.panes.rank.length")));
        addPane("updowninfo", new BasicPane(config.getInt("menu.panes.up_down_info.x"),
                config.getInt("menu.panes.up_down_info.y"),
                config.getInt("menu.panes.up_down_info.height"),
                config.getInt("menu.panes.up_down_info.length")));
        insertElement("updowninfo", 0, new BasicElement(ItemStackBuilder.of(Material.ARROW, true).name("&c<---").build(),
                        new BasicTarget(e -> {
                            e.cancel();
                            int localCurrentPage = currentPage - 1;
                            if (localCurrentPage < 1) return;
                            if (!hasPane("ranks", localCurrentPage - 1)) return;
                            currentPage = localCurrentPage;
                            setCurrentPageElement();
                            build(getPane("ranks", localCurrentPage - 1), getPane("updowninfo", 0));
                            showTo(e.player());
                        })), config.getInt("menu.panes.up_down_info.elements.down.x"),
                config.getInt("menu.panes.up_down_info.elements.down.y"), false);
        setCurrentPageElement();
        insertElement("updowninfo", 0, new BasicElement(ItemStackBuilder.of(Material.ARROW, true).name("&c--->").build(),
                        new BasicTarget(e -> {
                            e.cancel();
                            int localCurrentPage = currentPage + 1;
                            if (!hasPane("ranks", localCurrentPage - 1)) return;
                            currentPage = localCurrentPage;
                            setCurrentPageElement();
                            build(getPane("ranks", localCurrentPage - 1), getPane("updowninfo", 0));
                            showTo(e.player());
                        })), config.getInt("menu.panes.up_down_info.elements.up.x"),
                config.getInt("menu.panes.up_down_info.elements.up.y"), false);
        if (config.getBoolean("menu.panes.up_down_info.empty_fill.status")) {
            if (config.contains("menu.panes.up_down_info.empty_fill.icon") && config.isConfigurationSection("menu.panes.up_down_info.empty_fill.icon")) {
                ItemStack item = ItemUtil.deserializeItemStack(config.getConfigurationSection("menu.panes.up_down_info.empty_fill.icon"), null);
                fillElement("updowninfo", 0, ElementUtil.emptyElement(item));
            }
        }
        List<Rank> ranks = Main.getInstance().getRankManager().getSortedRanks();
        if (ranks != null && !ranks.isEmpty()) {
            Integer indexPlayer = null;
            for (Rank rank : ranks.toArray(new Rank[0])) {
                if (rank.getGroup().equals(group)) {
                    indexPlayer = rank.getQueue();
                }
            }
            if (indexPlayer == null) indexPlayer = 0;
            final Integer finalIndexPlayer = indexPlayer;
            ranks.forEach(rank -> {
                if (!rank.getGroup().equals(group)) {
                    if (finalIndexPlayer > rank.getQueue()) {
                        addLast(ElementUtil.emptyElement(ItemUtil.redesignPlaceholderItemStack(player, rank.getIconLow())));
                    } else if (finalIndexPlayer < rank.getQueue()) {
                        if (rank.getQueue().equals(finalIndexPlayer + 1)) {
                            addLast(new BasicElement(ItemUtil.redesignPlaceholderItemStack(player, rank.getIconJump()),
                                    new BasicTarget(e -> {
                                        e.cancel();
                                        boolean controlConditions = ConditionProvider.testAllConditions(player, rank);
                                        if (controlConditions) {
                                            Main.getInstance().getGroupManager().setPlayerPrimaryGroup(player, rank.getGroup());
                                            ExecutorProvider.runAllExecutors(player, rank);
                                        }
                                        e.closeView();
                                    })));
                        } else {
                            addLast(ElementUtil.emptyElement(ItemUtil.redesignPlaceholderItemStack(player, rank.getIconHigh())));
                        }
                    } else if (rank.getGroup().equals(group)) {
                        addLast(ElementUtil.emptyElement(ItemUtil.redesignPlaceholderItemStack(player, rank.getIconEqual())));
                    }
                } else {
                    addLast(ElementUtil.emptyElement(ItemUtil.redesignPlaceholderItemStack(player, rank.getIconEqual())));
                }
            });
        }
        build(getPane("ranks", 0), getPane("updowninfo", 0));
    }

    private void addLast(Element element) {
        if (!addToLastPane("ranks", element)) {
            addPane("ranks", new BasicPane(config.getInt("menu.panes.rank.x"),
                    config.getInt("menu.panes.rank.y"),
                    config.getInt("menu.panes.rank.height"),
                    config.getInt("menu.panes.rank.length")));
            addToLastPane("ranks", element);
        }
    }

    private void setCurrentPageElement() {
        insertElement("updowninfo", 0,
                ElementUtil.emptyElement(ItemStackBuilder.of(Material.MAGMA_CREAM, true).name("&c&l" + currentPage).build()),
                config.getInt("menu.panes.up_down_info.elements.info.x"),
                config.getInt("menu.panes.up_down_info.elements.info.y"), false);
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public Player getPlayer() {
        return player;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }
}
