package ru.rstudios.creative1.user;

import net.kyori.adventure.text.Component;
import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import ru.rstudios.creative1.handlers.GlobalListener;
import ru.rstudios.creative1.utils.DatabaseUtil;

import java.io.File;
import java.util.*;

import static ru.rstudios.creative1.Creative_1.plugin;

public class LocaleManages {

    // Хранилище локализации в памяти
    private static final Map<String, Map<String, Object>> loadedLocales = new HashMap<>();

    /**
     * Загружает все локализационные файлы в память.
     */
    public static void loadLocales() {
        plugin.getLogger().info("Started initialization process for locales");
        long startTime = System.currentTimeMillis();

        String[] availableLocales = {"ru_RU", "en_US"};
        for (String locale : availableLocales) {
            File file = new File(plugin.getDataFolder() + File.separator + "locale" + File.separator + locale + ".yml");
            if (file.exists()) {
                FileConfiguration config = YamlConfiguration.loadConfiguration(file);
                Map<String, Object> localeMap = new HashMap<>();
                for (String key : config.getKeys(true)) {
                    localeMap.put(key, config.isList(key) ? config.getStringList(key) : config.getString(key));
                }
                loadedLocales.put(locale, localeMap);
            } else {
                plugin.getLogger().warning("Localization file " + file.getName() + " not found");
            }
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        plugin.getLogger().info("Locales successfully loaded in " + duration + "ms");
    }

    /**
     * Возвращает локализацию из памяти.
     */
    private static Map<String, Object> getLocaleMap(String locale) {
        return loadedLocales.getOrDefault(locale, loadedLocales.get("en_US"));
    }

    /**
     * Получает сообщение по коду.
     */
    public static String getLocaleMessage(String locale, String messageCode, boolean needPrefix, String... localeChanges) {
        Map<String, Object> localeMap = getLocaleMap(locale);
        String message = (String) localeMap.getOrDefault(messageCode, messageCode);

        if (localeChanges.length > 0) {
            message = String.format(message, (Object[]) localeChanges);
        }

        if (needPrefix) {
            String prefix = (String) localeMap.getOrDefault("prefix", "prefix");
            message = prefix + message;
        }

        return GlobalListener.parseColors(message);
    }

    /**
     * Получает список сообщений по коду.
     */
    public static List<Component> getLocaleMessages(String locale, String messageCode, HashMap<Integer, String> localeChanges) {
        Map<String, Object> localeMap = getLocaleMap(locale);
        List<String> messages = new ArrayList<>((List<String>) localeMap.getOrDefault(messageCode, Collections.emptyList()));
        List<Component> messagesC = new LinkedList<>();

        for (Map.Entry<Integer, String> entry : localeChanges.entrySet()) {
            int index = entry.getKey();
            if (index >= 0 && index < messages.size()) {
                messages.set(index, String.format(messages.get(index), entry.getValue()));
            }
        }

        for (String s : messages) {
            messagesC.add(Component.text(GlobalListener.parseColors(s)));
        }

        if (messagesC.isEmpty()) {
            messagesC.add(Component.text("Not found! " + messageCode));
        }

        return messagesC;
    }

    /**
     * Получает список строковых сообщений.
     */
    public static List<String> getLocaleMessagesS(String locale, String messageCode, HashMap<Integer, String> localeChanges) {
        Map<String, Object> localeMap = getLocaleMap(locale);
        List<String> messages = new ArrayList<>((List<String>) localeMap.getOrDefault(messageCode, Collections.emptyList()));

        for (Map.Entry<Integer, String> entry : localeChanges.entrySet()) {
            int index = entry.getKey();
            if (index >= 0 && index < messages.size()) {
                messages.set(index, String.format(messages.get(index), entry.getValue()));
            }
        }

        for (int i = 0; i < messages.size(); i++) {
            messages.set(i, GlobalListener.parseColors(messages.get(i)));
        }

        if (messages.isEmpty()) {
            messages.add("Not found! " + messageCode);
        }

        return messages;
    }

    /**
     * Получает локаль игрока.
     */
    public static String getLocale(Player player) {
        return (String) DatabaseUtil.getValue("players", "player_locale", "player_name", player.getName());
    }

    /**
     * Устанавливает локаль для пользователя.
     */
    public static void setLocale(User user, String locale) {
        user.setLocale(locale);
        DatabaseUtil.updateValue("players", "player_locale", locale, "player_name", user.name());
    }

    /**
     * Форматирует строку с заменой подстроки.
     */
    public static String formattedString(String s, String change, String changeFor) {
        return StringUtils.replace(s, change, changeFor);
    }
}
