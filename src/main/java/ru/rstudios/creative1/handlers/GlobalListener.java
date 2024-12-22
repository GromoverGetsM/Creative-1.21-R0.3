package ru.rstudios.creative1.handlers;

import com.jeff_media.morepersistentdatatypes.DataType;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.event.extent.EditSessionEvent;
import com.sk89q.worldedit.util.eventbus.Subscribe;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import ru.rstudios.creative1.coding.actions.ActionCategory;
import ru.rstudios.creative1.coding.starters.StarterCategory;
import ru.rstudios.creative1.coding.starters.playerevent.*;
import ru.rstudios.creative1.menu.CodingMenu;
import ru.rstudios.creative1.menu.ProtectedMenu;
import ru.rstudios.creative1.menu.selector.CodingCategoriesMenu;
import ru.rstudios.creative1.menu.selector.CodingMultipagesMenu;
import ru.rstudios.creative1.menu.selector.ValuesMenu;
import ru.rstudios.creative1.plots.LimitManager;
import ru.rstudios.creative1.plots.Plot;
import ru.rstudios.creative1.plots.PlotManager;
import ru.rstudios.creative1.user.LocaleManages;
import ru.rstudios.creative1.user.User;
import ru.rstudios.creative1.utils.ChestMenuHook;
import ru.rstudios.creative1.utils.DatabaseUtil;
import ru.rstudios.creative1.utils.Development;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ru.rstudios.creative1.Creative_1.plugin;

public class GlobalListener implements Listener {

    public static String parseColors(String message) {
        Pattern pattern = Pattern.compile("&#[a-fA-F0-9]{6}");
        Matcher matcher = pattern.matcher(message);

        while (matcher.find()) {
            String color = message.substring(matcher.start() + 1, matcher.end());
            message = message.replace("&" + color, ChatColor.of(color) + "");
            matcher = pattern.matcher(message);
        }

        return ChatColor.translateAlternateColorCodes('&', message);
    }

    @EventHandler
    public void onWorldChanged (PlayerChangedWorldEvent event) {
        User user = User.asUser(event.getPlayer());
        String from = event.getFrom().getName();
        String destination = user.player().getWorld().getName();

        if (from.endsWith("_dev") && !destination.endsWith("_dev")) {
            boolean isMovingToLinkedCraftPlot = from.replace("_dev", "_CraftPlot").equalsIgnoreCase(destination);

            boolean handlingPaper = user.datastore().containsKey("HandlingPaper");

            if (!isMovingToLinkedCraftPlot || !handlingPaper) {
                if (user.isOnPlot()) user.getCurrentPlot().handler.parseCodeBlocks();
            }
        }
    }

    @EventHandler
    public void onPlayerJoin (PlayerJoinEvent event) {
        User user = User.asUser(event.getPlayer());
        user.clear();
        user.player().teleport(Bukkit.getWorld("world").getSpawnLocation());

        if (!DatabaseUtil.isValueExist("players", "player_name", user.name())) {
            DatabaseUtil.insertValue("players", "player_name", user.name());
            DatabaseUtil.updateValue("players", "plot_limit", 3, "player_name", user.name());

            String locale = event.getPlayer().getLocale().equalsIgnoreCase("ru_ru") ? "ru_RU" : "en_US";

            DatabaseUtil.updateValue("players", "player_locale", locale, "player_name", user.name());
        }
    }

