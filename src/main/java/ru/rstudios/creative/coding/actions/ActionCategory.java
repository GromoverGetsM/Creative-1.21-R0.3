package ru.rstudios.creative.coding.actions;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.rstudios.creative.coding.MenuCategory;
import ru.rstudios.creative.coding.actions.actionvar.*;
import ru.rstudios.creative.coding.actions.entityaction.settings.SpawnMob;
import ru.rstudios.creative.coding.actions.entityaction.settings.SwitchAI;
import ru.rstudios.creative.coding.actions.ifplayer.*;
import ru.rstudios.creative.coding.actions.ifvariable.*;
import ru.rstudios.creative.coding.actions.playeraction.appearence.HideScoreboard;
import ru.rstudios.creative.coding.actions.playeraction.appearence.ResetWorldBorder;
import ru.rstudios.creative.coding.actions.playeraction.appearence.SetWorldBorder;
import ru.rstudios.creative.coding.actions.playeraction.appearence.ShowScoreboard;
import ru.rstudios.creative.coding.actions.playeraction.communication.*;
import ru.rstudios.creative.coding.actions.playeraction.inventory.*;
import ru.rstudios.creative.coding.actions.playeraction.movement.Teleport;
import ru.rstudios.creative.coding.actions.playeraction.movement.ToOtherPlot;
import ru.rstudios.creative.coding.actions.playeraction.params.*;
import ru.rstudios.creative.coding.actions.select.*;
import ru.rstudios.creative.coding.actions.worldaction.appearence.*;
import ru.rstudios.creative.coding.actions.worldaction.lines.*;
import ru.rstudios.creative.coding.actions.worldaction.world.*;
import ru.rstudios.creative.menu.CodingMenu;
import ru.rstudios.creative.menu.SwitchItem;
import ru.rstudios.creative.user.LocaleManages;
import ru.rstudios.creative.user.User;
import ru.rstudios.creative.utils.Development;
import ru.rstudios.creative.utils.ItemUtil;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Supplier;

public enum ActionCategory {

    // Действие игрока - Работа с инвентарём
    GIVE_ITEMS(Development.BlockTypes.PLAYER_ACTION, MenuCategory.INVENTORY, GiveItems::new, Material.CHEST, true, CodingMenu.MenuType.ALL_IN, List.of(CodingMenu.ArgumentType.ITEMSTACK), new LinkedHashMap<>()),
    SET_ITEMS(Development.BlockTypes.PLAYER_ACTION, MenuCategory.INVENTORY, SetItems::new, Material.ENDER_CHEST, true, CodingMenu.MenuType.ALL_IN, List.of(CodingMenu.ArgumentType.ITEMSTACK), new LinkedHashMap<>(Map.of(49,
            new SwitchItem(List.of("true", "false"), "menus.switches.actions.si", List.of(Material.LIME_CONCRETE_POWDER, Material.RED_CONCRETE_POWDER))))),
    SET_ARMOR(Development.BlockTypes.PLAYER_ACTION, MenuCategory.INVENTORY, SetArmor::new, Material.IRON_CHESTPLATE, true, CodingMenu.MenuType.DEFAULT, List.of(CodingMenu.ArgumentType.ITEMSTACK, CodingMenu.ArgumentType.ITEMSTACK, CodingMenu.ArgumentType.ITEMSTACK, CodingMenu.ArgumentType.ITEMSTACK), new LinkedHashMap<>(Map.of(22,
            new SwitchItem(List.of("true", "false"), "menus.switches.actions.sa", List.of(Material.LIME_CONCRETE_POWDER, Material.RED_CONCRETE_POWDER))))),
    CLEAR_INVENTORY(Development.BlockTypes.PLAYER_ACTION, MenuCategory.INVENTORY, ClearInventory::new, Material.GLASS, false, null, null, null),
    CLOSE_INVENTORY(Development.BlockTypes.PLAYER_ACTION, MenuCategory.INVENTORY, CloseInventory::new, Material.STRUCTURE_VOID, false, null, null, null),
    SET_ITEM_DELAY(Development.BlockTypes.PLAYER_ACTION, MenuCategory.INVENTORY, SetItemDelay::new, Material.CLOCK, true, CodingMenu.MenuType.DEFAULT, List.of(CodingMenu.ArgumentType.ITEMSTACK, CodingMenu.ArgumentType.NUMERIC), new LinkedHashMap<>()),
    REMOVE_ITEM(Development.BlockTypes.PLAYER_ACTION, MenuCategory.INVENTORY, RemoveItem::new, Material.COBWEB, true, CodingMenu.MenuType.ALL_IN, List.of(CodingMenu.ArgumentType.ITEMSTACK), new LinkedHashMap<>()),
    GIVE_RANDOM_ITEM(Development.BlockTypes.PLAYER_ACTION, MenuCategory.INVENTORY, GiveRandomItem::new, Material.BLUE_SHULKER_BOX, true, CodingMenu.MenuType.ALL_IN, List.of(CodingMenu.ArgumentType.ITEMSTACK), new LinkedHashMap<>()),
    GET_PLAYER_ITEM(Development.BlockTypes.PLAYER_ACTION, MenuCategory.INVENTORY, GetPlayerItem::new, Material.ITEM_FRAME, true,  CodingMenu.MenuType.DEFAULT, List.of(CodingMenu.ArgumentType.DYNAMIC_VAR, CodingMenu.ArgumentType.NUMERIC), new LinkedHashMap<>()),
    OPEN_INTERFACE(Development.BlockTypes.PLAYER_ACTION, MenuCategory.INVENTORY, OpenInterface::new, Material.BROWN_SHULKER_BOX, true, CodingMenu.MenuType.DEFAULT, new LinkedList<>(),
            new LinkedHashMap<>(Map.of(13, new SwitchItem(List.of("workbench", "enchanting", "anvil", "cartography", "grindstone", "loom", "smith", "stonecutter"), "menus.switches.actions.oi", List.of(
                    Material.CRAFTING_TABLE, Material.ENCHANTING_TABLE, Material.ANVIL, Material.CARTOGRAPHY_TABLE, Material.GRINDSTONE, Material.LOOM, Material.SMITHING_TABLE, Material.STONECUTTER
            ))))),
    OPEN_CONTAINER(Development.BlockTypes.PLAYER_ACTION, MenuCategory.INVENTORY, OpenContainer::new, Material.TRAPPED_CHEST, true, CodingMenu.MenuType.DEFAULT, List.of(CodingMenu.ArgumentType.LOCATION), new LinkedHashMap<>(Map.of(22,
            new SwitchItem(List.of("orig", "copy"), "menus.switches.actions.opencont", List.of(Material.CHEST, Material.ENDER_CHEST))))),

