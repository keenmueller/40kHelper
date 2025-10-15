package org.fortyK.reader;

import org.fortyK.model.Squad;
import org.fortyK.model.Unit;
import org.fortyK.model.Weapon;
import org.fortyK.utilities.StringMatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ModelExtractor {
    private final static Pattern multiplesRegex = Pattern.compile("^\\d*?(?=x)");
    private final String modelsTxt;

    ModelExtractor(String modelsTxt)
    {
        this.modelsTxt = modelsTxt;
    }

    public void armUnits(Squad squad, List<Unit> units, List<Weapon> weapons)
    {
        List<String> lines = modelsTxt.lines().toList();
        int i = 0;
        while (i < lines.size())
        {
            if(lines.get(i).matches("^\\d+x[\\S\\s]*?(?=$)"))
            {
                String line = lines.get(i);
                Matcher multMatcher = multiplesRegex.matcher(line);
                int mult = multMatcher.find() ? Integer.parseInt(multMatcher.group()) : null; //intentional npe if this triggers
                String trimmedTxt = line.replaceAll("^\\d+x ", "");

                Unit unitToAdd = matchUnitWithWeapons(trimmedTxt, units, weapons);
                addMultiples(squad, unitToAdd, mult);
                i++;
            }
            else //I've only seen 3 lines of options max, so I'm just going to assume that will always be the case for now
            {
                i += 3;
                //TODO Try to sort that damn tank's options
            }
        }
    }

    private Unit matchUnitWithWeapons(String modelTxt, List<Unit> units, List<Weapon> weapons)
    {
        Unit unitToAdd = null;
        String matchedNameRegex = null;
        for (Unit unit : units)
        {
            //Direct Name Match
            if (modelTxt.matches("^" + unit.getName() + " [\\S\\s]*?(?=$)")) {
                unitToAdd = unit;
                matchedNameRegex = "^" + unit.getName();
                break;
            }

            //Name but Plural
            if (modelTxt.matches("^" + unit.getName() + "s [\\S\\s]*?(?=$)")) {
                unitToAdd = unit;
                matchedNameRegex = "^" + unit.getName() + "s";
                break;
            }
        }


        String options = modelTxt.replaceAll(matchedNameRegex + " ", "");
        String[] optionList = options.split(", ");
        List<Weapon> weaponsToEquip = new ArrayList<>();
        for (String option : optionList)
        {
            weapons.forEach(weapon -> {
                if(option.equals(weapon.getName()) || option.equals(weapon.getShortName()) || StringMatcher.compareStringWithTolerance(option, weapon.getName(), 1))
                {
                    weaponsToEquip.add(weapon);
                }
            });
        }
        unitToAdd.setWeapons(weaponsToEquip);
        return unitToAdd;
    }

    private void addMultiples(Squad squad, Unit unit, int multiple)
    {
        for (int i = 0; i < multiple; i++)
        {
            squad.addUnit(unit);
        }
    }
}
