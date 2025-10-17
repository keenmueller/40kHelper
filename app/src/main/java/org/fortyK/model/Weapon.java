package org.fortyK.model;

import java.util.ArrayList;
import java.util.List;

public class Weapon {
    private final String name;
    private final String range;
    private final String attacks;
    private final String weaponOrBalisticSkill;
    private final int strength;
    private final int armourPenetration; //Wow, what British spelling
    private final String damage;
    private final List<String> keywords; //I may change this from String to some Enum later, but that is a later me problem
    private final boolean isRanged;
    private final List<Weapon> altModes;

    public Weapon(WeaponModel weaponModel){
        this.name = weaponModel.getName();
        this.range = weaponModel.getRange();
        this.attacks = weaponModel.getAttacks();
        this.weaponOrBalisticSkill = weaponModel.getWeaponOrBalisticSkill();
        this.strength = weaponModel.getStrength();
        this.armourPenetration = weaponModel.getArmourPenetration();
        this.damage = weaponModel.getDamage();
        this.keywords = weaponModel.getKeywords();
        this.isRanged = weaponModel.isRanged();
        this.altModes = generateAltModes(weaponModel.getAltModes());
    }

    public String getName() {
        return name;
    }

    public String getRange() {
        return range;
    }

    public String getAttacks() {
        return attacks;
    }

    public String getWeaponOrBalisticSkill() {
        return weaponOrBalisticSkill;
    }

    public int getStrength() {
        return strength;
    }

    public int getArmourPenetration() {
        return armourPenetration;
    }

    public String getDamage() {
        return damage;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public boolean isRanged() {
        return isRanged;
    }

    public List<Weapon> getAltModes() {
        return altModes;
    }

    private List<Weapon> generateAltModes(List<WeaponModel> models) {
        if (models == null)
            return null;
        List<Weapon> altModes = new ArrayList<>();
        for (WeaponModel weaponModel : models)
        {
            altModes.add(new Weapon(weaponModel));
        }
        return altModes;
    }
}
