package ru.rstudios.creative1.coding;

import com.google.common.collect.Lists;
import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.WorldBorder;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import ru.rstudios.creative1.coding.actions.*;
import ru.rstudios.creative1.coding.events.GameEvent;
import ru.rstudios.creative1.coding.starters.Starter;
import ru.rstudios.creative1.coding.starters.StarterCategory;
import ru.rstudios.creative1.coding.starters.playerevent.PlayerQuit;
import ru.rstudios.creative1.coding.starters.uncommon.Cycle;
import ru.rstudios.creative1.coding.starters.uncommon.Function;
import ru.rstudios.creative1.coding.supervariables.DynamicVariable;
import ru.rstudios.creative1.plots.LimitManager;
import ru.rstudios.creative1.plots.Plot;
import ru.rstudios.creative1.user.User;
import ru.rstudios.creative1.utils.Development;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import static ru.rstudios.creative1.Creative_1.plugin;

public class CodeHandler {

    public Plot plot;
    public List<Starter> starters = new LinkedList<>();
    public Map<String, DynamicVariable> dynamicVariables = new LinkedHashMap<>();
    private final Map<String, BossBar> bossBars = new LinkedHashMap<>();
    private final Map<String, Scoreboard> scoreboards = new LinkedHashMap<>();
    private final Map<String, WorldBorder> borders = new LinkedHashMap<>();
    public List<Cycle> cycles = new LinkedList<>();

    public int callsAmount = 0;

    public CodeHandler (Plot plot) {
        this.plot = plot;
    }

    public void parseCodeBlocks() {
        Location startBlock = new Location(plot.getDev().world(), 60, -59, 60);
        if (!this.starters.isEmpty()) this.starters.clear();
        List<Starter> starters = new LinkedList<>();

        for (int dz = 60; dz > -60; dz -= 4) {
            Location loc = startBlock.clone().set(startBlock.getBlockX(), startBlock.getBlockY(), dz);
            Development.BlockTypes type = Development.BlockTypes.getByMainBlock(loc.getBlock());

            if (type == null || !type.isEvent()) continue;

            Sign sign = (Sign) loc.getBlock().getRelative(BlockFace.NORTH).getState();
            StarterCategory stc;

            if (loc.getBlock().getType() == Development.BlockTypes.FUNCTION.getMainBlock()) {
                stc = StarterCategory.FUNCTION;
            } else if (loc.getBlock().getType() == Development.BlockTypes.CYCLE.getMainBlock()) {
                stc = StarterCategory.CYCLE;
            }
            else stc = StarterCategory.byName(sign.getLine(2).replace("coding.events.", ""));

            if (stc == null || stc.getConstructor() == null) continue;

            Starter starter = stc.getConstructor().get();

            if (stc == StarterCategory.FUNCTION) {
                if (sign.getLine(2).isEmpty()) {
                    continue;
                }
                ((Function) starter).setName(sign.getLine(2));
            }
            if (stc == StarterCategory.CYCLE) {
                if (sign.getLine(2).isEmpty() || sign.getLine(3).isEmpty()) return;

                ((Cycle) starter).setName(sign.getLine(2));
                ((Cycle) starter).setRepeatTime(Integer.parseInt(sign.getLine(3)) <= 0 ? 20 : Integer.parseInt(sign.getLine(3)) > 432000 ? 20 : Integer.parseInt(sign.getLine(3)));
            }

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

                boolean inverted = actionsSign.getLine(0).equalsIgnoreCase("coding.tech.not");

                if (actionBlock.getType() == Material.PURPUR_BLOCK) {
                    if (!actionsSign.getLine(3).isEmpty()) {
                        ActionCategory selectCondition = ActionCategory.byName(actionsSign.getLine(3).replace("coding.actions.", ""));

                        if (selectCondition == null || selectCondition.getConstructor() == null) continue;

                        ActionIf selectCond = ((ActionIf) selectCondition.getConstructor().get());
                        selectCond.setInverted(inverted);
                        if (selectCondition.hasChest()) {
                            ActionChest actionChest = new ActionChest(action, actionBlock.getRelative(BlockFace.UP));
                            selectCond.setChest(actionChest);
                        }
                        ((ActionSelect) action).setCondition(selectCond);
                    }
                }

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

                    ((ActionIf) action).setInverted(inverted);
                    ((ActionIf) action).setInConditionalActions(inConditionalActions);
                    dx = lastPiston.getX() + 1;
                }
                actions.add(action);
            }

