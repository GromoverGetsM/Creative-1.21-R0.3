package ru.rstudios.creative.menu.selector;

import org.bukkit.NamespacedKey;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import ru.rstudios.creative.coding.eventvalues.Value;
import ru.rstudios.creative.coding.eventvalues.ValueType;
import ru.rstudios.creative.menu.ProtectedMenu;
import ru.rstudios.creative.menu.ProtectedMultipages;
import ru.rstudios.creative.user.LocaleManages;
import ru.rstudios.creative.user.User;

import java.util.LinkedList;
import java.util.List;

import static ru.rstudios.creative.CreativePlugin.plugin;

public class ValuesMenu extends ProtectedMenu {
    public ValuesMenu(User user) {
        super(LocaleManages.getLocaleMessage(user.getLocale(), "coding.tech.values", false, ""), (byte) 1);
    }

    @Override
    public void fillItems(User user) {
        List<Value.Category> types = new LinkedList<>(ValueType.getCategories());

        for (int i = 0; i < Math.min(9, types.size()); i++) {
            setItem((byte) i, types.get(i).getIcon(user));
        }
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        User user = User.asUser(event.getWhoClicked());

        if (event.getCurrentItem() != null) {
            event.setCancelled(true);
            Value.Category category = Value.Category.getByMaterial(event.getCurrentItem().getType());

            if (category != null) {
                ValuesMenu.Values menu = new Values(user, category);
                menu.open(user);
            }
        }
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {

    }

    static class Values extends ProtectedMultipages {

        private final Value.Category category;

        public Values(User user, Value.Category category) {
            super(LocaleManages.getLocaleMessage(user.getLocale(), "coding.tech.values", false, ""), user);
            this.category = category;
            this.itemsSlots = new byte[]{10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34};
        }

        @Override
        public List<ItemStack> getMenuElements() {
            List<ValueType> types = ValueType.getByCategory(category);

            return types.stream()
                    .map(type -> type.getIcon(user))
                    .toList();
        }

        @Override
        public void onMenuElementClick(InventoryClickEvent event) {
            ValueType type = ValueType.getByMaterial(event.getCurrentItem().getType());

            if (type != null) {
                user.player().closeInventory();

                ItemStack toUpdate = user.player().getInventory().getItemInMainHand();
                ItemMeta meta = toUpdate.getItemMeta();

                if (meta != null) {
                    meta.setDisplayName(event.getCurrentItem().getItemMeta().getDisplayName());
                    NamespacedKey valueType = new NamespacedKey(plugin, "valueType");
                    meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "variable"), PersistentDataType.BOOLEAN, true);
                    meta.getPersistentDataContainer().set(valueType, PersistentDataType.STRING, type.name().toUpperCase());

                    toUpdate.setItemMeta(meta);
                }

                user.player().setItemInHand(toUpdate);
            }
        }

        @Override
        public void fillOther() {

        }

        @Override
        public void onOpen(InventoryOpenEvent event) {

        }
    }
}