    // Действие игрока - Коммуникация с игроком
    SEND_MESSAGE(Development.BlockTypes.PLAYER_ACTION, MenuCategory.COMMUNICATION, SendMessage::new, Material.WRITABLE_BOOK, true, CodingMenu.MenuType.ALL_IN, List.of(CodingMenu.ArgumentType.TEXT), new LinkedHashMap<>(Map.of(49,
            new SwitchItem(List.of("together", "space", "newline"), "menus.switches.actions.sm", List.of(Material.SLIME_BALL, Material.RABBIT_FOOT, Material.SHEARS))))),
    SEND_DIALOGUE(Development.BlockTypes.PLAYER_ACTION, MenuCategory.COMMUNICATION, SendDialogue::new, Material.PAPER, true, CodingMenu.MenuType.DUO_SET, List.of(CodingMenu.ArgumentType.NUMERIC, CodingMenu.ArgumentType.TEXT, CodingMenu.ArgumentType.TEXT), new LinkedHashMap<>()),
    SEND_TITLE(Development.BlockTypes.PLAYER_ACTION, MenuCategory.COMMUNICATION, SendTitle::new, Material.ARMS_UP_POTTERY_SHERD, true, CodingMenu.MenuType.DEFAULT, List.of(CodingMenu.ArgumentType.TEXT, CodingMenu.ArgumentType.TEXT, CodingMenu.ArgumentType.NUMERIC, CodingMenu.ArgumentType.NUMERIC, CodingMenu.ArgumentType.NUMERIC),
            new LinkedHashMap<>()),
    SEND_ACTIONBAR(Development.BlockTypes.PLAYER_ACTION, MenuCategory.COMMUNICATION, SendActionBar::new, Material.BOOK, true, CodingMenu.MenuType.DEFAULT, Collections.singletonList(CodingMenu.ArgumentType.TEXT), new LinkedHashMap<>()),
    SEND_ADVANCEMENT_TOAST(Development.BlockTypes.PLAYER_ACTION, MenuCategory.COMMUNICATION, SendAdvancementToast::new, Material.EMERALD, true, CodingMenu.MenuType.DEFAULT, List.of(CodingMenu.ArgumentType.TEXT, CodingMenu.ArgumentType.ITEMSTACK), new LinkedHashMap<>(Map.of(22,
            new SwitchItem(List.of("task", "goal", "challenge"), "menus.switches.actions.sat", List.of(Material.WHITE_WOOL, Material.YELLOW_WOOL, Material.PURPLE_WOOL))))),
    CLEAR_CHAT(Development.BlockTypes.PLAYER_ACTION, MenuCategory.COMMUNICATION, ClearChat::new, Material.GLASS, false, null, null, null),
    PLAY_SOUND(Development.BlockTypes.PLAYER_ACTION, MenuCategory.COMMUNICATION, PlaySound::new, Material.MUSIC_DISC_CAT, true, CodingMenu.MenuType.DEFAULT, List.of(CodingMenu.ArgumentType.TEXT, CodingMenu.ArgumentType.NUMERIC, CodingMenu.ArgumentType.NUMERIC, CodingMenu.ArgumentType.LOCATION),
            new LinkedHashMap<>(Map.of(22, new SwitchItem(List.of("master", "music", "records", "weather", "blocks", "hostile", "neutral", "players", "ambient", "voice"), "menus.switches.actions.ps",
                    List.of(Material.BRICKS, Material.NOTE_BLOCK, Material.LIGHTNING_ROD, Material.PISTON, Material.ZOMBIE_HEAD, Material.BEEF, Material.PLAYER_HEAD, Material.GRASS_BLOCK, Material.BELL))))),
    STOP_SOUND(Development.BlockTypes.PLAYER_ACTION, MenuCategory.COMMUNICATION, StopSound::new, Material.MUSIC_DISC_CHIRP, true, CodingMenu.MenuType.ALL_IN, List.of(CodingMenu.ArgumentType.TEXT),
            new LinkedHashMap<>()),
    SHOW_BOOK(Development.BlockTypes.PLAYER_ACTION, MenuCategory.COMMUNICATION, ShowBook::new, Material.WRITTEN_BOOK, true, CodingMenu.MenuType.ALL_IN, List.of(CodingMenu.ArgumentType.TEXT),
            new LinkedHashMap<>()),
    SHOW_WIN_SCREEN(Development.BlockTypes.PLAYER_ACTION, MenuCategory.COMMUNICATION, ShowWinScreen::new, Material.GOLD_INGOT, false, null, null, null),
    SHOW_DEMO_SCREEN(Development.BlockTypes.PLAYER_ACTION, MenuCategory.COMMUNICATION, ShowDemoScreen::new, Material.ITEM_FRAME, false, null, null, null),
    SHOW_ELDER_GUARDIAN(Development.BlockTypes.PLAYER_ACTION, MenuCategory.COMMUNICATION, ShowElderGuardian::new, Material.ELDER_GUARDIAN_SPAWN_EGG, false, null, null, null),

