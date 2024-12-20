package ru.rstudios.creative1.coding.supervariables;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import ru.rstudios.creative1.plots.Plot;

import java.io.Serial;
import java.io.Serializable;

public class DynamicVariable implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String name;
    private boolean isSaved;
    private Object value;

    public DynamicVariable (@NotNull String name) {
        this(name, null);
    }

    public DynamicVariable (@NotNull String name, Object value) {
        this(name, false, value);
    }

    public DynamicVariable (@NotNull String name, boolean isSaved) {
        this(name, isSaved, null);
    }

    public DynamicVariable (@NotNull String name, boolean isSaved, Object value) {
        this.name = name;
        this.isSaved = isSaved;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public boolean isSaved() {
        return isSaved;
    }

    public void setSaved(boolean saved) {
        isSaved = saved;
    }

    public Object getValue(Plot plot) {
        DynamicVariable var = plot.handler.getDynamicVariables().get(this.getName());
        return var != null ? var.value : null;
    }

    public void setName (String name) {
        this.name = cutManySymbols(name);
    }

    public void setValue (Plot plot, Object value) {
        this.setValue(plot, value, this.isSaved);
    }
    public void setValue (Plot plot, Object value, boolean saved) {
        if (value instanceof String) {
            value = cutManySymbols(value.toString());
        }

        this.value = value;
        this.isSaved = saved;
        DynamicVariable newVariable = new DynamicVariable(name, saved, value);
        plot.handler.putDynamicVariable(this.name, newVariable);
    }

    @Override
    public String toString() {
        return "DynamicVariable{name='" + name + "', saved=" + isSaved + ", value=" + value + "}";
    }

    public static String cutManySymbols (String s) {
        return s.length() > 1024 ? s.substring(0, 1024) : s;
    }

    public static boolean isVarSaved (ItemStack item) {
        return item.getItemMeta().getLore().get(0).contains("СОХРАНЕНО") || item.getItemMeta().getLore().get(0).contains("SAVED");
    }
}
