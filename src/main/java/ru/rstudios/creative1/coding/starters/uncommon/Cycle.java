package ru.rstudios.creative1.coding.starters.uncommon;

import org.bukkit.scheduler.BukkitRunnable;
import ru.rstudios.creative1.coding.CodeHandler;
import ru.rstudios.creative1.coding.events.GameEvent;
import ru.rstudios.creative1.coding.starters.Starter;
import ru.rstudios.creative1.coding.starters.StarterCategory;

import static ru.rstudios.creative1.Creative_1.plugin;

public class Cycle extends Starter {

    private String name;
    private int repeatTime;
    private boolean enabled = false;
    private BukkitRunnable runnable = null;

    public void executeCycle(GameEvent event) {
        if (!enabled) {
            enabled = true;

            runnable = new BukkitRunnable() {
                @Override
                public void run() {
                    execute(event);
                }
            };
            event.getPlot().handler.cycles.add(this);
            runnable.runTaskTimer(plugin, 0, repeatTime);
        }
    }

    public void stop(CodeHandler handler) {
        if (runnable != null) {
            runnable.cancel();
            runnable = null;
            enabled = false;
            handler.cycles.remove(this);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRepeatTime() {
        return repeatTime;
    }

    public void setRepeatTime(int repeatTime) {
        this.repeatTime = repeatTime;
    }

    @Override
    public StarterCategory getCategory() {
        return StarterCategory.CYCLE;
    }
}
