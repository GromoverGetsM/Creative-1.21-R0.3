package ru.rstudios.creative.utils;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class FlatChunkGenerator extends ChunkGenerator {

    @Override
    public @NotNull ChunkData generateChunkData(@NotNull World world, @NotNull Random random, int x, int z, @NotNull BiomeGrid biome) {
        ChunkData chunkData = createChunkData(world);
        int baseY = world.getMinHeight();
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                chunkData.setBlock(i, baseY, j, Material.BEDROCK);
                chunkData.setBlock(i, baseY+1, j, Material.DIRT);
                chunkData.setBlock(i, baseY+2, j, Material.DIRT);
                chunkData.setBlock(i, baseY+3, j, Material.GRASS_BLOCK);
            }
        }
        return chunkData;
    }



}
