package org.fortyK.reader;

import org.fortyK.model.Ability;

import java.util.HashSet;
import java.util.Set;

public class AbilityExtractor {

    public static Set<Ability> extractAbilities(String abilityTxt)
    {
        Set<Ability> abilities = new HashSet<>();

        abilityTxt.lines().forEach(s -> {
            for (String name : Ability.ALL_NAMES)
            {
                if (s.matches("^" + name + ".*")) {
                    abilities.add(Ability.fromName(name));
                }
            }
        });

        return abilities;
    }
}