    // Действие игрока - Перемещение игрока
    TELEPORT(Development.BlockTypes.PLAYER_ACTION, MenuCategory.MOVEMENT, Teleport::new, Material.ENDER_PEARL, true, CodingMenu.MenuType.DEFAULT, List.of(CodingMenu.ArgumentType.LOCATION), new LinkedHashMap<>(Map.of(22,
            new SwitchItem(List.of("all", "coords", "eyedir"), "menus.switches.actions.teleport", List.of(Material.PAPER, Material.COMPASS, Material.ENDER_EYE))))),
    TO_OTHER_PLOT(Development.BlockTypes.PLAYER_ACTION, MenuCategory.MOVEMENT, ToOtherPlot::new, Material.DARK_OAK_DOOR, true, CodingMenu.MenuType.DEFAULT, List.of(CodingMenu.ArgumentType.NUMERIC), new LinkedHashMap<>()),

    // Действие игрока - Параметры (Сделанные чаем)
    SET_FLYING(Development.BlockTypes.PLAYER_ACTION, MenuCategory.PARAMS, SetFlying::new, Material.FEATHER, true, CodingMenu.MenuType.DEFAULT, new LinkedList<>(), new LinkedHashMap<>(Map.of(11,
            new SwitchItem(List.of("true", "false"), "menus.switches.actions.set_fly", List.of(Material.LIME_CONCRETE_POWDER, Material.RED_CONCRETE_POWDER)), 15, new SwitchItem(List.of("true", "false"), "menus.switches.actions.allow_fly", List.of(Material.LIME_CONCRETE_POWDER, Material.RED_CONCRETE_POWDER))))),
    SET_GAMEMODE(Development.BlockTypes.PLAYER_ACTION, MenuCategory.PARAMS, SetGamemode::new, Material.ANVIL, true, CodingMenu.MenuType.DEFAULT, new LinkedList<>(), new LinkedHashMap<>(Map.of(13,
            new SwitchItem(List.of("creative", "survival", "adventure", "spectator"), "menus.switches.actions.set_gm", List.of(Material.BRICKS, Material.BEEF, Material.IRON_SWORD, Material.ENDER_EYE))))),
    SET_IMMORTALITY(Development.BlockTypes.PLAYER_ACTION, MenuCategory.PARAMS, SetImmortality::new, Material.TOTEM_OF_UNDYING, true, CodingMenu.MenuType.DEFAULT, new LinkedList<>(), new LinkedHashMap<>(Map.of(13,
            new SwitchItem(List.of("true", "false"), "menus.switches.actions.set_immortality", List.of(Material.LIME_CONCRETE_POWDER, Material.RED_CONCRETE_POWDER))))),
    SET_HEALTH(Development.BlockTypes.PLAYER_ACTION, MenuCategory.PARAMS, SetHealth::new, Material.APPLE, true, CodingMenu.MenuType.DEFAULT, List.of(CodingMenu.ArgumentType.NUMERIC), new LinkedHashMap<>()),

    SET_GLOWING(Development.BlockTypes.PLAYER_ACTION, MenuCategory.PARAMS, SetGlowing::new, Material.BEACON, true, CodingMenu.MenuType.DEFAULT, new LinkedList<>(), new LinkedHashMap<>(Map.of(13,
            new SwitchItem(List.of("true", "false"), "menus.switches.actions.set_glowing", List.of(Material.LIME_CONCRETE_POWDER, Material.RED_CONCRETE_POWDER))))),

    SHOW_SCOREBOARD(Development.BlockTypes.PLAYER_ACTION, MenuCategory.APPEARANCE, ShowScoreboard::new, Material.LANTERN, true, CodingMenu.MenuType.DEFAULT, List.of(CodingMenu.ArgumentType.TEXT), new LinkedHashMap<>()),
    HIDE_SCOREBOARD(Development.BlockTypes.PLAYER_ACTION, MenuCategory.APPEARANCE, HideScoreboard::new, Material.GLASS, false, null, null, null),
    SET_BORDER(Development.BlockTypes.PLAYER_ACTION, MenuCategory.APPEARANCE, SetWorldBorder::new, Material.SHIELD, true, CodingMenu.MenuType.DEFAULT, List.of(CodingMenu.ArgumentType.TEXT), new LinkedHashMap<>()),
    RESET_BORDER(Development.BlockTypes.PLAYER_ACTION, MenuCategory.APPEARANCE, ResetWorldBorder::new, Material.IRON_NUGGET, false, null, null, null),

