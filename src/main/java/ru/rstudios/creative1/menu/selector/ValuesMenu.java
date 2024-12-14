package ru.rstudios.creative1.menu.selector;

import org.bukkit.NamespacedKey;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import ru.rstudios.creative1.coding.eventvalues.Value;
import ru.rstudios.creative1.coding.eventvalues.ValueType;
import ru.rstudios.creative1.menu.ProtectedMenu;
import ru.rstudios.creative1.user.LocaleManages;
import ru.rstudios.creative1.user.User;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import static ru.rstudios.creative1.Creative_1.plugin;

public class ValuesMenu extends ProtectedMenu {

    protected boolean isCategorySelected = false;
    protected Value.Category selectedCategory = null;

    public ValuesMenu(User user) {
        super(LocaleManages.getLocaleMessage(user.getLocale(), "coding.tech.values", false, ""), (byte) 1);
    }

    @Override
    public void fillItems(User user) {
        if (isCategorySelected) fillItemsPage(user);
        else fillCategoryPage(user);
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        User user = User.asUser(event.getWhoClicked());
        event.setCancelled(true);

        if (event.getCurrentItem() != null) {
            if (selectedCategory == null) {
                selectedCategory = Value.Category.getByMaterial(event.getCurrentItem().getType());
                isCategorySelected = true;
                this.items.clear();
                this.setRows((byte) 6);
                user.player().closeInventory();
                open(user);
            } else {
                ValueType type = ValueType.getByMaterial(event.getCurrentItem().getType());

                if (type != null) {
                    user.player().closeInventory();

                    ItemStack toUpdate = user.player().getInventory().getItemInMainHand();
                    ItemMeta meta = toUpdate.getItemMeta();

                    if (meta != null) {
                        meta.setDisplayName(event.getCurrentItem().getItemMeta().getDisplayName());
                        meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "valueType"), PersistentDataType.STRING, type.name().toLowerCase(Locale.ROOT));

                        toUpdate.setItemMeta(meta);
                    }

                    user.player().setItemInHand(toUpdate);
                }
            }
        }
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {

    }

    private void fillCategoryPage (User user) {
        List<Value.Category> types = new LinkedList<>(ValueType.getCategories());

        for (int i = 0; i < Math.min(9, types.size()); i++) {
            setItem((byte) i, types.get(i).getIcon(user));
        }
    }

    private void fillItemsPage (User user) {
        List<ValueType> types = ValueType.getByCategory(selectedCategory);
        List<Byte> slots = new LinkedList<>();
        addRange(slots, (byte) 10, (byte) 16);
        addRange(slots, (byte) 19, (byte) 25);
        addRange(slots, (byte) 28, (byte) 34);
        addRange(slots, (byte) 37, (byte) 43);

        for (int i = 0; i < Math.min(types.size(), slots.size()); i++) {
            setItem(slots.get(i), types.get(i).getIcon(user));
        }
    }

    protected void addRange(List<Byte> list, byte start, byte end) {
        for (byte i = start; i <= end; i++) {
            list.add(i);
        }
    }
}
