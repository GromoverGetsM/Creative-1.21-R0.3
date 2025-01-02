package ru.rstudios.creative1.utils;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.World;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.*;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionEffectTypeCategory;
import org.jetbrains.annotations.Nullable;
import ru.rstudios.creative1.menu.ProtectedMenu;
import ru.rstudios.creative1.menu.selector.*;
import ru.rstudios.creative1.plots.DevPlot;
import ru.rstudios.creative1.plots.Plot;
import ru.rstudios.creative1.plots.PlotManager;
import ru.rstudios.creative1.user.LocaleManages;
import ru.rstudios.creative1.user.User;

import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Function;

import static ru.rstudios.creative1.Creative_1.plugin;

public class Development {

    public enum BlockTypes {

        PLAYER_EVENT(Material.DIAMOND_BLOCK, Material.DIAMOND_ORE, PlayerEvent::new,true, false),
        FUNCTION(Material.LAPIS_BLOCK, Material.LAPIS_ORE, null, true, false),
        CYCLE(Material.EMERALD_BLOCK, Material.EMERALD_ORE, null, true, false),
        BLOCK_EVENT(Material.PRISMARINE_BRICKS, Material.PRISMARINE, BlockEvent::new, true, false),
        PLAYER_ACTION(Material.COBBLESTONE, Material.STONE, PlayerAction::new,false, false),
        ENTITY_ACTION(Material.MUD_BRICKS, Material.PACKED_MUD, EntityAction::new,false, false),
        ACTION_VAR(Material.IRON_BLOCK, Material.IRON_ORE, ActionVar::new,false, false),
        WORLD_ACTION(Material.RED_NETHER_BRICKS, Material.NETHERRACK, WorldAction::new,false, false),
        IF_PLAYER(Material.OAK_PLANKS, Material.PISTON, IfPlayer::new,false, true),
        IF_VARIABLE(Material.OBSIDIAN, Material.PISTON, IfVariable::new,false, true),
        SELECT(Material.PURPUR_BLOCK, Material.PURPUR_PILLAR, SelectMenu::new, false, false);


        private Material mainBlock;
        private Material additionalBlock;
        private final Function<User, ProtectedMenu> constructor;
        private boolean isEvent;
        private boolean isCondition;

        BlockTypes (Material mainBlock, Material additionalBlock, Function<User, ProtectedMenu> constructor, boolean isEvent, boolean isCondition) {
            this.mainBlock = mainBlock;
            this.additionalBlock = additionalBlock;
            this.constructor = constructor;
            this.isEvent = isEvent;
            this.isCondition = isCondition;
        }

        public Material getAdditionalBlock() {
            return additionalBlock;
        }

        public Material getMainBlock() {
            return mainBlock;
        }

        public boolean isEvent() {
            return isEvent;
        }

        public boolean isCondition() {
            return isCondition;
        }

        public static @Nullable BlockTypes getByMainBlock (Material mainBlock) {
            return Arrays.stream(values()).filter(bt -> bt.mainBlock == mainBlock).findFirst().orElse(null);
        }

        public static @Nullable BlockTypes getByMainBlock (Block mainBlock) {
            return getByMainBlock(mainBlock.getType());
        }

        public boolean hasConstructor() {
            return constructor != null;
        }

        public ProtectedMenu createMenuInstance(User user) {
            return constructor.apply(user);
        }

        public ItemStack getIcon (User user) {
            ItemStack item = new ItemStack(getMainBlock());
            ItemMeta meta = item.getItemMeta();

            meta.setDisplayName(LocaleManages.getLocaleMessage(user.getLocale(), "coding." + name().toLowerCase(Locale.ROOT), false, ""));
            item.setItemMeta(meta);

            return item;
        }
    }