    // Действия мира - внешний вид
    CREATE_SCOREBOARD(Development.BlockTypes.WORLD_ACTION, MenuCategory.APPEARANCE, CreateScoreboard::new, Material.PAINTING, true, CodingMenu.MenuType.DEFAULT, List.of(CodingMenu.ArgumentType.TEXT, CodingMenu.ArgumentType.TEXT), new LinkedHashMap<>()),
    DELETE_SCOREBOARD(Development.BlockTypes.WORLD_ACTION, MenuCategory.APPEARANCE, DeleteScoreboard::new, Material.STRUCTURE_VOID, true, CodingMenu.MenuType.DEFAULT, List.of(CodingMenu.ArgumentType.TEXT), new LinkedHashMap<>()),
    SCOREBOARD_SET_SCORE(Development.BlockTypes.WORLD_ACTION, MenuCategory.APPEARANCE, ScoreboardSetScore::new, Material.EMERALD, true, CodingMenu.MenuType.DEFAULT, List.of(CodingMenu.ArgumentType.TEXT, CodingMenu.ArgumentType.NUMERIC, CodingMenu.ArgumentType.TEXT), new LinkedHashMap<>()),
    SCOREBOARD_RESET_SCORE(Development.BlockTypes.WORLD_ACTION, MenuCategory.APPEARANCE, ScoreboardResetScore::new, Material.GLASS, true, CodingMenu.MenuType.DEFAULT, List.of(CodingMenu.ArgumentType.TEXT, CodingMenu.ArgumentType.TEXT), new LinkedHashMap<>()),
    SCOREBOARD_SET_DISPLAY(Development.BlockTypes.WORLD_ACTION, MenuCategory.APPEARANCE, ScoreboardSetDisplayname::new, Material.WRITTEN_BOOK, true, CodingMenu.MenuType.DEFAULT, List.of(CodingMenu.ArgumentType.TEXT, CodingMenu.ArgumentType.TEXT), new LinkedHashMap<>()),

    // Действия мира - Линии
    CANCEL_EVENT(Development.BlockTypes.WORLD_ACTION, MenuCategory.LINES, CancelEvent::new, Material.BARRIER, false, null, null, null),
    WAIT(Development.BlockTypes.WORLD_ACTION, MenuCategory.LINES, Wait::new, Material.CLOCK, true, CodingMenu.MenuType.DEFAULT, List.of(CodingMenu.ArgumentType.NUMERIC), new LinkedHashMap<>(Map.of(22,
            new SwitchItem(List.of("ticks", "seconds", "minutes"), "menus.switches.actions.wait", List.of(Material.GOLDEN_BOOTS, Material.SNOWBALL, Material.CLOCK))))),
    LAUNCH_FUNCTION(Development.BlockTypes.WORLD_ACTION, MenuCategory.LINES, LaunchFunction::new, Material.LAPIS_LAZULI, true, CodingMenu.MenuType.DEFAULT, List.of(CodingMenu.ArgumentType.TEXT), new LinkedHashMap<>()),
    LAUNCH_CYCLE(Development.BlockTypes.WORLD_ACTION, MenuCategory.LINES, LaunchCycle::new, Material.POTATO, true, CodingMenu.MenuType.DEFAULT, List.of(CodingMenu.ArgumentType.TEXT), new LinkedHashMap<>()),
    STOP_CYCLE(Development.BlockTypes.WORLD_ACTION, MenuCategory.LINES, StopCycle::new, Material.POISONOUS_POTATO, true, CodingMenu.MenuType.DEFAULT, List.of(CodingMenu.ArgumentType.TEXT), new LinkedHashMap<>()),
    CLEAR_CACHE_VARS(Development.BlockTypes.WORLD_ACTION, MenuCategory.LINES, ClearCacheVars::new, Material.GLASS, false, null, null, null),
    BREAK_LINE_EXECUTE(Development.BlockTypes.WORLD_ACTION, MenuCategory.LINES, BreakLineExecute::new, Material.TNT, false, null, null, null),

