package org.freakz.pmud.pmudserver.objects;

public abstract class PMudObject {

    private static int objectCounter = 0;

    private final int id;

    public PMudObject() {
        this.id = ++objectCounter;
    }

    public static int getObjectCounter() {
        return objectCounter;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("[%06d] %s", id, this.getClass().getCanonicalName());
    }

}
