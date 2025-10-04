package org.fortyK.reader;

import org.fortyK.model.Weapon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WeaponExtractor {
    private final static Pattern validatorRegex = Pattern.compile("^(^\\u27A4 )?([a-zA-Z]+ )+(- [a-zA-Z]+ )?(\\(x\\d+\\) )?(Melee|\\d+\") D?\\d+ \\d+\\+ \\d (-\\d+|0) D?\\d+ (-$|([a-zA-Z1-9\\-]+( [a-zA-Z1-9\\-+]+)?)$|(([a-zA-Z1-9\\-]+( [a-zA-Z1-9\\-+]+)?, )+)([a-zA-Z1-9\\-]+ ?)+$)");
    private final static Pattern multiplesRegex = Pattern.compile("(?<=\\(x)\\d+(?=\\))");
    private final static Pattern nameRegex = Pattern.compile("^([a-zA-Z]+(-|\\s))*?(?=Melee|\\(|\\d)");
    private final static Pattern weaponStatRegex = Pattern.compile("(?<=\\D)Melee|(D?\\d+)(?=\\D|$)");
    private final static Pattern keywordRegex = Pattern.compile("(?<=\\d|D\\d)(\\w+\\s\\d?,?)*?");
    private final static Pattern altWeaponGroupRegex = Pattern.compile("(^\\u27A4.*\n){2}");
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

    private void addMultiples(List<Weapon> weapons, Weapon weapon, int multiple)
    {
        for (int i = 0; i < multiple; i++)
        {
            weapons.add(weapon);
        }
    }

    private void pullWeapons(List<Weapon> weapons, String rawText, boolean isRanged){
        //Pull Weapons without an Alt
        rawText.lines().forEach(s -> {
            if (!Integer.toHexString(s.charAt(0) | 0x10000).substring(1).equals("27a4")){
                //Check if there are multiples for this unit
                Matcher multiplesMatcher = multiplesRegex.matcher(s);
                int mult = multiplesMatcher.find() ? Integer.parseInt(multiplesMatcher.group()) : 1;

                //Pull name
                Matcher nameMatcher = nameRegex.matcher(s);
                String name = nameMatcher.find() ? nameMatcher.group().trim() : "NAME NOT FOUND";

                //remove the multiplier from the string
                String trimmed = s.replaceAll("\\(x\\d+\\)\\s*", "");

                //Stats no alt
                Matcher statMatcher = weaponStatRegex.matcher(trimmed);
                String range = statMatcher.find() ? statMatcher.group() : "RANGE NOT FOUND";
                String attacks = statMatcher.find() ? statMatcher.group() : "ATTACKS NOT FOUND";
                int weaponOrBalisticSkill = statMatcher.find() ? Integer.parseInt(statMatcher.group()) : -1;
                int strength = statMatcher.find() ? Integer.parseInt(statMatcher.group()) : -1;
                int armourPenetration = statMatcher.find() ? Integer.parseInt(statMatcher.group()) : -1;
                String damage = statMatcher.find() ? statMatcher.group() : "DAMAGE NOT FOUND";

                //Get Keywords
                Matcher keywordMatcher = keywordRegex.matcher(trimmed);
                List<String> keywords = keywordMatcher.find() ? Arrays.asList(keywordMatcher.group().split(", ")) : new ArrayList<>();

                //Create Weapon
                Weapon weapon = new Weapon.WeaponBuilder()
                        .name(name)
                        .range(range)
                        .attacks(attacks)
                        .weaponOrBalisticSkill(weaponOrBalisticSkill)
                        .strength(strength)
                        .armourPenetration(armourPenetration)
                        .damage(damage)
                        .keywords(keywords)
                        .isRanged(isRanged)
                        .altMode(null)
                        .build();

                if (mult > 1) //Add 1 if multiple was blank
                    addMultiples(weapons, weapon, mult);
                else //And the number specified by mult
                    weapons.add(weapon);
            }
        });

        //Pull Weapons with an Alt
    }
}