    // Действия мира - Мир
    SET_BLOCK(Development.BlockTypes.WORLD_ACTION, MenuCategory.WORLD, PlaceBlock::new, Material.GRASS_BLOCK, true, CodingMenu.MenuType.SET, List.of(CodingMenu.ArgumentType.ITEMSTACK, CodingMenu.ArgumentType.LOCATION), new LinkedHashMap<>()),
    SET_WORLD_ICON(Development.BlockTypes.WORLD_ACTION, MenuCategory.WORLD, SetWorldIcon::new, Material.NETHER_STAR, true, CodingMenu.MenuType.DEFAULT, List.of(CodingMenu.ArgumentType.ITEMSTACK), new LinkedHashMap<>()),
    SET_WORLD_CUSTOM_ID(Development.BlockTypes.WORLD_ACTION, MenuCategory.WORLD, SetWorldCustomId::new, Material.REPEATER, true, CodingMenu.MenuType.DEFAULT, List.of(CodingMenu.ArgumentType.TEXT), new LinkedHashMap<>()),
    SET_WORLD_NAME(Development.BlockTypes.WORLD_ACTION, MenuCategory.WORLD, SetWorldName::new, Material.NAME_TAG, true, CodingMenu.MenuType.DEFAULT, List.of(CodingMenu.ArgumentType.TEXT), new LinkedHashMap<>()),
    SET_WORLD_PRIVACY(Development.BlockTypes.WORLD_ACTION,MenuCategory.WORLD, SetWorldPrivacy::new, Material.IRON_DOOR, true, CodingMenu.MenuType.DEFAULT, new LinkedList<>(), new LinkedHashMap<>(Map.of(13,
            new SwitchItem(List.of("true", "false"), "menus.switches.actions.set_world_privacy", List.of(Material.LIME_CONCRETE_POWDER, Material.RED_CONCRETE_POWDER))))),
    SET_WORLD_LORE(Development.BlockTypes.WORLD_ACTION, MenuCategory.WORLD, SetWorldLore::new, Material.BOOKSHELF, true, CodingMenu.MenuType.ALL_IN, List.of(CodingMenu.ArgumentType.TEXT), new LinkedHashMap<>()),
    CREATE_WORLDBORDER(Development.BlockTypes.WORLD_ACTION, MenuCategory.WORLD, CreateWorldBorder::new, Material.HEART_OF_THE_SEA, true, CodingMenu.MenuType.DEFAULT, List.of(CodingMenu.ArgumentType.TEXT), new LinkedHashMap<>()),
    DELETE_WORLDBORDER(Development.BlockTypes.WORLD_ACTION, MenuCategory.WORLD, DeleteWorldBorder::new, Material.END_CRYSTAL, true, CodingMenu.MenuType.DEFAULT, List.of(CodingMenu.ArgumentType.TEXT), new LinkedHashMap<>()),
    GET_ITEM_FROM_CONTAINER(Development.BlockTypes.WORLD_ACTION, MenuCategory.WORLD, GetItemFromContainer::new, Material.CHEST, true, CodingMenu.MenuType.DEFAULT, List.of(CodingMenu.ArgumentType.DYNAMIC_VAR, CodingMenu.ArgumentType.LOCATION, CodingMenu.ArgumentType.NUMERIC), new LinkedHashMap<>()),
    GET_CONTAINER_TITLE(Development.BlockTypes.WORLD_ACTION, MenuCategory.WORLD, GetItemFromContainer::new, Material.NAME_TAG, true, CodingMenu.MenuType.DEFAULT, List.of(CodingMenu.ArgumentType.DYNAMIC_VAR, CodingMenu.ArgumentType.LOCATION), new LinkedHashMap<>()),
    SET_BORDER_CENTER(Development.BlockTypes.WORLD_ACTION, MenuCategory.WORLD, SetBorderCenter::new, Material.LIGHTNING_ROD, true, CodingMenu.MenuType.DEFAULT, List.of(CodingMenu.ArgumentType.TEXT, CodingMenu.ArgumentType.LOCATION), new LinkedHashMap<>()),
    SET_BORDER_SIZE(Development.BlockTypes.WORLD_ACTION, MenuCategory.WORLD, SetBorderSize::new, Material.WAXED_OXIDIZED_COPPER_BULB, true, CodingMenu.MenuType.DEFAULT, List.of(CodingMenu.ArgumentType.TEXT, CodingMenu.ArgumentType.NUMERIC), new LinkedHashMap<>()),
    SET_BORDER_DAMAGE(Development.BlockTypes.WORLD_ACTION, MenuCategory.WORLD, SetBorderDamage::new, Material.IRON_SWORD, true, CodingMenu.MenuType.DEFAULT, List.of(CodingMenu.ArgumentType.TEXT, CodingMenu.ArgumentType.NUMERIC), new LinkedHashMap<>()),
    SET_BORDER_BUFFER(Development.BlockTypes.WORLD_ACTION, MenuCategory.WORLD, SetBorderBuffer::new, Material.SHIELD, true, CodingMenu.MenuType.DEFAULT, List.of(CodingMenu.ArgumentType.TEXT, CodingMenu.ArgumentType.NUMERIC), new LinkedHashMap<>()),
    SET_BORDER_WARNING(Development.BlockTypes.WORLD_ACTION, MenuCategory.WORLD, SetBorderWarning::new, Material.GOAT_HORN, true, CodingMenu.MenuType.DEFAULT, List.of(CodingMenu.ArgumentType.TEXT, CodingMenu.ArgumentType.NUMERIC), new LinkedHashMap<>()),
    SPAWN_LIGHTING(Development.BlockTypes.WORLD_ACTION, MenuCategory.WORLD, SpawnLighting::new, Material.WIND_CHARGE, true, CodingMenu.MenuType.DEFAULT, List.of(CodingMenu.ArgumentType.LOCATION), new LinkedHashMap<>()),

    // Если игрок - общее
    NAME_EQUALS(Development.BlockTypes.IF_PLAYER, MenuCategory.PLAYER, NameEquals::new, Material.NAME_TAG, true, CodingMenu.MenuType.ALL_IN, List.of(CodingMenu.ArgumentType.TEXT), new LinkedHashMap<>()),
    MESSAGE_EQUALS(Development.BlockTypes.IF_PLAYER, MenuCategory.PLAYER, MessageEquals::new, Material.WRITABLE_BOOK, true, CodingMenu.MenuType.ALL_IN, List.of(CodingMenu.ArgumentType.TEXT), new LinkedHashMap<>()),

    // Если игрок - проверка состояний
    IF_PLAYER_FIRST_JOINED(Development.BlockTypes.IF_PLAYER, MenuCategory.STATE, IfFirstJoin::new, Material.GOLD_INGOT, false, null, null, null),
    IF_PLAYER_BLOCKING(Development.BlockTypes.IF_PLAYER, MenuCategory.STATE, IfPlayerBlocking::new, Material.SHIELD, false, null, null, null),
    IF_PLAYER_FLYING(Development.BlockTypes.IF_PLAYER, MenuCategory.STATE, IfPlayerFlying::new, Material.CHAINMAIL_BOOTS, false, null, null, null),
    IF_PLAYER_GLIDING(Development.BlockTypes.IF_PLAYER, MenuCategory.STATE, IfPlayerGliding::new, Material.ELYTRA, false, null, null, null),
    IF_PLAYER_LICENSED(Development.BlockTypes.IF_PLAYER, MenuCategory.STATE, IfPlayerLicensed::new, Material.GOLD_INGOT, false, null, null, null),
    IF_PLAYER_SLEEPING(Development.BlockTypes.IF_PLAYER, MenuCategory.STATE, IfPlayerSleeping::new, Material.RED_BED, false, null, null, null),
    IF_PLAYER_SNEAKING(Development.BlockTypes.IF_PLAYER, MenuCategory.STATE, IfPlayerSneaking::new, Material.CHAINMAIL_LEGGINGS, false, null, null, null),
    IF_PLAYER_SPRINTING(Development.BlockTypes.IF_PLAYER, MenuCategory.STATE, IfPlayerSprinting::new, Material.GOLDEN_BOOTS, false, null, null, null),
    IF_PLAYER_SWIMMING(Development.BlockTypes.IF_PLAYER, MenuCategory.STATE, IfPlayerSwiming::new, Material.WATER_BUCKET, false, null, null, null),

