package ru.rstudios.creative.coding.actions;

import com.jeff_media.morepersistentdatatypes.DataType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.ArrayUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;
import ru.rstudios.creative.coding.events.GameEvent;
import ru.rstudios.creative.coding.eventvalues.ValueType;
import ru.rstudios.creative.coding.starters.Starter;
import ru.rstudios.creative.coding.supervariables.DynamicVariable;
import ru.rstudios.creative.handlers.GlobalListener;
import ru.rstudios.creative.plots.Plot;
import ru.rstudios.creative.plots.PlotManager;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import static ru.rstudios.creative.CreativePlugin.plugin;

@Data
public class ActionChest {

    private static Starter starter;
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
    private ItemStack[] vectors;

    public final static Pattern NUMBER = Pattern.compile("-?[0-9]+(\\.[0-9]*)?([eE][+-]?[0-9]+)?");

    public ActionChest (Action linkedAction, Block chestBlock) {
        this.linkedAction = linkedAction;
        this.chestBlock = chestBlock;
        this.chest = (Chest) chestBlock.getState();
    }

    /**
     * Метод для проверки на нулевой предмет
     * @param item предмет для проверки
     * @return Возвращает true если предмет пустой (null или материал предмета - воздух)
     */
    public static boolean isNullOrAir(ItemStack item) {
        return item == null || item.getType() == Material.AIR;
    }

