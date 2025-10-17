package org.fortyK.reader;

import org.fortyK.model.Squad;
import org.fortyK.model.UnitModel;
import org.fortyK.model.WeaponModel;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SquadExtractor {
    private final static Pattern nameRegex = Pattern.compile("(?<=\\d{2,} PTS ).*");
    private final static Pattern modelsRegex = Pattern.compile("(?<=Options\\s\n)[\\s\\S]*?(?=Unit)");
    private final static Pattern unitsRegex = Pattern.compile("(?<=OC\\s\n)[\\s\\S]*?(?=Ranged)");
    private final static Pattern rangedWeaponsRegex = Pattern.compile("(?<=BS S AP D Keywords\\s\n)[\\s\\S]*?(?=Melee)");
    private final static Pattern meleeWeaponsRegex = Pattern.compile("(?<=WS S AP D Keywords\\s\n)[\\s\\S]*?(?=Abilities)");
    private final List<String> txtBoxes;

    public SquadExtractor(List<String> txtBoxes)
    {
        this.txtBoxes = txtBoxes;
    }

    public List<Squad> extractSquad()
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
            UnitExtractor unitExtractor = new UnitExtractor(unitsTxt);
            List<UnitModel> unitModels = unitExtractor.generateUnits();

            //Extract Weapon info
            Matcher rangedWeaponsMatcher = rangedWeaponsRegex.matcher(txt);
            String rangedWeaponsTxt = rangedWeaponsMatcher.find() ? rangedWeaponsMatcher.group() : "RANGED WEAPONS NOT FOUND";
            Matcher meleeWeaponsMatcher = meleeWeaponsRegex.matcher(txt);
            String meleeWeaponsTxt = meleeWeaponsMatcher.find() ? meleeWeaponsMatcher.group() : "MELEE WEAPONS NOT FOUND";
            WeaponExtractor weaponExtractor = new WeaponExtractor(rangedWeaponsTxt, meleeWeaponsTxt);
            List<WeaponModel> weaponModels = weaponExtractor.generateWeapons();

            //Match Models to Unit and Weapons
            Matcher modelsMatcher = modelsRegex.matcher(txt);
            String modelsTxt = modelsMatcher.find() ? modelsMatcher.group() : "MODELS NOT FOUND";
            ModelExtractor modelExtractor = new ModelExtractor(modelsTxt);
            modelExtractor.armUnits(squad, unitModels, weaponModels);

            squads.add(squad);
        }
        return squads;
    }
}
