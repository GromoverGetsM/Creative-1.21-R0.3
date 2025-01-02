package ru.rstudios.creative1.coding.starters;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.rstudios.creative1.coding.MenuCategory;
import ru.rstudios.creative1.coding.starters.playerevent.*;
import ru.rstudios.creative1.coding.starters.uncommon.Cycle;
import ru.rstudios.creative1.coding.starters.uncommon.Function;
import ru.rstudios.creative1.user.LocaleManages;
import ru.rstudios.creative1.user.User;
import ru.rstudios.creative1.utils.Development;
import ru.rstudios.creative1.utils.ItemUtil;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Supplier;

public enum StarterCategory {

    FUNCTION(Development.BlockTypes.FUNCTION, null, Function::new, null, Material.AIR),
    CYCLE(Development.BlockTypes.CYCLE, null, Cycle::new, null, Material.AIR),

    PLAYER_JOIN(Development.BlockTypes.PLAYER_EVENT, MenuCategory.WORLD, PlayerJoin::new, PlayerJoin.Event.class, Material.OAK_DOOR),
    PLAYER_QUIT(Development.BlockTypes.PLAYER_EVENT, MenuCategory.WORLD, PlayerQuit::new, PlayerQuit.Event.class, Material.IRON_DOOR),
    PLAYER_BLOCK_PLACE(Development.BlockTypes.PLAYER_EVENT, MenuCategory.WORLD, PlayerBlockPlace::new, PlayerBlockPlace.Event.class, Material.STONE),
    PLAYER_BREAK_BLOCK(Development.BlockTypes.PLAYER_EVENT, MenuCategory.WORLD, PlayerBlockBreak::new, PlayerBlockBreak.Event.class, Material.COBBLESTONE),
    PLAYER_DAMAGED_BLOCK(Development.BlockTypes.PLAYER_EVENT, MenuCategory.WORLD, PlayerBlockDamaged::new, PlayerBlockDamaged.Event.class, Material.GRAVEL),
    PLAYER_DAMAGE_ABORTED(Development.BlockTypes.PLAYER_EVENT, MenuCategory.WORLD, PlayerDamageAborted::new, PlayerDamageAborted.Event.class, Material.ANDESITE),
    PLAYER_LEFT_CLICK(Development.BlockTypes.PLAYER_EVENT, MenuCategory.WORLD, PlayerLeftClicked::new, PlayerLeftClicked.Event.class, Material.IRON_PICKAXE),
    PLAYER_RIGHT_CLICK(Development.BlockTypes.PLAYER_EVENT, MenuCategory.WORLD, PlayerRightClicked::new, PlayerRightClicked.Event.class, Material.DIAMOND_PICKAXE),
    PLAYER_PHYSICAL_INTERACT(Development.BlockTypes.PLAYER_EVENT, MenuCategory.WORLD, PlayerPhysicalInteract::new, PlayerPhysicalInteract.Event.class, Material.GOLDEN_PICKAXE),

    PLAYER_INV_CLICK(Development.BlockTypes.PLAYER_EVENT, MenuCategory.INVENTORY, PlayerClickedInventory::new, PlayerClickedInventory.Event.class, Material.PAINTING),
    PLAYER_OPENED_INVENTORY(Development.BlockTypes.PLAYER_EVENT, MenuCategory.INVENTORY, PlayerOpenInventory::new, PlayerOpenInventory.Event.class, Material.LIME_GLAZED_TERRACOTTA),
    PLAYER_CLOSED_INVENTORY(Development.BlockTypes.PLAYER_EVENT, MenuCategory.INVENTORY, PlayerCloseInventory::new, PlayerCloseInventory.Event.class, Material.RED_GLAZED_TERRACOTTA),

    ENTITY_DAMAGED(Development.BlockTypes.PLAYER_EVENT, MenuCategory.FIGHTING, EntityDamaged::new, EntityDamaged.Event.class, Material.PORKCHOP),
    PLAYER_DAMAGED(Development.BlockTypes.PLAYER_EVENT, MenuCategory.FIGHTING, PlayerDamaged::new, PlayerDamaged.Event.class, Material.REDSTONE),
    PLAYER_DAMAGED_BY_MOB(Development.BlockTypes.PLAYER_EVENT, MenuCategory.FIGHTING, PlayerDamagedByMob::new, PlayerDamagedByMob.Event.class, Material.SWEET_BERRIES),
    PLAYER_DAMAGED_BY_PROJECTILE(Development.BlockTypes.PLAYER_EVENT, MenuCategory.FIGHTING, PlayerDamagedByProjectile::new, PlayerDamagedByProjectile.Event.class, Material.BOW),
    PLAYER_DAMAGED_MOB(Development.BlockTypes.PLAYER_EVENT, MenuCategory.FIGHTING, PlayerDamagedMob::new, PlayerDamagedMob.Event.class, Material.BEEF),
    PLAYER_DAMAGE_PLAYER(Development.BlockTypes.PLAYER_EVENT, MenuCategory.FIGHTING, PlayerDamagePlayer::new, PlayerDamagePlayer.Event.class, Material.PLAYER_HEAD),
    PLAYER_FALL_DAMAGED(Development.BlockTypes.PLAYER_EVENT, MenuCategory.FIGHTING, PlayerFallDamaged::new, PlayerFallDamaged.Event.class, Material.WIND_CHARGE),
    PLAYER_PROJECTILE_DAMAGE(Development.BlockTypes.PLAYER_EVENT, MenuCategory.FIGHTING, PlayerProjectileDamage::new, PlayerProjectileDamage.Event.class, Material.ARROW),

    PLAYER_MOVE_GENERALIZED(Development.BlockTypes.PLAYER_EVENT, MenuCategory.MOVEMENT, PlayerMoveGeneralized::new, PlayerMoveGeneralized.Event.class, Material.LEATHER_BOOTS),
    PLAYER_MOVE_BODY(Development.BlockTypes.PLAYER_EVENT, MenuCategory.MOVEMENT, PlayerMoveBody::new, PlayerMoveBody.Event.class, Material.CHAINMAIL_BOOTS),
    PLAYER_MOVE_HEAD(Development.BlockTypes.PLAYER_EVENT, MenuCategory.MOVEMENT, PlayerMoveHead::new, PlayerMoveHead.Event.class, Material.IRON_BOOTS),

    PLAYER_CHATTED(Development.BlockTypes.PLAYER_EVENT, MenuCategory.OTHER, PlayerChatted::new, PlayerChatted.Event.class, Material.WRITABLE_BOOK),
    FOOD_LEVEL_CHANGE(Development.BlockTypes.PLAYER_EVENT, MenuCategory.OTHER, FoodLevelChange::new, FoodLevelChange.Event.class, Material.COOKED_CHICKEN);

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

        return ItemUtil.clearItemFlags(icon);
    }

    public void setIcon(Material icon) {
        this.icon = icon;
    }
}
