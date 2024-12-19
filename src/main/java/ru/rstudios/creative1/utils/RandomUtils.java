package ru.rstudios.creative1.utils;

import java.util.List;
import java.util.Random;

public class RandomUtils {

    private static final Random random = new Random();

    public static boolean nextPercent(double percent) {
        return random.nextDouble() * 100 < percent;
    }

    public static int nextInt(int bound) { return random.nextInt(bound); }

    public static <T> T getRandomObject(T[] array) {
        return array[nextInt(array.length)];
    }

    public static <T> T getRandomObject(List<T> list) {
        return list.get(nextInt(list.size()));
    }

    public static int randInt(int min, int max) {
        return random.nextInt(min, max);
    }

}
