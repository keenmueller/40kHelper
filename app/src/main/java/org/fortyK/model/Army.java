package org.fortyK.model;

import java.util.List;

public class Army {
    private final String armyName;
    private Detachment detachment;
    private List<Squad> squads;

    public Army(String armyName)
    {
        this.armyName = armyName;
    }

    public String getArmyName() {
        return armyName;
    }

    public List<Squad> getSquads() {
        return squads;
    }

    public void setSquads(List<Squad> squads) {
        this.squads = squads;
    }

    public Detachment getDetachment() {
        return detachment;
    }

    public void setDetachment(Detachment detachment) {
        this.detachment = detachment;
    }
}
