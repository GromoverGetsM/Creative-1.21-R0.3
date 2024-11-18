package ru.rstudios.creative1.menu;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CodingMenu implements InventoryHolder {

    public enum MenuType {
        DEFAULT, SET, ALL_IN
    }

    public enum ArgumentType {

        TEXT("&bТекст", Material.LIGHT_BLUE_STAINED_GLASS_PANE),
        NUMERIC("&cЧисло", Material.RED_STAINED_GLASS_PANE),
        LOCATION("&fМестоположение", Material.WHITE_STAINED_GLASS_PANE),
        DYNAMIC_VAR("&6Динамическая переменная", Material.ORANGE_STAINED_GLASS_PANE);

        private String display;
        private Material marker;

        ArgumentType (String display, Material marker) {
            this.display = display;
            this.marker = marker;
        }

        private static final Map<String, ArgumentType> BY_NAME = Arrays.stream(values()).collect(Collectors.toMap((e) -> e.name().toUpperCase(Locale.ROOT), Function.identity()));

        public static @Nullable ArgumentType getByName (String name) {
            return BY_NAME.get(name.toUpperCase());
        }

        public static @Nullable ArgumentType getByDisplay (String name) {
            return BY_NAME.values().stream().filter(a -> a.display.equalsIgnoreCase(name)).findFirst().orElse(null);
        }

        public static @Nullable ArgumentType getByMarker (Material marker) {
            return BY_NAME.values().stream().filter(a -> a.marker.equals(marker)).findFirst().orElse(null);
        }

        public String getDisplay() {
            return this.display;
        }

        public Material getMarker() {
            return this.marker;
        }
    }

    public Inventory inventory;
    public HashMap<ArgumentType, List<Integer>> markers = new LinkedHashMap<>();
    public List<Integer> fillers = new LinkedList<>();
    public List<Integer> systemItems = new LinkedList<>();
    public MenuType menuType;

    public CodingMenu (String title, MenuType type, List<ArgumentType> args, HashMap<Integer, List<Object>> switches) {
        this.menuType = type;
        int size;

        switch (type) {
            case DEFAULT -> {
                size = 27;

                switch (args.size()) {
                    case 1 -> {
                        markers.put(args.get(0), List.of(4, 12, 14, 22));
                        fillers.addAll(List.of(0, 1, 2, 3, 5, 6, 7, 8, 9, 10, 11, 15, 16, 17, 18, 19, 20, 21, 23, 24, 25, 26));
                    }
                    case 2 -> {
                        markers.put(args.get(0), List.of(2, 10, 12, 20));
                        markers.put(args.get(1), List.of(6, 14, 16, 24));
                        fillers.addAll(List.of(0, 1, 3, 4, 5, 7, 8, 9, 13, 17, 18, 19, 21, 22, 23, 25, 26));
                    }
                    case 3 -> {
                        markers.put(args.get(0), List.of(1, 9, 11, 19));
                        markers.put(args.get(1), List.of(4, 12, 14, 22));
                        markers.put(args.get(2), List.of(7, 15, 17, 25));
                        fillers.addAll(List.of(0, 2, 3, 5, 6, 8, 18, 20, 21, 23, 24, 26));
                    }
                    case 4 -> {
                        markers.put(args.get(0), List.of(1, 19));
                        markers.put(args.get(1), List.of(3, 21));
                        markers.put(args.get(2), List.of(5, 23));
                        markers.put(args.get(3), List.of(7, 25));
                        fillers.addAll(List.of(0, 2, 4, 6, 8, 9, 11, 13, 15, 17, 18, 20, 22, 24, 26));
                    }
                    case 5 -> {
                        markers.put(args.get(0), List.of(0, 18));
                        markers.put(args.get(1), List.of(2, 20));
                        markers.put(args.get(2), List.of(4, 22));
                        markers.put(args.get(3), List.of(6, 24));
                        markers.put(args.get(4), List.of(8, 26));
                        fillers.addAll(List.of(1, 3, 5, 7, 10, 12, 14, 16, 19, 21, 23, 25));
                    }
                }
            }
            case SET -> {
                size = 54;

                markers.put(args.get(0), List.of(4, 12, 14));
                markers.put(args.get(1), List.of(18, 19, 20, 21, 22, 23, 24, 25, 26));
                fillers.addAll(List.of(0, 1, 2, 3, 5, 6, 7, 8, 9, 10, 11, 15, 16, 17));
            }
            case ALL_IN -> {
                size = 54;

                markers.put(args.get(0), List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 45, 46, 47, 48, 49, 50, 51, 52, 53));
            }
            default -> throw new IllegalArgumentException("Unknown menu type");
        }

        this.inventory = Bukkit.createInventory(null, size, Component.text(title));
        setupMainPart();
        setupSwitches(switches);
        parseSystemItems();
    }

    private void setupMainPart() {
        for (int slot : this.fillers) {
            this.inventory.setItem(slot, buildFiller());
        }

        for (ArgumentType key : this.markers.keySet()) {
            for (int slot : this.markers.get(key)) {
                this.inventory.setItem(slot, buildMarker(key));
            }
        }
    }

    private void setupSwitches (HashMap<Integer, List<Object>> switches) {
        for (int slot : switches.keySet()) {
            if (slot < this.inventory.getSize()) {
                List<Object> switchPreparations = switches.get(slot);

                SwitchItem item = SwitchItem.build((String) switchPreparations.get(0), (List<String>) switchPreparations.get(1), (List<String>) switchPreparations.get(2), (List<Material>) switchPreparations.get(3));
                this.inventory.setItem(slot, item.getCurrentIcon());
                if (this.fillers.contains(slot)) this.fillers.remove(slot);
                for (List<Integer> list : this.markers.values()) {
                    if (list.contains(slot)) list.remove(slot);
                }
            } else throw new IllegalArgumentException("Switch-Item marker slot out of inventory bounds");
        }
    }

    private ItemStack buildFiller() {
        ItemStack item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.displayName(Component.text(" "));
        }

        item.setItemMeta(meta);
        return item;
    }

    private ItemStack buildMarker(ArgumentType type) {
        ItemStack item = new ItemStack(type.getMarker());
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.displayName(Component.text(type.getDisplay()));
        }

        item.setItemMeta(meta);
        return item;
    }

    private void parseSystemItems() {
        List<Integer> forbiddenSlots = new LinkedList<>(this.fillers);
        this.markers.values().forEach(forbiddenSlots::addAll);
        this.systemItems = forbiddenSlots;
    }

    public @NotNull Inventory getInventory() {
        return this.inventory;
    }

}
