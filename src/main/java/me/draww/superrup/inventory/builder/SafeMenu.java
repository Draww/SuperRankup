package me.draww.superrup.inventory.builder;

import me.blackness.black.Page;
import me.blackness.black.Pane;
import me.blackness.black.page.TSafePage;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class SafeMenu extends HashPane implements IMenu {

    private String id;
    private String title;
    private Integer size;

    private Page page;

    public SafeMenu(final String id, final String title, final Integer size) {
        this.id = id;
        this.title = title;
        this.size = size;
    }

    @Override
    public Page build() {
        page = new TSafePage(new NormalMenu(id, title, size).build());
        return page;
    }

    @Override
    public Page build(final Pane... panes) {
        page = new TSafePage(new NormalMenu(id, title, size).build(panes));
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
