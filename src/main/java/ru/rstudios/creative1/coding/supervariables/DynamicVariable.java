package ru.rstudios.creative1.coding.supervariables;

import ru.rstudios.creative1.plots.Plot;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DynamicVariable {

    private String name;
    private Object value;
    private boolean isSaved;

    public DynamicVariable (String name) {
        this(name, null);
    }

    public DynamicVariable (String name, Object value) {
        this(name, value, false);
    }

    public DynamicVariable (String name, Object value, boolean isSaved) {
        if (value instanceof String) {
            value = cutManySymbols(value.toString());
        }

        name = cutManySymbols(name);
        this.name = name;
        this.value = value;
        this.isSaved = isSaved;
    }

    public String getName() {
        return this.name;
    }

    public Object getValue(Plot plot) {
        DynamicVariable var = plot.handler.getDynamicVariables().get(this.getName());
        return var != null ? var.value : null;
    }

    public boolean isSaved() {
        return this.isSaved;
    }

    public void setName (String name) {
        this.name = cutManySymbols(name);
    }

    public void setValue (Plot plot, Object value) {
        this.setValue(plot, value, this.isSaved);
    }
    public void setValue (Plot plot, Object value, boolean isSaved) {
        if (value instanceof String) {
            value = cutManySymbols(value.toString());
        }

        this.value = value;
        this.isSaved = isSaved;
        plot.handler.getDynamicVariables().put(this.getName(), new DynamicVariable(this.getName(), value, isSaved));
    }

    public void setSaved (boolean isSaved) {
        this.isSaved = isSaved;
    }

    public String toString() {
        return "DynamicVariable{name='" + this.name + "', value=" + this.value + ", isSaved=" + this.isSaved + "}";
    }

    public static DynamicVariable valueOf (String s) {
        Pattern pattern = Pattern.compile("name='(.*?)', value=(.*?), isSaved=(.*?)}");
        Matcher matcher = pattern.matcher(s);

        String name = null;
        Object value = null;
        boolean isSaved = false;

        if (matcher.find()) {
            name = matcher.group(1);
            value = matcher.group(2);
            String isSavedStr = matcher.group(3);

            isSaved = Boolean.parseBoolean(isSavedStr);
        }

        return new DynamicVariable(name, value, isSaved);
    }

    public static String cutManySymbols (String s) {
        return s.length() > 1024 ? s.substring(0, 1024) : s;
    }
}