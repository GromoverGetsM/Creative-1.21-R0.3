package ru.rstudios.creative1.coding.actions;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.rstudios.creative1.coding.MenuCategory;
import ru.rstudios.creative1.coding.actions.ifplayer.NameEquals;
import ru.rstudios.creative1.coding.actions.playeraction.communication.*;
import ru.rstudios.creative1.coding.actions.playeraction.inventory.*;
import ru.rstudios.creative1.coding.actions.playeraction.movement.Teleport;
import ru.rstudios.creative1.coding.actions.worldaction.lines.CancelEvent;
import ru.rstudios.creative1.coding.actions.worldaction.lines.Wait;
import ru.rstudios.creative1.menu.CodingMenu;
import ru.rstudios.creative1.menu.SwitchItem;
import ru.rstudios.creative1.user.LocaleManages;
import ru.rstudios.creative1.user.User;
import ru.rstudios.creative1.utils.Development;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Supplier;

public enum ActionCategory {

    // Работа с инвентарём
    GIVE_ITEMS(Development.BlockTypes.PLAYER_ACTION, MenuCategory.INVENTORY, GiveItems::new, Material.CHEST, true, "coding.actions.give_items", CodingMenu.MenuType.ALL_IN, List.of(CodingMenu.ArgumentType.ITEMSTACK), new LinkedHashMap<>()),
    SET_ITEMS(Development.BlockTypes.PLAYER_ACTION, MenuCategory.INVENTORY, SetItems::new, Material.ENDER_CHEST, true, "coding.actions.set_items", CodingMenu.MenuType.ALL_IN, List.of(CodingMenu.ArgumentType.ITEMSTACK), new LinkedHashMap<>(Map.of(49,
            new SwitchItem(List.of("true", "false"), "menus.switches.actions.si.", List.of(Material.LIME_CONCRETE_POWDER, Material.RED_CONCRETE_POWDER))))),
    SET_ARMOR(Development.BlockTypes.PLAYER_ACTION, MenuCategory.INVENTORY, SetArmor::new, Material.IRON_CHESTPLATE, true, "coding.actions.set_armor", CodingMenu.MenuType.DEFAULT, List.of(CodingMenu.ArgumentType.ITEMSTACK, CodingMenu.ArgumentType.ITEMSTACK, CodingMenu.ArgumentType.ITEMSTACK, CodingMenu.ArgumentType.ITEMSTACK), new LinkedHashMap<>(Map.of(22,
            new SwitchItem(List.of("true", "false"), "menus.switches.actions.sa.", List.of(Material.LIME_CONCRETE_POWDER, Material.RED_CONCRETE_POWDER))))),
    CLEAR_INVENTORY(Development.BlockTypes.PLAYER_ACTION, MenuCategory.INVENTORY, ClearInventory::new, Material.GLASS, false, null, null, null, null),
    CLOSE_INVENTORY(Development.BlockTypes.PLAYER_ACTION, MenuCategory.INVENTORY, CloseInventory::new, Material.STRUCTURE_VOID, false, null, null, null, null),
    SET_ITEM_DELAY(Development.BlockTypes.PLAYER_ACTION, MenuCategory.INVENTORY, SetItemDelay::new, Material.CLOCK, true, "coding.actions.set_item_delay", CodingMenu.MenuType.DEFAULT, List.of(CodingMenu.ArgumentType.ITEMSTACK, CodingMenu.ArgumentType.NUMERIC), new LinkedHashMap<>()),
    GIVE_RANDOM_ITEM(Development.BlockTypes.PLAYER_ACTION, MenuCategory.INVENTORY, GiveRandomItem::new, Material.BLUE_SHULKER_BOX, true, "coding.actions.give_random_item", CodingMenu.MenuType.ALL_IN, List.of(CodingMenu.ArgumentType.ITEMSTACK), new LinkedHashMap<>()),
    OPEN_INTERFACE(Development.BlockTypes.PLAYER_ACTION, MenuCategory.INVENTORY, OpenInterface::new, Material.BROWN_SHULKER_BOX, true, "coding.actions.open_interface", CodingMenu.MenuType.DEFAULT, new LinkedList<>(),
            new LinkedHashMap<>(Map.of(13, new SwitchItem(List.of("workbench", "enchanting", "anvil", "cartography", "grindstone", "loom", "smith", "stonecutter"), "menus.switches.actions.oi.", List.of(
                    Material.CRAFTING_TABLE, Material.ENCHANTING_TABLE, Material.ANVIL, Material.CARTOGRAPHY_TABLE, Material.GRINDSTONE, Material.LOOM, Material.SMITHING_TABLE, Material.STONECUTTER
            ))))),

