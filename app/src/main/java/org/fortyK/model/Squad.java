package org.fortyK.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Squad {
    private final String name;
    private List<Unit> units = new ArrayList<>();
    private Set<Ability> abilities = new HashSet<>();
    private Set<String> rules = new HashSet<>();
    private Set<String> categories = new HashSet<>();

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

    public void addUnit(Unit unit)
    {
        this.units.add(unit);
    }

    public Set<String> getRules() {
        return rules;
    }

    public void setRules(Set<String> rules) {
        this.rules = rules;
    }

    public Set<String> getCategories() {
        return categories;
    }

    public void setCategories(Set<String> categories) {
        this.categories = categories;
    }

    public Set<Ability> getAbilities() {
        return abilities;
    }

    public void setAbilities(Set<Ability> abilities) {
        this.abilities = abilities;
    }
}
