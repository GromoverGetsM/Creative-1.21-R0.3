package ru.rstudios.creative.coding.starters.uncommon;

import ru.rstudios.creative.coding.starters.Starter;
import ru.rstudios.creative.coding.starters.StarterCategory;

public class Function extends Starter {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public StarterCategory getCategory() {
        return StarterCategory.FUNCTION;
    }

    @Override
    public String toString() {
        return "Function{name=" + name + "}";
    }
}
