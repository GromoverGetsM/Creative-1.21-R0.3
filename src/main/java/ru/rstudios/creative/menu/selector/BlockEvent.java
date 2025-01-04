package ru.rstudios.creative.menu.selector;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import ru.rstudios.creative.coding.MenuCategory;
import ru.rstudios.creative.coding.starters.StarterCategory;
import ru.rstudios.creative.user.LocaleManages;
import ru.rstudios.creative.user.User;
import ru.rstudios.creative.utils.Development;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import static ru.rstudios.creative.CreativePlugin.plugin;

public class BlockEvent extends CodingCategoriesMenu {

    public BlockEvent(User user) {
        super(LocaleManages.getLocaleMessage(user.getLocale(), "coding.block_event", false, ""), (byte) 6);
    }

    @Override
    public void fillItems(User user) {
        List<MenuCategory> categories = new LinkedList<>(StarterCategory.getMenusCategories(Development.BlockTypes.BLOCK_EVENT));
        List<Byte> slots = Arrays.asList((byte) 10, (byte) 13, (byte) 16, (byte) 37, (byte) 40, (byte) 43);

        for (int i = 0; i < Math.min(8, categories.size()); i++) {
            setItem(slots.get(i), categories.get(i).getItem(user));
        }
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        User user = User.asUser(event.getWhoClicked());

        if (event.getCurrentItem() != null) {
            event.setCancelled(true);
            MenuCategory category = MenuCategory.getByMaterial(event.getCurrentItem().getType());

            if (category != null) {
                CodingMultipagesMenu menu = new BlockEvent.Actions(user, category, this.sign);
                menu.setSign(this.sign);
                menu.open(user);
            }
        }
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {

    }

    static class Actions extends CodingMultipagesMenu {

        private final MenuCategory category;
        private final Block sign;

        public Actions(User user, MenuCategory category, Block sign) {
            super(LocaleManages.getLocaleMessage(user.getLocale(), "coding.block_event", false, ""), user);
            this.itemsSlots = new byte[]{10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34};
            this.category = category;
            this.sign = sign;
        }

        @Override
        public List<ItemStack> getMenuElements() {
            List<StarterCategory> actions = StarterCategory.getStartersByCategory(Development.BlockTypes.BLOCK_EVENT, this.category);
            List<ItemStack> icons = new LinkedList<>();
            actions.forEach(actioncategory -> icons.add(actioncategory.getIcon(this.user)));
            return icons;
        }

        @Override
        public void onMenuElementClick(InventoryClickEvent event) {
            StarterCategory category = StarterCategory.get(event.getCurrentItem().getType(), Development.BlockTypes.BLOCK_EVENT, this.category);

            if (category != null) {
                user.player().closeInventory();
                Sign sign = (Sign) this.sign.getState();
                sign.setLine(2, "coding.events." + category.name().toLowerCase(Locale.ROOT));
                sign.update();
                user.sendTranslatedSign(this.sign.getLocation());

                this.sign.setMetadata("selectedAction", new FixedMetadataValue(plugin, category.name()));
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
