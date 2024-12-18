package ru.rstudios.creative1.coding.actions.ifvariable;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import ru.rstudios.creative1.coding.actions.ActionCategory;
import ru.rstudios.creative1.coding.actions.ActionChest;
import ru.rstudios.creative1.coding.actions.ActionIf;
import ru.rstudios.creative1.coding.events.GameEvent;
import ru.rstudios.creative1.menu.SwitchItem;
import ru.rstudios.creative1.utils.Development;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class IfTextContains extends ActionIf {
    @Override
    public boolean conditionExpression(GameEvent event) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        ItemStack[] args = Arrays.copyOfRange(chest.getOriginalContents(), 26, 54);
        ItemStack[] nonnull = Arrays.stream(args)
                .filter(item -> item != null && item.getType() != Material.AIR)
                .toArray(ItemStack[]::new);

        SwitchItem caseIgnore = getCategory().getCodingMenu().getSwitches().get(21);
        caseIgnore.setCurrentState(caseIgnore.getCurrentState(chest.getOriginalContents()[21]));

        SwitchItem colorsIgnore = getCategory().getCodingMenu().getSwitches().get(23);
        colorsIgnore.setCurrentState(colorsIgnore.getCurrentState(chest.getOriginalContents()[23]));

        boolean caseI = Boolean.parseBoolean(caseIgnore.getCurrentValue());
        boolean colorI = Boolean.parseBoolean(colorsIgnore.getCurrentValue());

        for (Entity entity : getStarter().getSelection()) {
            if (!Development.checkPlot(entity, event.getPlot())) {
                continue;
            }

            String text = String.valueOf(ActionChest.parseItem(chest.getOriginalContents()[13], event, entity));
            List<String> texts = new LinkedList<>();

            if (nonnull.length == 0) return false;

            Arrays.stream(nonnull).forEach(item -> texts.add(String.valueOf(ActionChest.parseItem(item, event, entity))));

            for (String s : texts) {
                if (caseI) {
                    s = s.toLowerCase();
                    text = text.toLowerCase();
                }

                if (colorI) {
                    s = ChatColor.stripColor(s);
                    text = ChatColor.stripColor(text);
                }

                if (s.equals(text)) return true;
            }
        }

        return false;
    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.IF_TEXT_CONTAINS;
    }
}
