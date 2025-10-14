package org.fortyK.reader;

import org.fortyK.model.Weapon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WeaponExtractor {
    private final static Pattern validatorRegex = Pattern.compile("^(^\\u27A4 )?([a-zA-Z\\-]+ )+(- [a-zA-Z]+ )?(\\(x\\d+\\) )?(Melee|\\d+\") D?\\d+ \\d+\\+ \\d+ (-\\d+|0) D?\\d+(\\+\\d+)? (-$|([a-zA-Z1-9\\-+ ],?)+$)");
    private final static Pattern multiplesRegex = Pattern.compile("(?<=\\(x)\\d+(?=\\))");
    private final static Pattern nameRegex = Pattern.compile("^(^\\u27A4 )?([a-zA-Z\\-]+ )+(- [a-zA-Z]+ )*?(?=Melee|\\d+\")");
    private final static Pattern baseNameRegex = Pattern.compile("(?<=^\\u27A4 )([a-zA-Z\\-]+ )*?(?=- )");
    private final static Pattern weaponStatRegex = Pattern.compile("(?<=\\D)Melee|(D?\\d+)(?=\\D|$)");
    private final String rangedWeaponTxt;
    private final String meleeWeaponTxt;

    public WeaponExtractor(String rangedWeaponTxt, String meleeWeaponTxt)
    {
        this.rangedWeaponTxt = rangedWeaponTxt;
        this.meleeWeaponTxt = meleeWeaponTxt;
    }

    public List<Weapon> generateWeapons()
    {
        List<Weapon> weapons = new ArrayList<>();

        //Pull Ranged Weapons
        pullWeapons(weapons, rangedWeaponTxt, true);

        //Pull Melee Weapons
        pullWeapons(weapons, meleeWeaponTxt, false);

        return weapons;
    }

    private void pullWeapons(List<Weapon> weapons, String rawText, boolean isRanged){
        List<String> problemLines = new ArrayList<>();
        List<String> weaponsWithAlt = new ArrayList<>();

        //Pull Weapons without an Alt
        rawText.lines().forEach(s -> {
            Matcher problemMatcher = validatorRegex.matcher(s);
            if (!problemMatcher.matches())
                problemLines.add(s);

            else if (Integer.toHexString(s.charAt(0) | 0x10000).substring(1).equals("27a4"))
                weaponsWithAlt.add(s);

            else{
                //Check if there are multiples for this weapon
                Matcher multiplesMatcher = multiplesRegex.matcher(s);
                int mult = multiplesMatcher.find() ? Integer.parseInt(multiplesMatcher.group()) : 1;

                //remove the multiplier from the string
                String trimmed = s.replaceAll("\\(x\\d+\\)\\s*", "");

                //Create Weapon
                Weapon weapon = createWeapon(trimmed, isRanged);

                if (mult > 1) //Add 1 if multiple was blank
                    addMultiples(weapons, weapon, mult);
                else //And the number specified by mult
                    weapons.add(weapon);
            }
        });

        //Create Weapons with an Alt
        while (!weaponsWithAlt.isEmpty())
        {
            //Create base weapon
            String baseWeaponTxt = weaponsWithAlt.getFirst();

            //Check if there are multiples for this weapon
            Matcher multiplesMatcher = multiplesRegex.matcher(baseWeaponTxt);
            int mult = multiplesMatcher.find() ? Integer.parseInt(multiplesMatcher.group()) : 1;

            //remove the multiplier from the string
            String trimmedBase = baseWeaponTxt.replaceAll("\\(x\\d+\\)\\s*", "");

            //Create base weapon and remove from list
            Weapon base = createWeapon(trimmedBase, isRanged);
            weaponsWithAlt.removeFirst();

            //Create Alt Weapons
            List<Weapon> altModes = new ArrayList<>();
            Matcher baseNameMatcher = baseNameRegex.matcher(baseWeaponTxt);
            String matchName = baseNameMatcher.find() ? baseNameMatcher.group() : "MATCH NAME NOT FOUND";

            List<String> toRemove = new ArrayList<>();
            for (String s : weaponsWithAlt)
            {
                if (s.contains(matchName))
                {
                    //remove the multiplier from the string
                    String trimmedAlt = s.replaceAll("\\(x\\d+\\)\\s*", "");
                    altModes.add(createWeapon(trimmedAlt, isRanged));
                    toRemove.add(s);
                }
            }
            weaponsWithAlt.removeAll(toRemove);
            base.setAltModes(altModes);

            if (mult > 1) //Add 1 if multiple was blank
                addMultiples(weapons, base, mult);
            else //And the number specified by mult
                weapons.add(base);
        }

        //Resolve Problem Lines
        if (!problemLines.isEmpty())
        {
            //TODO
        }
    }

    private Weapon createWeapon(String input, boolean isRanged)
    {
        //Pull name
        Matcher nameMatcher = nameRegex.matcher(input);
        String name = nameMatcher.find() ? nameMatcher.group().trim() : "NAME NOT FOUND";

        //Stats
        Matcher statMatcher = weaponStatRegex.matcher(input);
        String range = statMatcher.find() ? statMatcher.group() : "RANGE NOT FOUND";
        String attacks = statMatcher.find() ? statMatcher.group() : "ATTACKS NOT FOUND";
        int weaponOrBalisticSkill = statMatcher.find() ? Integer.parseInt(statMatcher.group()) : -1;
        int strength = statMatcher.find() ? Integer.parseInt(statMatcher.group()) : -1;
        int armourPenetration = statMatcher.find() ? Integer.parseInt(statMatcher.group()) : -1;
        String damage = statMatcher.find() ? statMatcher.group() : "DAMAGE NOT FOUND";

        //Get Keywords
        String keywordsString = input.replaceAll("^(^\\u27A4 )?([a-zA-Z\\-]+ )+(- [a-zA-Z]+ )?(\\(x\\d+\\) )?(Melee|\\d+\") D?\\d+ \\d+\\+ \\d+ (-\\d+|0) D?\\d+(\\+\\d+)? ","");
        List<String> keywords = Arrays.asList(keywordsString.split(", "));
        if (keywords.size() == 1 && keywords.getFirst().equals("-"))
            keywords = null;

        return new Weapon.WeaponBuilder()
                .name(name)
                .range(range)
                .attacks(attacks)
                .weaponOrBalisticSkill(weaponOrBalisticSkill)
                .strength(strength)
                .armourPenetration(armourPenetration)
                .damage(damage)
                .keywords(keywords)
                .isRanged(isRanged)
                .build();
    }

    private void addMultiples(List<Weapon> weapons, Weapon weapon, int multiple)
    {
        for (int i = 0; i < multiple; i++)
        {
            weapons.add(weapon);
        }
    }
}
