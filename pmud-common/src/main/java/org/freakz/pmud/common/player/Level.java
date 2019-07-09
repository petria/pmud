package org.freakz.pmud.common.player;

import org.freakz.pmud.common.enums.PClass;

import java.util.HashMap;
import java.util.Map;

public class Level {
    private int level;
    private int minScore;
    private Map<PClass, String> classTitle = new HashMap<>();

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getMinScore() {
        return minScore;
    }

    public void setMinScore(int minScore) {
        this.minScore = minScore;
    }

    public Map<PClass, String> getClassTitle() {
        return classTitle;
    }

    public void setClassTitle(Map<PClass, String> classTitle) {
        this.classTitle = classTitle;
    }

    public String title(int num) {
        return this.classTitle.get(PClass.values()[num]);
    }
}

