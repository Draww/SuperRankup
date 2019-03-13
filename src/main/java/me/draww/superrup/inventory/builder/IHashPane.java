package me.draww.superrup.inventory.builder;

import me.blackness.black.Element;
import me.blackness.black.Pane;
import me.blackness.observer.Target;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface IHashPane {

    boolean add(final String id);

    boolean remove(final String id);

    boolean has(final String id);

    boolean addPane(final String id, final Pane pane);

    boolean addPane(final String id, final Pane... panes);

    Pane removePane(final String id, final int number);

    Pane getPane(final String id, final int number);

    boolean hasPane(final String id, final int number);

    Pane getLastPane(final String id);

    boolean addElement(final String id, final int number, final Element element);

    Element[] addElements(final String id, final int number, final Element... elements);

    boolean addToLastPane(final String id, final Element element);

    void fillElement(final String id, final int number, final Element element);
    void fillElement(final String id, final int number, final Element... elements);

    default void fillElementExtra(final String id, final int number, final Element element) {
        //Unsupported
    };
    default void fillElementExtra(final String id, final int number, final Element... elements) {
        //Unsupported
    };

    default void fillRow(final String id, final int number, final int row, final Element element) {
        //Unsupported
    };
    default void fillRow(final String id, final int number, final int row, final Element... element) {
        //Unsupported
    };

    default void fillColumn(final String id, final int number, final int column, final Element element) {
        //Unsupported
    };
    default void fillColumn(final String id, final int number, final int column, final Element... element) {
        //Unsupported
    };

    default void fillBorders(final String id, final int number, final Element element) {
        //Unsupported
    };
    default void fillBorders(final String id, final int number, final Element... element) {
        //Unsupported
    };

    default void fillRect(final String id, final int number, final int fromRow, final int fromColumn, final int toRow, final int toColumn, final Element element) {
        //Unsupported
    };
    default void fillRect(final String id, final int number, final int fromRow, final int fromColumn, final int toRow, final int toColumn, final Element... element) {
        //Unsupported
    };

    void insertElement(final String id, final int number, final Element element, final int locX, final int locY, final boolean shift) throws IllegalArgumentException;

    void clear();

    void clear(final String id);

    void clear(final String id, final int number);

    void replaceAll(final String id, final int number, final Element... elements);

    void remove(final String id, final int number, final int locX, final int locY) throws IllegalArgumentException;

    void subscribe(final String id, final int number, final Target<Object> target);

    boolean contains(final String id, final int number, final ItemStack icon);

    void accept(final String id, final int number, final InventoryInteractEvent event);

    void displayOn(final String id, final int number, final Inventory inventory);

    <T> T property(String name);

    <T> T property(String name, T def);

    void setProperty(String name, Object value);

    List<Pane> getAllPane(final String id);

    List<Pane> getAllPane();
    
}
