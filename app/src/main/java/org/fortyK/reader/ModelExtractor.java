package org.fortyK.reader;

import org.fortyK.model.Squad;
import org.fortyK.model.Unit;
import org.fortyK.model.UnitModel;
import org.fortyK.model.WeaponModel;
import org.fortyK.utilities.StringMatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ModelExtractor {
    private final static Pattern multiplesRegex = Pattern.compile("^\\d*?(?=x)");
    private final static String MATCH_UNTIL_END_OF_LINE = " [\\S\\s]*?(?=$)";
    private final String modelsTxt;

    ModelExtractor(String modelsTxt) {
        this.modelsTxt = modelsTxt;
    }

    public void armUnits(Squad squad, List<UnitModel> unitModels, List<WeaponModel> weaponModels) {
        List<String> lines = modelsTxt.lines().toList();
        int i = 0;
        while (i < lines.size()) {
            if (lines.get(i).matches("^\\d+x[\\S\\s]*?(?=$)")) {
                String line = lines.get(i);
                Matcher multMatcher = multiplesRegex.matcher(line);
                int mult = multMatcher.find() ? Integer.parseInt(multMatcher.group()) : null; //intentional npe if this triggers
                String trimmedTxt = line.replaceAll("^\\d+x ", "");

                UnitModel unitModelToAdd = matchUnitWithWeapons(trimmedTxt, unitModels, weaponModels);
                addMultiples(squad, unitModelToAdd, mult);
                i++;
            } else //I've only seen 3 lines of options max, so I'm just going to assume that will always be the case for now
            {
                i += 3;
                //TODO Try to sort that damn tank's options
            }
        }
    }

    private UnitModel matchUnitWithWeapons(String modelTxt, List<UnitModel> unitModels, List<WeaponModel> weaponModels) {
        UnitModel unitModelToAdd = null;
        String matchedNameRegex = null;
        for (UnitModel unitModel : unitModels) {
            String unitModelName = unitModel.getName();
            //Direct Name Match
            if (modelTxt.matches("^" + unitModelName + MATCH_UNTIL_END_OF_LINE)) {
                unitModelToAdd = unitModel;
                matchedNameRegex = "^" + unitModelName;
                break;
            }

            /*
            It would be nice if the model names were the same as the unit names, but nooooooooooooooooooooo. So here are
            all the alternates, which there are a lot of.
             */

            //Name but Plural
            if (modelTxt.matches("^" + unitModelName + "s" + MATCH_UNTIL_END_OF_LINE)) {
                unitModelToAdd = unitModel;
                matchedNameRegex = "^" + unitModelName + "s";
                break;
            }

            //Name has Squad at the end
            if (unitModelName.contains("Squad") && !modelTxt.contains("Sergeant")) {
                String trimmedName = unitModelName.replaceAll("Squad", "").trim();
                if (modelTxt.matches("^" + trimmedName + MATCH_UNTIL_END_OF_LINE)) {
                    unitModelToAdd = unitModel;
                    if (modelTxt.matches("^" + trimmedName + " with [a-zA-Z\\-]+" + MATCH_UNTIL_END_OF_LINE))
                        matchedNameRegex = "^" + trimmedName + " with [a-zA-Z\\-]+";
                    else
                        matchedNameRegex = "^" + trimmedName;
                    break;
                }
            }

            //Name has Jump Pack
            if (unitModelName.contains("with Jump Pack")) {
                String trimmedName = unitModelName.replaceAll("with Jump Pack", "").trim();
                //Check the alt first
                if (modelTxt.matches("^" + trimmedName + " w/ alternate weapons"+ MATCH_UNTIL_END_OF_LINE)) {
                    unitModelToAdd = unitModel;
                    matchedNameRegex = "^" + trimmedName + " w/ alternate weapons";
                    break;
                }
                if (modelTxt.matches("^" + trimmedName + MATCH_UNTIL_END_OF_LINE)) {
                    unitModelToAdd = unitModel;
                    matchedNameRegex = "^" + trimmedName;
                    break;
                }
            }
        }

        String options = modelTxt.replaceAll(matchedNameRegex + " ", "");
        String[] optionList = options.split(", ");
        List<WeaponModel> weaponsToEquip = new ArrayList<>();
        for (String option : optionList) {
            weaponModels.forEach(weapon -> {
                if (StringMatcher.compareStringWithTolerance(option, weapon.getName(), 1) || option.equals(weapon.getShortName())) {
                    weaponsToEquip.add(weapon);
                }
            });
        }
        unitModelToAdd.setWeapons(weaponsToEquip);
        return unitModelToAdd;
    }

    private void addMultiples(Squad squad, UnitModel unitModel, int multiple) {
        for (int i = 0; i < multiple; i++) {
            squad.addUnit(new Unit(unitModel));
        }
    }
}
