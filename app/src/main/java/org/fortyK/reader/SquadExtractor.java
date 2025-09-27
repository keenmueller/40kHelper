package org.fortyK.reader;

import org.fortyK.model.Squad;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SquadExtractor {
    private final List<String> txtBoxes;
    private final static Pattern nameRegex = Pattern.compile("(?<=\\d{2,} PTS ).*");

    public SquadExtractor(List<String> txtBoxes)
    {
        this.txtBoxes = txtBoxes;
    }

    public List<Squad> extractSquad()
    {
        List<Squad> squads = new ArrayList<>();
        for (String txt : txtBoxes)
        {
            Matcher nameMatcher = nameRegex.matcher(txt);
            String name = nameMatcher.find() ? nameMatcher.group() : "";
            Squad squad = new Squad(name);
            squads.add(squad);
        }
        return squads;
    }
}
