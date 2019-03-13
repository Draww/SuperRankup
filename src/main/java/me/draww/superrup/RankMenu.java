package me.draww.superrup;

import me.blackness.black.Element;
import me.blackness.black.element.BasicElement;
import me.blackness.black.pane.BasicPane;
import me.blackness.black.target.BasicTarget;
import me.draww.superrup.condition.ConditionProvider;
import me.draww.superrup.executor.ExecutorProvider;
import me.draww.superrup.inventory.builder.Menu;
import me.draww.superrup.inventory.util.ElementUtil;
import me.draww.superrup.utils.ItemStackBuilder;
import me.draww.superrup.utils.Text;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class RankMenu extends Menu {

    private Player player;

    private Integer currentPage;

    public RankMenu(Player player) {
        super("Rank", Text.colorize("Rank Menu"), 45);
        this.player = player;
        this.currentPage = 1;
        add("ranks");
        add("updowninfo");
        addPane("ranks", new BasicPane(0, 0, 4, 9));
        addPane("updowninfo", new BasicPane(0, 4, 1, 9));
        insertElement("updowninfo", 0, new BasicElement(ItemStackBuilder.of(Material.ARROW).name("&c<---").build(),
                new BasicTarget(e -> {
                    e.cancel();
                    int localCurrentPage = currentPage - 1;
                    if (localCurrentPage < 1) return;
                    if (!hasPane("ranks", localCurrentPage - 1)) return;
                    currentPage = localCurrentPage;
                    setCurrentPageElement();
                    buildChest(getPane("ranks", localCurrentPage - 1), getPane("updowninfo", 0));
                    showTo(e.player());
                })), 0, 0, false);
        setCurrentPageElement();
        insertElement("updowninfo", 0, new BasicElement(ItemStackBuilder.of(Material.ARROW).name("&c--->").build(),
                new BasicTarget(e -> {
                    e.cancel();
                    int localCurrentPage = currentPage + 1;
                    if (!hasPane("ranks", localCurrentPage - 1)) return;
                    currentPage = localCurrentPage;
                    setCurrentPageElement();
                    buildChest(getPane("ranks", localCurrentPage - 1), getPane("updowninfo", 0));
                    showTo(e.player());
                })), 8, 0, false);
        List<Rank> ranks = Main.getInstance().getRankManager().getSortedRanks();
        if (ranks != null && !ranks.isEmpty()) {
            String group = Main.getInstance().getGroupManager().getPlayerPrimaryGroup(player);
            Main.getInstance().getLogger().info(group);
            Integer indexPlayer = null;
            for (Rank rank : ranks.toArray(new Rank[0])) {
                if (rank.getGroup().equals(group)) {
                    indexPlayer = rank.getQueue();
                }
            }
            if (indexPlayer == null) indexPlayer = 0;
            Main.getInstance().getLogger().info(indexPlayer.toString());
            final Integer finalIndexPlayer = indexPlayer;
            ranks.forEach(rank -> {
                if (!rank.getGroup().equals(group)) {
                    Main.getInstance().getLogger().info(rank.getId() + " 1");
                    if (finalIndexPlayer > rank.getQueue()) {
                        Main.getInstance().getLogger().info(rank.getId() + " 2");
                        addLast(ElementUtil.emptyElement(rank.getIconLow()));
                    } else if (finalIndexPlayer < rank.getQueue()) {
                        Main.getInstance().getLogger().info(rank.getId() + " 3");
                        if (rank.getQueue().equals(finalIndexPlayer + 1)) {
                            Main.getInstance().getLogger().info(rank.getId() + " 4");
                            addLast(new BasicElement(rank.getIconJump(),
                                    new BasicTarget(e -> {
                                        e.cancel();
                                        boolean controlConditions = ConditionProvider.testAllConditions(player, rank, new ArrayList<>(rank.getConditions().values()));
                                        if (controlConditions) {
                                            Main.getInstance().getGroupManager().setPlayerPrimaryGroup(player, rank.getGroup());
                                            ExecutorProvider.runAllExecutors(player, rank, new ArrayList<>(rank.getExecutors().values()));
                                        }
                                        e.closeView();
                                    })));
                        } else {
                            Main.getInstance().getLogger().info(rank.getId() + " 5");
                            addLast(ElementUtil.emptyElement(rank.getIconHigh()));
                        }
                    } else if (rank.getGroup().equals(group)) {
                        Main.getInstance().getLogger().info(rank.getId() + " 6");
                        addLast(ElementUtil.emptyElement(rank.getIconEqual()));
                    }
                } else {
                    Main.getInstance().getLogger().info(rank.getId() + " 7");
                    addLast(ElementUtil.emptyElement(rank.getIconEqual()));
                }
            });
        }
        buildChest(getPane("ranks", 0), getPane("updowninfo", 0));
    }

    private void addLast(Element element) {
        if (!addToLastPane("ranks", element)) {
            addPane("ranks", new BasicPane(0, 0, 4, 9));
            addToLastPane("ranks", element);
        }
    }

    private void setCurrentPageElement() {
        insertElement("updowninfo", 0,
                ElementUtil.emptyElement(ItemStackBuilder.of(Material.MAGMA_CREAM).name("&c&l" + currentPage).build()),
                4, 0, false);
    }

    public Player getPlayer() {
        return player;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }
}
