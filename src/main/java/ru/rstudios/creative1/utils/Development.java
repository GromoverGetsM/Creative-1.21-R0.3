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
import com.sk89q.worldedit.world.block.BlockTypes;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.event.block.BlockPlaceEvent;
import org.jetbrains.annotations.Nullable;
import ru.rstudios.creative1.plots.DevPlot;
import ru.rstudios.creative1.plots.Plot;
import ru.rstudios.creative1.plots.PlotManager;

import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

import static ru.rstudios.creative1.Creative_1.plugin;

public class Development {

    public enum BlockTypes {

        PLAYER_EVENT(Material.DIAMOND_BLOCK, Material.DIAMOND_ORE, true),
        BLOCK_EVENT(Material.PRISMARINE_BRICKS, Material.PRISMARINE, true),
        PLAYER_ACTION(Material.COBBLESTONE, Material.STONE, false),
        ENTITY_ACTION(Material.MUD_BRICKS, Material.PACKED_MUD, false),
        ACTION_VAR(Material.IRON_BLOCK, Material.IRON_ORE, false),
        WORLD_ACTION(Material.RED_NETHER_BRICKS, Material.NETHERRACK, false),
        IF_PLAYER(Material.OAK_PLANKS, Material.PISTON, false),
        IF_VARIABLE(Material.OBSIDIAN, Material.PISTON, false);


        private Material mainBlock;
        private Material additionalBlock;
        private boolean isEvent;

        BlockTypes (Material mainBlock, Material additionalBlock, boolean isEvent) {
            this.mainBlock = mainBlock;
            this.additionalBlock = additionalBlock;
            this.isEvent = isEvent;
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

        public static @Nullable BlockTypes getByMainBlock (Material mainBlock) {
            return Arrays.stream(values()).filter(bt -> bt.mainBlock == mainBlock).findFirst().orElse(null);
        }

        public static @Nullable BlockTypes getByMainBlock (Block mainBlock) {
            return getByMainBlock(mainBlock.getType());
        }
    }

    public void setCodingBlock (BlockPlaceEvent event) {
        Block block = event.getBlock();
        BlockTypes type = BlockTypes.getByMainBlock(block);

        if (type != null) {
            if (PlotManager.isDevWorld(block.getWorld())) {
                Block additional = block.getRelative(BlockFace.WEST);
                additional.setType(type.additionalBlock);

                if (type.additionalBlock == Material.PISTON) {
                    setPistonDirection(additional, BlockFace.WEST);

                    // TODO: IF THERE ANY BLOCKS???

                    Block reversed = additional.getRelative(BlockFace.WEST, 2);
                    reversed.setType(Material.PISTON);
                    setPistonDirection(reversed, BlockFace.EAST);
                }
            }
        } else {
            event.setCancelled(true);
        }
    }

    public void setPistonDirection (Block piston, BlockFace direction) {
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

}
