package ru.rstudios.creative1.menu;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import ru.rstudios.creative1.user.LocaleManages;
import ru.rstudios.creative1.user.User;

import java.util.*;

import static ru.rstudios.creative1.Creative_1.plugin;

public class SwitchItem {

    private final String nameKey;
    private final String descriptionKey;
    private final List<String> statesKeys;
    private final String pathStart;
    private final List<Material> icons;
    private int currentState;

    public SwitchItem(String nameKey, String descriptionKey, List<String> statesKeys, String pathStart, List<Material> icons) {
        this.nameKey = nameKey;
        this.descriptionKey = descriptionKey;
        this.statesKeys = statesKeys;
        this.pathStart = pathStart;
        this.icons = icons;
        this.currentState = 0;
    }

    public void onClick(User user, InventoryClickEvent event) {
        if (event.getCurrentItem() != null && isSwitchItem(event.getCurrentItem())) {
            event.setCancelled(true);

            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                ItemStack clickedItem = event.getCurrentItem();
                int currentState = clickedItem.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(plugin, "switch_item"), PersistentDataType.INTEGER);

                this.setCurrentState(currentState);
                if (event.isLeftClick()) {
                    this.nextState();
                } else if (event.isRightClick()) {
                    this.previousState();
                }

                ItemStack newIcon = this.getLocalizedIcon(user);
                event.getInventory().setItem(event.getSlot(), newIcon);
            }, 1L);
        }
    }

    public void nextState() {
        currentState = (currentState + 1) % statesKeys.size();
    }

    public void previousState() {
        currentState = (currentState - 1 + statesKeys.size()) % statesKeys.size();
    }

    public ItemStack getLocalizedIcon(User user) {
        Material material = icons.get(currentState);
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            String localizedName = LocaleManages.getLocaleMessage(user.getLocale(), nameKey, false, "");
            List<String> localizedDescription = LocaleManages.getLocaleMessagesS(user.getLocale(), descriptionKey, new LinkedHashMap<>());

            meta.displayName(Component.text("§3" + localizedName));

            List<Component> lore = new LinkedList<>();
            localizedDescription.forEach(line -> lore.add(Component.text(line)));
            lore.add(Component.text(""));
            lore.addAll(generateStateLore(user));

            meta.lore(lore);

            PersistentDataContainer data = meta.getPersistentDataContainer();
            NamespacedKey key = new NamespacedKey(plugin, "switch_item");
            data.set(key, PersistentDataType.INTEGER, currentState);

            item.setItemMeta(meta);
        }

        return item;
    }

    private List<Component> generateStateLore(User user) {
        List<Component> stateLore = new LinkedList<>();
        int totalStates = this.statesKeys.size();

        // Определяем индексы для предыдущих и следующих состояний
        int startIndex = Math.max(0, this.currentState - 2);
        int endIndex = Math.min(totalStates, this.currentState + 3);

        // Если есть состояния перед видимой зоной, добавляем первое состояние и "..."
        if (startIndex > 0) {
            stateLore.add(Component.text("§7 ○ " + translateState(user, statesKeys.get(0))));
            stateLore.add(Component.text("§8 (...)"));
        }

        for (int i = startIndex; i < endIndex; i++) {
            String translatedState = translateState(user, statesKeys.get(i));
            if (i == this.currentState) {
                stateLore.add(Component.text("§4 ● " + translatedState));
            } else {
                stateLore.add(Component.text("§7 ○ " + translatedState));
            }
        }

        // Если есть состояния после видимой зоны, добавляем "..." и последнее состояние
        if (endIndex < totalStates) {
            stateLore.add(Component.text("§8 (...)"));
            stateLore.add(Component.text("§7 ○ " + translateState(user, statesKeys.get(totalStates - 1))));
        }

        return stateLore;
    }

    private String translateState(User user, String stateKey) {
        return LocaleManages.getLocaleMessage(user.getLocale(), pathStart + stateKey, false, "");
    }

    public void setCurrentState(int currentState) {
        this.currentState = currentState;
    }

    public void setCurrentState (String s) {
        this.currentState = statesKeys.contains(s) ? statesKeys.indexOf(s) : 0;
    }

    public int getCurrentState() {
        return this.currentState;
    }

    public int getCurrentState (ItemStack item) {
        return this.icons.indexOf(item.getType());
    }

    public static boolean isSwitchItem(ItemStack item) {
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            NamespacedKey key = new NamespacedKey(plugin, "switch_item");
            PersistentDataContainer container = meta.getPersistentDataContainer();

            return container.has(key, PersistentDataType.INTEGER);
        }
        return false;
    }

    public String getCurrentValue() {
        return statesKeys.get(getCurrentState());
    }

    public List<Material> getIcons() {
        return icons;
    }
}
