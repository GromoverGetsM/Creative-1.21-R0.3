package ru.rstudios.creative1.menu.selector;

import org.bukkit.block.Block;
import ru.rstudios.creative1.menu.ProtectedMultipages;
import ru.rstudios.creative1.user.User;

public abstract class CodingMultipagesMenu extends ProtectedMultipages {

    protected Block sign;

    public CodingMultipagesMenu(String title, User user) {
        super(title, user);
        this.itemsSlots = new byte[]{10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34};
    }

    public void setSign(Block sign) {
        this.sign = sign;
    }

    public Block getSign() {
        return sign;
    }
}
