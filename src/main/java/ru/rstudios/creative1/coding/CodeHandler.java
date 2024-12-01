package ru.rstudios.creative1.coding;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import ru.rstudios.creative1.coding.actions.Action;
import ru.rstudios.creative1.coding.actions.ActionCategory;
import ru.rstudios.creative1.coding.actions.ActionChest;
import ru.rstudios.creative1.coding.actions.ActionIf;
import ru.rstudios.creative1.coding.events.GameEvent;
import ru.rstudios.creative1.coding.starters.Starter;
import ru.rstudios.creative1.coding.starters.StarterCategory;
import ru.rstudios.creative1.plots.Plot;
import ru.rstudios.creative1.utils.Development;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static ru.rstudios.creative1.Creative_1.plugin;

public class CodeHandler {

    public Plot plot;
    public List<Starter> starters = new LinkedList<>();

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
            StarterCategory stc = StarterCategory.byName(sign.getLine(2).replace("coding.events.", "").replace(".name", ""));

            if (stc == null || stc.getConstructor() == null) continue;

            Starter starter = stc.getConstructor().get();
            List<Action> actions = new LinkedList<>();

            for (int dx = 58; dx > -60; dx -= 2) {
                Location actionLoc = loc.clone().set(dx, loc.getBlockY(), loc.getBlockZ());
                Block actionBlock = actionLoc.getBlock();
                Development.BlockTypes actionType = Development.BlockTypes.getByMainBlock(actionBlock);

                if (actionType == null) continue;
                Sign actionsSign = (Sign) actionBlock.getRelative(BlockFace.NORTH).getState();
                ActionCategory acc = ActionCategory.byName(actionsSign.getLine(2).replace("coding.actions.", "").replace(".name", ""));

                if (acc == null || acc.getConstructor() == null) continue;

                Action action = acc.getConstructor().get();
                action.setActionBlock(actionBlock);
                action.setStarter(starter);
                if (acc.hasChest()) {
                    ActionChest actionChest = new ActionChest(action, actionBlock.getRelative(BlockFace.UP));
                    action.setChest(actionChest);
                }

                if (type.isCondition()) {
                    Block lastPiston = Development.getClosingPiston(actionBlock);

                    if (lastPiston == null) continue;
                    List<Action> inConditionalActions = new LinkedList<>();

                    for (int insideDx = dx - 2; insideDx > lastPiston.getX(); insideDx -= 2) {
                        Location insideLoc = loc.clone().set(insideDx, loc.getBlockY(), loc.getBlockZ());
                        Block insideBlock = insideLoc.getBlock();
                        Development.BlockTypes insideType = Development.BlockTypes.getByMainBlock(insideBlock);

                        if (insideType == null || insideType.isEvent()) continue;
                        Sign insideSign = (Sign) insideBlock.getRelative(BlockFace.NORTH).getState();
                        String actName = insideSign.getLine(2).replace("coding.actions.", "").replace(".name", "");

                        ActionCategory insideCategory = ActionCategory.byName(actName);
                        if (insideCategory == null || insideCategory.getConstructor() == null) continue;


                        Action insideAction = insideCategory.getConstructor().get();
                        insideAction.setActionBlock(insideBlock);
                        insideAction.setStarter(starter);

                        if (insideCategory.hasChest()) {
                            ActionChest actionChest = new ActionChest(insideAction, insideBlock.getRelative(BlockFace.UP));
                            insideAction.setChest(actionChest);
                        }

                        inConditionalActions.add(insideAction);
                    }

                    ((ActionIf) action).setInConditionalActions(inConditionalActions);
                    dx = lastPiston.getX();
                }
                actions.add(action);
            }

            starter.setActions(actions);
            starters.add(starter);
        }
        this.starters.addAll(starters);
    }

    public void sendStarter (GameEvent event, StarterCategory sct) {
        if (this.starters != null && !this.starters.isEmpty()) {

            for (Starter starter : this.starters) {
                if (starter.getCategory() == sct) {
                    starter.setSelection(Collections.singletonList(event.getDefaultEntity()));
                    starter.execute(event);
                }
            }

        }
    }


}
