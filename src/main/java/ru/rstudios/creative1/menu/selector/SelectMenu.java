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
import ru.rstudios.creative1.user.LocaleManages;
import ru.rstudios.creative1.user.User;
import ru.rstudios.creative1.utils.Development;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import static ru.rstudios.creative1.Creative_1.plugin;

public class SelectMenu extends CodingMultipagesMenu {

    private final MenuCategory category;

    public SelectMenu(User user) {
        super(LocaleManages.getLocaleMessage(user.getLocale(), "coding.select", false, ""), user);
        this.itemsSlots = new byte[]{10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34};
        this.category = MenuCategory.OTHER;
    }

    @Override
    public List<ItemStack> getMenuElements() {
        List<ActionCategory> actions = ActionCategory.getActionsByCategory(Development.BlockTypes.SELECT, this.category);
        List<ItemStack> icons = new LinkedList<>();
        actions.forEach(actioncategory -> icons.add(actioncategory.getIcon(this.user)));
        return icons;
    }

    @Override
    public void onMenuElementClick(InventoryClickEvent event) {
        event.setCancelled(true);
        boolean isFinalCategory = !List.of(14, -2).contains(event.getSlot());

        ActionCategory category = ActionCategory.get(event.getCurrentItem().getType(), Development.BlockTypes.SELECT, this.category);

        if (category != null) {
            if (isFinalCategory) user.player().closeInventory();
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

        if (!isFinalCategory) {
            switch (event.getSlot()) {
                case 14 -> new PlayerConditions(user).open(user);
            }
        }
    }

    @Override
    public void fillOther() {

    }

    @Override
    public void onOpen(InventoryOpenEvent event) {

    }
}