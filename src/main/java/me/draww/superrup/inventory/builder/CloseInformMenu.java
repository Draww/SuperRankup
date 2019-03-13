package me.draww.superrup.inventory.builder;

import me.blackness.black.Page;
import me.blackness.black.Pane;
import me.blackness.black.page.CloseInformerPage;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class CloseInformMenu extends HashPane implements IMenu {

    private String id;
    private String title;
    private Integer size;

    private final Consumer<Player> closeHandler;

    private Page page;

    public CloseInformMenu(final String id, final String title, final Integer size, final Consumer<Player> closeHandler) {
        this.id = id;
        this.title = title;
        this.size = size;
        this.closeHandler = closeHandler;
    }

    public CloseInformMenu(final String id, final String title, final Integer size) {
        this.id = id;
        this.title = title;
        this.size = size;
        this.closeHandler = player -> { };
    }

    @Override
    public Page build() {
        page = new CloseInformerPage(new NormalMenu(id, title, size).build(), closeHandler);
        return page;
    }

    @Override
    public Page build(Pane... panes) {
        page = new CloseInformerPage(new NormalMenu(id, title, size).build(panes), closeHandler);
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