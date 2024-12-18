package ru.rstudios.creative1.coding.eventvalues;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;
import ru.rstudios.creative1.coding.eventvalues.specific.EventBlockLocationValue;
import ru.rstudios.creative1.coding.eventvalues.specific.EventBlockValue;
import ru.rstudios.creative1.coding.eventvalues.specific.EventClickedItemValue;
import ru.rstudios.creative1.coding.eventvalues.specific.EventMessage;
import ru.rstudios.creative1.user.LocaleManages;
import ru.rstudios.creative1.user.User;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public enum ValueType {

    PLAYER_MESSAGE("coding.values.specific.player_message", Value.Category.EVENT, Material.WRITABLE_BOOK, EventMessage::new),
    EVENT_BLOCK("coding.values.specific.event_block", Value.Category.EVENT, Material.GRASS_BLOCK, EventBlockValue::new),
    EVENT_BLOCK_LOC("coding.values.specific.event_block_loc", Value.Category.EVENT, Material.PAPER, EventBlockLocationValue::new),
    EVENT_ITEM("coding.values.specific.event_item", Value.Category.EVENT, Material.CRAFTING_TABLE, EventClickedItemValue::new);

    private final String localeCode;
    private final Value.Category category;
    private final Material icon;
    private final Supplier<Value> supplier;

    ValueType (String localeCode, Value.Category category, Material icon, Supplier<Value> supplier) {
        this.localeCode = localeCode;
        this.category = category;
        this.icon = icon;
        this.supplier = supplier;
    }

    private static final Map<String, ValueType> BY_NAME = Arrays.stream(values()).collect(Collectors.toMap((e) -> e.name().toUpperCase(Locale.ROOT), Function.identity()));

    public String getLocaleCode() {
        return this.localeCode;
    }

    public Value getValueInstance() { return supplier.get(); }

    public ItemStack getIcon(User user) {
        ItemStack icon = new ItemStack(this.icon);
        ItemMeta meta = icon.getItemMeta();

        if (meta != null) {
            String localizedName = LocaleManages.getLocaleMessage(user.getLocale(), getLocaleCode() + ".name", false, "");
            List<String> localizedLore = LocaleManages.getLocaleMessagesS(user.getLocale(), getLocaleCode() + ".lore", new LinkedHashMap<>());

            Value instance = getValueInstance();
            String superclass = "coding.values.type." + instance.getClass().getSuperclass().getSimpleName().toLowerCase(Locale.ROOT);
            localizedLore.add("§7");
            localizedLore.add(LocaleManages.getLocaleMessage(user.getLocale(), superclass, false, ""));

            meta.setDisplayName(localizedName);
            meta.setLore(localizedLore);

            icon.setItemMeta(meta);
        }

        return icon;
    }

    public static @Nullable ValueType byName (String name) {
        return BY_NAME.get(name.toUpperCase(Locale.ROOT));
    }


    public static @Nullable ValueType getByLocaleCode (String localeCode) {
        return BY_NAME.values().stream().filter((e) -> e.localeCode.equalsIgnoreCase(localeCode)).findFirst().orElse(null);
    }

    public static @Nullable ValueType getByMaterial (Material material) {
        return BY_NAME.values().stream().filter(e -> e.icon == material).findFirst().orElse(null);
    }

    public static List<ValueType> getByCategory (Value.Category category) {
        List<ValueType> types = new LinkedList<>();

        Arrays.stream(values()).forEach(value -> {
            if (value.category == category) {
                types.add(value);
            }
        });

        return types;
    }

    public static Set<Value.Category> getCategories() {
        Set<Value.Category> categories = new LinkedHashSet<>();

        for (ValueType type : values()) {
            categories.add(type.category);
        }

        return categories;
    }

}