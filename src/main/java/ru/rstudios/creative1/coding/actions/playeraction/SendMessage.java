package ru.rstudios.creative1.coding.actions.playeraction;

import ru.rstudios.creative1.coding.actions.Action;
import ru.rstudios.creative1.coding.actions.ActionCategory;
import ru.rstudios.creative1.coding.actions.ActionChest;
import ru.rstudios.creative1.coding.events.GameEvent;
import ru.rstudios.creative1.menu.SwitchItem;

import java.util.List;

public class SendMessage extends Action {
    @Override
    public void execute(GameEvent event) throws Exception {
        ActionChest chest = getChest();
        chest.initInventorySort();

        getStarter().getSelection().forEach(e -> {
            StringBuilder builder = new StringBuilder();
            List<String> texts = chest.getAsTexts(event, e);

            SwitchItem item = getCategory().getCodingMenu().getSwitches().get(49);
            int currentState = item.getCurrentState(chest.getOriginalContents()[49]);

            switch (currentState) {
                case 0 -> texts.forEach(builder::append);
                case 1 -> texts.forEach(text -> builder.append(" ").append(text));
                case 2 -> texts.forEach(text -> builder.append('\n').append(text));
            }

            String itog = this.replacePlaceholders(builder.toString().trim(), event, e);
            e.sendMessage(itog);
        });
    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.SEND_MESSAGE;
    }
}
