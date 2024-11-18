package ru.rstudios.creative1.utils;

import org.bukkit.GameRule;

import java.util.regex.Pattern;

public class WorldUtil {

    public static long getLastWorldId() {
        Object result = DatabaseUtil.selectValue("plots", "MAX(id) as max_id");

        if (result != null) {
            return ((Number) result).longValue();
        }

        return 1;
    }
}
