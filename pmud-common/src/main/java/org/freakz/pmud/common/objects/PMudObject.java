package org.freakz.pmud.common.objects;

import java.io.Serializable;

public abstract class PMudObject implements Serializable {

    private static int objectCounter = 0;

    private final int id;

    private int visibilityLevel = 0;

    protected String name;


    public PMudObject() {
        this.id = ++objectCounter;
    }

    public static int getObjectCounter() {
        return objectCounter;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("[%06d] %s", id, this.getClass().getCanonicalName());
    }

    public int getVisibilityLevel() {
        return this.visibilityLevel;
    }

    public void setVisibilityLevel(int visibilityLevel) {
        this.visibilityLevel = visibilityLevel;
    }
}