    // Если игрок - предметы
    PLAYER_HOLD_ITEM(Development.BlockTypes.IF_PLAYER, MenuCategory.INVENTORY, PlayerHoldItem::new, Material.SHIELD, true, CodingMenu.MenuType.ALL_IN, List.of(CodingMenu.ArgumentType.ITEMSTACK), new LinkedHashMap<>(Map.of(49,
            new SwitchItem(List.of("hand", "off_hand"), "menus.switches.actions.player_hold_item", List.of(Material.BRICK, Material.NETHER_BRICK))))),
    ITEM_EQUALS(Development.BlockTypes.IF_PLAYER, MenuCategory.INVENTORY, ItemEquals::new, Material.CRAFTING_TABLE, true, CodingMenu.MenuType.ALL_IN, List.of(CodingMenu.ArgumentType.ITEMSTACK), new LinkedHashMap<>()),
    INV_TITLE_EQUALS(Development.BlockTypes.IF_PLAYER, MenuCategory.INVENTORY, IfInventoryTitleEquals::new, Material.PAINTING, true, CodingMenu.MenuType.ALL_IN, List.of(CodingMenu.ArgumentType.TEXT), new LinkedHashMap<>()),

    // Работа с переменными - присвоение
    SET_VAR(Development.BlockTypes.ACTION_VAR, MenuCategory.VARS_ASSIGNMENT, SetVar::new, Material.IRON_INGOT, true, CodingMenu.MenuType.SET, List.of(CodingMenu.ArgumentType.DYNAMIC_VAR, CodingMenu.ArgumentType.ANY), new LinkedHashMap<>()),
    SET_ITEM_VAR(Development.BlockTypes.ACTION_VAR, MenuCategory.VARS_ASSIGNMENT, SetItemVar::new, Material.CRAFTING_TABLE, true, CodingMenu.MenuType.DEFAULT, List.of(CodingMenu.ArgumentType.DYNAMIC_VAR, CodingMenu.ArgumentType.ITEMSTACK), new LinkedHashMap<>()),

    // Работа с переменными - операции с числом
    VAR_ADDITION(Development.BlockTypes.ACTION_VAR, MenuCategory.NUMBER_OPERATIONS, VarAddition::new, Material.IRON_BLOCK, true, CodingMenu.MenuType.DUO_SET, List.of(CodingMenu.ArgumentType.DYNAMIC_VAR, CodingMenu.ArgumentType.NUMERIC, CodingMenu.ArgumentType.NUMERIC), new LinkedHashMap<>()),
    VAR_SUBTRACTION(Development.BlockTypes.ACTION_VAR, MenuCategory.NUMBER_OPERATIONS, VarSubtraction::new, Material.COPPER_BLOCK, true, CodingMenu.MenuType.DUO_SET, List.of(CodingMenu.ArgumentType.DYNAMIC_VAR, CodingMenu.ArgumentType.NUMERIC, CodingMenu.ArgumentType.NUMERIC), new LinkedHashMap<>()),
    VAR_MULTIPLY(Development.BlockTypes.ACTION_VAR, MenuCategory.NUMBER_OPERATIONS, VarMultiply::new, Material.REINFORCED_DEEPSLATE, true, CodingMenu.MenuType.DUO_SET, List.of(CodingMenu.ArgumentType.DYNAMIC_VAR, CodingMenu.ArgumentType.NUMERIC, CodingMenu.ArgumentType.NUMERIC), new LinkedHashMap<>()),
    VAR_DIVIDE(Development.BlockTypes.ACTION_VAR, MenuCategory.NUMBER_OPERATIONS, VarDivide::new, Material.DEEPSLATE, true, CodingMenu.MenuType.DUO_SET, List.of(CodingMenu.ArgumentType.DYNAMIC_VAR, CodingMenu.ArgumentType.NUMERIC, CodingMenu.ArgumentType.NUMERIC), new LinkedHashMap<>()),
    VAR_INCREMENT(Development.BlockTypes.ACTION_VAR, MenuCategory.NUMBER_OPERATIONS, VarIncrement::new, Material.IRON_INGOT, true, CodingMenu.MenuType.DEFAULT, List.of(CodingMenu.ArgumentType.DYNAMIC_VAR, CodingMenu.ArgumentType.NUMERIC), new LinkedHashMap<>()),
    VAR_DECREMENT(Development.BlockTypes.ACTION_VAR, MenuCategory.NUMBER_OPERATIONS, VarDecrement::new, Material.COPPER_INGOT, true, CodingMenu.MenuType.DEFAULT, List.of(CodingMenu.ArgumentType.DYNAMIC_VAR, CodingMenu.ArgumentType.NUMERIC), new LinkedHashMap<>()),

    // Работа с переменными - операции с текстом
    COMBINE_TEXT(Development.BlockTypes.ACTION_VAR, MenuCategory.TEXT_OPERATIONS, CombineText::new, Material.BOOK, true, CodingMenu.MenuType.SET, List.of(CodingMenu.ArgumentType.DYNAMIC_VAR, CodingMenu.ArgumentType.TEXT), new LinkedHashMap<>(Map.of(22,
            new SwitchItem(List.of("together", "space", "newline"), "menus.switches.actions.sm", List.of(Material.SLIME_BALL, Material.RABBIT_FOOT, Material.SHEARS))))),
    PARSE_NUMBER(Development.BlockTypes.ACTION_VAR, MenuCategory.TEXT_OPERATIONS, ParseNumber::new, Material.ANVIL, true, CodingMenu.MenuType.DEFAULT, List.of(CodingMenu.ArgumentType.DYNAMIC_VAR, CodingMenu.ArgumentType.TEXT), new LinkedHashMap<>()),
    GET_TEXT_LEN(Development.BlockTypes.ACTION_VAR, MenuCategory.TEXT_OPERATIONS, GetTextLength::new, Material.BOOK, true, CodingMenu.MenuType.DEFAULT, List.of(CodingMenu.ArgumentType.DYNAMIC_VAR, CodingMenu.ArgumentType.TEXT), new LinkedHashMap<>()),

