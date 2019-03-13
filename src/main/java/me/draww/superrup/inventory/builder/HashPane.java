package me.draww.superrup.inventory.builder;

import me.blackness.black.Element;
import me.blackness.black.Pane;
import me.blackness.observer.Target;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class HashPane implements IHashPane {

    private HashMap<String, List<Pane>> paneMap = new HashMap<>();
    private Map<String, Object> properties = new HashMap<>();

    @Override
    public boolean add(final String id) {
        if (has(id)) return false;
        paneMap.put(id, new ArrayList<>());
        return true;
    }

    @Override
    public boolean remove(final String id) {
        if (!has(id)) return false;
        paneMap.remove(id);
        return true;
    }

    @Override
    public boolean has(final String id) {
        return paneMap.containsKey(id);
    }

    @Override
    public boolean addPane(final String id, final Pane pane) {
        if (!has(id)) return false;
        return paneMap.get(id).add(pane);
    }

    @Override
    public boolean addPane(final String id, final Pane... panes) {
        if (!has(id)) return false;
        return paneMap.get(id).addAll(Arrays.asList(panes));
    }

    @Override
    public Pane removePane(final String id, final int number) {
        if (has(id)) return null;
        return paneMap.get(id).remove(number);
    }

    @Override
    public Pane getPane(final String id, final int number) {
        if (!has(id)) return null;
        return paneMap.get(id).get(number);
    }

    @Override
    public boolean hasPane(final String id, final int number) {
        if (!has(id)) return false;
        try {
            paneMap.get(id).get(number);
            return true;
        } catch (IndexOutOfBoundsException ex) {
            return false;
        }
    }

    @Override
    public Pane getLastPane(final String id) {
        if (!has(id)) return null;
        return paneMap.get(id).get(paneMap.get(id).size() - 1);
    }

    @Override
    public boolean addElement(final String id, final int number, final Element element) {
        if (!has(id)) return false;
        return paneMap.get(id).get(number).add(element);
    }

    @Override
    public Element[] addElements(final String id, final int number, final Element... elements) {
        if (!has(id)) return null;
        return paneMap.get(id).get(number).add(elements);
    }

    @Override
    public boolean addToLastPane(final String id, final Element element) {
        return paneMap.get(id).get(paneMap.get(id).size() - 1).add(element);
    }

    @Override
    public void fillElement(final String id, final int number, final Element element) {
        if (!has(id)) return;
        paneMap.get(id).get(number).fill(element);
    }

    @Override
    public void fillElement(final String id, final int number, final Element... elements) {
        if (!has(id)) return;
        paneMap.get(id).get(number).fill(elements);
    }

    @Override
    public void insertElement(final String id, final int number, final Element element, final int locX, final int locY, final boolean shift) throws IllegalArgumentException {
        if (!has(id)) return;
        paneMap.get(id).get(number).insert(element, locX, locY, shift);
    }

    @Override
    public void clear() {
        paneMap.clear();
    }

    @Override
    public void clear(final String id) {
        if (!has(id)) return;
        paneMap.get(id).clear();
    }

    @Override
    public void clear(final String id, final int number) {
        if (!has(id)) return;
        paneMap.get(id).get(number).clear();
    }

    @Override
    public void replaceAll(final String id, final int number, final Element... elements) {
        if (!has(id)) return;
        paneMap.get(id).get(number).replaceAll(elements);
    }

    @Override
    public void remove(final String id, final int number, final int locX, final int locY) throws IllegalArgumentException {
        if (!has(id)) return;
        paneMap.get(id).get(number).remove(locX, locY);
    }

    @Override
    public void subscribe(final String id, final int number, final Target<Object> target) {
        if (!has(id)) return;
        paneMap.get(id).get(number).subscribe(target);
    }

    @Override
    public boolean contains(final String id, final int number, final ItemStack icon) {
        if (!has(id)) return false;
        return paneMap.get(id).get(number).contains(icon);
    }

    @Override
    public void accept(final String id, final int number, final InventoryInteractEvent event) {
        if (!has(id)) return;
        paneMap.get(id).get(number).accept(event);
    }

    @Override
    public void displayOn(final String id, final int number, final Inventory inventory) {
        if (!has(id)) return;
        paneMap.get(id).get(number).displayOn(inventory);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T property(String name) {
        return (T) properties.get(name);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T property(String name, T def) {
        return properties.containsKey(name) ? (T) properties.get(name) : def;
    }

    @Override
    public void setProperty(String name, Object value) {
        properties.put(name, value);
    }

    @Override
    public List<Pane> getAllPane(final String id) {
        return paneMap.get(id);
    }

    @Override
    public List<Pane> getAllPane() {
        List<Pane> panes = new ArrayList<>();
        paneMap.values().forEach(panes::addAll);
        return panes;
    }

}
