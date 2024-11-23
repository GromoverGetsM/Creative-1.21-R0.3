package ru.rstudios.creative1.coding.events;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public interface BlockEvent {

    Block getEventBlock();
    BlockFace getBlockFace();

}
