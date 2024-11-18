package ru.rstudios.creative1.menu;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.rstudios.creative1.user.LocaleManages;
import ru.rstudios.creative1.user.User;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public abstract class ProtectedMultipages extends ProtectedMenu {

    protected final User user;
    protected Inventory inventory;
    protected byte currentPage;

    protected List<ItemStack> menuElements = new LinkedList<>();
    protected byte[] itemsSlots = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25};

    protected byte menuDoNotFilled = 22;
    protected byte previousPageSlot = 47;
    protected byte returnSlot = 49;
    protected byte nextPageSlot = 51;

    public ProtectedMultipages (String title, User user) {
        super(title, (byte) 6);
        this.user = user;
        this.currentPage = 1;
    }

    @Override
    public void fillItems (User user) {
        menuElements.addAll(getMenuElements());
        fillMenuElements(getCurrentPage());
        fillArrows(getCurrentPage());
        fillOther();
    }

    private void fillMenuElements (byte page) {
        fillEmpty();
        if (menuElements == null || menuElements.isEmpty()) {
            setItem(menuDoNotFilled, getMenuNotFilledItem());
            updateSlot(menuDoNotFilled);
        } else {
            List<ItemStack> items = getElementsPart(page);
            byte slot = 0;

            for (ItemStack item : items) {
                setItem(itemsSlots[slot], item);
                updateSlot(slot);
                slot++;
            }
        }
    }

    protected void fillEmpty() {
        for (byte slot : itemsSlots) {
            setItem(slot, new ItemStack(Material.AIR));
            updateSlot(slot);
        }
        setItem(nextPageSlot, new ItemStack(Material.AIR));
        setItem(previousPageSlot, new ItemStack(Material.AIR));
        updateSlot(nextPageSlot);
        updateSlot(previousPageSlot);
    }


    private void fillArrows(byte currentPage) {
        if (menuElements == null || menuElements.isEmpty()) {
            setItem(menuDoNotFilled, getMenuNotFilledItem());
            updateSlot(menuDoNotFilled);
        } else {
            int pages = getPagesCount();

            if (currentPage > pages || currentPage < 1) {
                currentPage = 1;
            }
            if (currentPage > 1) {
                setItem(previousPageSlot, getPrevPageItem());
                updateSlot(previousPageSlot);
            }
            if (currentPage < pages) {
                setItem(nextPageSlot, getNextPageItem());
                updateSlot(nextPageSlot);
            }

        }
    }

    @Override
    public void onClick (InventoryClickEvent event) {
        event.setCancelled(true);
        Player player1 = (Player) event.getWhoClicked();

        if ((!isPlayerClicked(event) || !isPossibleItemClicked((byte) event.getSlot())) || Objects.equals(event.getCurrentItem(), DECORATION_ITEM)) {
            return;
        }

        if (isPossibleItemClicked((byte) event.getSlot()) && !isNullOrAir(event.getCurrentItem()) && !Objects.equals(event.getCurrentItem(), getMenuNotFilledItem())) {
            event.setCancelled(false);
            onMenuElementClick(event);
        }

        if (isPossibleItemClicked((byte) event.getSlot()) && Objects.equals(event.getCurrentItem(), getNextPageItem())) {
            player1.playSound(player1.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1F, 1F);
            nextPage();
        }

        if (isPossibleItemClicked((byte) event.getSlot()) && Objects.equals(event.getCurrentItem(), getPrevPageItem())) {
            player1.playSound(player1.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1F, 1F);
            previousPage();
        }
    }

    protected boolean isPossibleItemClicked (byte slot) {
        for (int slot1 : itemsSlots) {
            if (slot == slot1) return true;
        }

        return false;
    }

    protected List<ItemStack> getElementsPart (byte page) {
        if (page < 1 || page > getPagesCount()) page = 1;

        int startIndex = (page-1) * itemsSlots.length;
        int endIndex = Math.min(menuElements.size(), page * itemsSlots.length);
        return menuElements.subList(startIndex, endIndex);
    }

    public byte getCurrentPage() {
        return currentPage;
    }

    protected int getPagesCount() {
        return (menuElements.size() + itemsSlots.length - 1) / itemsSlots.length;
    }

    private void previousPage() {
        currentPage = getPreviousPage();
        fillMenuElements(currentPage);
        fillArrows(currentPage);
    }
    private void nextPage() {
        currentPage = getNextPage();
        fillMenuElements(currentPage);
        fillArrows(currentPage);
    }

    private byte getPreviousPage() {
        byte previousPage = (byte) (currentPage - 1);
        int maxPages = getPagesCount();
        if (previousPage > maxPages || previousPage < 1) previousPage = 1;
        return previousPage;
    }

    private byte getNextPage() {
        byte nextPage = (byte) (currentPage + 1);
        int maxPages = getPagesCount();
        if (nextPage > maxPages || nextPage < 1) nextPage = 1;
        return nextPage;
    }

    protected ItemStack getMenuNotFilledItem() {
        ItemStack item = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.displayName(Component.text(LocaleManages.getLocaleMessage(user.getLocale(), "global-items.menu-not-filled.name", false, "")));
            meta.lore(LocaleManages.getLocaleMessages(user.getLocale(), "global-items.menu-not-filled.lore", new LinkedHashMap<>()));
            item.setItemMeta(meta);
        }

        return item;
    }

    protected ItemStack getPrevPageItem() {
        ItemStack item = new ItemStack(Material.ARROW);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.displayName(Component.text(LocaleManages.getLocaleMessage(user.getLocale(), "global-items.prev-page.name", false, String.valueOf(getPreviousPage()))));
            meta.lore(LocaleManages.getLocaleMessages(user.getLocale(), "global-items.prev-page.lore", new LinkedHashMap<>()));
            item.setItemMeta(meta);
        }

        return item;
    }

    protected ItemStack getNextPageItem() {
        ItemStack item = new ItemStack(Material.ARROW);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.displayName(Component.text(LocaleManages.getLocaleMessage(user.getLocale(), "global-items.next-page.name", false, String.valueOf(getNextPage()))));
            meta.lore(LocaleManages.getLocaleMessages(user.getLocale(), "global-items.next-page.lore", new LinkedHashMap<>()));
            item.setItemMeta(meta);
        }

        return item;
    }

    public abstract List<ItemStack> getMenuElements();
    public abstract void onMenuElementClick(InventoryClickEvent event);
    public abstract void fillOther();

}