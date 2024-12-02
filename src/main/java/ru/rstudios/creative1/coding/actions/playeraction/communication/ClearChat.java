package ru.rstudios.creative1.coding.actions.playeraction.communication;

import ru.rstudios.creative1.coding.actions.Action;
import ru.rstudios.creative1.coding.actions.ActionCategory;
import ru.rstudios.creative1.coding.events.GameEvent;

public class ClearChat extends Action {
    @Override
    public void execute(GameEvent event) {
        StringBuilder sb = new StringBuilder();
        sb.append(" \n".repeat(256));
        String message = sb.toString();

        getStarter().getSelection().forEach(e -> e.sendMessage(message));
    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.CLEAR_CHAT;
    }
}