    public static void setCodingBlock (BlockPlaceEvent event) {
        Block block = event.getBlock();
        BlockTypes type = BlockTypes.getByMainBlock(block);

        if (type != null && PlotManager.isDevWorld(block.getWorld())) {
            if ((type.isEvent && block.getRelative(BlockFace.DOWN).getType() != Material.LIGHT_BLUE_STAINED_GLASS) || (!type.isEvent && block.getRelative(BlockFace.DOWN).getType() != Material.LIGHT_GRAY_STAINED_GLASS)) {
                event.setCancelled(true);
                return;
            }
            Block additional = block.getRelative(BlockFace.WEST);
            Block lastBlock = getLastBlockInString(additional);

            if (lastBlock != null) {
                Location last = lastBlock.getLocation();
                last.add(0, 1, -1);

                int distance = type.isCondition ? 4 : 2;

                if (!type.isEvent) {
                    if (getLastBlockInString(block) != additional) {
                        event.setCancelled(!moveBlocks(additional.getLocation(), last, BlockFace.WEST, distance));
                        if (event.isCancelled()) return;
                    }
                }
            }

            block.getLocation().getBlock().setType(block.getType());
            additional.setType(type.additionalBlock);

            if (type.additionalBlock == Material.PISTON) {
                setPistonDirection(additional, BlockFace.WEST);

                Block reversed = additional.getRelative(BlockFace.WEST, 2);
                reversed.setType(Material.PISTON);
                setPistonDirection(reversed, BlockFace.EAST);
            }

            Block signBlock = block.getRelative(BlockFace.NORTH);
            signBlock.setType(Material.OAK_WALL_SIGN);

            if (signBlock.getState() instanceof Sign sign) {
                sign.setLine(1, "coding." + type.name().toLowerCase(Locale.ROOT));

                if (type == BlockTypes.CYCLE) {
                    sign.setLine(3, "20");
                }

                sign.update();
            }

        } else {
            event.setCancelled(true);
        }
    }

    public static void breakCodingBlock (BlockBreakEvent event) {
        event.setCancelled(true);

        Block block = event.getBlock();
        if (block.getType() == Material.OAK_WALL_SIGN) block = block.getRelative(BlockFace.SOUTH);

        BlockTypes type = BlockTypes.getByMainBlock(block);

        if (type != null && PlotManager.isDevWorld(block.getWorld())) {
            if (type.isEvent) {
                if (event.getPlayer().isSneaking()) breakAsDefault(block, false);
                else {
                    Block last = getLastBlockInString(block);

                    if (last != null) {
                        Location loc = last.getLocation();
                        loc.add(0, 1, -1);

                        setBlocks(block.getWorld(), block.getLocation(), loc);
                    }
                }

                return;
            }

            if (type.isCondition) {
                Block closingBracket = getClosingPiston(block);

                if (closingBracket != null) {
                    Location loc = closingBracket.getLocation();
                    loc.add(0, 1, -1);

                    setBlocks(block.getWorld(), block.getLocation(), loc);

                    Block lastInString = getLastBlockInString(closingBracket);
                    if (lastInString != null) {
                        int distance = (int) block.getLocation().distance(loc);

                        moveBlocks(closingBracket.getRelative(BlockFace.WEST).getLocation(), lastInString.getLocation().add(0, 1, -1), BlockFace.EAST, distance + 1);
                    }
                }
            } else {
                breakAsDefault(block, true);
            }


        }
    }

    private static void breakAsDefault (Block block, boolean needToMoveBack) {
        setBlocks(block.getWorld(), block.getLocation(), block.getLocation().add(-1, 1, -1));

        if (needToMoveBack) {
            Block lastInString = getLastBlockInString(block.getRelative(BlockFace.WEST, 2));
            if (lastInString != null) {
                Location loc = lastInString.getLocation();
                loc.add(0, 1, -1);

                moveBlocks(block.getRelative(BlockFace.WEST, 2).getLocation(), loc, BlockFace.EAST, 2);
            }
        }
    }

    public static Block getLastBlockInString(Block block) {
        Location main = new Location(block.getWorld(), -64, block.getY(), block.getZ());

        for (int x = -64; x <= block.getX(); x++) {
            main.setX(x);

            if (main.getBlock().getType() != Material.AIR) {
                return main.getBlock();
            }
        }

        return null;
    }

    public static Block getClosingPiston (Block main) {
        if (main.getType() != Material.PISTON) main = main.getRelative(BlockFace.WEST);
        int openingBrackets = 1;

        org.bukkit.World dev = main.getWorld();

        Plot p = PlotManager.plots.get(dev.getName().replace("_dev", "_CraftPlot"));

        if (p != null && dev == p.dev().world()) {
            while (p.dev().inTerritory(main.getLocation())) {
                main = main.getRelative(BlockFace.WEST, 2);

                if (main.getType() == Material.PISTON) {
                    BlockFace facing = ((Directional) main.getBlockData()).getFacing();

                    if (facing == BlockFace.WEST) openingBrackets++;
                    else if (facing == BlockFace.EAST) openingBrackets--;

                    if (openingBrackets == 0) return main;
                }
            }
        }
        return null;

    }


    public static void setPistonDirection (Block piston, BlockFace direction) {
        BlockData data = piston.getBlockData();

        if (data instanceof Directional directional) {
            directional.setFacing(direction);
            piston.setBlockData(data);
        }
    }

    public static boolean moveBlocks (Location pos1, Location pos2, BlockFace direction, int distance) {
        return moveBlocks(pos1, pos2, direction, distance, EditSession::close);
    }

