package ru.rstudios.creative1.handlers.customevents.main;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import ru.rstudios.creative1.utils.DatabaseUtil;

public class DatabaseUpdateEvent extends Event implements Cancellable {

    public static HandlerList HANDLERS = new HandlerList();
    public boolean isCancelled;

    private final String tableName;
    private final String columnName;
    private final Object newValue;
    private final Object oldValue;
    private final String relatedColumn;
    private final Object relatedValue;

    public DatabaseUpdateEvent (String tableName, String columnName, Object newValue, Object oldValue, String relatedColumn, Object relatedValue) {
        this.tableName = tableName;
        this.columnName = columnName;
        this.newValue = newValue;
        this.oldValue = oldValue;
        this.relatedColumn = relatedColumn;
        this.relatedValue = relatedValue;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.isCancelled = b;

        if (isCancelled) {
            DatabaseUtil.updateValue(tableName, columnName, oldValue, relatedColumn, relatedValue);
        }
    }

    public Object getNewValue() {
        return newValue;
    }

    public Object getOldValue() {
        return oldValue;
    }

    public Object getRelatedValue() {
        return relatedValue;
    }

    public String getColumnName() {
        return columnName;
    }

    public String getRelatedColumn() {
        return relatedColumn;
    }

    public String getTableName() {
        return tableName;
    }
}
