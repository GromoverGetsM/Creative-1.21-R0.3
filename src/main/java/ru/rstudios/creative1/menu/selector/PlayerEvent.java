package ru.rstudios.creative1.menu.selector;

import org.bukkit.block.Sign;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.metadata.FixedMetadataValue;
import ru.rstudios.creative1.coding.MenuCategory;
import ru.rstudios.creative1.coding.starters.StarterCategory;
import ru.rstudios.creative1.user.LocaleManages;
import ru.rstudios.creative1.user.User;
import ru.rstudios.creative1.utils.Development;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import static ru.rstudios.creative1.Creative_1.plugin;

public class PlayerEvent extends CodingCategoriesMenu {

    public PlayerEvent(User user) {
        super(LocaleManages.getLocaleMessage(user.getLocale(), "coding.player_event", false, ""), (byte) 6);
    }

    @Override
    public void fillCategoryPage(User user) {
        List<MenuCategory> categories = new LinkedList<>(StarterCategory.getMenusCategories(Development.BlockTypes.PLAYER_EVENT));
        List<Byte> slots = Arrays.asList((byte) 10, (byte) 13, (byte) 16, (byte) 37, (byte) 40, (byte) 43);

        for (int i = 0; i < Math.min(7, categories.size()); i++) {
            setItem(slots.get(i), categories.get(i).getItem(user));
        }
    }

    @Override
    public void fillItemsPage(User user) {
        List<StarterCategory> starters = StarterCategory.getStartersByCategory(Development.BlockTypes.PLAYER_EVENT, this.selectedCategory);
        List<Byte> slots = new LinkedList<>();
        addRange(slots, (byte) 10, (byte) 16);
        addRange(slots, (byte) 19, (byte) 25);
        addRange(slots, (byte) 28, (byte) 34);
        addRange(slots, (byte) 37, (byte) 43);

        for (int i = 0; i < Math.min(starters.size(), slots.size()); i++) {
            setItem(slots.get(i), starters.get(i).getIcon(user));
            updateSlot(slots.get(i));
        }
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        User user = User.asUser(event.getWhoClicked());

        if (event.getCurrentItem() != null) {
            if (selectedCategory == null) {
                selectedCategory = MenuCategory.getByMaterial(event.getCurrentItem().getType());
                isCategorySelected = true;
                this.items.clear();
                user.player().closeInventory();
                open(user);
            } else {
                StarterCategory category = StarterCategory.getByMaterial(event.getCurrentItem().getType());

                if (category != null) {
                    user.player().closeInventory();
                    Sign sign = (Sign) this.sign.getState();
                    sign.setLine(2, "coding.events." + category.name().toLowerCase(Locale.ROOT));
                    sign.update();
                    user.sendTranslatedSign(this.sign.getLocation());

                    this.sign.setMetadata("selectedStarter", new FixedMetadataValue(plugin, category.name()));
                }
            }
        }
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {

    }
}
