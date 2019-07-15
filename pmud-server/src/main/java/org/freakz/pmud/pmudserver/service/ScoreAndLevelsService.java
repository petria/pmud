package org.freakz.pmud.pmudserver.service;

import lombok.extern.slf4j.Slf4j;
import org.freakz.pmud.common.enums.PClass;
import org.freakz.pmud.common.objects.PMudPlayer;
import org.freakz.pmud.common.player.Level;
import org.freakz.pmud.common.player.Levels;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ScoreAndLevelsService {

    private static final int MAX_LEVEL = 21;

    private Levels levels = new Levels();

    public ScoreAndLevelsService() {
        initLevels();
    }

    private void initLevels() {
        levels.addLevel(1, 0, "Private", "Novice", "Footpad", "Acolyte");
        levels.addLevel(2, 2000, "Soldier", "Trickster", "Cutpurse", "Adept");
        levels.addLevel(3, 4000, "Mercenary", "Cabalist", "Robber", "Priest");
        levels.addLevel(4, 8000, "Vetran", "Visionist", "Burglar", "Curate");
        levels.addLevel(5, 14000, "Warrior", "Phantasmist", "Filcher", "Canon");
        levels.addLevel(6, 22000, "Swordsman", "Shadowist", "Sharper", "Low Lama");
        levels.addLevel(7, 36000, "Hero", "Spellbinder", "Magsman", "Lama");
        levels.addLevel(8, 42000, "Myrmidon", "Illusionist", "Rogue", "High Lama");
        levels.addLevel(9, 56000, "Champion", "Evoker", "High Rogue", "Great Lama");
        levels.addLevel(10, 72000, "Superhero", "Conjurer", "Chief Rogue", "Patriarch");
        levels.addLevel(11, 90000, "Knight", "Theurgist", "Prime Rogue", "Priest(1st)");
        levels.addLevel(12, 110000, "Guardian", "Thaumaturge", "Low Thief", "Priest(2nd)");
        levels.addLevel(13, 132000, "Legend", "Magician", "Thief(1st)", "Priest(3rd)");
        levels.addLevel(14, 156000, "Baron", "Enchanter", "Thief(2nd)", "Priest(4th)");
        levels.addLevel(15, 182000, "Duke", "Warlock", "Thief(3rd)", "Priest(5th)");
        levels.addLevel(16, 210000, "Lord(1st)", "Mage(1st)", "Thief(4th)", "High Priest");
        levels.addLevel(17, 240000, "Lord(2nd)", "Mage(2nd)", "Thief(5th)", "Saint");
        levels.addLevel(18, 272000, "Lord(3rd)", "Mage(3rd)", "High Thief", "Angel(1st)");
        levels.addLevel(19, 308000, "Lord(4th)", "Mage(4th)", "Executioner", "Angel(2nd)");
        levels.addLevel(20, 322000, "Lord(5th)", "Mage(5th)", "Assasin", "Angel(3rd)");
        levels.addLevel(21, 356000, "High Lord", "Wizard", "Guildmaster", "ArchAngel");
        //Level level = createLevel(0, "");
    }

    public List<Level> getLevels() {
        return levels.getLevels();
    }

    public Level setLevels(PMudPlayer player) {
        int oldLevel = player.getLevelNum();

        Level level = findLevel(player.getScore());
        player.setLevelNum(level.getLevel());
        PClass pClass = player.getpClass();
        String name = level.getClassTitle().get(pClass);
        player.setTitle("the " + name);

        int newLevel = player.getLevelNum();
        int maxStrength = 75 + ((newLevel - 1) * 8);
        int maxMana = 15 + ((newLevel - 1) * 3);

        player.setMaxStrength(maxStrength);
        player.setMaxMana(maxMana);
        if (newLevel != oldLevel) {
            log.debug("{} level {} -> {}", player.getName(), oldLevel, newLevel);
            return level;
        }

        return null;
    }

    private Level findLevel(int score) {
        for (Level level : getLevels()) {
            if (score > level.getMinScore()) {
                continue;
            }
            return level;
        }
        return getLevels().get(MAX_LEVEL);
    }

}