    /**
     * Инициализация сортировки инвентаря действия
     */
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
        this.vectors = new ItemStack[0];
        sort();
    }

    private void sort() {
        for (ItemStack item : this.nonNullItems) {
            if (!isNullOrAir(item)) {
                switch (item.getType()) {
                    case BOOK -> this.texts = ArrayUtils.add(this.texts, item);
                    case SLIME_BALL -> this.numbers = ArrayUtils.add(this.numbers, item);
                    case PAPER -> this.locations = ArrayUtils.add(this.locations, item);
                    case APPLE -> {
                        ValueType type = parseGameValue(item);
                        String superclass = type.getValueInstance().getClass().getSuperclass().getSimpleName().toLowerCase();
                        this.texts = ArrayUtils.add(this.texts, item);

                        switch (superclass) {
                            case "numericvalue" -> this.numbers = ArrayUtils.add(this.numbers, item);
                            case "locationvalue" -> this.locations = ArrayUtils.add(this.locations, item);
                            case "itemstackvalue" -> this.itemStackGameValues = ArrayUtils.add(this.itemStackGameValues, item);
                            case "vectorvalue" -> this.vectors = ArrayUtils.add(this.vectors, item);
                        }
                    }
                    case MAGMA_CREAM -> {
                        this.dynamicVariables = ArrayUtils.add(this.dynamicVariables, item);
                        this.texts = ArrayUtils.add(this.texts, item);
                        this.numbers = ArrayUtils.add(this.numbers, item);
                        this.locations = ArrayUtils.add(this.locations, item);
                        this.vectors = ArrayUtils.add(this.vectors, item);
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
                case BOOK -> list.add(Action.replacePlaceholders(parseText(item), event, entity));
                case APPLE -> {
                    ValueType type = parseGameValue(item);
                    if (type.getValueInstance() != null) {
                    list.add(Action.replacePlaceholders(String.valueOf(type.getValueInstance().get(event, entity)), event, entity));
                    }
                }
                case MAGMA_CREAM -> {
                    if (item.getItemMeta().hasDisplayName()) {
                        String displayName = item.getItemMeta().getDisplayName();
                        displayName = Action.replacePlaceholders(displayName, event, entity);

                        list.add(new DynamicVariable(ChatColor.stripColor(displayName)).getValue(event.getPlot()) == null ? "" : String.valueOf(new DynamicVariable(ChatColor.stripColor(displayName)).getValue(event.getPlot())));
                    }
                }
            }
        }

        return list;
    }

    public List<String> getAsTexts (GameEvent event, Entity entity, int from, int to) {
        List<String> list = new LinkedList<>();
        ItemStack[] args = Arrays.copyOfRange(originalContents, from, to);

        for (ItemStack item : args) {
            if (!isNullOrAir(item)) {
                list.add(parseTextPlus(item, "", event, entity));
            }
        }

        return list;
    }

    public List<ItemStack> getAsItemStacks (GameEvent event, Entity entity, int from, int to) {
        List<ItemStack> items = new LinkedList<>();
        ItemStack[] args = Arrays.copyOfRange(originalContents, from, to);

        for (ItemStack item : args) {
            if (!isNullOrAir(item)) {
                Object o = parseItemArgument(item, event, entity);

                if (o instanceof ItemStack itemStack) {
                    items.add(itemStack);
                }
            }
        }

        return items;
    }

    public List<Double> getAsNumbers (GameEvent event, Entity entity, int from, int to) {
        List<Double> items = new LinkedList<>();
        ItemStack[] args = Arrays.copyOfRange(originalContents, from, to);

        for (ItemStack item : args) {
            if (!isNullOrAir(item)) {
                items.add(parseNumberPlus(item, 1.0, event, entity));
            }
        }

        return items;
    }

    public List<Location> getAsLocations (GameEvent event, Entity entity, int from, int to) {
        List<Location> items = new LinkedList<>();
        ItemStack[] args = Arrays.copyOfRange(originalContents, from, to);

        for (ItemStack item : args) {
            if (!isNullOrAir(item)) {
                items.add(parseLocationPlus(item, event.getPlot().world().getSpawnLocation(), event, entity));
            }
        }

        return items;
    }

    public List<Double> getAsNumerics() {
        List<Double> numerics = new LinkedList<>();

        for (ItemStack item : this.numbers) {
            numerics.add(parseNumber(item));
        }

        return numerics;
    }

    public Object parseItem(ItemStack item, @Nullable GameEvent event, @Nullable Entity entity) {
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
            case APPLE -> {
                return parseGameValue(item, null).getValueInstance().get(event,  entity);
            }
            case MAGMA_CREAM -> {
                return parseDynamicVariable(item, "", true, event, entity);
            }
        }
        return null;
    }

    public static ItemStack parseItemArgument (ItemStack item, GameEvent event, Entity entity) {
        if (item == null || event == null || entity == null) return item;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;

        NamespacedKey key = new NamespacedKey(plugin, "variable");
        if (!meta.getPersistentDataContainer().has(key, PersistentDataType.BOOLEAN)) {
            return item;
        }

        Object value;
        if (item.getType() == Material.MAGMA_CREAM) {
            String name = Action.replacePlaceholders(ChatColor.stripColor(meta.getDisplayName()), event, entity);
            value = new DynamicVariable(name).getValue(event.getPlot());
        } else if (item.getType() == Material.APPLE) {
            value = parseGameValue(item, null).getValueInstance().get(event, entity);
        } else return item;

        return value instanceof ItemStack ? (ItemStack) value : item;
    }


    public Double parseNumberPlus(ItemStack item, double defValue, GameEvent event, Entity entity) {
        if (item == null) {
            return defValue;
        }

        Object o = parseItem(item, event, entity);
        if (o instanceof Number number) return number.doubleValue();

        return defValue;
    }

    public String parseTextPlus(ItemStack item, String defValue, GameEvent event, Entity entity) {
        if (item == null) return defValue;

        Object o = parseItem(item, event, entity);
        if (o == null) return defValue;
        else return GlobalListener.parseColors(String.valueOf(o));
    }

    public Location parseLocationPlus(ItemStack item, Location defaultValue, GameEvent event, Entity entity) {
        if (item == null) return defaultValue;

        Object o = parseItem(item, event, entity);
        if (o instanceof Location location) return location;

        return defaultValue;
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

    public Location parseLocation(ItemStack itemStack, Location def) {
        return parseLocation(itemStack, def, true);
    }

    public Location parseLocation(ItemStack itemStack, Location def, boolean checkType) {
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

    public static ValueType parseGameValue (ItemStack item) {
        return parseGameValue(item, null);
    }
    public static ValueType parseGameValue (ItemStack item, ValueType defaultValue) {
        return parseGameValue(item, defaultValue, true);
    }
    public static ValueType parseGameValue (ItemStack item, ValueType defaultValue, boolean checkTypeMatches) {
        if (item == null) {
            return defaultValue;
        }

        if (checkTypeMatches && item.getType() != Material.APPLE) {
            return defaultValue;
        } else {
            PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
            NamespacedKey valueType = new NamespacedKey(plugin, "valueType");

            if (pdc.has(valueType)) {
                ValueType type = ValueType.byName(pdc.get(valueType, PersistentDataType.STRING).toUpperCase(Locale.ROOT));

                return type == null ? defaultValue : type;
            } else {
                return defaultValue;
            }
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

    public Location toLocation(String code) {
        if (code != null && !code.isEmpty()) {
            String[] loc = code.split(":");
            if (loc.length == 3) {
                return fixNan(new Location(getChestBlock().getWorld(), Double.parseDouble(loc[0]), Double.parseDouble(loc[1]), Double.parseDouble(loc[2])));
            } else if (loc.length == 4) {
                return fixNan(new Location(getChestBlock().getWorld(), Double.parseDouble(loc[1]), Double.parseDouble(loc[2]), Double.parseDouble(loc[3])));
            } else {
                return loc.length == 6 ? fixNan(new Location(getChestBlock().getWorld(), Double.parseDouble(loc[1]), Double.parseDouble(loc[2]), Double.parseDouble(loc[3]), Float.parseFloat(loc[4]), Float.parseFloat(loc[5]))) : null;
            }
        } else {
            return null;
        }
    }

    /**
     * Парсит динамическую переменную из предмета. Возвращает ЗНАЧЕНИЕ переменной
     * @param item предмет для парсинга
     * @param defaultValue значение, которое вернётся если что-то пойдёт не так
     * @param checkTypeMatches проверка на тип предмета. Если != магмакрему, вернёт defaultValue
     * @param event событие для плота и замены переменных
     * @param entity сущность для замены переменных
     * @return Возвращает значение переменной
     */
    public static Object parseDynamicVariable (ItemStack item, Object defaultValue, boolean checkTypeMatches, GameEvent event, Entity entity) {
        if (item == null) {
            return defaultValue;
        }

        if (checkTypeMatches && item.getType() != Material.MAGMA_CREAM) {
            return defaultValue;
        } else {
            if (item.getItemMeta().hasDisplayName()) {
                String displayName = Action.replacePlaceholders(item.getItemMeta().getDisplayName(), event, entity);
                DynamicVariable variable = event.getPlot().handler.getDynamicVariables().get(ChatColor.stripColor(displayName));
                return variable == null ? "" : variable.getValue(event.getPlot());
            } else {
                return defaultValue;
            }
        }
    }

    /**
     * Метод для конвертации предмета в динамическую переменную
      * @param item предмет, предположительная переменная
     * @param event событие, для получения плота, а также для замены плейсхолдеров
     * @param entity сущность, для замены плейсхолдеров
     * @return Возвращает экземпляр динамической переменной с пустым именем, если переменная не найдена или найденную переменную
     */
    public static DynamicVariable asDynamicVariable (ItemStack item, GameEvent event, Entity entity) {
        ItemMeta meta = item.getItemMeta();

        if (meta != null && meta.hasDisplayName()) {
            String name = Action.replacePlaceholders(meta.getDisplayName(), event, entity);

            DynamicVariable variable = event.getPlot().handler.getDynamicVariables().get(ChatColor.stripColor(name));
            if (variable != null) {
                variable.setSaved(DynamicVariable.isVarSaved(item));
                return variable;
            }
            return new DynamicVariable(name);
        }

        return new DynamicVariable("");
    }

    public static boolean isVector (Object value) {
        return value instanceof Vector;
    }

    public static boolean isVector (DynamicVariable variable, Plot plot) {
        return isVector(variable.getValue(plot));
    }

    public static Vector asVector (Object o) {
        if (o instanceof Vector vector) return vector;
        return null;
    }

    public static Vector asVector (DynamicVariable variable, Plot plot) {
        return asVector(variable.getValue(plot));
    }
}
