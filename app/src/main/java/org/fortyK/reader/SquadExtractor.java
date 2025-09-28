package org.fortyK.reader;

import org.fortyK.model.Squad;
import org.fortyK.model.Unit;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SquadExtractor {
    private final static Pattern nameRegex = Pattern.compile("(?<=\\d{2,} PTS ).*");
    private final static Pattern modelsRegex = Pattern.compile("(?<=Options\\s\n)[\\s\\S]*?(?=Unit)");
    private final static Pattern unitsRegex = Pattern.compile("(?<=OC\\s\n)[\\s\\S]*?(?=Ranged)");
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

            //Find Models/Units with Stats
            Matcher modelsMatcher = modelsRegex.matcher(txt);
            Matcher unitsMatcher = unitsRegex.matcher(txt);
            String modelsTxt = modelsMatcher.find() ? modelsMatcher.group() : "MODELS NOT FOUND";
            String unitsTxt = unitsMatcher.find() ? unitsMatcher.group() : "UNITS NOT FOUND";
            UnitExtractor unitExtractor = new UnitExtractor(modelsTxt, unitsTxt);
            squad.setUnits(unitExtractor.generateUnits());

            squads.add(squad);
        }
        return squads;
    }
}
