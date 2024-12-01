package ru.rstudios.creative1.handlers;

import com.jeff_media.morepersistentdatatypes.DataType;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.util.Vector;
import ru.rstudios.creative1.coding.actions.ActionCategory;
import ru.rstudios.creative1.coding.starters.StarterCategory;
import ru.rstudios.creative1.coding.starters.playerevent.PlayerJoin;
import ru.rstudios.creative1.menu.CodingMenu;
import ru.rstudios.creative1.menu.selector.CodingCategoriesMenu;
import ru.rstudios.creative1.plots.Plot;
import ru.rstudios.creative1.plots.PlotManager;
import ru.rstudios.creative1.user.User;
import ru.rstudios.creative1.utils.DatabaseUtil;
import ru.rstudios.creative1.utils.Development;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ru.rstudios.creative1.Creative_1.plugin;

public class GlobalListener implements Listener {

    @EventHandler
    public void onWorldChanged (PlayerChangedWorldEvent event) {
        User user = User.asUser(event.getPlayer());
        String from = event.getFrom().getName();
        String destination = user.player().getWorld().getName();

        if (from.endsWith("_dev") && !destination.endsWith("_dev")) {
            boolean isMovingToLinkedCraftPlot = from.replace("_dev", "_CraftPlot").equalsIgnoreCase(destination);

            boolean handlingPaper = user.datastore().containsKey("HandlingPaper");

            if (!isMovingToLinkedCraftPlot || !handlingPaper) {
                user.getCurrentPlot().handler.parseCodeBlocks();
            }
        }

        if (user.isOnPlot() && user.isOnPlayingWorld() && !user.datastore().containsKey("HandlingPaper")) {
            user.getCurrentPlot().handler.sendStarter(new PlayerJoin.Event(user.player(), user.getCurrentPlot(), event), StarterCategory.PLAYER_JOIN);
        }
    }

    @EventHandler
    public void onPlayerJoin (PlayerJoinEvent event) {
        User user = User.asUser(event.getPlayer());

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
                String rawMessage = ChatColor.stripColor(plotName);

                if (rawMessage.length() <= 40) {
                    message = message.replace("\\n", "\n");
                    plot.setIconName(message);
                    user.sendMessage("info.plot-displayname-set", true, message);
                } else {
                    user.sendMessage("errors.plot-displayname-too-long", true, String.valueOf(rawMessage.length()));
                }
            }

            user.datastore().remove("inputtingPlotName");
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
    }

    @EventHandler
    public void onPlayerInteract (PlayerInteractEvent event) {

        User user = User.asUser(event.getPlayer());
        Plot p = user.getCurrentPlot();

        if (p.isUserInDev(user)) {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock() != null) {
                if (event.getClickedBlock().getType() == Material.OAK_WALL_SIGN) {
                    event.setCancelled(true);

                    Development.BlockTypes type = Development.BlockTypes.getByMainBlock(event.getClickedBlock().getRelative(BlockFace.SOUTH));
                    if (type != null && type.hasConstructor()) {
                        CodingCategoriesMenu menu = type.createMenuInstance(user);
                        menu.open(user);
                        menu.setSign(event.getClickedBlock());
                    }
                } else if (event.getClickedBlock().getType() == Material.CHEST) {
                    event.setCancelled(true);

                    NamespacedKey inventory = new NamespacedKey(plugin, "inventory");

                    Chest chest = (Chest) event.getClickedBlock().getState();
                    ItemStack[] contents = chest.getPersistentDataContainer().get(inventory, DataType.ITEM_STACK_ARRAY);
                    Sign sign = (Sign) event.getClickedBlock().getRelative(BlockFace.DOWN).getRelative(BlockFace.NORTH).getState();

                    ActionCategory category = ActionCategory.byName(sign.getLine(2).replace("coding.actions.", "").replace(".name", ""));
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
        }
    }

    @EventHandler
    public void onInventoryClick (InventoryClickEvent event) {
        User user = User.asUser(event.getWhoClicked());

        if (event.getClickedInventory() != null && event.getClickedInventory().getHolder() instanceof CodingMenu menu) {
            if (!menu.getArgumentSlots().contains(event.getSlot())) {
                event.setCancelled(true);
                if (menu.getSwitches().containsKey(event.getSlot())) {
                    menu.getSwitches().get(event.getSlot()).onClick(user, event);
                }
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
                Chest ch = (Chest) block.getState();
                NamespacedKey inventoryKey = new NamespacedKey(plugin, "inventory");

                PersistentDataContainer container = ch.getPersistentDataContainer();
                if (container.has(inventoryKey, DataType.ITEM_STACK_ARRAY)) container.remove(inventoryKey);

                container.set(inventoryKey, DataType.ITEM_STACK_ARRAY, contents);
                ch.update();

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
    }

    @EventHandler
    public void onBlockBroken (BlockBreakEvent event) {
        User user = User.asUser(event.getPlayer());
        Plot p = user.getCurrentPlot();

        if (p != null && p.isUserInDev(user)) {
            Development.breakCodingBlock(event);
        }
    }

}
