package org.fortyK.model;

import java.util.List;

public class Weapon {
    private final String name;
    private final String range;
    private final String attacks;
    private final int weaponOrBalisticSkill;
    private final int strength;
    private final int armourPenetration; //Wow, what British spelling
    private final String damage;
    private final List<String> keywords; //I may change this from String to some Enum later, but that is a later me problem
    private final boolean isRanged;
    private final Weapon altMode;

    public Weapon(WeaponBuilder builder){
        this.name = builder.name;
        this.range = builder.range;
        this.attacks = builder.attacks;
        this.weaponOrBalisticSkill = builder.weaponOrBalisticSkill;
        this.strength = builder.strength;
        this.armourPenetration = builder.armourPenetration;
        this.damage = builder.damage;
        this.keywords = builder.keywords;
        this.isRanged = builder.isRanged;
        this.altMode = builder.altMode;
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

    public int getWeaponOrBalisticSkill() {
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

    public static class WeaponBuilder {
        private String name;
        private String range;
        private String attacks;
        private int weaponOrBalisticSkill;
        private int strength;
        private int armourPenetration;
        private String damage;
        private List<String> keywords;
        private boolean isRanged;
        private Weapon altMode;

        public WeaponBuilder name(String name) {
            this.name = name;
            return this;
        }

        public WeaponBuilder range(String range) {
            this.range = range;
            return this;
        }

        public WeaponBuilder attacks(String attacks) {
            this.attacks = attacks;
            return this;
        }

        public WeaponBuilder weaponOrBalisticSkill(int weaponOrBalisticSkill) {
            this.weaponOrBalisticSkill = weaponOrBalisticSkill;
            return this;
        }

        public WeaponBuilder strength(int strength) {
            this.strength = strength;
            return this;
        }

        public WeaponBuilder armourPenetration(int armourPenetration) {
            this.armourPenetration = armourPenetration;
            return this;
        }

        public WeaponBuilder damage(String damage) {
            this.damage = damage;
            return this;
        }

        public WeaponBuilder keywords(List<String> keywords) {
            this.keywords = keywords;
            return this;
        }

        public WeaponBuilder isRanged(boolean isRanged) {
            this.isRanged = isRanged;
            return this;
        }

        public WeaponBuilder altMode(Weapon altMode) {
            this.altMode = altMode;
            return this;
        }

        public Weapon build(){
            return new Weapon(this);
        }
    }
}
