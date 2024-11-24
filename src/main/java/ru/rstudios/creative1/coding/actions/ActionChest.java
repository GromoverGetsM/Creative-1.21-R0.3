package ru.rstudios.creative1.coding.actions;

import org.apache.commons.lang3.ArrayUtils;
import org.bukkit.ChatColor;
import org.bukkit.GameEvent;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ActionChest {

    private Action linkedAction;
    private Block chestBlock;
    private Chest chest;

    private ItemStack[] originalContents;
    private ItemStack[] nonNullItems;
    private ItemStack[] texts;
    private ItemStack[] numbers;
    private ItemStack[] locations;
    private ItemStack[] dynamicVariables;
    private ItemStack[] itemStackGameValues;

    public ActionChest (Action linkedAction, Block chestBlock) {
        this.linkedAction = linkedAction;
        this.chestBlock = chestBlock;
        this.chest = (Chest) chestBlock.getState();
    }

    public static boolean isNullOrAir(ItemStack item) {
        return item == null || item.getType() == Material.AIR;
    }

    public void initInventorySort() {
        this.originalContents = this.chest.getInventory().getContents();
        this.nonNullItems = Arrays.stream(this.chest.getInventory().getContents())
                .filter(item -> item != null && item.getType() != Material.AIR)
                .toArray(ItemStack[]::new);
        this.texts = new ItemStack[0];
        this.numbers = new ItemStack[0];
        this.locations = new ItemStack[0];
        this.dynamicVariables = new ItemStack[0];
        this.itemStackGameValues = new ItemStack[0];
        sort();
    }

    private void sort() {
        for (int i = 0; i < this.nonNullItems.length; i++) {
            ItemStack item = this.nonNullItems[i];

            if (!isNullOrAir(item)) {
                switch (item.getType()) {
                    case BOOK -> this.texts = ArrayUtils.add(this.texts, item);
                    case SLIME_BALL -> this.numbers = ArrayUtils.add(this.numbers, item);
                    case PAPER -> this.locations = ArrayUtils.add(this.locations, item);
                    /*case APPLE -> {
                        Object o = CodingHandleUtils.parseGameValue(item);
                        if (o instanceof StringValue) {
                            this.texts = ArrayUtils.add(this.texts, item);
                        }
                        if (o instanceof NumericValue) {
                            this.numbers = ArrayUtils.add(this.numbers, item);
                        }
                        if (o instanceof LocationValue) {
                            this.locations = ArrayUtils.add(this.locations, item);
                        }
                        if (o instanceof ItemStackValue) {
                            this.itemStackGameValues = ArrayUtils.add(this.itemStackGameValues, item);
                        }
                    }*/
                    case MAGMA_CREAM -> {
                        this.dynamicVariables = ArrayUtils.add(this.dynamicVariables, item);
                        this.texts = ArrayUtils.add(this.texts, item);
                        this.numbers = ArrayUtils.add(this.numbers, item);
                        this.locations = ArrayUtils.add(this.locations, item);
                    }
                }
            }
        }
    }

    public ItemStack[] getOriginalContents() {
        return this.originalContents;
    }

    public ItemStack[] getNonNullItems() {
        return this.nonNullItems;
    }

    public ItemStack[] getTexts() {
        return this.texts;
    }

    public ItemStack[] getNumbers() {
        return this.numbers;
    }

    public ItemStack[] getLocations() {
        return this.locations;
    }

    public ItemStack[] getItemStackGameValues() {
        return this.itemStackGameValues;
    }

    public ItemStack[] getDynamicVariables() {
        return this.dynamicVariables;
    }

    public List<String> getAsTexts(GameEvent event, Entity entity) {
        List<String> list = new LinkedList<>();

        for (ItemStack item : this.texts) {
            switch (item.getType()) {
                case BOOK -> {
                    if (item.getItemMeta().hasDisplayName()) {
                        list.add(item.getItemMeta().getDisplayName());
                    } else {
                        list.add(item.getI18NDisplayName());
                    }
                }
                /*case APPLE -> {
                    Object o = CodingHandleUtils.parseGameValue(item);
                    if (o instanceof StringValue) {
                        list.add(((StringValue) o).get(event, entity));
                    }
                }
                case MAGMA_CREAM -> {
                    if (item.getItemMeta().hasDisplayName()) {
                        String displayName = item.getItemMeta().getDisplayName();
                        displayName = this.replacePlaceholders(displayName, event, entity);

                        list.add(new DynamicVariable(ChatColor.stripColor(displayName)).getValue(event.getPlot()) == null ? "" : new DynamicVariable(ChatColor.stripColor(displayName)).getValue(event.getPlot()).toString());
                    }
                }*/
            }
        }

        return list;
    }

    public Action getLinkedAction() {
        return linkedAction;
    }

    public void setLinkedAction(Action linkedAction) {
        this.linkedAction = linkedAction;
    }

    public Block getChestBlock() {
        return chestBlock;
    }

    public void setChestBlock(Block chestBlock) {
        this.chestBlock = chestBlock;
    }

    public Chest getChest() {
        return chest;
    }

    public void setChest(Chest chest) {
        this.chest = chest;
    }
}
