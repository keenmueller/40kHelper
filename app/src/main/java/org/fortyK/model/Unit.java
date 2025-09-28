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
    private final List<String> weaponOptions;
    private List<Weapon> weapons = new ArrayList<>();

    public Unit(Builder builder)
    {
        this.name = builder.name;
        this.weaponOptions = builder.weaponOptions;
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

    public List<String> getWeaponOptions() {
        return weaponOptions;
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

    public static class Builder {
        private String name;
        private int movement;
        private int toughness;
        private int save;
        private int wounds;
        private int leadership;
        private int objectiveControl;
        private List<String> weaponOptions;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder movement(int movement) {
            this.movement = movement;
            return this;
        }

        public Builder toughness(int toughness) {
            this.toughness = toughness;
            return this;
        }

        public Builder save(int save) {
            this.save = save;
            return this;
        }

        public Builder wounds(int wounds) {
            this.wounds = wounds;
            return this;
        }

        public Builder leadership(int leadership) {
            this.leadership = leadership;
            return this;
        }

        public Builder objectiveControl(int objectiveControl) {
            this.objectiveControl = objectiveControl;
            return this;
        }

        public Builder weaponOptions(List<String> weaponOptions) {
            this.weaponOptions = weaponOptions;
            return this;
        }

        public Unit build() {
            return new Unit(this);
        }
    }
}
