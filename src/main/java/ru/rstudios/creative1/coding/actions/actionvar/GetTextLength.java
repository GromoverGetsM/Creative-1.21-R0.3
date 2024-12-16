package ru.rstudios.creative1.coding.actions.actionvar;

import org.bukkit.entity.Entity;
import ru.rstudios.creative1.coding.actions.Action;
import ru.rstudios.creative1.coding.actions.ActionCategory;
import ru.rstudios.creative1.coding.actions.ActionChest;
import ru.rstudios.creative1.coding.events.GameEvent;
import ru.rstudios.creative1.coding.supervariables.DynamicVariable;
import ru.rstudios.creative1.utils.Development;

public class GetTextLength extends Action {
    @Override
    public void execute(GameEvent event) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        for (Entity entity : getStarter().getSelection()) {
            if (!Development.checkPlot(entity, event.getPlot())) {
                continue;
            }

            DynamicVariable variable = ActionChest.asDynamicVariable(chest.getOriginalContents()[11], event, entity);

            if (variable.getName().isEmpty()) {
                event.getPlot().throwException(this, new IllegalStateException("Переменная не указана или повреждена!"));
                return;
            }

            Object o = ActionChest.parseItem(chest.getOriginalContents()[15], event, entity);

            if (o == null) {
                event.getPlot().throwException(this, new IllegalStateException("Не удалось обработать текст"));
                return;
            }

            String text = String.valueOf(ActionChest.parseItem(chest.getOriginalContents()[15], event, entity));
            boolean saved = DynamicVariable.isVarSaved(chest.getOriginalContents()[11]);

            variable.setValue(event.getPlot(), text.length(), saved);
        }
    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.GET_TEXT_LEN;
    }
}
