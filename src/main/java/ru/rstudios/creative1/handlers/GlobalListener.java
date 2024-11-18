package ru.rstudios.creative1.handlers;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import ru.rstudios.creative1.user.User;
import ru.rstudios.creative1.utils.DatabaseUtil;

public class GlobalListener implements Listener {

    @EventHandler
    public void onPlayerJoin (PlayerJoinEvent event) {
        User user = User.asUser(event.getPlayer());

        if (!DatabaseUtil.isValueExist("players", "player_name", user.name())) {
            DatabaseUtil.insertValue("players", "player_name", user.name());
            DatabaseUtil.updateValue("players", "plot_limit", 3, "player_name", user.name());

            String locale = event.getPlayer().getLocale().equalsIgnoreCase("ru_ru") ? "ru_RU" : "en_US";

            DatabaseUtil.updateValue("players", "player_locale", locale, "player_name", user.name());
        }
    }

}
