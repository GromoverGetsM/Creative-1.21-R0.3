package ru.rstudios.creative1.coding.actions.playeraction.communication;

import org.bukkit.entity.Player;
import ru.rstudios.creative1.coding.actions.Action;
import ru.rstudios.creative1.coding.actions.ActionCategory;
import ru.rstudios.creative1.coding.events.GameEvent;

public class ShowDemoScreen extends Action {
    @Override
    public void execute(GameEvent event) {
        getStarter().getSelection().forEach(e -> {
            if (e instanceof Player player) player.showDemoScreen();
        });
    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.SHOW_DEMO_SCREEN;
    }
}
