package ru.rstudios.creative1.coding.events;

import org.bukkit.entity.Entity;

public interface KillEvent {

    Entity getKiller();
    Entity getVictim();

}
