package ru.rstudios.creative1.menu.selector;

import com.jeff_media.morepersistentdatatypes.DataType;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import ru.rstudios.creative1.coding.MenuCategory;
import ru.rstudios.creative1.coding.actions.ActionCategory;
import ru.rstudios.creative1.menu.CodingMenu;
import ru.rstudios.creative1.menu.ProtectedMultipages;
import ru.rstudios.creative1.user.LocaleManages;
import ru.rstudios.creative1.user.User;
import ru.rstudios.creative1.utils.Development;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import static ru.rstudios.creative1.Creative_1.plugin;

public class EntityAction extends CodingCategoriesMenu {
    public EntityAction(User user) {
        super(LocaleManages.getLocaleMessage(user.getLocale(), "coding.entity_action", false, ""), (byte) 6);
    }

    @Override
    public void fillItems(User user) {
        List<MenuCategory> categories = new LinkedList<>(ActionCategory.getMenuCategories(Development.BlockTypes.ENTITY_ACTION));
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
                CodingMultipagesMenu menu = new EntityAction.Actions(user, category, this.sign);
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
            super(LocaleManages.getLocaleMessage(user.getLocale(), "coding.entity_action", false, ""), user);
            this.itemsSlots = new byte[]{10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34};
            this.category = category;
            this.sign = sign;
        }

        @Override
        public List<ItemStack> getMenuElements() {
            List<ActionCategory> actions = ActionCategory.getActionsByCategory(Development.BlockTypes.ENTITY_ACTION, this.category);
            List<ItemStack> icons = new LinkedList<>();
            actions.forEach(actioncategory -> icons.add(actioncategory.getIcon(this.user)));
            return icons;
        }

        @Override
        public void onMenuElementClick(InventoryClickEvent event) {
            ActionCategory category = ActionCategory.get(event.getCurrentItem().getType(), Development.BlockTypes.ENTITY_ACTION, this.category);

            if (category != null) {
                user.player().closeInventory();
                Sign sign = (Sign) this.sign.getState();
                sign.setLine(2, "coding.actions." + category.name().toLowerCase(Locale.ROOT));
                sign.update();
                user.sendTranslatedSign(this.sign.getLocation());

                this.sign.setMetadata("selectedAction", new FixedMetadataValue(plugin, category.name()));

                Block chestBlock = this.sign.getRelative(BlockFace.SOUTH).getRelative(BlockFace.UP);

                if (category.hasChest()) {
                    chestBlock.setType(Material.CHEST);


                    Chest chest = (Chest) chestBlock.getState();
                    CodingMenu menu = category.getCodingMenu();

                    NamespacedKey inventory = new NamespacedKey(plugin, "inventory");

                    chest.getPersistentDataContainer().set(inventory, DataType.ITEM_STACK_ARRAY, menu.getInventory(user).getContents());
                    chest.update();

                } else chestBlock.setType(Material.AIR);
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
