package ru.rstudios.creative1.coding;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import ru.rstudios.creative1.coding.actions.Action;
import ru.rstudios.creative1.coding.actions.ActionCategory;
import ru.rstudios.creative1.coding.starters.Starter;
import ru.rstudios.creative1.coding.starters.StarterCategory;
import ru.rstudios.creative1.plots.Plot;
import ru.rstudios.creative1.utils.Development;

import java.util.LinkedList;
import java.util.List;

public class CodeHandler {

    public Plot plot;
    public List<Starter> starters;

    public CodeHandler (Plot plot) {
        this.plot = plot;
    }

    public void parseCodeBlocks() {
        Location startBlock = new Location(plot.dev().world(), 60, -59, 60);
        if (!this.starters.isEmpty()) this.starters.clear();
        List<Starter> starters = new LinkedList<>();

        for (int dz = 60; dz > -60; dz -= 4) {
            Location loc = startBlock.clone().set(startBlock.getBlockX(), startBlock.getBlockY(), dz);
            Development.BlockTypes type = Development.BlockTypes.getByMainBlock(loc.getBlock());

            if (type == null || !type.isEvent()) continue;

            Sign sign = (Sign) loc.getBlock().getRelative(BlockFace.NORTH).getState();
            StarterCategory stc = StarterCategory.byName(sign.getLine(2).replace("coding.starters.", "").replace(".name", ""));

            if (stc == null || stc.getConstructor() == null) continue;

            Starter starter = stc.getConstructor().get();
            List<Action> actions = new LinkedList<>();

            for (int dx = 58; dx > -60; dx -= 2) {
                Location action = loc.clone().set(dx, loc.getBlockY(), loc.getBlockZ());
                Block actionBlock = action.getBlock();
                Development.BlockTypes actionType = Development.BlockTypes.getByMainBlock(actionBlock);

                if (actionType == null) continue;
                Sign actionsSign = (Sign) actionBlock.getRelative(BlockFace.NORTH).getState();
                ActionCategory acc = ActionCategory.byName(actionsSign.getLine(2).replace("coding.actions.", "").replace(".name", ""));

                if (acc == null || acc.getConstructor() == null) continue;

            }
        }
    }

}
