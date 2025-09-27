package org.fortyK.model;

import java.util.ArrayList;
import java.util.List;

public class Squad {
    private final String name;
    private List<Unit> units = new ArrayList<>();

    public Squad(String name)
    {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Unit> getUnits() {
        return units;
    }

    public void setUnits(List<Unit> units) {
        this.units = units;
    }
}
