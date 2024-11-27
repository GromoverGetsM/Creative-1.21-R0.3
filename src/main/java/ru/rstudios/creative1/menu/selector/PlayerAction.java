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
import org.bukkit.metadata.FixedMetadataValue;
import ru.rstudios.creative1.coding.MenuCategory;
import ru.rstudios.creative1.coding.actions.ActionCategory;
import ru.rstudios.creative1.menu.CodingMenu;
import ru.rstudios.creative1.user.LocaleManages;
import ru.rstudios.creative1.user.User;
import ru.rstudios.creative1.utils.Development;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import static ru.rstudios.creative1.Creative_1.plugin;

public class PlayerAction extends CodingCategoriesMenu {
    public PlayerAction(User user) {
        super(LocaleManages.getLocaleMessage(user.getLocale(), "coding.player_action.name", false, ""), (byte) 6);
    }

    @Override
    public void fillCategoryPage(User user) {
        List<MenuCategory> categories = new LinkedList<>(ActionCategory.getMenuCategories(Development.BlockTypes.PLAYER_ACTION));
        List<Byte> slots = Arrays.asList((byte) 10, (byte) 13, (byte) 16, (byte) 37, (byte) 40, (byte) 43);

        for (int i = 0; i < Math.min(8, categories.size()); i++) {
            setItem(slots.get(i), categories.get(i).getItem(user));
        }
    }

    @Override
    public void fillItemsPage(User user) {
        List<ActionCategory> actions = ActionCategory.getActionsByCategory(Development.BlockTypes.PLAYER_ACTION, this.selectedCategory);
        List<Byte> slots = new LinkedList<>();
        addRange(slots, (byte) 10, (byte) 16);
        addRange(slots, (byte) 19, (byte) 25);
        addRange(slots, (byte) 28, (byte) 34);
        addRange(slots, (byte) 37, (byte) 43);

        for (int i = 0; i < Math.min(actions.size(), slots.size()); i++) {
            setItem(slots.get(i), actions.get(i).getIcon(user));
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
                this.getItems().clear();
                user.player().closeInventory();
                open(user);
            } else {
                ActionCategory category = ActionCategory.getByMaterial(event.getCurrentItem().getType());

                if (category != null) {
                    user.player().closeInventory();
                    Sign sign = (Sign) this.sign.getState();
                    sign.setLine(2, "coding.actions." + category.name().toLowerCase(Locale.ROOT) + ".name");
                    sign.update();
                    user.sendTranslatedSign(this.sign.getLocation());

                    this.sign.setMetadata("selectedAction", new FixedMetadataValue(plugin, category.name()));

                    if (category.hasChest()) {
                        Block chestBlock = this.sign.getRelative(BlockFace.SOUTH).getRelative(BlockFace.UP);
                        chestBlock.setType(Material.CHEST);


                        Chest chest = (Chest) chestBlock.getState();
                        CodingMenu menu = category.getCodingMenu();

                        NamespacedKey inventory = new NamespacedKey(plugin, "inventory");

                        chest.getPersistentDataContainer().set(inventory, DataType.ITEM_STACK_ARRAY, menu.getInventory(user).getContents());
                        chest.update();

                    }
                }
            }
        }
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {

    }
}
