package org.fortyK.reader;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.fortyK.model.Army;
import org.fortyK.model.Squad;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Extractor {

    private static final Pattern detachmentRegex = Pattern.compile("DETACHMENT[\\s\\S]*?(?=\\d{2,} PTS)");
    private static final Pattern squadRegex = Pattern.compile("(\\d{2,})\\sPTS[\\s\\S]*?(?=\\d{2,} PTS)");

    public Extractor(){}

    public static Army loadArmy(String armyName, String filepath)
    {
        Army army = new Army(armyName);

        //Scrape txt from pdf and clean out all non ASNI characters
        String rawText = extractStringFromPDF(filepath);
        String cleanText = rawText
                .replaceAll("\\s.*https[\\s\\S]*?Page \\d+ of \\d+\\s", "")
                .replaceAll("Army Roster[\\s\\S]*","")
                .replaceAll("[^\\x00-\\x7F] ", "");

        //Extract Detachment info
        Matcher detachmentMatcher = detachmentRegex.matcher(cleanText);
        DetachmentExtractor detachmentExtractor = detachmentMatcher.find()
                ? new DetachmentExtractor(detachmentMatcher.group())
                : new DetachmentExtractor("DETACHMENT NAME NOT FOUND"); //Will probably have this throw an error later or something, for now this is fine
        army.setDetachment(detachmentExtractor.extractDetachment());

        //Extract Squad info
        Matcher squadMatcher = squadRegex.matcher(cleanText);
        List<String> squadTxtBoxes = new ArrayList<>();
        squadMatcher.results().forEach(mr -> squadTxtBoxes.add(mr.group()));
        SquadExtractor squadExtractor = new SquadExtractor(squadTxtBoxes);
        army.setSquads(squadExtractor.extractSquad());

        return army;
    }

    public static String extractStringFromPDF(String filePath) throws RuntimeException
    {
        try
        {
            PDDocument document = Loader.loadPDF(new File(filePath));
            PDFTextStripper textStripper = new PDFTextStripper();
            textStripper.setSortByPosition(true);
            return textStripper.getText(document);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