    // Работа с переменными - операции с местоположениями
    GET_LOCS_DISTANCE(Development.BlockTypes.ACTION_VAR, MenuCategory.LOCATION_OPERATIONS, GetLocationsDistance::new, Material.REPEATER, true, CodingMenu.MenuType.DEFAULT, List.of(CodingMenu.ArgumentType.DYNAMIC_VAR, CodingMenu.ArgumentType.LOCATION, CodingMenu.ArgumentType.LOCATION), new LinkedHashMap<>()),
    BLOCK_FROM_LOC(Development.BlockTypes.ACTION_VAR, MenuCategory.LOCATION_OPERATIONS, BlockFromLoc::new, Material.GRASS_BLOCK, true, CodingMenu.MenuType.DEFAULT, List.of(CodingMenu.ArgumentType.DYNAMIC_VAR, CodingMenu.ArgumentType.LOCATION), new LinkedHashMap<>()),
    VALUE_FROM_LOC(Development.BlockTypes.ACTION_VAR, MenuCategory.LOCATION_OPERATIONS, ValueFromLoc::new, Material.END_ROD, true, CodingMenu.MenuType.DEFAULT, List.of(CodingMenu.ArgumentType.DYNAMIC_VAR, CodingMenu.ArgumentType.LOCATION, CodingMenu.ArgumentType.NULL), new LinkedHashMap<>(Map.of(16,
            new SwitchItem(List.of("x", "y", "z", "yaw", "pitch"), "menus.switches.actions.value_from_loc", List.of(Material.PINK_DYE, Material.LIGHT_BLUE_DYE, Material.PURPLE_DYE, Material.ENDER_PEARL, Material.ENDER_EYE))))),

    // Работа с переменными - операции с векторами
    CREATE_VECTOR(Development.BlockTypes.ACTION_VAR, MenuCategory.VECTOR_OPERATIONS, CreateVector::new, Material.PRISMARINE_SHARD, true, CodingMenu.MenuType.DEFAULT, List.of(CodingMenu.ArgumentType.DYNAMIC_VAR, CodingMenu.ArgumentType.NUMERIC, CodingMenu.ArgumentType.NUMERIC, CodingMenu.ArgumentType.NUMERIC), new LinkedHashMap<>()),

    // Работа с переменными - операции с предметом
    SET_ITEM_NAME(Development.BlockTypes.ACTION_VAR, MenuCategory.TEXT_OPERATIONS, SetItemName::new, Material.NAME_TAG, true, CodingMenu.MenuType.DEFAULT, List.of(CodingMenu.ArgumentType.DYNAMIC_VAR, CodingMenu.ArgumentType.TEXT), new LinkedHashMap()),


    // Если переменная - условия чисел
    COMPARE_NUM_EZ(Development.BlockTypes.IF_VARIABLE, MenuCategory.NUMBER_OPERATIONS, IfVariableCompareNumberEasy::new, Material.SANDSTONE_STAIRS, true, CodingMenu.MenuType.DEFAULT, List.of(CodingMenu.ArgumentType.NUMERIC, CodingMenu.ArgumentType.NULL, CodingMenu.ArgumentType.NUMERIC), new LinkedHashMap<>(Map.of(13,
            new SwitchItem(List.of("more", "moreorequals", "lower", "lowerorequals"), "menus.switches.actions.compare_ez", List.of(Material.COPPER_BLOCK, Material.COPPER_INGOT, Material.GOLD_BLOCK, Material.GOLD_INGOT))))),

    // Если переменная - условия текста
    IF_TEXT_CONTAINS(Development.BlockTypes.IF_VARIABLE, MenuCategory.TEXT_OPERATIONS, IfTextContains::new, Material.LECTERN, true, CodingMenu.MenuType.SET, List.of(CodingMenu.ArgumentType.TEXT, CodingMenu.ArgumentType.TEXT), new LinkedHashMap<>(Map.of(21,
            new SwitchItem(List.of("false", "true"), "menus.switches.ignorecase", List.of(Material.RED_CONCRETE_POWDER, Material.GREEN_CONCRETE_POWDER)), 23, new SwitchItem(List.of("false", "true"), "menus.switches.ignorecolors", List.of(Material.RED_CONCRETE_POWDER, Material.GREEN_CONCRETE_POWDER))))),
    IF_TEXT_STARTS_WITH(Development.BlockTypes.IF_VARIABLE, MenuCategory.TEXT_OPERATIONS, IfTextStartsWith::new, Material.BOOK, true, CodingMenu.MenuType.SET, List.of(CodingMenu.ArgumentType.TEXT, CodingMenu.ArgumentType.TEXT), new LinkedHashMap<>(Map.of(21,
            new SwitchItem(List.of("false", "true"), "menus.switches.ignorecase", List.of(Material.RED_CONCRETE_POWDER, Material.GREEN_CONCRETE_POWDER)), 23, new SwitchItem(List.of("false", "true"), "menus.switches.ignorecolors", List.of(Material.RED_CONCRETE_POWDER, Material.GREEN_CONCRETE_POWDER))))),
    IF_TEXT_ENDS_WITH(Development.BlockTypes.IF_VARIABLE, MenuCategory.TEXT_OPERATIONS, IfTextEndsWith::new, Material.WRITTEN_BOOK, true, CodingMenu.MenuType.SET, List.of(CodingMenu.ArgumentType.TEXT, CodingMenu.ArgumentType.TEXT), new LinkedHashMap<>(Map.of(21,
            new SwitchItem(List.of("false", "true"), "menus.switches.ignorecase", List.of(Material.RED_CONCRETE_POWDER, Material.GREEN_CONCRETE_POWDER)), 23, new SwitchItem(List.of("false", "true"), "menus.switches.ignorecolors", List.of(Material.RED_CONCRETE_POWDER, Material.GREEN_CONCRETE_POWDER))))),

