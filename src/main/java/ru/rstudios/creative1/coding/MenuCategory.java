package ru.rstudios.creative1.coding;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import ru.rstudios.creative1.user.LocaleManages;
import ru.rstudios.creative1.user.User;
import ru.rstudios.creative1.utils.Development;
import ru.rstudios.creative1.utils.ItemUtil;

import java.util.LinkedHashMap;
import java.util.Locale;

public enum MenuCategory {

    FIGHTING(Material.NETHERITE_SWORD),
    INTERACTION(Material.GRASS_BLOCK),
    INVENTORY(Material.CHEST),
    MOVEMENT(Material.CHAINMAIL_BOOTS),
    BLOCKS(Material.STRUCTURE_BLOCK),
    WORLD(Material.BEACON),
    ENTITY(Material.VILLAGER_SPAWN_EGG),
    COMMUNICATION(Material.OAK_SIGN),
    STATE(Material.NAME_TAG),
    LINES(Material.REPEATING_COMMAND_BLOCK),
    APPEARANCE(Material.ARMOR_STAND),
    EVENTS(Material.NETHER_BRICKS),
    PARAMS(Material.ITEM_FRAME),
    PLAYER(Material.PLAYER_HEAD),
    OTHER(Material.PUMPKIN_SEEDS),

    TEXT_OPERATIONS(Material.BOOK),
    NUMBER_OPERATIONS(Material.SLIME_BALL),
    LOCATION_OPERATIONS(Material.PAPER),
    ITEM_OPERATIONS(Material.GLOW_ITEM_FRAME),
    LIST_OPERATIONS(Material.BOOKSHELF),
    MAP_OPERATIONS(Material.CHEST_MINECART);


    private final Material material;

    MenuCategory(Material material) {
        this.material = material;
    }

    public ItemStack getItem(User user) {
        String localeSubBuilder = "coding." + name().toLowerCase(Locale.ROOT);
        return ItemUtil.item(material, LocaleManages.getLocaleMessage(user.getLocale(), localeSubBuilder + ".name", false, ""), LocaleManages.getLocaleMessagesS(user.getLocale(), localeSubBuilder + ".lore", new LinkedHashMap<>()));
    }

    public static MenuCategory getByMaterial(Material material) {
        for (MenuCategory category : values()) {
            if (category.material == material) {
                return category;
            }
        }
        return null;
    }

}

