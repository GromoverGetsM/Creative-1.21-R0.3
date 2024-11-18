package ru.rstudios.creative1.menu;

import com.jeff_media.morepersistentdatatypes.DataType;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.LinkedList;
import java.util.List;

import static ru.rstudios.creative1.Creative_1.plugin;

public class SwitchItem {

    private final List<String> states;
    private final List<Material> icons;
    private final String name;
    private final List<String> description;
    private int currentState;

    public SwitchItem (String name, List<String> description, List<String> states, List<Material> icons) {
        this.name = name;
        this.description = description;
        this.states = states;
        this.icons = icons;
        this.currentState = 0;
    }

    public void onClick(InventoryClickEvent event) {
        if (event.getCurrentItem() != null && isSwitchItem(event.getCurrentItem())) {
            event.setCancelled(true);

            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                int currentState = event.getCurrentItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(plugin, "switch_item"), PersistentDataType.INTEGER);
                SwitchItem item = SwitchItem.buildFromIcon(event.getCurrentItem());

                if (item != null) {
                    item.setCurrentState(currentState);

                    if (event.isLeftClick()) item.nextState();
                    else if (event.isRightClick()) item.previousState();

                    event.getInventory().setItem(event.getSlot(), item.getCurrentIcon());
                    event.setCursor(null);
                }
            }, 1L);
        }
    }

    public void nextState() { currentState = (currentState + 1) % states.size(); }

    public void previousState() { currentState = (currentState - 1 + states.size()) % states.size(); }

    public ItemStack getCurrentIcon() {
        Material material = icons.get(currentState);
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.displayName(Component.text("§3" + name));

            List<Component> lore = new LinkedList<>(description.stream().map(Component::text).toList());
            lore.add(Component.text(""));
            lore.addAll(generateStateLore());

            meta.lore(lore);

            PersistentDataContainer data = meta.getPersistentDataContainer();

            NamespacedKey key = new NamespacedKey(plugin, "switch_item");
            NamespacedKey name = new NamespacedKey(plugin, "switch_name");
            NamespacedKey desc = new NamespacedKey(plugin, "switch_description");
            NamespacedKey states = new NamespacedKey(plugin, "switch_states");
            NamespacedKey icons = new NamespacedKey(plugin, "switch_icons");

            List<ItemStack> rebuild = new LinkedList<>();
            for (Material m : this.icons) {
                rebuild.add(new ItemStack(m));
            }

            data.set(key, PersistentDataType.INTEGER, currentState);
            data.set(name, PersistentDataType.STRING, this.name);
            data.set(desc, DataType.asList(DataType.STRING), description);
            data.set(states, DataType.asList(DataType.STRING), this.states);
            data.set(icons, DataType.asList(DataType.ITEM_STACK), rebuild);
            item.setItemMeta(meta);
        }

        return item;
    }

    public List<Component> generateStateLore() {
        List<Component> stateLore = new LinkedList<>();
        int totalStates = this.states.size();

        // Определяем индексы для предыдущих и следующих состояний
        int startIndex = Math.max(0, this.currentState - 2);
        int endIndex = Math.min(totalStates, this.currentState + 3);

        // Если есть состояния перед видимой зоной, добавляем первое состояние и "..."
        if (startIndex > 0) {
            stateLore.add(Component.text("§7 ○ " + this.states.get(0)));
            stateLore.add(Component.text("§8 (...)"));
        }

        for (int i = startIndex; i < endIndex; i++) {
            if (i == this.currentState) stateLore.add(Component.text("§4 ● " + this.states.get(i)));
            else stateLore.add(Component.text("§7 ○ " + this.states.get(i)));
        }

        // Если есть состояния после видимой зоны, добавляем "..." и последнее состояние
        if (endIndex < totalStates) {
            stateLore.add(Component.text("§8 (...)"));
            stateLore.add(Component.text("§7 ○ " + this.states.get(totalStates - 1)));
        }

        return stateLore;
    }

    public void setCurrentState (int currentState) {
        this.currentState = currentState;
    }

    public int getCurrentState() {
        return this.currentState;
    }

    public static boolean isSwitchItem (ItemStack item) {
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            NamespacedKey key = new NamespacedKey(plugin, "switch_item");
            PersistentDataContainer container = meta.getPersistentDataContainer();

            return container.has(key, PersistentDataType.INTEGER);
        }
        return false;
    }

    public static SwitchItem buildFromIcon (ItemStack icon) {
        ItemMeta meta = icon.getItemMeta();

        if (meta != null) {
            PersistentDataContainer pdc = meta.getPersistentDataContainer();

            NamespacedKey key = new NamespacedKey(plugin, "switch_item");
            NamespacedKey name = new NamespacedKey(plugin, "switch_name");
            NamespacedKey desc = new NamespacedKey(plugin, "switch_description");
            NamespacedKey states = new NamespacedKey(plugin, "switch_states");
            NamespacedKey icons = new NamespacedKey(plugin, "switch_icons");

            if (pdc.has(key, PersistentDataType.INTEGER)) {
                String itemName = pdc.get(name, PersistentDataType.STRING);
                List<String> itemDesc = pdc.get(desc, DataType.asList(DataType.STRING));
                List<String> itemStates = pdc.get(states, DataType.asList(PersistentDataType.STRING));
                List<ItemStack> rawIcons = pdc.get(icons, DataType.asList(DataType.ITEM_STACK));

                List<Material> preparedIcons = new LinkedList<>();
                for (ItemStack item : rawIcons) {
                    preparedIcons.add(item.getType());
                }

                SwitchItem item = new SwitchItem(itemName, itemDesc, itemStates, preparedIcons);
                item.setCurrentState(pdc.get(key, PersistentDataType.INTEGER));

                return item;
            }
        }

        return null;
    }

    public static SwitchItem build (String name, List<String> desc, List<String> states, List<Material> icons) {
        return new SwitchItem(name, desc, states, icons);
    }
}
