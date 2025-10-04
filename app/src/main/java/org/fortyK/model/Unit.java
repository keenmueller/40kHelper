package org.fortyK.model;

import java.util.ArrayList;
import java.util.List;

public class Unit {
    private final String name;
    private final int movement;
    private final int toughness;
    private final int save;
    private final int wounds;
    private final int leadership;
    private final int objectiveControl;
    private List<Weapon> weapons = new ArrayList<>();

    public Unit(UnitBuilder builder)
    {
        this.name = builder.name;
        this.movement = builder.movement;
        this.toughness = builder.toughness;
        this.save = builder.save;
        this.wounds = builder.wounds;
        this.leadership = builder.leadership;
        this.objectiveControl = builder.objectiveControl;
    }

    public String getName() {
        return name;
    }

    public int getMovement() {
        return movement;
    }

    public int getToughness() {
        return toughness;
    }

    public int getSave() {
        return save;
    }

    public int getWounds() {
        return wounds;
    }

    public int getLeadership() {
        return leadership;
    }

    public int getObjectiveControl() {
        return objectiveControl;
    }

    public List<Weapon> getWeapons() {
        return weapons;
    }

    public void setWeapons(List<Weapon> weapons) {
        this.weapons = weapons;
    }

    public static class UnitBuilder {
        private String name;
        private int movement;
        private int toughness;
        private int save;
        private int wounds;
        private int leadership;
        private int objectiveControl;

        public UnitBuilder name(String name) {
            this.name = name;
            return this;
        }

        public UnitBuilder movement(int movement) {
            this.movement = movement;
            return this;
        }

        public UnitBuilder toughness(int toughness) {
            this.toughness = toughness;
            return this;
        }

        public UnitBuilder save(int save) {
            this.save = save;
            return this;
        }

        public UnitBuilder wounds(int wounds) {
            this.wounds = wounds;
            return this;
        }

        public UnitBuilder leadership(int leadership) {
            this.leadership = leadership;
            return this;
        }

        public UnitBuilder objectiveControl(int objectiveControl) {
            this.objectiveControl = objectiveControl;
            return this;
        }

        public Unit build() {
            return new Unit(this);
        }
    }
}
