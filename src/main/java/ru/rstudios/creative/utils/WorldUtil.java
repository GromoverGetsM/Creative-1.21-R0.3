package ru.rstudios.creative.utils;

import lombok.SneakyThrows;

import java.util.Collections;
import java.util.List;

public class WorldUtil {

    @SneakyThrows
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
