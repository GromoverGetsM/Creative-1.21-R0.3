package ru.rstudios.creative1.coding;

import javassist.bytecode.Bytecode;
import org.apache.commons.lang3.SerializationUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import ru.rstudios.creative1.coding.actions.Action;
import ru.rstudios.creative1.coding.actions.ActionCategory;
import ru.rstudios.creative1.coding.actions.ActionChest;
import ru.rstudios.creative1.coding.actions.ActionIf;
import ru.rstudios.creative1.coding.events.GameEvent;
import ru.rstudios.creative1.coding.starters.Starter;
import ru.rstudios.creative1.coding.starters.StarterCategory;
import ru.rstudios.creative1.coding.supervariables.DynamicVariable;
import ru.rstudios.creative1.plots.Plot;
import ru.rstudios.creative1.utils.Development;

import java.io.*;
import java.util.*;

public class CodeHandler {

    public Plot plot;
    public List<Starter> starters = new LinkedList<>();
    public Map<String, DynamicVariable> dynamicVariables = new LinkedHashMap<>();

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
            StarterCategory stc = StarterCategory.byName(sign.getLine(2).replace("coding.events.", ""));

            if (stc == null || stc.getConstructor() == null) continue;

            Starter starter = stc.getConstructor().get();
            List<Action> actions = new LinkedList<>();

            for (int dx = 58; dx > -60; dx -= 2) {
                Location actionLoc = loc.clone().set(dx, loc.getBlockY(), loc.getBlockZ());
                Block actionBlock = actionLoc.getBlock();
                Development.BlockTypes actionType = Development.BlockTypes.getByMainBlock(actionBlock);

                if (actionType == null) continue;
                Sign actionsSign = (Sign) actionBlock.getRelative(BlockFace.NORTH).getState();
                ActionCategory acc = ActionCategory.byName(actionsSign.getLine(2).replace("coding.actions.", ""));

                if (acc == null || acc.getConstructor() == null) continue;

                Action action = acc.getConstructor().get();
                action.setActionBlock(actionBlock);
                action.setStarter(starter);
                if (acc.hasChest()) {
                    ActionChest actionChest = new ActionChest(action, actionBlock.getRelative(BlockFace.UP));
                    action.setChest(actionChest);
                }

                if (actionType.isCondition()) {
                    Block lastPiston = Development.getClosingPiston(actionBlock);

                    if (lastPiston == null) continue;
                    List<Action> inConditionalActions = new LinkedList<>();

                    for (int insideDx = dx - 2; insideDx > lastPiston.getX(); insideDx -= 2) {
                        Location insideLoc = loc.clone().set(insideDx, loc.getBlockY(), loc.getBlockZ());
                        Block insideBlock = insideLoc.getBlock();
                        Development.BlockTypes insideType = Development.BlockTypes.getByMainBlock(insideBlock);

                        if (insideType == null || insideType.isEvent()) continue;
                        Sign insideSign = (Sign) insideBlock.getRelative(BlockFace.NORTH).getState();
                        String actName = insideSign.getLine(2).replace("coding.actions.", "");

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
                    dx = lastPiston.getX() + 1;
                }
                actions.add(action);
            }

            starter.setActions(actions);
            starters.add(starter);
        }
        this.starters.addAll(starters);
    }

    public Map<String, DynamicVariable> getDynamicVariables() {
        return dynamicVariables;
    }

    public void sendStarter (GameEvent event, StarterCategory sct) {
        if (plot.plotMode == Plot.PlotMode.PLAY) {
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

    public void saveDynamicVars() {
        Map<String, DynamicVariable> vars = new LinkedHashMap<>();
        dynamicVariables.forEach((name, variable) -> {
            if (variable.isSaved()) vars.put(name, variable);
        });

        File file = new File(Bukkit.getWorldContainer() + File.separator + plot.dev.world().getName() + File.separator + "dynamicVars.txt");
        if (!file.exists() || !file.isFile()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try (FileOutputStream fileOutputStream = new FileOutputStream(file);
             BukkitObjectOutputStream objectOutputStream = new BukkitObjectOutputStream(fileOutputStream)) {
            objectOutputStream.writeObject(vars);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public void loadDynamicVars() {
        File file = new File(Bukkit.getWorldContainer() + File.separator + plot.dev.world().getName() + File.separator + "dynamicVars.txt");
        if (!file.exists() || !file.isFile() || file.length() == 0) return;

        try (FileInputStream fileInputStream = new FileInputStream(file);
             BukkitObjectInputStream objectInputStream = new BukkitObjectInputStream(fileInputStream)) {
            dynamicVariables.putAll((Map<String, DynamicVariable>) objectInputStream.readObject());

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


}
