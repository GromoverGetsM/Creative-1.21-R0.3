package ru.rstudios.creative.menu.selector;

import org.bukkit.block.Block;
import ru.rstudios.creative.menu.ProtectedMenu;

public abstract class CodingCategoriesMenu extends ProtectedMenu {

    protected Block sign;

    public CodingCategoriesMenu(String title, byte rows) {
        super(title, rows);
    }

    public void setSign(Block sign) {
        this.sign = sign;
    }

    public Block getSign() {
        return sign;
    }
}
