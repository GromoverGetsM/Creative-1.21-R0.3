package ru.rstudios.creative1.coding.actions;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.rstudios.creative1.coding.MenuCategory;
import ru.rstudios.creative1.coding.starters.StarterCategory;
import ru.rstudios.creative1.menu.CodingMenu;
import ru.rstudios.creative1.menu.SwitchItem;
import ru.rstudios.creative1.user.LocaleManages;
import ru.rstudios.creative1.user.User;
import ru.rstudios.creative1.utils.Development;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Supplier;

public enum ActionCategory {

    SEND_MESSAGE(Development.BlockTypes.PLAYER_ACTION, MenuCategory.COMMUNICATION, null, Material.WRITABLE_BOOK, true, "coding.actions.send_message.name", CodingMenu.MenuType.ALL_IN, List.of(CodingMenu.ArgumentType.TEXT), new LinkedHashMap<>());

    private Development.BlockTypes type;
    private MenuCategory category;
    private final Supplier<Object> constructor;
    private Material icon;
    private boolean hasChest;
    private String menuNamePath;
    private CodingMenu.MenuType menuType;
    private List<CodingMenu.ArgumentType> args;
    private HashMap<Integer, SwitchItem> switches;

    ActionCategory (Development.BlockTypes type, MenuCategory category, Supplier<Object> constructor, Material icon, boolean hasChest, String menuNamePath, CodingMenu.MenuType menuType, List<CodingMenu.ArgumentType> args, HashMap<Integer, SwitchItem> switches) {
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

    public Supplier<Object> getConstructor() {
        return constructor;
    }

    public ItemStack getIcon(User user) {
        ItemStack icon = new ItemStack(this.icon);
        ItemMeta meta = icon.getItemMeta();

        if (meta != null) {
            meta.displayName(Component.text(LocaleManages.getLocaleMessage(user.getLocale(), "menus.coding.actions." + name().toLowerCase(Locale.ROOT) + ".name", false, "")));
            meta.lore(LocaleManages.getLocaleMessages(user.getLocale(), "menus.coding.actions." + name().toLowerCase(Locale.ROOT) + ".lore", new LinkedHashMap<>()));

            icon.setItemMeta(meta);
        }

        return icon;
    }

    public void setIcon(Material icon) {
        this.icon = icon;
    }
}
