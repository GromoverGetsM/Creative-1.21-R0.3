package ru.rstudios.creative1.coding.starters;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.rstudios.creative1.coding.MenuCategory;
import ru.rstudios.creative1.coding.actions.ActionCategory;
import ru.rstudios.creative1.coding.starters.playerevent.*;
import ru.rstudios.creative1.user.LocaleManages;
import ru.rstudios.creative1.user.User;
import ru.rstudios.creative1.utils.Development;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Supplier;

public enum StarterCategory {

    PLAYER_JOIN(Development.BlockTypes.PLAYER_EVENT, MenuCategory.WORLD, PlayerJoin::new, PlayerJoin.Event.class, Material.OAK_DOOR),
    PLAYER_QUIT(Development.BlockTypes.PLAYER_EVENT, MenuCategory.WORLD, PlayerQuit::new, PlayerQuit.Event.class, Material.IRON_DOOR),
    PLAYER_BLOCK_PLACE(Development.BlockTypes.PLAYER_EVENT, MenuCategory.WORLD, PlayerBlockPlace::new, PlayerBlockPlace.Event.class, Material.STONE),
    PLAYER_BREAK_BLOCK(Development.BlockTypes.PLAYER_EVENT, MenuCategory.WORLD, PlayerBlockBreak::new, PlayerBlockBreak.Event.class, Material.COBBLESTONE),
    PLAYER_DAMAGED_BLOCK(Development.BlockTypes.PLAYER_EVENT, MenuCategory.WORLD, PlayerBlockDamaged::new, PlayerBlockDamaged.Event.class, Material.GRAVEL),
    PLAYER_DAMAGE_ABORTED(Development.BlockTypes.PLAYER_EVENT, MenuCategory.WORLD, PlayerDamageAborted::new, PlayerDamageAborted.Event.class, Material.ANDESITE),
    BLOCK_EXPLODED(Development.BlockTypes.BLOCK_EVENT, MenuCategory.WORLD, null, null, Material.TNT);

    private Development.BlockTypes type;
    private MenuCategory category;
    private final Supplier<Starter> constructor;
    private Class<? extends Event> eClass;
    private Material icon;

    StarterCategory (Development.BlockTypes type, MenuCategory category, Supplier<Starter> constructor, Class<? extends Event> eClass, Material icon) {
        this.type = type;
        this.category = category;
        this.constructor = constructor;
        this.eClass = eClass;
        this.icon = icon;
    }

    public static StarterCategory byName (String name) {
        return Arrays.stream(values()).filter(starterCat -> starterCat.name().equals(name.toUpperCase(Locale.ROOT))).findFirst().orElse(null);
    }

    public static @Nullable StarterCategory getByMaterial (Material m) {
        return Arrays.stream(values()).filter(cat -> cat.icon == m).findFirst().orElse(null);
    }

    public static @Nullable StarterCategory get(Material m, Development.BlockTypes type, MenuCategory category) {
        return Arrays.stream(values()).filter(cat -> cat.icon == m && cat.type == type && cat.category == category).findFirst().orElse(null);
    }

    public static Set<MenuCategory> getMenusCategories(Development.BlockTypes type) {
        Set<MenuCategory> set = new HashSet<>();
        for (StarterCategory category : values()) {
            if (category.type == type) {
                set.add(category.category);
            }
        }
        return set;
    }

    public static List<StarterCategory> getStartersByCategory (Development.BlockTypes type, MenuCategory category) {
        List<StarterCategory> list = new LinkedList<>();

        for (StarterCategory category1 : values()) {
            if (category1.category == category && category1.type == type) list.add(category1);
        }

        return list;
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

    public Supplier<Starter> getConstructor() {
        return constructor;
    }

    public Class<? extends Event> geteClass() {
        return eClass;
    }

    public void seteClass(Class<? extends Event> eClass) {
        this.eClass = eClass;
    }

    public ItemStack getIcon(User user) {
        ItemStack icon = new ItemStack(this.icon);
        ItemMeta meta = icon.getItemMeta();

        if (meta != null) {
            meta.displayName(Component.text(LocaleManages.getLocaleMessage(user.getLocale(), "menus.coding." + type.name().toLowerCase(Locale.ROOT) + "." + name().toLowerCase(Locale.ROOT) + ".name", false, "")));
            meta.lore(LocaleManages.getLocaleMessages(user.getLocale(), "menus.coding." + type.name().toLowerCase(Locale.ROOT) + "." + name().toLowerCase(Locale.ROOT) + ".lore", new LinkedHashMap<>()));

            meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP, ItemFlag.HIDE_ARMOR_TRIM, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_STORED_ENCHANTS, ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_DYE, ItemFlag.HIDE_UNBREAKABLE);

            icon.setItemMeta(meta);
        }

        return icon;
    }

    public void setIcon(Material icon) {
        this.icon = icon;
    }
}
