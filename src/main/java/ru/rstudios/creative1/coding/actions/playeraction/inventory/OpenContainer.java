package ru.rstudios.creative1.coding.actions.playeraction.inventory;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import ru.rstudios.creative1.coding.actions.Action;
import ru.rstudios.creative1.coding.actions.ActionCategory;
import ru.rstudios.creative1.coding.actions.ActionChest;
import ru.rstudios.creative1.coding.events.GameEvent;
import ru.rstudios.creative1.menu.SwitchItem;
import ru.rstudios.creative1.utils.Development;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class OpenContainer extends Action {

    private static final Set<Material> INTERFACE_BLOCKS = EnumSet.of(
            Material.ANVIL,
            Material.CHIPPED_ANVIL,
            Material.DAMAGED_ANVIL,
            Material.CRAFTING_TABLE,
            Material.CARTOGRAPHY_TABLE,
            Material.SMITHING_TABLE,
            Material.ENCHANTING_TABLE,
            Material.GRINDSTONE,
            Material.STONECUTTER,
            Material.LOOM,
            Material.FLETCHING_TABLE
    );

    @Override
    public void execute(GameEvent event, List<Entity> selection) {
        ActionChest chest = getChest();
        chest.initInventorySort();

        SwitchItem switchItem = getCategory().getCodingMenu().getSwitches().get(22);
        switchItem.setCurrentState(switchItem.getCurrentState(chest.getOriginalContents()[22]));

        for (Entity entity : selection) {
            if (!Development.checkPlot(entity, event.getPlot())) continue;

            if (entity instanceof Player player) {
                Location test = chest.parseLocationPlus(chest.getOriginalContents()[13], event.getPlot().world().getSpawnLocation(), event, entity);
                Block b = test.getBlock();

                if (b.getState() instanceof Container container) {
                    Inventory orig = container.getInventory();

                    switch (switchItem.getCurrentValue()) {
                        case "orig" -> player.openInventory(orig);
                        case "copy" -> {
                            Component copiedTitle = container.customName() == null ? orig.getType().defaultTitle() : container.customName();
                            Inventory copy = Bukkit.createInventory(orig.getHolder(), orig.getSize(), copiedTitle);
                            ItemStack[] copiedContents = Arrays.copyOf(orig.getContents(), orig.getContents().length);

                            copy.setContents(copiedContents);
                            player.openInventory(copy);
                        }
                    }
                } else {
                    if (INTERFACE_BLOCKS.contains(b.getType())) {
                        event.getPlot().throwException(this, new UnsupportedOperationException("Попытка вызова открытия инвентаря на блок интерфейса. Используйте для этого действие ДействиеИгрока#ОткрытьИнтерфейс"));
                    } else event.getPlot().throwException(this, new UnsupportedOperationException("Попытка вызова открытия инвентаря на блок без инвентаря! На местоположении X=" + test.getX() + ", Y=" + test.getY() +
                            ", Z=" + test.getZ() + " обнаружен блок " + b.getType()));
                }
            }
        }
    }

    @Override
    public ActionCategory getCategory() {
        return ActionCategory.OPEN_CONTAINER;
    }
}
