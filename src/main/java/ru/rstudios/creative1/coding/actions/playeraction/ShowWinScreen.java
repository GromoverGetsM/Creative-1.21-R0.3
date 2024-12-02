package ru.rstudios.creative1.coding.actions.playeraction;

import org.bukkit.entity.Player;
import ru.rstudios.creative1.coding.actions.Action;
import ru.rstudios.creative1.coding.actions.ActionCategory;
import ru.rstudios.creative1.coding.events.GameEvent;

public class ShowWinScreen extends Action {
    @Override
    public void execute(GameEvent event) {
        getStarter().getSelection().forEach(e -> {
            if (e instanceof Player player) player.showWinScreen();
        });
    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.SHOW_WIN_SCREEN;
    }
}
