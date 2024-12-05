package ru.rstudios.creative1.coding.actions;

import com.jeff_media.morepersistentdatatypes.DataType;
import org.apache.commons.lang3.ArrayUtils;
import org.bukkit.*;
import org.jetbrains.annotations.Nullable;
import ru.rstudios.creative1.coding.events.GameEvent;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import ru.rstudios.creative1.coding.starters.Starter;
import ru.rstudios.creative1.plots.PlotManager;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import static ru.rstudios.creative1.Creative_1.plugin;

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

    public static final Pattern NUMBER = Pattern.compile("-?[0-9]+\\.?[0-9]*");

    public ActionChest (Action linkedAction, Block chestBlock) {
        this.linkedAction = linkedAction;
        this.chestBlock = chestBlock;
        this.chest = (Chest) chestBlock.getState();
    }

    public static boolean isNullOrAir(ItemStack item) {
        return item == null || item.getType() == Material.AIR;
    }

    public void initInventorySort() {
        this.originalContents = this.chest.getPersistentDataContainer().get(new NamespacedKey(plugin, "inventory"), DataType.ITEM_STACK_ARRAY);
        this.nonNullItems = Arrays.stream(this.originalContents)
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
        for (ItemStack item : this.nonNullItems) {
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
                    list.add(parseText(item));
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

    public List<Double> getAsNumerics() {
        List<Double> numerics = new LinkedList<>();

        for (ItemStack item : this.numbers) {
            numerics.add(parseNumber(item));
        }

        return numerics;
    }

    public static Object parseItem (ItemStack item, @Nullable GameEvent event, @Nullable Entity entity, @Nullable Starter starter) {
        if (item == null) {
            return null;
        }

        switch (item.getType()) {
            case BOOK -> {
                return parseText(item);
            }
            case SLIME_BALL -> {
                return parseNumber(item);
            }
            case PAPER -> {
                return parseLocation(item, event.getPlot().world().getSpawnLocation());
            }
            /*case APPLE -> {
                return parseGameValue(item, event);
            }
            case MAGMA_CREAM -> {
                return parseDynamicVariable(item, "", true, event, starter);
            }*/
        }
        return null;
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

    public static String parseText (ItemStack item) {
        return parseText(item, "");
    }

    public static String parseText (ItemStack item, String defaultText) {
        return parseText(item, defaultText, true);
    }

    public static String parseText (ItemStack item, String defaultText, boolean checkTypeMatches) {
        if (item == null) {
            return defaultText;
        }
        if (checkTypeMatches && item.getType() != Material.BOOK) {
            return defaultText;
        } else {
            if (item.getItemMeta().hasDisplayName()) {
                String text = item.getItemMeta().getDisplayName();
                if (text.length() > 256) {
                    text = text.substring(0, 1024);
                }
                return text;
            } else {
                return defaultText;
            }
        }
    }

    public static double parseNumber (ItemStack item) {
        return parseNumber(item, 0.0);
    }

    public static double parseNumber (ItemStack item, double defaultNum) {
        return parseNumber(item, defaultNum, true);
    }

    public static double parseNumber (ItemStack item, double defaultNum, boolean checkTypeMatches) {
        if (item == null) {
            return defaultNum;
        }

        if (checkTypeMatches && item.getType() != Material.SLIME_BALL) {
            return defaultNum;
        } else {
            if (item.getItemMeta().hasDisplayName()) {
                String num = ChatColor.stripColor(item.getItemMeta().getDisplayName()).trim();
                return NUMBER.matcher(num).matches() ? Double.parseDouble(num) : defaultNum;
            } else {
                return defaultNum;
            }
        }
    }

    public static Location parseLocation(ItemStack itemStack, Location def) {
        return parseLocation(itemStack, def, true);
    }

    public static Location parseLocation(ItemStack itemStack, Location def, boolean checkType) {
        if (!isNullOrAir(itemStack)) {
            if (checkType && itemStack.getType() != Material.PAPER) {
                return def;
            } else {
                String text = ChatColor.stripColor(parseText(itemStack, "", false));
                Location parsedLoc = toLocation(text);
                if (parsedLoc != null) {
                    return parsedLoc;
                } else {
                    String[] split = text.split(" ");
                    double[] numbers = new double[split.length];

                    for(int i = 0; i < split.length; ++i) {
                        if (!NUMBER.matcher(split[i]).matches()) {
                            return def;
                        }

                        numbers[i] = Double.parseDouble(split[i]);
                    }

                    return new Location(PlotManager.byWorld(def.getWorld()).world(), numbers[0], numbers[1], numbers[2], (float)numbers[3], (float)numbers[4]);
                }
            }
        } else {
            return def;
        }
    }

    public static Location fixNan(Location loc) {
        if (Double.isNaN(loc.getX())) {
            loc.setX(0.0);
        }

        if (Double.isNaN(loc.getY())) {
            loc.setY(0.0);
        }

        if (Double.isNaN(loc.getZ())) {
            loc.setZ(0.0);
        }

        if (Float.isNaN(loc.getYaw())) {
            loc.setYaw(0.0F);
        }

        if (Float.isNaN(loc.getPitch())) {
            loc.setPitch(0.0F);
        }

        return loc;
    }

    public static Location toLocation(String code) {
        if (code != null && !code.isEmpty()) {
            String[] loc = code.split(":");
            if (loc.length == 3) {
                return fixNan(new Location(Bukkit.getWorlds().get(0), Double.parseDouble(loc[0]), Double.parseDouble(loc[1]), Double.parseDouble(loc[2])));
            } else if (loc.length == 4) {
                return fixNan(new Location(Bukkit.getWorld(loc[0]), Double.parseDouble(loc[1]), Double.parseDouble(loc[2]), Double.parseDouble(loc[3])));
            } else {
                return loc.length == 6 ? fixNan(new Location(Bukkit.getWorld(loc[0]), Double.parseDouble(loc[1]), Double.parseDouble(loc[2]), Double.parseDouble(loc[3]), Float.parseFloat(loc[4]), Float.parseFloat(loc[5]))) : null;
            }
        } else {
            return null;
        }
    }
}
