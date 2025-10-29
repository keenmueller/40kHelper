package org.fortyK.reader;

import org.fortyK.model.Squad;
import org.fortyK.model.Unit;
import org.fortyK.model.UnitModel;
import org.fortyK.model.WeaponModel;
import org.fortyK.utilities.Pair;
import org.fortyK.utilities.StringMatcher;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ModelExtractor {
    private final static Pattern multiplesRegex = Pattern.compile("^\\d*?(?=x)");
    private final String modelsTxt;
    private final static String MATCH_UNTIL_END_OF_LINE = " [\\S\\s]*?(?=$)";

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
                int multiplier = multMatcher.find() ? Integer.parseInt(multMatcher.group()) : null; //intentional npe if this triggers
                String trimmedTxt = line.replaceAll("^\\d+x ", "");

                UnitModel unitModelToAdd = matchUnitWithWeapons(trimmedTxt, unitModels, weaponModels);
                addMultiples(squad, unitModelToAdd, multiplier);
                i++;
            } else //I've only seen 3 lines of options max, so I'm just going to assume that will always be the case for now
            {
                String modelLine = lines.get(i + 1);
                Matcher multMatcher = multiplesRegex.matcher(modelLine);
                int multiplier = multMatcher.find() ? Integer.parseInt(multMatcher.group()) : null; //intentional npe if this triggers
                String trimmedTxt = modelLine.replaceAll("^\\d+x ", "");

                UnitModel unitModelToAdd = multiLineMatchUnitWithWeapons(lines.get(i), trimmedTxt, lines.get(i + 2), unitModels, weaponModels);
                addMultiples(squad, unitModelToAdd, multiplier);
                i += 3;
            }
        }
    }

    private UnitModel matchUnitWithWeapons(String modelTxt, List<UnitModel> unitModels, List<WeaponModel> weaponModels) {
        Pair<UnitModel, String> modelWithMatchedString = findModelFromTxt(modelTxt, unitModels);
        UnitModel unitModelToAdd = modelWithMatchedString.getLeft(); //NPE is possible here. Maybe one day I'll add custom exceptions, but today is not that day

        String options = modelTxt.replaceAll(modelWithMatchedString.getRight() + " ", "");
        List<WeaponModel> weaponsToEquip = findWeaponsFromOptions(options, weaponModels);
        unitModelToAdd.setWeapons(weaponsToEquip);
        return unitModelToAdd;
    }

    private UnitModel multiLineMatchUnitWithWeapons(String line1, String line2, String line3, List<UnitModel> unitModels, List<WeaponModel> weaponModels) {
        Pair<UnitModel, String> modelWithMatchedString = findModelFromTxt(line2, unitModels);
        UnitModel unitModelToAdd = modelWithMatchedString.getLeft(); //NPE is possible here. Maybe one day I'll add custom exceptions, but today is not that day
        String cleanedModelLine = line2.replaceAll(modelWithMatchedString.getRight() + " ?", "");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(line1).append(" ");
        if (!cleanedModelLine.isBlank())
            stringBuilder.append(cleanedModelLine).append(" ");
        stringBuilder.append(line3);

        /*
        For the life of me, I have no idea what actually determines if valid options are put inside of parenthesis or if
        it's a clarification on an option because BOTH ARE POSSIBLE!!! The rules of this PDF are stupid and thus my code
        is messy to compensate.
         */
        String options = stringBuilder.toString().replaceAll(" \\(", ", ").replaceAll("\\)", "");
        List<WeaponModel> weaponsToEquip = new ArrayList<>(new HashSet<>(findWeaponsFromOptions(options, weaponModels)));
        unitModelToAdd.setWeapons(weaponsToEquip);
        return unitModelToAdd;
    }

    private Pair<UnitModel, String> findModelFromTxt(String txt, List<UnitModel> unitModels){
        for (UnitModel unitModel : unitModels) {
            String unitModelName = unitModel.getName();
            //Literal Match (This only really happens with the multi-lined options)
            if(txt.equals(unitModelName))
                return new Pair<>(unitModel, unitModelName);

            //Direct Name Match
            if (txt.matches("^" + unitModelName + MATCH_UNTIL_END_OF_LINE))
                return new Pair<>(unitModel, "^" + unitModelName);

            /*
            It would be nice if the model names were the same as the unit names, but nooooooooooooooooooooo. So here are
            all the alternates, which there are a lot of.
             */

            //Name but Plural
            if (txt.matches("^" + unitModelName + "s" + MATCH_UNTIL_END_OF_LINE))
                return new Pair<>(unitModel, "^" + unitModelName + "s");

            //Name has Squad at the end
            if (unitModelName.contains("Squad") && !txt.contains("Sergeant")) {
                String trimmedName = unitModelName.replaceAll("Squad", "").trim();
                if (txt.matches("^" + trimmedName + MATCH_UNTIL_END_OF_LINE)) {
                    if (txt.matches("^" + trimmedName + " with [a-zA-Z\\-]+" + MATCH_UNTIL_END_OF_LINE))
                        return new Pair<>(unitModel, "^" + trimmedName + " with [a-zA-Z\\-]+");
                    else
                        return new Pair<>(unitModel, "^" + trimmedName);
                }
            }

            //Name has Jump Pack
            if (unitModelName.contains("with Jump Pack")) {
                String trimmedName = unitModelName.replaceAll("with Jump Pack", "").trim();
                //Check the alt first
                if (txt.matches("^" + trimmedName + " w/ alternate weapons"+ MATCH_UNTIL_END_OF_LINE)) {
                    return new Pair<>(unitModel, "^" + trimmedName + " w/ alternate weapons");
                }
                if (txt.matches("^" + trimmedName + MATCH_UNTIL_END_OF_LINE)) {
                    return new Pair<>(unitModel, "^" + trimmedName);
                }
            }
        }
        return null;
    }

    private List<WeaponModel> findWeaponsFromOptions(String options, List<WeaponModel> weaponModels){
        String[] optionList = options.split(", ");
        List<WeaponModel> weaponsToEquip = new ArrayList<>();
        for (String option : optionList) {
            weaponModels.forEach(weapon -> {
                if (StringMatcher.compareStringWithTolerance(option, weapon.getName(), 1) || option.equals(weapon.getShortName())) {
                    weaponsToEquip.add(weapon);
                }
            });
        }
        return weaponsToEquip;
    }

    private void addMultiples(Squad squad, UnitModel unitModel, int multiple) {
        for (int i = 0; i < multiple; i++) {
            squad.addUnit(new Unit(unitModel));
        }
    }
}
