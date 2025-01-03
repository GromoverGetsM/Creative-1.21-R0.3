package ru.rstudios.creative.coding.events;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public interface BlockEvent {

    Block getEventBlock();
    BlockFace getBlockFace();

}
