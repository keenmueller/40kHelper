package org.fortyK.reader;

import org.fortyK.model.Unit;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UnitExtractor {
    private final static Pattern nameRegex = Pattern.compile("^([a-zA-Z]+\\s)*");
    private final static Pattern unitStatRegex = Pattern.compile("(?<=\\D)(\\d+)(?=\\D|$)");
    private final String unitsTxt;

    public UnitExtractor(String unitsTxt)
    {
        this.unitsTxt = unitsTxt;
    }

    public List<Unit> generateUnits()
    {
        List<Unit> units = new ArrayList<>();

        //Pull Units
        unitsTxt.lines().forEach(s -> {
            //Pull name
            Matcher nameMatcher = nameRegex.matcher(s);
            String name = nameMatcher.find() ? nameMatcher.group().trim() : "NAME NOT FOUND";

            //remove the multiplier from the string
            String trimmed = s.replaceAll("\\(x\\d+\\)\\s*", "");

            //Grab stats
            Matcher statMatcher = unitStatRegex.matcher(trimmed);
            int movement = statMatcher.find() ? Integer.parseInt(statMatcher.group()) : -1;
            int toughness = statMatcher.find() ? Integer.parseInt(statMatcher.group()) : -1;
            int save = statMatcher.find() ? Integer.parseInt(statMatcher.group()) : -1;
            int wounds = statMatcher.find() ? Integer.parseInt(statMatcher.group()) : -1;
            int leadership = statMatcher.find() ? Integer.parseInt(statMatcher.group()) : -1;
            int objectiveControl = statMatcher.find() ? Integer.parseInt(statMatcher.group()) : -1;

            //Create Unit
            Unit unit = new Unit.UnitBuilder()
                    .name(name)
                    .movement(movement)
                    .toughness(toughness)
                    .save(save)
                    .wounds(wounds)
                    .leadership(leadership)
                    .objectiveControl(objectiveControl)
                    .build();

            units.add(unit);
        });

        return units;
    }
}
