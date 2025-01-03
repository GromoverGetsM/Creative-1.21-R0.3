package ru.rstudios.creative.coding.actions.playeraction.communication;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import ru.rstudios.creative.coding.actions.Action;
import ru.rstudios.creative.coding.actions.ActionCategory;
import ru.rstudios.creative.coding.actions.ActionChest;
import ru.rstudios.creative.coding.events.GameEvent;
import ru.rstudios.creative.menu.SwitchItem;
import ru.rstudios.creative.utils.Development;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static ru.rstudios.creative.CreativePlugin.plugin;

public class SendAdvancementToast extends Action {
    @Override
    public void execute(GameEvent event, List<Entity> selection) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        for (Entity e : selection) {
            if (!Development.checkPlot(e, event.getPlot())) {

                continue;
            }
            if (e instanceof Player player) {
                NamespacedKey key = new NamespacedKey(plugin, "advancementToast" + UUID.randomUUID());

                String title = ActionChest.parseText(chest.getOriginalContents()[11]);
                Material icon = chest.getOriginalContents()[15] == null ? Material.DIAMOND : chest.getOriginalContents()[15].getType();

                SwitchItem item = getCategory().getCodingMenu().getSwitches().get(22);
                item.setCurrentState(item.getCurrentState(chest.getOriginalContents()[22]));
                String frame = item.getCurrentValue();

                String advancementJSON = "{"
                        + "\"criteria\": {"
                        + "  \"impossible\": {"
                        + "    \"trigger\": \"minecraft:impossible\""
                        + "  }"
                        + "},"
                        + "\"display\": {"
                        + "  \"icon\": {"
                        + "    \"id\": \"" + "minecraft:" + icon.name().toLowerCase(Locale.ROOT) + "\""
                        + "  },"
                        + "  \"title\": \"" + title + "\","
                        + "  \"description\": \"Unknown\","
                        + "  \"frame\": \"" + frame + "\","
                        + "  \"announce_to_chat\": false,"
                        + "  \"show_toast\": true,"
                        + "  \"hidden\": true"
                        + "}"
                        + "}";

                Bukkit.getUnsafe().removeAdvancement(key);
                Advancement advancement = Bukkit.getUnsafe().loadAdvancement(key, advancementJSON);

                if (advancement != null) {
                    AdvancementProgress progress = player.getAdvancementProgress(advancement);

                    progress.awardCriteria("impossible");

                    Bukkit.getScheduler().runTaskLater(plugin, () -> Bukkit.getUnsafe().removeAdvancement(key), 2L);
                }
            }
        }

    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.SEND_ADVANCEMENT_TOAST;
    }
}
