package ru.rstudios.creative1.menu.selector;

import org.bukkit.block.Block;
import ru.rstudios.creative1.coding.MenuCategory;
import ru.rstudios.creative1.menu.ProtectedMenu;
import ru.rstudios.creative1.user.User;

import java.util.List;

public abstract class CodingCategoriesMenu extends ProtectedMenu {

    protected boolean isCategorySelected = false;
    protected MenuCategory selectedCategory = null;
    protected Block sign;

    public CodingCategoriesMenu(String title, byte rows) {
        super(title, rows);
    }

    public void setSign(Block sign) {
        this.sign = sign;
    }

    @Override
    public void fillItems(User user) {
        if (isCategorySelected) fillItemsPage(user);
        else fillCategoryPage(user);
    }

    protected void addRange(List<Byte> list, byte start, byte end) {
        for (byte i = start; i <= end; i++) {
            list.add(i);
        }
    }

    public abstract void fillCategoryPage (User user);
    public abstract void fillItemsPage (User user);
}
