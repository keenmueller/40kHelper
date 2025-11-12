package org.fortyK.model;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public enum Ability {
    //TODO: A lot of these will need to be flushed out once I figure out how the game flow will work
    ACROBATIC_ESCAPE("Acrobatic Escape", 0, "Who knows", 0), //This thing has an essay attached to it
    ANGELIC_VISAGE("Angelic Visage", 3, "Hit", -1), //When targeted by a melee attack, subtract 1 from hit roll
    ATTACHED_UNIT("Attached Unit", 0, "bleh", 0), //yeahhhh I don't care about this ability
    BLACK_RAGE("Black Rage", 3, "Melee", 0), //re-roll the hit roll, call fall back when 6" from Blood Angels Character or 12" from Chaplain -> OC becomes 0
    BLADEGUARD("Bladeguard", 3, "???", 0), //This one lets you choose between two abilities which is going to be super fun to figure out
    BRUTALIS_CHARGE("Brutalis Charge", 2, "Enemy", 3), //Each time this model ends a Charge move, select one enemy unit within Engagement Range of it and roll one D6: on a 2-3, that enemy unit suffers D3 mortal wounds; on a 4-5, that enemy unit suffers 3 mortal wounds; on a 6, that enemy unit suffers D3+3 mortal wounds
    DAMAGED_4("Damaged: 1-4 Wounds Remaining", 3, "Attack", 1), //While this model has 1-4 wounds remaining, each time this model makes an attack, subtract 1 from the Hit roll
    DAMAGED_5("Damaged: 1-5 Wounds Remaining", 3, "Attack", 1),//While this model has 1-5 wounds remaining, each time this model makes an attack, subtract 1 from the Hit roll
    DEATH_MASK_OF_SANGUINIUS("Death Mask of Sanguinius", 2, "Battle-shock test", -1),
    INVULNERABLE_SAVE("Invulnerable Save", 3, "Save", 4),
    EXECUTIONER("Executioner", 3, "Hit", 1), //Each time this model makes an attack that targets a unit that is Below Half-strength, add 1 to the Hit roll.
    EXHORTATION_OF_RAGE("Exhortation of Rage", 3, "Enemy", 3), //Select an enemy in range, roll a D6, on a 4-5 roll D3 mortal wounds, on a 6 enemy takes 3 mortal wounds
    FINEST_HOUR("Finest Hour", 3, "Attacks", 3), //Also Adds Devastating Wounds to melee weapons, can only be done once per game
    FURY_UNBOUND("Fury Unbound", 0, "Keyword", 0), //Adds Lethal Hits to Squad's Melee weapons
    GUARDIAN_OF_THE_LOST("Guardian of the Lost", 3, "Damage", -1),
    HEIRS_OF_AZKAELLON("Heirs of Azkaellon", 3, "Wounds", -1), //If a Character is leading this unit, When targeted by a melee attack, subtract 1 from Wound roll
    LITANY_OF_HATE("Litany of Hate", 3, "Wound", 1),
    LORD_OF_DECEIT("Lord of Deceit", 2, "Stratagem", 1), //When an opponent targets one of their units with a strategem, increase the cost by 1 CP
    LORD_REGENT_OF_THE_IMPERIUM_NIHILUS("Lord Regent of the Imperium Nihilus", 0, "Advance, Charge, and Hit", 1),
    LEADER("Leader", 0, "I'll do this later", 0), //I don't really care about this one right now until I have a basic game flow working
    OBJECTIVE_SECURED("Objective Secured", 2, "Objective", 0), //Sticky objective
    RITES_OF_BATTLE("Rites of Battle", 3, "Stratagem", -1), //Happens when the unit is targeted
    SAVAGE_FURY("Savage Fury", 2, "Charge", 0), //re-roll charge rolls
    SHADOW_ASSIGNMENT("Shadow Assignment", 0, "Nope", 0), //I doubt I'll ever come back to sort this one out
    SHOCK_ASSAULT("Shock Assault", 3, "Wound", 0), //re-roll wound 1 rolls, or if in range of an objective re-roll wound roll
    SPEED_OF_THE_PRIMARCH("Speed of the Primarch", 3, "Ability", 0), //Lets every model in the unit use Fights First, can only be done once per game
    TARGET_ELIMINATION("Target Elimination", 3, "Attacks", 2), //Bolt Rifle only, can only select 1 target
    TOTAL_OBLITERATION("Total Obliteration", 3, "Hit, Wound, and Damage", 0), //re-roll Hit, Wound, and Damage when targeting Monster or Vehicle
    TRANSPORT("Transport", 0, "I'll do this later", 0) //I'll get around to this eventually
    ;

    private final String name;
    private final int step; //Placeholder until I can flush out how turns work
    private final String target; //Keeping as String for now but will probably make some type of class for this
    private final int modifier; //May be rolled into the same object as "target" because there can be multiple modifications
    private static final Map<String, Ability> NAME_LOOKUP = EnumSet.allOf(Ability.class).stream().collect(Collectors.toMap(Ability::getName, v -> v));
    public static final Set<String> ALL_NAMES = EnumSet.allOf(Ability.class).stream().map(Ability::getName).collect(Collectors.toSet());

    Ability(String name, int step, String target, int modifier){
        this.name = name;
        this.step = step;
        this.target = target;
        this.modifier = modifier;
    }

    public String getName() {
        return name;
    }

    public int getStep() {
        return step;
    }

    public String getTarget() {
        return target;
    }

    public int getModifier() {
        return modifier;
    }

    public static Ability fromName(String name) {
        return NAME_LOOKUP.get(name);
    }
}
