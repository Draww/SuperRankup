package me.draww.superrup.menus;

import me.blackness.black.Element;
import me.blackness.black.Pane;
import me.blackness.black.element.BasicElement;
import me.blackness.black.pane.BasicPane;
import me.blackness.black.target.BasicTarget;
import me.draww.superrup.Main;
import me.draww.superrup.Rank;
import me.draww.superrup.Settings;
import me.draww.superrup.api.ConditionRegisterer;
import me.draww.superrup.api.ExecutorRegisterer;
import me.draww.superrup.api.SuperRankupAPI;
import me.draww.superrup.inventory.builder.NormalMenu;
import me.draww.superrup.inventory.util.ElementUtil;
import me.draww.superrup.utils.ItemStackBuilder;
import me.draww.superrup.utils.ItemUtil;
import me.draww.superrup.utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class MainMenu extends NormalMenu {

    private Player player;
    private String group;
    private int currentPage = 1;

    private int ranksPaneX, ranksPaneY, ranksPaneHeight, ranksPaneLength;

    public MainMenu(Player player) {
        super("r:Main");
        this.player = player;
        group = Main.INSTANCE.getGroupManager().getPlayerPrimaryGroup(player, "default");
        Settings settings = SuperRankupAPI.INSTANCE.getSettings();
        if (settings.getDisabledGroups().contains(group)) {
            player.sendMessage(Text.colorize(Main.INSTANCE.getLanguageConfig().getConfig().getString("disable_group")));
            return;
        }
        Settings.MenuSettings menuSettings = settings.getMenuSettings();
        setTitle(Text.colorize(Text.replacePlayerPlaceholders(player, menuSettings.getTitle())));
        List<Rank> ranks = Main.INSTANCE.getRankManager().getSortedRanks();
        int size = menuSettings.getSize();
        if (size < 0) {
            int calcSize = calcSize(ranks.size());
            if (calcSize <= 0) {
                return;
            }
            setSize(calcSize);
            load(calcSize / 9, menuSettings, ranks);
        } else {
            setSize(size * 9);
            load(size, menuSettings, ranks);
        }
    }

    private void load(int size, Settings.MenuSettings menuSettings, List<Rank> ranks) {
        String arrowsPosition = menuSettings.getDirectionArrowsPosition();
        if (arrowsPosition.equalsIgnoreCase("TOP")) {
            if (size == 1) {
                size = size + 1;
                setSize(size * 9);
            }
            add("ranks");
            add("arrows");
            ranksPaneX = 0;
            ranksPaneY = 1;
            ranksPaneHeight = size - 1;
            ranksPaneLength = 9;
            addPane("ranks", new BasicPane(ranksPaneX, ranksPaneY, ranksPaneHeight, ranksPaneLength));
            addPane("arrows", new BasicPane(0, 0, 1, 9));
            insertArrowTopOrDown(size, menuSettings, true);
            loadRanks(size, menuSettings, ranks);
            build(getPane("ranks", 0), getPane("arrows", 0));
            showTo(player);
        } else if (arrowsPosition.equalsIgnoreCase("DOWN")) {
            if (size == 1) {
                size = size + 1;
                setSize(size * 9);
            }
            add("ranks");
            add("arrows");
            ranksPaneX = 0;
            ranksPaneY = 0;
            ranksPaneHeight = size - 1;
            ranksPaneLength = 9;
            addPane("ranks", new BasicPane(ranksPaneX, ranksPaneY, ranksPaneHeight, ranksPaneLength));
            addPane("arrows", new BasicPane(0, size - 1, 1, 9));
            insertArrowTopOrDown(size, menuSettings, true);
            loadRanks(size, menuSettings, ranks);
            build(getPane("ranks", 0), getPane("arrows", 0));
            showTo(player);
        } else if (arrowsPosition.equalsIgnoreCase("SIDE")) {
            add("ranks");
            add("arrows");
            ranksPaneX = 1;
            ranksPaneY = 0;
            ranksPaneHeight = size;
            ranksPaneLength = 7;
            addPane("ranks", new BasicPane(ranksPaneX, ranksPaneY, ranksPaneHeight, ranksPaneLength));
            addPane("arrows", new BasicPane(0, 0, size, 1));
            addPane("arrows", new BasicPane(8, 0, size, 1));
            insertArrowSide(size, menuSettings);
            loadRanks(size, menuSettings, ranks);
            build(getPane("ranks", 0), getPane("arrows", 0), getPane("arrows", 1));
            showTo(player);
        }
    }

    private void loadRanks(int size, Settings.MenuSettings menuSettings, List<Rank> ranks) {
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
                                        e.closeView();
                                        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.INSTANCE, () -> {
                                            boolean controlConditions = ConditionRegisterer.testAllConditions(player, rank);
                                            if (controlConditions) {
                                                ExecutorRegisterer.runAllExecutors(player, rank);
                                                Main.INSTANCE.getGroupManager().setPlayerPrimaryGroup(player, rank.getGroup());
                                            }
                                        }, 20L);
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
            for (int i = 0; i < 60; i++) {
                addLast(ElementUtil.emptyElement(ItemStackBuilder.of(Material.PAPER).build()));
            }
            Settings.MenuSettings.EmptyFillSetting emptyFillSetting = menuSettings.getEmptyRankContainer();
            if (emptyFillSetting.isStatus()) {
                for (Pane pane : getAllPane("ranks")) {
                    pane.fill(ElementUtil.emptyElement(emptyFillSetting.getIcon()));
                }
            }
        }
    }

    private void insertArrowTopOrDown(int size, Settings.MenuSettings menuSettings, boolean fill) {
        insertElement("arrows", 0, new BasicElement(ItemUtil.redesignPlaceholderItemStack(player, menuSettings.getElementDownArrow(), "%currentPage%", String.valueOf(this.currentPage)),
                new BasicTarget(e -> {
                    e.cancel();
                    int localCurrentPage = currentPage - 1;
                    if (localCurrentPage < 1) return;
                    if (!hasPane("ranks", localCurrentPage - 1)) return;
                    currentPage = localCurrentPage;
                    clear("arrows", 0);
                    insertArrowTopOrDown(size, menuSettings, false);
                    build(getPane("ranks", localCurrentPage - 1), getPane("arrows", 0));
                    showTo(e.player());
                })), 0, 0, false);
        insertElement("arrows", 0, new BasicElement(ItemUtil.redesignPlaceholderItemStack(player, menuSettings.getElementUpArrow(), "%currentPage%", String.valueOf(this.currentPage)),
                new BasicTarget(e -> {
                    e.cancel();
                    int localCurrentPage = currentPage + 1;
                    if (!hasPane("ranks", localCurrentPage - 1)) return;
                    currentPage = localCurrentPage;
                    clear("arrows", 0);
                    insertArrowTopOrDown(size, menuSettings, false);
                    build(getPane("ranks", localCurrentPage - 1), getPane("arrows", 0));
                    showTo(e.player());
                })), 8, 0, false);
        Settings.MenuSettings.EmptyFillSetting emptyFillSetting = menuSettings.getEmptyArrowContainer();
        if (emptyFillSetting.isStatus()) {
            fillElement("arrows", 0, ElementUtil.emptyElement(emptyFillSetting.getIcon()));
        }
    }

    private void insertArrowSide(int size, Settings.MenuSettings menuSettings) {
        fillElement("arrows", 0, new BasicElement(ItemUtil.redesignPlaceholderItemStack(player, menuSettings.getElementDownArrow(), "%currentPage%", String.valueOf(this.currentPage)),
                new BasicTarget(e -> {
                    e.cancel();
                    int localCurrentPage = currentPage - 1;
                    if (localCurrentPage < 1) return;
                    if (!hasPane("ranks", localCurrentPage - 1)) return;
                    currentPage = localCurrentPage;
                    clear("arrows", 0);
                    clear("arrows", 1);
                    insertArrowSide(size, menuSettings);
                    build(getPane("ranks", localCurrentPage - 1), getPane("arrows", 0), getPane("arrows", 1));
                    showTo(e.player());
                })));
        fillElement("arrows", 1, new BasicElement(ItemUtil.redesignPlaceholderItemStack(player, menuSettings.getElementUpArrow(), "%currentPage%", String.valueOf(this.currentPage)),
                new BasicTarget(e -> {
                    e.cancel();
                    int localCurrentPage = currentPage + 1;
                    if (!hasPane("ranks", localCurrentPage - 1)) return;
                    currentPage = localCurrentPage;
                    clear("arrows", 0);
                    clear("arrows", 1);
                    insertArrowSide(size, menuSettings);
                    build(getPane("ranks", localCurrentPage - 1), getPane("arrows", 0), getPane("arrows", 1));
                    showTo(e.player());
                })));
    }

    private void addLast(Element element) {
        if (!addToLastPane("ranks", element)) {
            addPane("ranks", new BasicPane(ranksPaneX, ranksPaneY, ranksPaneHeight, ranksPaneLength));
            addToLastPane("ranks", element);
        }
    }

    private int calcSize(int i) {
        int temp = i;
        while (temp % 9 != 0) temp++;
        return Math.min(temp, 54);
    }
}
