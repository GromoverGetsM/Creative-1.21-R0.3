package ru.rstudios.creative1.utils;

import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.AbstractDelegateExtent;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.block.BlockStateHolder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;

import static ru.rstudios.creative1.Creative_1.plugin;

public class ChestMenuHook extends AbstractDelegateExtent {

    private final World world;

    public ChestMenuHook(Extent extent, World bukkitWorld) {
        super(extent);
        this.world = bukkitWorld;
    }

    @Override
    public <T extends BlockStateHolder<T>> boolean setBlock(BlockVector3 position, T block) throws WorldEditException {
        org.bukkit.World adapted = BukkitAdapter.adapt(world);
        Block adaptedBlock = adapted.getBlockAt(new Location(adapted, position.x(), position.y(), position.z()));

        if (adaptedBlock.getType() == Material.CHEST) {

            String worldName = world.getName();
            if (worldName.endsWith("_dev")) {
                Block bukkitBlock = adapted.getBlockAt(position.x(), position.y(), position.z());

                if (bukkitBlock.getState() instanceof Chest) {
                    PersistentDataContainer dataContainer = ((Chest) bukkitBlock.getState()).getPersistentDataContainer();
                    NamespacedKey key = new NamespacedKey(plugin, "openedName");

                    if (dataContainer.has(key, org.bukkit.persistence.PersistentDataType.STRING)) {
                        String playerName = dataContainer.get(key, org.bukkit.persistence.PersistentDataType.STRING);

                        super.setBlock(position, block);

                        Player player = Bukkit.getPlayer(playerName);
                        if (player != null && player.isOnline()) {
                            player.closeInventory();
                        }

                        return true;
                    }
                }
            }
        }

        return super.setBlock(position, block);
    }
}