    public static boolean moveBlocks (Location pos1, Location pos2, BlockFace direction, int distance, Consumer<EditSession> consumer) {
        return moveBlocks(pos1, pos2, direction, distance, consumer, null);
    }


    public static boolean moveBlocks (Location pos1, Location pos2, BlockFace direction, int distance, Consumer<EditSession> consumer, Executor executor) {
        Preconditions.checkNotNull(pos1);
        Preconditions.checkNotNull(pos2);
        Preconditions.checkNotNull(direction);
        Preconditions.checkNotNull(consumer);

        World world = BukkitAdapter.adapt(pos1.getWorld());
        CuboidRegion region = new CuboidRegion(world, BlockVector3.at(pos1.getBlockX(), pos1.getBlockY(), pos1.getBlockZ()), BlockVector3.at(pos2.getBlockX(), pos2.getBlockY(), pos2.getBlockZ()));

        BlockVector3 offset = BlockVector3.at(direction.getModX() * distance, direction.getModY() * distance, direction.getModZ() * distance);
        BlockVector3 finalPoint = region.getMaximumPoint().add(offset);

        Location finalAsLoc = new Location(pos1.getWorld(), finalPoint.x(), finalPoint.y(), finalPoint.z());
        Plot plot = PlotManager.plots.get(pos1.getWorld().getName().replace("_dev", "_CraftPlot"));

        if (plot != null) {
            DevPlot dev = plot.dev;

            if (dev.world() != pos1.getWorld() || !dev.inTerritory(finalAsLoc)) {
                return false;
            }
        }

        Runnable runnable = () -> {
            EditSession session = WorldEdit.getInstance().newEditSession(world);

            try {
                session.moveRegion(region, BlockVector3.at(direction.getModX(), direction.getModY(), direction.getModZ()), distance, true, null);
            } catch (MaxChangedBlocksException e) {
                plugin.getLogger().severe(e.getLocalizedMessage());
            }
            consumer.accept(session);
        };
        if (executor != null) {
            executor.execute(runnable);
        } else {
            runnable.run();
        }

        return true;
    }


    public static void setBlocks (org.bukkit.World world, Location pos1, Location pos2) {
        setBlocks(BukkitAdapter.adapt(world), BlockVector3.at(pos1.x(), pos1.y(), pos1.z()), BlockVector3.at(pos2.x(), pos2.y(), pos2.z()));
    }

    public static void setBlocks (World world, BlockVector3 pos1, BlockVector3 pos2) {
        try (EditSession session = WorldEdit.getInstance().newEditSession(world)) {
            Region region = new CuboidRegion(pos1, pos2);
            session.setBlocks(region, com.sk89q.worldedit.world.block.BlockTypes.AIR.getDefaultState());
        } catch (MaxChangedBlocksException e) {
            e.printStackTrace();
        }
    }

    /**
     * Проверяет, если сущность находится на плоте
     * @param entity сущность для проверки
     * @param plot плот, где должна быть сущность
     * @return true если на плоте, false если где-то еще гуляет
     */
    public static boolean checkPlot (Entity entity, Plot plot) {
        return entity.getWorld() == plot.world();
    }


    public static Player getDamager (Entity entity) {
        if (entity instanceof Player player) return player;

        Object damager = getEntityDamager(entity);
        if (damager == null) return null;
        if (damager instanceof Player player) return player;

        return null;
    }

    public static @Nullable Object getEntityDamager (Entity entity) {
        if (entity instanceof Projectile p) return p.getShooter();
        if (entity instanceof AreaEffectCloud cloud) {
            if (!containsNegativeEffects(cloud)) return null;
            return cloud.getSource();
        }

        if (entity instanceof TNTPrimed tnt) return tnt.getSource();
        return entity;
    }

    /**
     * Проверка для облаков зелья (от оседающих) на содержание негативных эффектов
     * @param cloud облако для проверки
     * @return true если содержит негативку
     */
    public static boolean containsNegativeEffects(AreaEffectCloud cloud) {
        for (PotionEffect effect : cloud.getCustomEffects()) {
            PotionEffectType effectType = effect.getType();
            if (effectType.getCategory() == PotionEffectTypeCategory.HARMFUL) {
                return true;
            }
        }
        return false;
    }

    public static boolean containsNegativeEffects(ThrownPotion potion) {
        for (PotionEffect effect : potion.getEffects()) {
            PotionEffectType effectType = effect.getType();
            if (effectType.getCategory() == PotionEffectTypeCategory.HARMFUL) {
                return true;
            }
        }
        return false;
    }
}
