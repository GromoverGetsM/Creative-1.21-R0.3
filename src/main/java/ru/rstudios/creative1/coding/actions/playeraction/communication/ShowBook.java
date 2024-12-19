package ru.rstudios.creative1.coding.actions.playeraction.communication;

import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import ru.rstudios.creative1.coding.actions.Action;
import ru.rstudios.creative1.coding.actions.ActionCategory;
import ru.rstudios.creative1.coding.actions.ActionChest;
import ru.rstudios.creative1.coding.events.GameEvent;
import ru.rstudios.creative1.utils.Development;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ShowBook extends Action {
    @Override
    public void execute(GameEvent event, List<Entity> selection) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        for (Entity e : selection) {
            if (!Development.checkPlot(e, event.getPlot())) {

                continue;
            }

            List<String> rawPages = chest.getAsTexts(event, e);
            Collection<Component> pages = new LinkedList<>();
            rawPages.forEach(p -> pages.add(Component.text(p)));

            if (e instanceof Player player) {
                player.openBook(Book.book(Component.text("Книга"), Component.text("Creative-1.21-R0.3"), pages));
            }
        }

    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.SHOW_BOOK;
    }
}
