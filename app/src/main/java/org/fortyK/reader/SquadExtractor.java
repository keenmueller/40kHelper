package org.fortyK.reader;

import org.fortyK.model.Squad;
import org.fortyK.model.UnitModel;
import org.fortyK.model.WeaponModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SquadExtractor {
    private final static Pattern nameRegex = Pattern.compile("(?<=\\d{2,} PTS ).*");
    private final static Pattern modelsRegex = Pattern.compile("(?<=Options\\s\n)[\\s\\S]*?(?=Unit)");
    private final static Pattern unitsRegex = Pattern.compile("(?<=OC\\s\n)[\\s\\S]*?(?=Ranged)");
    private final static Pattern rangedWeaponsRegex = Pattern.compile("(?<=BS S AP D Keywords\\s\n)[\\s\\S]*?(?=Melee)");
    private final static Pattern meleeWeaponsRegex = Pattern.compile("(?<=WS S AP D Keywords\\s\n)[\\s\\S]*?(?=Abilities)");
    private final static Pattern abilitiesRegex = Pattern.compile("Abilities[\\s\\S]*?(?=Rules)");
    private final static Pattern rulesRegex = Pattern.compile("(?<=Rules )[\\s\\S]*?(?=Categories)");
    private final static Pattern categoriesRegex = Pattern.compile("(?<=Categories )[\\s\\S]*");

    public static List<Squad> extractSquad(List<String> txtBoxes)
    {
        List<Squad> squads = new ArrayList<>();
        for (String txt : txtBoxes)
        {
            //Find Squad Name
            Matcher nameMatcher = nameRegex.matcher(txt);
            String name = nameMatcher.find() ? nameMatcher.group() : "SQUAD NAME NOT FOUND";
            Squad squad = new Squad(name);

            //Extract Units
            Matcher unitsMatcher = unitsRegex.matcher(txt);
            String unitsTxt = unitsMatcher.find() ? unitsMatcher.group() : "UNITS NOT FOUND";
            List<UnitModel> unitModels = UnitExtractor.generateUnits(unitsTxt);

            //Extract Weapon info
            Matcher rangedWeaponsMatcher = rangedWeaponsRegex.matcher(txt);
            String rangedWeaponsTxt = rangedWeaponsMatcher.find() ? rangedWeaponsMatcher.group() : "RANGED WEAPONS NOT FOUND";
            Matcher meleeWeaponsMatcher = meleeWeaponsRegex.matcher(txt);
            String meleeWeaponsTxt = meleeWeaponsMatcher.find() ? meleeWeaponsMatcher.group() : "MELEE WEAPONS NOT FOUND";
            List<WeaponModel> weaponModels = WeaponExtractor.generateWeapons(rangedWeaponsTxt, meleeWeaponsTxt);

            //Match Models to Unit and Weapons
            Matcher modelsMatcher = modelsRegex.matcher(txt);
            String modelsTxt = modelsMatcher.find() ? modelsMatcher.group() : "MODELS NOT FOUND";
            ModelExtractor.armUnits(modelsTxt, squad, unitModels, weaponModels);

            //Extract Abilities
            Matcher abilitiesMatcher = abilitiesRegex.matcher(txt);
            String abilitiesTxt = abilitiesMatcher.find() ? abilitiesMatcher.group() : "ABILITIES NOT FOUND";
            squad.setAbilities(AbilityExtractor.extractAbilities(abilitiesTxt));

            //Extract Rules
            Matcher rulesMatcher = rulesRegex.matcher(txt);
            String rulesTxt = rulesMatcher.find() ? rulesMatcher.group() : "RULES NOT FOUND";
            rulesTxt = rulesTxt.replaceAll("\\r\\n", " ");
            squad.setRules(new HashSet<>(Arrays.asList(rulesTxt.split(", "))));

            //Extract Categories
            Matcher categoriesMatcher = categoriesRegex.matcher(txt);
            String categoriesTxt = categoriesMatcher.find() ? categoriesMatcher.group() : "CATEGORIES NOT FOUND";
            categoriesTxt = categoriesTxt.replaceAll("\\r\\n", " ");
            squad.setCategories(new HashSet<>(Arrays.asList(categoriesTxt.split(", "))));

            squads.add(squad);
        }
        return squads;
    }
}
