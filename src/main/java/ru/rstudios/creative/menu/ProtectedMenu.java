package ru.rstudios.creative.menu;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import ru.rstudios.creative.user.User;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class ProtectedMenu implements InventoryHolder {

    private String title;
    private byte rows;
    protected Map<Byte, ItemStack> items = new LinkedHashMap<>();
    protected Inventory inventory;

    protected final static ItemStack AIR = new ItemStack(Material.AIR, 0);
    protected final static ItemStack UNFILLED_FIELD = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS, 1);
    protected final static ItemStack DECORATION_ITEM = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);

    public ProtectedMenu (String title, byte rows) {
        this.title = title;
        this.rows = rows;
    }

    public boolean addItem (ItemStack item) {
        for (byte i = 0; i < rows*9; i++) {
            if (items.get(i) == null) {
                items.put(i,item);
                return true;
            }
        }
        return false;
    }

    public void setItem (byte slot, ItemStack item) {
        if (item == null) item = new ItemStack(Material.AIR);
        if (!(slot >= rows*9) && !(slot<0)) {
            items.put(slot,item);
        }
    }

    public void updateSlot (byte slot) {
        if (inventory != null && slot < rows*9 && slot >= 0) {
            inventory.setItem(slot,items.get(slot));
        }
    }

    public ItemStack getItem (byte slot) {
        if (slot < 0 || slot >= getItems().size()) return new ItemStack(Material.AIR);
        return getItems().get(slot);
    }

    public void setItems (Map<Byte, ItemStack> items) {
        this.items = items;
    }

    public List<ItemStack> getItems() {
        return new ArrayList<>(items.values());
    }

    public void setTitle (String title) {
        this.title = title;
    }

    public @NotNull Inventory getInventory() {
        if (rows > 6 || rows < 1) rows = 6;
        Inventory inventory = Bukkit.createInventory(this, this.rows * 9, Component.text(this.title));
        for (Map.Entry<Byte,ItemStack> item : items.entrySet()) {
            inventory.setItem(item.getKey(),item.getValue());
        }
        return inventory;
    }

    public @NotNull Inventory getCurrentInventory() {
        if (inventory == null) {
            return getInventory();
        }
        return inventory;
    }

    public void open (User user) {
        ProtectedManager.addMenu(this);
        try {
            fillItems(user);
            inventory = getInventory();
            user.player().openInventory(inventory);
        } catch (Exception e) {
            e.printStackTrace();
            user.sendMessage("errors.unknown", true, "");
        }

    }

    public abstract void fillItems (User user);
    public abstract void onClick (InventoryClickEvent event);
    public abstract void onOpen (InventoryOpenEvent event);

    public void onClose (InventoryCloseEvent event) {
        destroy();
    }

    protected final boolean isClickedInMenuSlots (InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return false;
        if (event.getInventory().getHolder() == null) return false;
        return event.getInventory().getHolder().equals(event.getClickedInventory().getHolder());
    }

    protected final boolean isPlayerClicked (InventoryClickEvent event) {
        return (event.getWhoClicked() instanceof Player);
    }

    protected void addRange(List<Byte> list, byte start, byte end) {
        for (byte i = start; i <= end; i++) {
            list.add(i);
        }
    }


    public byte getSize() {
        return (byte) (rows*9);
    }

    public byte getRows() {
        return rows;
    }

    protected void setRows (byte rows) {
        this.rows = rows;
    }


    public static boolean isNullOrAir (ItemStack item) {
        return item == null || item.getType() == Material.AIR;
    }
    public void destroy() {
        ProtectedManager.removeMenu(this);
    }
}