    @EventHandler
    public void onPlayerLeft (PlayerQuitEvent event) {
        User user = User.asUser(event.getPlayer());

        if (user.isOnPlot()) {
            Plot p = user.getCurrentPlot();
            p.onPlayerLeft();
            user.player().getOpenInventory().close();
            user.destroy();
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        User user = User.asUser(event.getPlayer());
        Plot plot = user.getCurrentPlot();

        if (plot != null && plot.isUserInDev(user)) {
            long lastChecked = user.datastore().containsKey("lastSignTranslate") ? (long) user.datastore().get("lastSignTranslate") : 0;
            long current = System.currentTimeMillis();

            if (current - lastChecked > 1000) {
                Player player = user.player();
                Location eyeLocation = player.getEyeLocation();
                Vector direction = eyeLocation.getDirection();

                double viewAngle = Math.toRadians(60);
                int viewDistance = 10;

                int targetY = -59;

                World world = player.getWorld();

                Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                    List<Block> visibleSigns = new ArrayList<>();

                    int minX = eyeLocation.getBlockX() - viewDistance;
                    int maxX = eyeLocation.getBlockX() + viewDistance;
                    int minZ = eyeLocation.getBlockZ() - viewDistance;
                    int maxZ = eyeLocation.getBlockZ() + viewDistance;

                    for (int x = minX; x <= maxX; x++) {
                        for (int z = minZ; z <= maxZ; z++) {
                            Block block = world.getBlockAt(x, targetY, z);
                            if (block.getType() == Material.OAK_WALL_SIGN) {
                                Vector toBlock = block.getLocation().toVector().subtract(eyeLocation.toVector());
                                if (direction.angle(toBlock) <= viewAngle) {
                                    visibleSigns.add(block);
                                }
                            }
                        }
                    }

                    Bukkit.getScheduler().runTask(plugin, () -> {
                        for (Block signBlock : visibleSigns) {
                            user.sendTranslatedSign(signBlock.getLocation());
                        }
                    });
                });
                user.datastore().put("lastSignTranslate", System.currentTimeMillis());
            }
        }
    }



    @EventHandler
    public void onPlayerChatted (AsyncChatEvent event) {
        User user = User.asUser(event.getPlayer());
        String message = LegacyComponentSerializer.legacySection().serialize(event.message());

        if (user.datastore().containsKey("inputtingPlotName")) {
            event.setCancelled(true);
            String plotName = String.valueOf(user.datastore().get("inputtingPlotName"));
            Plot plot = PlotManager.plots.get(plotName);

            if (plot != null && plot.owner().equalsIgnoreCase(user.name())) {
                if (message.length() <= 40) {
                    plot.setIconName(message);
                    user.sendMessage("info.plot-displayname-set", true, message);
                } else {
                    user.sendMessage("errors.plot-displayname-too-long", true, String.valueOf(message.length()));
                }
            }

            user.datastore().remove("inputtingPlotName");
        }

        if (user.datastore().containsKey("inputtingCustomId")) {
            event.setCancelled(true);
            String plotName = String.valueOf(user.datastore().get("inputtingCustomId"));
            Plot plot = PlotManager.plots.get(plotName);

            if (plot != null && plot.owner().equalsIgnoreCase(user.name())) {
                String rawId = ChatColor.stripColor(message);

                if (rawId.length() < 17) {
                    if (rawId.matches(".*[a-zA-Z].*")) {
                        if (!DatabaseUtil.isValueExist("plots", "custom_id", rawId)) {
                            Bukkit.getScheduler().runTask(plugin, () -> plot.setCustomId(rawId));
                            user.sendMessage("info.plot-customid-set", true, message);
                        } else {
                            user.sendMessage("errors.customid-already-taken", true, String.valueOf(DatabaseUtil.getValue("plots", "owner_name", "custom_id", rawId)));
                        }
                    } else {
                        user.sendMessage("errors.plot-customid-no-letter", true, rawId);
                    }
                } else {
                    user.sendMessage("errors.plot-customid-too-long", true, String.valueOf(rawId.length()));
                }
            }

            user.datastore().remove("inputtingCustomId");
        }


        if (user.datastore().containsKey("inputtingLore")) {
            event.setCancelled(true);
            String plotName = String.valueOf(user.datastore().get("inputtingLore"));
            Plot plot = PlotManager.plots.get(plotName);

            message = parseColors(message);

            if (plot != null && plot.owner().equalsIgnoreCase(user.name())) {
                List<String> lines = new ArrayList<>(Arrays.asList(message.split("\\\\n")));

                plot.setIconLore(lines);
                user.sendMessage("info.plot-lore-set", true, "");
            }

            user.datastore().remove("inputtingLore");
        }

        if (user.isOnPlayingWorld()) {
            if (!event.isCancelled()) {
                user.getCurrentPlot().handler.sendStarter(new PlayerChatted.Event(user.player(), user.getCurrentPlot(), event), StarterCategory.PLAYER_CHATTED);
            }
        }

        if (user.isInDev()) {
            if (message.length() > 256) message = message.substring(0, 256);
            if (message.contains("&")) message = message.replace("&", "§");

            ItemStack activeItem = user.player().getInventory().getItemInMainHand();
            ItemMeta meta = activeItem.getItemMeta();

            if (activeItem.getType() != Material.AIR) {

                switch (activeItem.getType()) {
                    case BOOK, MAGMA_CREAM -> {
                        event.setCancelled(true);
                        user.player().playSound(user.player().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
                        user.sendTitle("coding.tech.var-set", message, 10, 70, 20, true, false);

                        meta = activeItem.getItemMeta();
                        if (meta != null) {
                            if (activeItem.getType() == Material.MAGMA_CREAM) {
                                meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "variable"), PersistentDataType.BOOLEAN, true);
                            }
                            meta.setDisplayName(message);
                        }
                    }
                    case SLIME_BALL -> {
                        event.setCancelled(true);

                        try {
                            double d = Double.parseDouble(message);

                            String displayValue;
                            if (d == (long) d) {
                                displayValue = String.valueOf((long) d);
                            } else {
                                displayValue = String.valueOf(d);
                            }

                            user.player().playSound(user.player().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
                            user.sendTitle("coding.tech.var-set", displayValue, 10, 70, 20, true, false);

                            meta = activeItem.getItemMeta();
                            if (meta != null) {
                                meta.setDisplayName(displayValue);
                                activeItem.setItemMeta(meta);
                            }
                        } catch (NumberFormatException e) {
                            user.player().playSound(user.player().getLocation(), Sound.BLOCK_ANVIL_PLACE, 1.0F, 1.0F);
                            user.sendTitle("coding.tech.incorrect-value", "§6" + message, 10, 70, 20, true, false);
                        }
                    }

                }

                activeItem.setItemMeta(meta);
                user.player().getInventory().setItemInMainHand(activeItem);
            }
        }

        if (event.isCancelled()) return;
        event.setCancelled(true);

        String formatted = parseColors("&f<%player%> &r" + message).replace("%player%", user.player().getName());

        if (user.isOnPlot()) {
            if (formatted.startsWith("!")) {
                Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(formatted.replaceFirst("!", "")));
            } else {
                user.getCurrentPlot().online().forEach(player -> player.sendMessage(formatted));
            }
        } else {
            user.player().getWorld().getPlayers().forEach(player -> player.sendMessage(formatted));
        }
    }

    @EventHandler
    public void onPlayerInteract (PlayerInteractEvent event) {

        User user = User.asUser(event.getPlayer());
        Plot p = user.getCurrentPlot();

        if (p != null) {
            if (p.isUserInDev(user)) {
                if ((event.getAction() == Action.RIGHT_CLICK_AIR || (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getPlayer().getTargetBlockExact(5) != null && event.getPlayer().getTargetBlockExact(5).getType() != Material.CHEST)) && event.getItem() != null && event.getItem().getType() == Material.APPLE) {
                    new ValuesMenu(user).open(user);
                }
                if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock() != null) {
                    if (event.getClickedBlock().getType() == Material.OAK_WALL_SIGN) {
                        event.setCancelled(true);

                        Development.BlockTypes type = Development.BlockTypes.getByMainBlock(event.getClickedBlock().getRelative(BlockFace.SOUTH));
                        if (type != null && type.hasConstructor()) {
                            ProtectedMenu menu = type.createMenuInstance(user);
                            menu.open(user);
                            if (menu instanceof CodingCategoriesMenu ccmenu) ccmenu.setSign(event.getClickedBlock());
                            else if (menu instanceof CodingMultipagesMenu cmmenu) cmmenu.setSign(event.getClickedBlock());
                        } else if ((type == Development.BlockTypes.FUNCTION || type == Development.BlockTypes.CYCLE) && event.getItem() != null && event.getItem().getType() == Material.BOOK) {
                            ItemStack book = event.getItem();
                            if (book.getItemMeta() == null || !book.getItemMeta().hasDisplayName()) return;

                            Sign sign = (Sign) event.getClickedBlock().getState();
                            sign.setLine(2, book.getItemMeta().getDisplayName());
                            sign.update();
                        } else if (type == Development.BlockTypes.CYCLE && event.getItem() != null && event.getItem().getType() == Material.SLIME_BALL) {
                            ItemStack slime = event.getItem();
                            if (slime.getItemMeta() == null || !slime.getItemMeta().hasDisplayName()) return;

                            Sign sign = (Sign) event.getClickedBlock().getState();
                            sign.setLine(3, slime.getItemMeta().getDisplayName());
                            sign.update();
                        }
                    } else if (event.getClickedBlock().getType() == Material.CHEST) {
                        event.setCancelled(true);

                        NamespacedKey inventory = new NamespacedKey(plugin, "inventory");

                        Chest chest = (Chest) event.getClickedBlock().getState();
                        if (chest.getPersistentDataContainer().has(new NamespacedKey(plugin, "openedName"))) {
                            user.sendMessage("errors.chest-already-taken", true, "");
                            return;
                        }

                        ItemStack[] contents = chest.getPersistentDataContainer().get(inventory, DataType.ITEM_STACK_ARRAY);
                        Sign sign = (Sign) event.getClickedBlock().getRelative(BlockFace.DOWN).getRelative(BlockFace.NORTH).getState();
                        ActionCategory category;

                        if (event.getClickedBlock().getRelative(BlockFace.DOWN).getType() != Material.PURPUR_BLOCK) {
                            category = ActionCategory.byName(sign.getLine(2).replace("coding.actions.", ""));
                        } else {
                            category = ActionCategory.byName(sign.getLine((sign.getLine(3).isEmpty() ? 2 : 3)).replace("coding.actions.", ""));
                        }

                        if (category.hasChest()) {
                            CodingMenu codingMenu = category.getCodingMenu();

                            codingMenu.build(user);
                            Inventory inv = codingMenu.getInventory(user);

                            inv.setContents(contents);

                            codingMenu.open(user);
                            user.datastore().put("chestBlockActive", event.getClickedBlock());
                        }
                    }
                }
                if (event.getAction().isRightClick() && event.getItem() != null && event.getItem().getType() == Material.MAGMA_CREAM) {
                    if (user.player().isSneaking()) {
                        ItemStack eventItem = event.getItem();
                        ItemMeta meta = eventItem.getItemMeta();
                        List<String> lore = meta.getLore();
                        String isSaved = lore.get(0);
                        if (isSaved.equalsIgnoreCase(LocaleManages.getLocaleMessage(user.getLocale(), "coding.tech.dynvarsaved", false, ""))) {
                            lore.remove(0);
                        } else {
                            lore.add(0, LocaleManages.getLocaleMessage(user.getLocale(), "coding.tech.dynvarsaved", false, ""));
                        }
                        meta.setLore(lore);
                        eventItem.setItemMeta(meta);
                    }
                }
                if (event.getAction() == Action.LEFT_CLICK_AIR && event.getItem() != null && event.getItem().getType() == Material.PAPER) {
                    user.datastore().put("HandlingPaper", true);
                    user.player().teleport(user.getCurrentPlot().world().getSpawnLocation());
                }
            } else if (user.isOnPlayingWorld()) {
                ItemStack item = event.getItem();

                if (user.datastore().containsKey("HandlingPaper") && item != null && item.getType() == Material.PAPER) {

                    switch (event.getAction()) {
                        case LEFT_CLICK_AIR -> {
                            user.datastore().remove("HandlingPaper");
                            user.player().teleport(user.getCurrentPlot().dev().world().getSpawnLocation());
                        }

                        case RIGHT_CLICK_BLOCK, RIGHT_CLICK_AIR -> {
                            Block b = event.getClickedBlock();
                            Location loc = b == null ? user.player().getLocation() : b.getLocation();
                            ItemMeta meta = item.getItemMeta();

                            String value = loc.getX() + " " +
                                    loc.getY() + " " +
                                    loc.getZ() + " " +
                                    loc.getYaw() + " " +
                                    loc.getPitch();
                            meta.setDisplayName(value);
                            item.setItemMeta(meta);
                            user.sendTitle("coding.tech.var-set", value, 10, 70, 20, true, false);
                        }

                    }
                } else {
                    if (event.getHand() == EquipmentSlot.HAND) {
                        switch (event.getAction()) {
                            case LEFT_CLICK_AIR, LEFT_CLICK_BLOCK -> user.getCurrentPlot().handler.sendStarter(new PlayerLeftClicked.Event(user.player(), user.getCurrentPlot(), event), StarterCategory.PLAYER_LEFT_CLICK);
                            case RIGHT_CLICK_BLOCK, RIGHT_CLICK_AIR -> user.getCurrentPlot().handler.sendStarter(new PlayerRightClicked.Event(user.player(), user.getCurrentPlot(), event), StarterCategory.PLAYER_RIGHT_CLICK);
                            case PHYSICAL -> user.getCurrentPlot().handler.sendStarter(new PlayerPhysicalInteract.Event(user.player(), user.getCurrentPlot(), event), StarterCategory.PLAYER_PHYSICAL_INTERACT);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClick (InventoryClickEvent event) {
        User user = User.asUser(event.getWhoClicked());

        if (event.getClickedInventory() != null && event.getClickedInventory().getHolder() instanceof CodingMenu menu) {
            if (!menu.getArgumentSlots().contains(event.getSlot())) {
                event.setCancelled(true);
            }
            if (menu.getSwitches().containsKey(event.getSlot())) {
                menu.getSwitches().get(event.getSlot()).onClick(user, event);
            }
        } else if (!(event.getClickedInventory() instanceof ProtectedMenu)) {
            if (user.isOnPlayingWorld()) {
                user.getCurrentPlot().handler.sendStarter(new PlayerClickedInventory.Event((Player) event.getWhoClicked(), user.getCurrentPlot(), event), StarterCategory.PLAYER_INV_CLICK);
            }
        }
    }

    @EventHandler
    public void onInventoryClose (InventoryCloseEvent event) {
        User user = User.asUser(event.getPlayer());
        Inventory inv = event.getInventory();

        if (inv.getHolder() instanceof CodingMenu) {
            ItemStack[] contents = inv.getContents();

            if (user.datastore().containsKey("chestBlockActive")) {
                Block block = (Block) user.datastore().get("chestBlockActive");

                if (block.getState() instanceof Chest ch) {
                    NamespacedKey inventoryKey = new NamespacedKey(plugin, "inventory");
                    ch.getPersistentDataContainer().remove(new NamespacedKey(plugin, "openedName"));

                    PersistentDataContainer container = ch.getPersistentDataContainer();
                    if (container.has(inventoryKey, DataType.ITEM_STACK_ARRAY)) container.remove(inventoryKey);

                    container.set(inventoryKey, DataType.ITEM_STACK_ARRAY, contents);
                    ch.update();
                }
            }

            user.datastore().remove("chestBlockActive");
        }
    }

    @EventHandler
    public void onBlockPlace (BlockPlaceEvent event) {
        User user = User.asUser(event.getPlayer());
        Plot p = user.getCurrentPlot();

        if (p != null && p.isUserInDev(user)) {
            Development.setCodingBlock(event);
        }

        if (user.isOnPlayingWorld()) {
            user.getCurrentPlot().handler.sendStarter(new PlayerBlockPlace.Event(user.player(), user.getCurrentPlot(), event), StarterCategory.PLAYER_BLOCK_PLACE);
        }
    }

    @EventHandler
    public void onBlockBroken (BlockBreakEvent event) {
        User user = User.asUser(event.getPlayer());
        Plot p = user.getCurrentPlot();

        if (p != null && p.isUserInDev(user)) {
            Development.breakCodingBlock(event);
        }

        if (user.isOnPlayingWorld()) {
            user.getCurrentPlot().handler.sendStarter(new PlayerBlockBreak.Event(user.player(), user.getCurrentPlot(), event), StarterCategory.PLAYER_BREAK_BLOCK);
        }
    }

    @EventHandler
    public void onBlockDamaged (BlockDamageEvent event) {
        User user = User.asUser(event.getPlayer());

        if (user.isOnPlayingWorld()) {
            user.getCurrentPlot().handler.sendStarter(new PlayerBlockDamaged.Event(user.player(), user.getCurrentPlot(), event), StarterCategory.PLAYER_DAMAGED_BLOCK);
        }
    }

    @EventHandler
    public void onDamageAborted (BlockDamageAbortEvent event) {
        User user = User.asUser(event.getPlayer());

        if (user.isOnPlayingWorld()) {
            user.getCurrentPlot().handler.sendStarter(new PlayerDamageAborted.Event(user.player(), user.getCurrentPlot(), event), StarterCategory.PLAYER_DAMAGE_ABORTED);
        }
    }

    @EventHandler
    public void onPlayerRespawn (PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        World world = player.getLastDeathLocation().getWorld();

        event.setRespawnLocation(world.getSpawnLocation());
    }

    @EventHandler
    public void onInventoryOpen (InventoryOpenEvent event) {
        if (event.getInventory().getHolder() instanceof CodingMenu) {
            Player player = (Player) event.getPlayer();
            Block target = player.getTargetBlockExact(5);

            if (target != null && target.getType() == Material.CHEST) {
                Chest chest = (Chest) target.getState();

                chest.getPersistentDataContainer().set(new NamespacedKey(plugin, "openedName"), PersistentDataType.STRING, player.getName());
                chest.update();
            }
        }
    }

    public static void onEditSessionEvent (EditSessionEvent event) {
        event.setExtent(new ChestMenuHook(event.getExtent(), event.getWorld()));
    }

    @EventHandler
    public void onBlockRedstone(BlockRedstoneEvent event) {
        Location location = event.getBlock().getLocation();

        Plot plot = PlotManager.byWorld(event.getBlock().getWorld());
        if (plot != null) {
            plot.setLastRedstoneOperationsAmount(plot.getLastRedstoneOperationsAmount()+1);
            if (plot.getLastRedstoneOperationsAmount() > LimitManager.getLimitValue(plot, "redstone_operations")) {
                plot.throwException("redstone-limit", String.valueOf(LimitManager.getLimitValue(plot, "redstone_operations")));
                if (location.getBlock().getType() == Material.OBSERVER) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            location.getBlock().setType(Material.AIR);
                        }
                    }.runTaskLater(plugin,1L);
                } else {
                    location.getBlock().setType(Material.CAVE_AIR);
                }
                plot.setLastRedstoneOperationsAmount(0);
            }
            if (plot.getLastRedstoneOperationsAmount() > 0) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        plot.setLastRedstoneOperationsAmount(plot.getLastRedstoneOperationsAmount()-1);
                    }
                }.runTaskLater(plugin,5L);
            }
        }

    }

    @EventHandler
    public void onEntitySpawn (EntitySpawnEvent event) {
        Entity entity = event.getEntity();
        World world = entity.getWorld();
        Plot possible = PlotManager.byWorld(world);

        if (possible != null) {
            if (world.getEntities().stream().filter(e -> !(e instanceof Player)).toList().size() < LimitManager.getLimitValue(possible, "entities")) {
                event.setCancelled(true);
                possible.throwException("entities", String.valueOf(world.getEntities().stream().filter(e -> !(e instanceof Player)).toList().size()), String.valueOf(LimitManager.getLimitValue(possible, "entities")));
            }
        }
    }



}
