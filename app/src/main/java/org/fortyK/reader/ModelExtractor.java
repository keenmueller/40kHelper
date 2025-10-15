package org.fortyK.reader;

import org.fortyK.model.Squad;
import org.fortyK.model.Unit;
import org.fortyK.model.Weapon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
        for (Unit unit : units)
        {
            if (modelTxt.matches("^" + unit.getName() + " [\\S\\s]*?(?=$)")) {
                unitToAdd = unit;
                break;
            }
        }

        //Direct Name Match
        String options = modelTxt.replaceAll("^" + unitToAdd.getName() + " ", "");
        String[] optionList = options.split(", ");
        List<Weapon> weaponsToEquip = new ArrayList<>();
        for (String option : optionList)
        {
            weapons.forEach(weapon -> {
                if(option.equals(weapon.getName()) || option.equals(weapon.getShortName()))
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