    // Если переменная - другое
    IF_VAR_EQUALS(Development.BlockTypes.IF_VARIABLE, MenuCategory.OTHER, IfVariableEquals::new, Material.BRICK, true, CodingMenu.MenuType.SET, List.of(CodingMenu.ArgumentType.ANY, CodingMenu.ArgumentType.ANY), new LinkedHashMap<>()),
    IF_VAR_EXIST(Development.BlockTypes.IF_VARIABLE, MenuCategory.OTHER, IfVariableExist::new, Material.MAGMA_CREAM, true, CodingMenu.MenuType.DEFAULT, List.of(CodingMenu.ArgumentType.DYNAMIC_VAR), new LinkedHashMap<>()),
    IF_LOC_IN_REG(Development.BlockTypes.IF_VARIABLE, MenuCategory.OTHER, IfLocationInRegion::new, Material.HEAVY_CORE, true, CodingMenu.MenuType.DEFAULT, List.of(CodingMenu.ArgumentType.LOCATION, CodingMenu.ArgumentType.LOCATION, CodingMenu.ArgumentType.LOCATION), new LinkedHashMap<>()),
    IF_LOC_IN_BARRIER(Development.BlockTypes.IF_VARIABLE, MenuCategory.OTHER, IfLocationInBorder::new, Material.HEART_OF_THE_SEA, true, CodingMenu.MenuType.DEFAULT, List.of(CodingMenu.ArgumentType.LOCATION, CodingMenu.ArgumentType.NULL), new LinkedHashMap<>(Map.of(15,
            new SwitchItem(List.of("local", "global"), "menus.switches.actions.locinbarrier", List.of(Material.WHITE_DYE, Material.HEART_OF_THE_SEA))))),

    // Выбрать объект
    SELECT_ALL_PLAYERS(Development.BlockTypes.SELECT, MenuCategory.OTHER, SelectAllPlayers::new, Material.BEACON, false, null, null, null),
    SELECT_ALL_ENTITIES(Development.BlockTypes.SELECT, MenuCategory.OTHER, SelectAllEntities::new, Material.PIGLIN_HEAD, false, null, null, null),
    SELECT_DEFAULT_ENTITY(Development.BlockTypes.SELECT, MenuCategory.OTHER, SelectDefaultEntity::new, Material.LEVER, false, null, null, null),
    SELECT_DEFAULT_PLAYER(Development.BlockTypes.SELECT, MenuCategory.OTHER, SelectDefaultPlayer::new, Material.REDSTONE_TORCH, false, null, null, null),
    SELECT_LAST_SPAWNED_ENTITY(Development.BlockTypes.SELECT, MenuCategory.OTHER, SelectLastSpawnedEntity::new, Material.TOTEM_OF_UNDYING, false, null, null, null),
    SELECT_PLAYER_BY_COND(Development.BlockTypes.SELECT, MenuCategory.OTHER, SelectPlayerByCond::new, Material.PLAYER_HEAD, false, null, null, null),
    SEND_SELECTION(Development.BlockTypes.SELECT, MenuCategory.OTHER, SendSelection::new, Material.WRITTEN_BOOK, false, null, null, null),

    // Действия сущности
    SPAWN_MOB(Development.BlockTypes.ENTITY_ACTION, MenuCategory.OTHER, SpawnMob::new, Material.ALLAY_SPAWN_EGG, true, CodingMenu.MenuType.DUO_SET, List.of(CodingMenu.ArgumentType.ITEMSTACK, CodingMenu.ArgumentType.TEXT, CodingMenu.ArgumentType.LOCATION), new LinkedHashMap<>()),
    SWITCH_AI(Development.BlockTypes.ENTITY_ACTION, MenuCategory.PARAMS, SwitchAI::new, Material.ZOMBIE_HEAD, true, CodingMenu.MenuType.DEFAULT, List.of(), new LinkedHashMap<>(Map.of(13,
            new SwitchItem(List.of("true", "false"), "menus.switches.actions.switch_ai", List.of(Material.LIME_CONCRETE_POWDER, Material.RED_CONCRETE_POWDER)))));


    private Development.BlockTypes type;
    private MenuCategory category;
    private final Supplier<Action> constructor;
    private Material icon;
    private final boolean hasChest;
    private final CodingMenu.MenuType menuType;
    private final List<CodingMenu.ArgumentType> args;
    private final HashMap<Integer, SwitchItem> switches;

    ActionCategory (Development.BlockTypes type, MenuCategory category, Supplier<Action> constructor, Material icon, boolean hasChest, CodingMenu.MenuType menuType, List<CodingMenu.ArgumentType> args, HashMap<Integer, SwitchItem> switches) {
        this.category = category;
        this.type = type;
        this.constructor = constructor;
        this.icon = icon;
        this.hasChest = hasChest;
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
        if (hasChest) return new CodingMenu("coding.actions." + name().toLowerCase(), menuType, args, switches);
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

            icon.setItemMeta(meta);
        }

        return ItemUtil.clearItemFlags(icon);
    }

    public void setIcon(Material icon) {
        this.icon = icon;
    }
}