    // Коммуникация с игроком
    SEND_MESSAGE(Development.BlockTypes.PLAYER_ACTION, MenuCategory.COMMUNICATION, SendMessage::new, Material.WRITABLE_BOOK, true, "coding.actions.send_message", CodingMenu.MenuType.ALL_IN, List.of(CodingMenu.ArgumentType.TEXT), new LinkedHashMap<>(Map.of(49,
            new SwitchItem(List.of("together", "space", "newline"), "menus.switches.actions.sm.", List.of(Material.SLIME_BALL, Material.RABBIT_FOOT, Material.SHEARS))))),
    SEND_DIALOGUE(Development.BlockTypes.PLAYER_ACTION, MenuCategory.COMMUNICATION, SendDialogue::new, Material.PAPER, true, "coding.actions.send_dialogue", CodingMenu.MenuType.SET, List.of(CodingMenu.ArgumentType.NUMERIC, CodingMenu.ArgumentType.TEXT), new LinkedHashMap<>()),
    SEND_TITLE(Development.BlockTypes.PLAYER_ACTION, MenuCategory.COMMUNICATION, SendTitle::new, Material.ARMS_UP_POTTERY_SHERD, true, "coding.actions.send_title", CodingMenu.MenuType.DEFAULT, List.of(CodingMenu.ArgumentType.TEXT, CodingMenu.ArgumentType.TEXT, CodingMenu.ArgumentType.NUMERIC, CodingMenu.ArgumentType.NUMERIC, CodingMenu.ArgumentType.NUMERIC),
            new LinkedHashMap<>()),
    SEND_ACTIONBAR(Development.BlockTypes.PLAYER_ACTION, MenuCategory.COMMUNICATION, SendActionBar::new, Material.BOOK, true, "coding.actions.send_actionbar", CodingMenu.MenuType.DEFAULT, Collections.singletonList(CodingMenu.ArgumentType.TEXT), new LinkedHashMap<>()),
    SEND_ADVANCEMENT_TOAST(Development.BlockTypes.PLAYER_ACTION, MenuCategory.COMMUNICATION, SendAdvancementToast::new, Material.EMERALD, true, "coding.actions.send_advancement_toast", CodingMenu.MenuType.DEFAULT, List.of(CodingMenu.ArgumentType.TEXT, CodingMenu.ArgumentType.ITEMSTACK), new LinkedHashMap<>(Map.of(22,
            new SwitchItem(List.of("task", "goal", "challenge"), "menus.switches.actions.sat.", List.of(Material.WHITE_WOOL, Material.YELLOW_WOOL, Material.PURPLE_WOOL))))),
    CLEAR_CHAT(Development.BlockTypes.PLAYER_ACTION, MenuCategory.COMMUNICATION, ClearChat::new, Material.GLASS, false, null, null, null, null),
    PLAY_SOUND(Development.BlockTypes.PLAYER_ACTION, MenuCategory.COMMUNICATION, PlaySound::new, Material.MUSIC_DISC_CAT, true, "coding.actions.play_sound", CodingMenu.MenuType.DEFAULT, List.of(CodingMenu.ArgumentType.TEXT, CodingMenu.ArgumentType.NUMERIC, CodingMenu.ArgumentType.NUMERIC, CodingMenu.ArgumentType.LOCATION),
            new LinkedHashMap<>(Map.of(22, new SwitchItem(List.of("master", "music", "records", "weather", "blocks", "hostile", "neutral", "players", "ambient", "voice"), "menus.switches.actions.ps.",
                    List.of(Material.BRICKS, Material.NOTE_BLOCK, Material.LIGHTNING_ROD, Material.PISTON, Material.ZOMBIE_HEAD, Material.BEEF, Material.PLAYER_HEAD, Material.GRASS_BLOCK, Material.BELL))))),
    STOP_SOUND(Development.BlockTypes.PLAYER_ACTION, MenuCategory.COMMUNICATION, StopSound::new, Material.MUSIC_DISC_CHIRP, true, "coding.actions.stop_sound", CodingMenu.MenuType.ALL_IN, List.of(CodingMenu.ArgumentType.TEXT),
            new LinkedHashMap<>()),
    SHOW_BOOK(Development.BlockTypes.PLAYER_ACTION, MenuCategory.COMMUNICATION, ShowBook::new, Material.WRITTEN_BOOK, true, "coding.actions.show_book", CodingMenu.MenuType.ALL_IN, List.of(CodingMenu.ArgumentType.TEXT),
            new LinkedHashMap<>()),
    SHOW_WIN_SCREEN(Development.BlockTypes.PLAYER_ACTION, MenuCategory.COMMUNICATION, ShowWinScreen::new, Material.GOLD_INGOT, false, null, null, null, null),
    SHOW_DEMO_SCREEN(Development.BlockTypes.PLAYER_ACTION, MenuCategory.COMMUNICATION, ShowDemoScreen::new, Material.ITEM_FRAME, false, null, null, null, null),
    SHOW_ELDER_GUARDIAN(Development.BlockTypes.PLAYER_ACTION, MenuCategory.COMMUNICATION, ShowElderGuardian::new, Material.ELDER_GUARDIAN_SPAWN_EGG, false, null, null, null, null),

