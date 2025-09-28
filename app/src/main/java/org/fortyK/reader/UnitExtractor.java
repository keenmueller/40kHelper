package org.fortyK.reader;

import org.fortyK.model.Unit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UnitExtractor {
    private final static Pattern multiplesRegex = Pattern.compile("\\d*?(?=x)");
    private final static Pattern nameRegex = Pattern.compile("(?<=\\d{1,2}x\\s)(?:\\w+\\s)*(?=\\w+,)");
    private final static Pattern unitStatRegex = Pattern.compile("(?<=\\D)(\\d+)(?=\\D|$)");
    private final String modelsTxt;
    private final String unitsTxt;

    public UnitExtractor(String modelsTxt, String unitsTxt)
    {
        this.modelsTxt = modelsTxt;
        this.unitsTxt = unitsTxt;
    }

    public List<Unit> generateUnits()
    {
        List<Unit> units = new ArrayList<>();

        //Pull Stats
        String statLine = unitsTxt.substring(0, unitsTxt.indexOf("\n"));
        Matcher statMatcher = unitStatRegex.matcher(statLine);
        int movement = statMatcher.find() ? Integer.parseInt(statMatcher.group()) : -1;
        int toughness = statMatcher.find() ? Integer.parseInt(statMatcher.group()) : -1;
        int save = statMatcher.find() ? Integer.parseInt(statMatcher.group()) : -1;
        int wounds = statMatcher.find() ? Integer.parseInt(statMatcher.group()) : -1;
        int leadership = statMatcher.find() ? Integer.parseInt(statMatcher.group()) : -1;
        int objectiveControl = statMatcher.find() ? Integer.parseInt(statMatcher.group()) : -1;


        modelsTxt.lines().forEach(s -> {
            //Check if there are multiples for this unit
            Matcher multiplesMatcher = multiplesRegex.matcher(s);
            int mult = multiplesMatcher.find() ? Integer.parseInt(multiplesMatcher.group()) : 1;

            //Pull name
            Matcher nameMatcher = nameRegex.matcher(s);
            String name = nameMatcher.find() ? nameMatcher.group().trim() : "NAME NOT FOUND";

            String trimmed = s.replaceAll("^.*?(?=\\w+,)", "");

            Unit unit = new Unit.Builder()
                    .name(name)
                    .movement(movement)
                    .toughness(toughness)
                    .save(save)
                    .wounds(wounds)
                    .leadership(leadership)
                    .objectiveControl(objectiveControl)
                    .weaponOptions(Arrays.asList(trimmed.split(", ")))
                    .build();
            if (mult > 1)
                addMultiples(units, unit, mult);
            else
                units.add(unit);
        });

        return units;
    }

    private void addMultiples(List<Unit> units, Unit unitToAdd, int multiple)
    {
        for (int i = 0; i < multiple; i++)
        {
            units.add(unitToAdd);
        }
    }
}
