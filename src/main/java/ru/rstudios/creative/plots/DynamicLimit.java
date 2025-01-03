package ru.rstudios.creative.plots;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DynamicLimit {

    public String name;
    public int value;
    public int perPlayerModifier;

    public DynamicLimit (String name) {
        this(name, 0);
    }

    public DynamicLimit (String name, int value) {
        this(name, value, 1);
    }

    public DynamicLimit (String name, int value, int perPlayerModifier) {
        this.perPlayerModifier = perPlayerModifier;
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public int getPerPlayerModifier() {
        return perPlayerModifier;
    }

    public int getValue() {
        return value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setPerPlayerModifier(int perPlayerModifier) {
        this.perPlayerModifier = perPlayerModifier;
    }

    public int calculateLimit (Plot plot) {
        return value + (name.equalsIgnoreCase("variables") ? plot.uniquePlayers.size() : plot.online().size()) * perPlayerModifier;
    }

    @Override
    public String toString() {
        return "DynamicLimit{name='" + name + "', value=" + value + ", perPlayerModifier=" + perPlayerModifier + "}";
    }

    public static DynamicLimit valueOf (String s) {
        Pattern pattern = Pattern.compile("name='(.*?)', value=(.*?), perPlayerModifier=(.*?)}");
        Matcher matcher = pattern.matcher(s);

        String name = "";
        int value = 0;
        int perPlayerModifier = 1;

        if (matcher.find()) {
            name = matcher.group(1);
            value = Integer.parseInt(matcher.group(2));
            perPlayerModifier = Integer.parseInt(matcher.group(3));
        }

        return new DynamicLimit(name, value, perPlayerModifier);
    }

}