            starter.setOriginalActions(actions);
            starters.add(starter);
        }
        this.starters.addAll(starters);
    }

    public Map<String, DynamicVariable> getDynamicVariables() {
        return dynamicVariables;
    }

    public Map<String, Scoreboard> getScoreboards() {
        return scoreboards;
    }

    public Map<String, BossBar> getBossBars() {
        return bossBars;
    }

    public void setDynamicVariables(Map<String, DynamicVariable> dynamicVariables) {
        this.dynamicVariables = dynamicVariables;
    }

    public void sendStarter (GameEvent event, StarterCategory sct) {
        if (plot.plotMode == Plot.PlotMode.PLAY) {
            if (this.starters != null && !this.starters.isEmpty()) {
                if (!LimitManager.checkLimit(plot, "code_operations", callsAmount)) {
                    for (Player player1 : plot.online()) {
                        User.asUser(player1).sendMessage("info.plot-set-mode-build", true, "");
                        User.asUser(player1).clear();
                        plot.throwException("code_operations", String.valueOf(callsAmount), String.valueOf(LimitManager.getLimitValue(plot, "code_operations")));
                    }
                    plot.handler.stopCycles();
                    plot.plotMode = Plot.PlotMode.BUILD;
                } else {
                    for (Starter starter : this.starters) {
                        if (starter.getCategory() == sct) {
                            starter.setSelection(Collections.singletonList(event.getDefaultEntity()));
                            starter.execute(event);
                            increaseCalls();
                            for (Action action : starter.getActions()) increaseCalls();
                        }
                    }
                }

            }
        }
    }

    public void launchFunction (Starter s, String name) {
        if (!LimitManager.checkLimit(plot, "code_operations", callsAmount)) {
            for (Player player1 : plot.online()) {
                User.asUser(player1).sendMessage("info.plot-set-mode-build", true, "");
                User.asUser(player1).clear();
                plot.throwException("code_operations", String.valueOf(callsAmount), String.valueOf(LimitManager.getLimitValue(plot, "code_operations")));
                plot.handler.sendStarter(new PlayerQuit.Event(player1, plot, new PlayerChangedWorldEvent(player1, player1.getWorld())), StarterCategory.PLAYER_QUIT);
            }
            plot.handler.stopCycles();
            plot.plotMode = Plot.PlotMode.BUILD;
        } else {
            for (Starter starter : starters) {
                if (starter instanceof Function function && function.getName().equals(name)) {
                    starter.getActions().addAll(s.getCurrentIndex() + 1, function.getActions());
                    increaseCalls(function.getActions().size());
                }
            }
        }
    }

    public void launchCycle (GameEvent event, String name, List<Entity> selection) {
        for (Starter starter : starters) {
            if (starter instanceof Cycle cycle && cycle.getName().equals(name)) {
                cycle.setSelection(selection);
                cycle.executeCycle(event);
            }
        }
    }

    public void stopCycle (String name) {
        for (Cycle cycle : cycles) {
            if (cycle.getName().equals(name)) cycle.stop(this);
        }
    }

    public void saveDynamicVars() {
        Map<String, DynamicVariable> vars = dynamicVariables.entrySet().stream()
                .filter(entry -> entry.getValue().isSaved())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> b, LinkedHashMap::new));


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

    public void stopCycles() {
        for (Cycle cycle : cycles) {
             cycle.stop(this);
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

    public void increaseCalls() {
        callsAmount++;
        Bukkit.getScheduler().runTaskLater(plugin, () -> callsAmount--, 35L);
    }

    public void increaseCalls(int i) {
        callsAmount += i;
        Bukkit.getScheduler().runTaskLater(plugin, () -> callsAmount -= i, 35L);
    }

    public void putDynamicVariable (DynamicVariable variable) {
        if (LimitManager.checkLimit(plot, "variables", dynamicVariables.size())) dynamicVariables.put(variable.getName(), variable);
    }

    public void putDynamicVariable (String name, DynamicVariable variable) {
        if (LimitManager.checkLimit(plot, "variables", dynamicVariables.size())) dynamicVariables.put(name, variable);
    }

    public void tryAddWorldBorder (String name, WorldBorder border) {
        if (LimitManager.checkLimit(plot, "worldborders", borders.size())) borders.put(name, border);
    }

    public Map<String, WorldBorder> getBorders() {
        return borders;
    }
}
