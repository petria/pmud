package org.freakz.pmud.common.player;

import org.freakz.pmud.common.enums.PClass;

import java.util.ArrayList;
import java.util.List;

public class Levels {
    List<Level> levels = new ArrayList<>();

    public void addLevel(int levelNum, int minScore, String... titles) {
        Level level = new Level();
        level.setLevel(levelNum);
        level.setMinScore(minScore);
        for (int i = 0; i < titles.length; i++) {
            level.getClassTitle().put(PClass.values()[i], titles[i]);
        }
        levels.add(level);
    }

    public List<Level> getLevels() {
        return levels;
    }
}