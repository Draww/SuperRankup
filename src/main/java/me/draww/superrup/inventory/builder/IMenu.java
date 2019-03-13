package me.draww.superrup.inventory.builder;

import me.blackness.black.Page;
import me.blackness.black.Pane;

public interface IMenu {

    Page build();

    Page build(Pane... panes);

}
