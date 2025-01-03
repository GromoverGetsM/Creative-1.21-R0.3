package ru.rstudios.creative1.utils;

import org.bukkit.GameRule;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class WorldUtil {

    public static long getLastWorldId() {
        List<Long> ids = DatabaseUtil.selectAllValues("plots", "id");

        Collections.sort(ids);

        for (long i = 0; i <= ids.size(); i++) {
            if (!ids.contains(i)) {
                return i;
            }
        }

        return ids.size();
    }
}
