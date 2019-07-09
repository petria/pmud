package org.freakz.pmud.pmudserver.service;

import lombok.extern.slf4j.Slf4j;
import org.freakz.pmud.common.enums.PClass;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ScoreAndLevelsService {


    public class Level {
        public int level;
        public int minScore;
        public Map<PClass, String> classTitle = new HashMap<>();

        public String title(int num) {
            return classTitle.get(PClass.values()[num]);
        }
    }

    public class Levels {
        List<Level> levels = new ArrayList<>();

        void addLevel(int levelNum, int minScore, String... titles) {
            Level level = new Level();
            level.level = levelNum;
            level.minScore = minScore;
            for (int i = 0; i < titles.length; i++) {
                level.classTitle.put(PClass.values()[i], titles[i]);
            }
            levels.add(level);
        }

        public List<Level> getLevels() {
            return levels;
        }
    }

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
}
