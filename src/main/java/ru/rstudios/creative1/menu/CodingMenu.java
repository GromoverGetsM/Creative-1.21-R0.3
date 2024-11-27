package ru.rstudios.creative1.menu;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import ru.rstudios.creative1.user.LocaleManages;
import ru.rstudios.creative1.user.User;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CodingMenu implements InventoryHolder {

    public enum MenuType { DEFAULT, SET, ALL_IN }

    public enum ArgumentType {
        TEXT("coding.var-types.text.name", "coding.var-types.text.lore", Material.LIGHT_BLUE_STAINED_GLASS_PANE),
        NUMERIC("coding.var-types.numeric.name", "coding.var-types.numeric.lore", Material.RED_STAINED_GLASS_PANE),
        LOCATION("coding.var-types.location.name", "coding.var-types.location.lore", Material.WHITE_STAINED_GLASS_PANE),
        DYNAMIC_VAR("coding.var-types.dynamic.name", "coding.var-types.dynamic.lore", Material.ORANGE_STAINED_GLASS_PANE);

        private final String namePath;
        private final String lorePath;
        private final Material marker;

        ArgumentType(String namePath, String lorePath, Material marker) {
            this.namePath = namePath;
            this.lorePath = lorePath;
            this.marker = marker;
        }

        public String getNamePath() {
            return namePath;
        }

        public String getLorePath() {
            return lorePath;
        }

        public Material getMarker() {
            return marker;
        }
    }

    private final String titlePath;
    private final MenuType menuType;
    private final List<ArgumentType> args;
    private final Map<Integer, SwitchItem> switches;
    private Inventory inventory;
    private final List<Integer> fillers = new ArrayList<>();
    private final Map<ArgumentType, List<Integer>> markers = new LinkedHashMap<>();
    private final List<Integer> argumentSlots = new LinkedList<>();
    private final HashSet<Player> viewers = new LinkedHashSet<>();
    public final Map<Player, Inventory> localizedInventories = new HashMap<>();

    public CodingMenu() {
        this.titlePath = "";
        this.menuType = MenuType.DEFAULT;
        this.args = new LinkedList<>();
        this.switches = new LinkedHashMap<>();
    }

    public CodingMenu(String titlePath, MenuType menuType, List<ArgumentType> args, Map<Integer, SwitchItem> switches) {
        this.titlePath = titlePath;
        this.menuType = menuType;
        this.args = args;
        this.switches = switches;
        setupMarkersAndFillers(menuType, args);
    }

    private void setupMarkersAndFillers(MenuType type, List<ArgumentType> args) {
        switch (type) {
            case DEFAULT -> {
                switch (args.size()) {
                    case 1 -> {
                        argumentSlots.add(13);
                        setupDefaultMarkers(args, List.of(List.of(4, 12, 14, 22)));
                    }
                    case 2 -> {
                        argumentSlots.add(11);
                        argumentSlots.add(15);
                        setupDefaultMarkers(args, List.of(List.of(2, 10, 12, 20), List.of(6, 14, 16, 24)));
                    }
                    case 3 -> {
                        argumentSlots.add(10);
                        argumentSlots.add(13);
                        argumentSlots.add(16);
                        setupDefaultMarkers(args, List.of(
                                List.of(1, 9, 11, 19),
                                List.of(4, 12, 14, 22),
                                List.of(7, 15, 17, 25)
                        ));
                    }
                    case 4 -> {
                        argumentSlots.add(10);
                        argumentSlots.add(12);
                        argumentSlots.add(14);
                        argumentSlots.add(16);
                        setupDefaultMarkers(args, List.of(
                                List.of(1, 19), List.of(3, 21), List.of(5, 23), List.of(7, 25)
                        ));
                    }
                    case 5 -> {
                        argumentSlots.add(9);
                        argumentSlots.add(11);
                        argumentSlots.add(13);
                        argumentSlots.add(15);
                        argumentSlots.add(17);
                        setupDefaultMarkers(args, List.of(
                                List.of(0, 18), List.of(2, 20), List.of(4, 22), List.of(6, 24), List.of(8, 26)
                        ));
                    }
                }
            }
            case SET -> {
                argumentSlots.add(13);
                argumentSlots.addAll(IntStream.rangeClosed(27, 53).boxed().toList());
                setupDefaultMarkers(args, List.of(
                        List.of(4, 12, 14), List.of(18, 19, 20, 21, 22, 23, 24, 25, 26)
                ));
            }
            case ALL_IN -> {
                argumentSlots.addAll(IntStream.rangeClosed(9, 44).boxed().toList());
                setupDefaultMarkers(args, List.of(
                        List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 45, 46, 47, 48, 49, 50, 51, 52, 53)
                ));
            }

        }
    }

    public List<Integer> getArgumentSlots() {
        return argumentSlots;
    }

    private void setupDefaultMarkers(List<ArgumentType> args, List<List<Integer>> positions) {
        for (int i = 0; i < args.size(); i++) {
            markers.put(args.get(i), positions.get(i));
        }
        fillers.clear();
        fillers.addAll(getFillerSlots());
    }

    private List<Integer> getFillerSlots() {
        Set<Integer> filledSlots = markers.values().stream().flatMap(Collection::stream).collect(Collectors.toSet());
        List<Integer> allSlots = new ArrayList<>();
        for (int i = 0; i < 54; i++) {
            if (!getArgumentSlots().contains(i)) allSlots.add(i);
        }
        allSlots.removeAll(filledSlots);
        return allSlots;
    }

    private void build(User user) {
        Inventory localizedInventory = Bukkit.createInventory(this, getSize(),
                Component.text(LocaleManages.getLocaleMessage(user.getLocale(), titlePath, false, "")));

        setupTranslatedItems(user, localizedInventory);
        localizedInventories.put(user.player(), localizedInventory);
    }


    public void open(Player player) {
        open(User.asUser(player));
    }

    public void open(User user) {
        build(user);
        user.player().openInventory(localizedInventories.get(user.player()));
        viewers.add(user.player());
    }


    private void setupTranslatedItems(User user, Inventory inventory) {
        fillers.forEach(slot -> inventory.setItem(slot, buildFiller()));

        markers.forEach((type, slots) ->
                slots.forEach(slot -> inventory.setItem(slot, buildMarker(type, user))));

        switches.forEach((slot, params) -> {
            if (slot >= inventory.getSize())
                throw new IllegalArgumentException("Switch slot out of bounds");

            SwitchItem switchItem = switches.get(slot);
            inventory.setItem(slot, switchItem.getLocalizedIcon(user));
        });
    }


    private ItemStack buildFiller() {
        return createItem(Material.GRAY_STAINED_GLASS_PANE, " ", Collections.emptyList());
    }

    private ItemStack buildMarker(ArgumentType type, User user) {
        String name = LocaleManages.getLocaleMessage(user.getLocale(), type.getNamePath(), false, "");
        List<String> lore = LocaleManages.getLocaleMessagesS(user.getLocale(), type.getLorePath(), new LinkedHashMap<>());
        return createItem(type.getMarker(), name, lore);
    }

    private ItemStack createItem(Material material, String name, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(name));
            meta.lore(lore.stream().map(Component::text).collect(Collectors.toList()));
            item.setItemMeta(meta);
        }
        return item;
    }

    private int getSize() {
        return (menuType == MenuType.DEFAULT) ? 27 : 54;
    }

    public Inventory getInventory (User user) {
        if (inventory == null) build(user);
        return this.inventory;
    }

    public void onClick (InventoryClickEvent event) {
        int slot = event.getSlot();

        if (switches.containsKey(slot)) {
            event.setCancelled(true);
            SwitchItem item = switches.get(slot);

            if (event.isLeftClick()) item.nextState();
            else item.previousState();

            viewers.forEach(player -> {
                Inventory inv = player.getOpenInventory().getTopInventory();
                ItemStack icon = item.getLocalizedIcon(User.asUser(player));
                inv.setItem(slot, icon);
            });
        }
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}
