package ru.rstudios.creative1.menu.selector;

import org.bukkit.block.Block;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import ru.rstudios.creative1.menu.ProtectedMenu;
import ru.rstudios.creative1.user.LocaleManages;
import ru.rstudios.creative1.user.User;
import ru.rstudios.creative1.utils.Development;

public class PlayerConditions extends ProtectedMenu {

    private Block sign;

    public PlayerConditions(User user) {
        super(LocaleManages.getLocaleMessage(user.getLocale(), "coding.player_conds", false, ""), (byte) 1);
    }

    @Override
    public void fillItems(User user) {
        setItem((byte) 0, Development.BlockTypes.IF_PLAYER.getIcon(user));
        setItem((byte) 1, Development.BlockTypes.IF_VARIABLE.getIcon(user));
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        User user = User.asUser(event.getWhoClicked());
        event.setCancelled(true);

        switch (event.getSlot()) {
            case 0 -> {
                IfPlayer menu = new IfPlayer(user);
                menu.setSign(this.sign);
                menu.open(user);
            }
            case 1 -> {
                IfVariable menu = new IfVariable(user);
                menu.setSign(this.sign);
                menu.open(user);
            }
        }
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {

    }

    public void setSign(Block sign) {
        this.sign = sign;
    }

    public Block getSign() {
        return sign;
    }
}
