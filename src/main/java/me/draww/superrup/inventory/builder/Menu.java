package me.draww.superrup.inventory.builder;

import me.blackness.black.Page;
import me.blackness.black.Pane;
import me.blackness.black.page.ChestPage;
import me.blackness.black.page.CloseInformerPage;
import me.blackness.black.page.TSafePage;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.function.Consumer;

public class Menu extends HashPane {

    private String id;
    private String title;
    private Integer size;

    private Page page;

    public Menu(final String id, final String title, final Integer size) {
        this.id = id;
        this.title = title;
        this.size = size;
    }

    public Page buildChest() {
        page = new ChestPage(title, size, getAllPane().toArray(new Pane[0]));
        return page;
    }

    public Page buildChest(final Pane... panes) {
        page = new ChestPage(title, size, panes);
        return page;
    }

    public Page buildSafeChest() {
        page = new TSafePage(buildChest());
        return page;
    }

    public Page buildSafeChest(final Pane... panes) {
        page = new TSafePage(buildChest(panes));
        return page;
    }

    public Page buildCloseChest(Consumer<Player> closeHandler) {
        page = new CloseInformerPage(buildChest(), closeHandler);
        return page;
    }

    public Page buildCloseChest(final Consumer<Player> closeHandler, final Pane... panes) {
        page = new CloseInformerPage(buildChest(panes), closeHandler);
        return page;
    }

    public void rearrange(final int paneIndex, final int position) {
        if (page == null) return;
        page.rearrange(paneIndex, position);
    }

    public void showTo(final Player player) {
        page.showTo(player);
    }

    public void showToAll(final Player... players) {
        Arrays.stream(players).forEach(this::showTo);
    }

    public void closeView(final Player player) {
        player.closeInventory();
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public void setSize(final Integer size) {
        this.size = size;
    }

    public void setPage(final Page page) {
        this.page = page;
    }

    public Page getPage() {
        return page;
    }

    public String getTitle() {
        return title;
    }

    public Integer getSize() {
        return size;
    }
}
