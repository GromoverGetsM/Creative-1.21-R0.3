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
import ru.rstudios.creative1.coding.starters.Starter;
import ru.rstudios.creative1.coding.starters.StarterCategory;
import ru.rstudios.creative1.plots.Plot;
import ru.rstudios.creative1.utils.Development;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static ru.rstudios.creative1.Creative_1.plugin;

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
        plugin.getLogger().severe("Starting code parser with plot=" + plot.plotName());

        for (int dz = 60; dz > -60; dz -= 4) {
            Location loc = startBlock.clone().set(startBlock.getBlockX(), startBlock.getBlockY(), dz);
            Development.BlockTypes type = Development.BlockTypes.getByMainBlock(loc.getBlock());

            if (type == null || !type.isEvent()) continue;

            Sign sign = (Sign) loc.getBlock().getRelative(BlockFace.NORTH).getState();
            StarterCategory stc = StarterCategory.byName(sign.getLine(2).replace("coding.starters.", "").replace(".name", ""));
            plugin.getLogger().warning("Init starter with category=" + stc);

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
                System.out.println("Action a=" + acc.name() + "ActionLocation=" + actionLoc);

                Action action = acc.getConstructor().get();

                if (actionType.isCondition()) {
                    // Логика для парсинга условий внутри условия
                    Block closingPiston = Development.getClosingPiston(actionBlock);
                    if (closingPiston == null) {
                        // TODO: Логирование ошибки, если не нашли закрывающий поршень
                        continue;
                    }

                    List<Action> conditionalActions = new LinkedList<>();
                    Block currentBlock = actionBlock.getRelative(BlockFace.WEST); // Начинаем парсить с блока, расположенного слева от основного действия

                    // Парсим блоки внутри условия, пропуская соединительные блоки
                    while (currentBlock.getX() > closingPiston.getX()) {
                        System.out.println("=============PARSING INCOND BLOCK===========");
                        System.out.println("Location=" + currentBlock.getLocation());
                        System.out.println("Type=" + currentBlock.getType());
                        // Пропускаем соединительные блоки (которые не являются действиями)
                        Block finalCurrentBlock = currentBlock;
                        if (Arrays.stream(Development.BlockTypes.values()).filter(type2 -> type2.getAdditionalBlock() == finalCurrentBlock.getType()).findFirst().orElse(null) != null) {
                            currentBlock = currentBlock.getRelative(BlockFace.WEST);
                            continue;
                        }

                        // Проверяем, является ли текущий блок действием
                        Development.BlockTypes innerType = Development.BlockTypes.getByMainBlock(currentBlock);
                        if (innerType == null || !innerType.isEvent()) {
                            currentBlock = currentBlock.getRelative(BlockFace.WEST);
                            continue;
                        }
                        System.out.println("IsAction=TRUE");

                        Sign sign2 = (Sign) currentBlock.getRelative(BlockFace.NORTH).getState();
                        ActionCategory innerAcc = ActionCategory.byName(sign2.getLine(2).replace("coding.actions.", "").replace(".name", ""));

                        if (innerAcc == null || innerAcc.getConstructor() == null) {
                            currentBlock = currentBlock.getRelative(BlockFace.WEST);
                            continue;
                        }
                        System.out.println("INNERCAT=" + innerAcc.name());

                        Action innerAction = innerAcc.getConstructor().get();
                        if (innerAction != null) {
                            // Добавляем сундук, если действие имеет сундук
                            hasChest(conditionalActions, currentBlock, innerAcc, innerAction);
                        }

                        // Переходим на два блока дальше, чтобы пропустить соединитель
                        currentBlock = currentBlock.getRelative(BlockFace.WEST, 2);
                    }

                    ActionIf actionIf = (ActionIf) acc.getConstructor().get();

                    // Применяем инверсию, если она есть
                    String invertedText = actionsSign.getLine(3);
                    if ("coding.inverted".equals(invertedText)) {
                        actionIf.setInverted(true);
                    }

                    actions.add(actionIf);

                    // Пропускаем до закрывающего поршня
                    dx = closingPiston.getX() - 2;
                    continue;
                }

                // Добавляем сундук, если действие имеет сундук
                hasChest(actions, actionBlock, acc, action);
            }

            if (starter != null) {
                starter.setActions(actions);
                starters.add(starter);
            } else {
                // TODO: Логирование ошибки, если событие не найдено
            }
        }

        this.starters.addAll(starters);
    }

    private void hasChest(List<Action> actions, Block actionBlock, ActionCategory acc, Action action) {
        if (acc.hasChest()) {
            Location chestLoc = actionBlock.getRelative(BlockFace.UP).getLocation();
            if (chestLoc.getBlock().getType() == Material.CHEST) {
                ActionChest chest = new ActionChest(action, chestLoc.getBlock());
                action.setChest(chest);
            }
        }

        actions.add(action);
    }


}
