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
    private final List<Weapon> weapons;

    public Unit(UnitModel unitModel)
    {
        this.name = unitModel.getName();
        this.movement = unitModel.getMovement();
        this.toughness = unitModel.getToughness();
        this.save = unitModel.getSave();
        this.wounds = unitModel.getWounds();
        this.leadership = unitModel.getLeadership();
        this.objectiveControl = unitModel.getObjectiveControl();
        this.weapons = generateWeaponsFromModel(unitModel);
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

    private List<Weapon> generateWeaponsFromModel(UnitModel model){
        List<Weapon> weaponList = new ArrayList<>();
        for (WeaponModel weaponModel : model.getWeapons()){
            weaponList.add(new Weapon(weaponModel));
        }
        return weaponList;
    }
}