    // Перемещение игрока
    TELEPORT(Development.BlockTypes.PLAYER_ACTION, MenuCategory.MOVEMENT, Teleport::new, Material.ENDER_EYE, true, "coding.actions.teleport", CodingMenu.MenuType.DEFAULT, List.of(CodingMenu.ArgumentType.LOCATION), new LinkedHashMap<>(Map.of(22,
            new SwitchItem(List.of("all", "coords", "eyedir"), "menus.switches.actions.teleport", List.of(Material.PAPER, Material.COMPASS, Material.ENDER_EYE))))),

    // Действия мира
    CANCEL_EVENT(Development.BlockTypes.WORLD_ACTION, MenuCategory.LINES, CancelEvent::new, Material.BARRIER, false, null, null, null, null),
    WAIT(Development.BlockTypes.WORLD_ACTION, MenuCategory.LINES, Wait::new, Material.CLOCK, true, "coding.actions.wait", CodingMenu.MenuType.DEFAULT, List.of(CodingMenu.ArgumentType.NUMERIC), new LinkedHashMap<>(Map.of(22,
            new SwitchItem(List.of("ticks", "seconds", "minutes"), "menus.switches.actions.wait", List.of(Material.GOLDEN_BOOTS, Material.SNOWBALL, Material.CLOCK))))),

    // Если игрок
    NAME_EQUALS(Development.BlockTypes.IF_PLAYER, MenuCategory.PLAYER, NameEquals::new, Material.NAME_TAG, true, "coding.actions.if_player", CodingMenu.MenuType.ALL_IN, List.of(CodingMenu.ArgumentType.TEXT), new LinkedHashMap<>());


    private Development.BlockTypes type;
    private MenuCategory category;
    private final Supplier<Action> constructor;
    private Material icon;
    private final boolean hasChest;
    private final String menuNamePath;
    private final CodingMenu.MenuType menuType;
    private final List<CodingMenu.ArgumentType> args;
    private final HashMap<Integer, SwitchItem> switches;

    ActionCategory (Development.BlockTypes type, MenuCategory category, Supplier<Action> constructor, Material icon, boolean hasChest, String menuNamePath, CodingMenu.MenuType menuType, List<CodingMenu.ArgumentType> args, HashMap<Integer, SwitchItem> switches) {
        this.category = category;
        this.type = type;
        this.constructor = constructor;
        this.icon = icon;
        this.hasChest = hasChest;
        this.menuNamePath = menuNamePath;
        this.menuType = menuType;
        this.args = args;
        this.switches = switches;
    }

    public boolean hasChest() {
        return hasChest;
    }

    public static ActionCategory byName (String name) {
        return Arrays.stream(values()).filter(actionCategory -> actionCategory.name().equals(name.toUpperCase(Locale.ROOT))).findFirst().orElse(null);
    }

    public static Set<MenuCategory> getMenuCategories (Development.BlockTypes type) {
        Set<MenuCategory> categories = new LinkedHashSet<>();

        for (ActionCategory category : values()) {
            if (category.type == type) categories.add(category.category);
        }

        return categories;
    }

    public static List<ActionCategory> getActionsByCategory (Development.BlockTypes type, MenuCategory category) {
        List<ActionCategory> actions = new LinkedList<>();

        for (ActionCategory category1 : values()) {
            if (category1.type == type && category1.category == category) actions.add(category1);
        }

        return actions;
    }

    public static @Nullable ActionCategory getByMaterial (Material m) {
        return Arrays.stream(values()).filter(cat -> cat.icon == m).findFirst().orElse(null);
    }

    public static @Nullable ActionCategory get(Material m, Development.BlockTypes type, MenuCategory category) {
        return Arrays.stream(values()).filter(cat -> cat.icon == m && cat.type == type && cat.category == category).findFirst().orElse(null);
    }

    public CodingMenu getCodingMenu() {
        if (hasChest) return new CodingMenu(menuNamePath, menuType, args, switches);
        else return null;
    }

    public Development.BlockTypes getType() {
        return type;
    }

    public void setType(Development.BlockTypes type) {
        this.type = type;
    }

    public MenuCategory getCategory() {
        return category;
    }

    public void setCategory(MenuCategory category) {
        this.category = category;
    }

    public Supplier<Action> getConstructor() {
        return constructor;
    }

    public ItemStack getIcon(User user) {
        ItemStack icon = new ItemStack(this.icon);
        ItemMeta meta = icon.getItemMeta();

        if (meta != null) {
            meta.displayName(Component.text(LocaleManages.getLocaleMessage(user.getLocale(), "menus.coding.actions." + name().toLowerCase(Locale.ROOT) + ".name", false, "")));
            meta.lore(LocaleManages.getLocaleMessages(user.getLocale(), "menus.coding.actions." + name().toLowerCase(Locale.ROOT) + ".lore", new LinkedHashMap<>()));

            meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP, ItemFlag.HIDE_ARMOR_TRIM, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_STORED_ENCHANTS, ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_DYE, ItemFlag.HIDE_UNBREAKABLE);

            icon.setItemMeta(meta);
        }

        return icon;
    }

    public void setIcon(Material icon) {
        this.icon = icon;
    }
}
