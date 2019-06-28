package org.freakz.pmud.pmudserver.objects;

public abstract class PMudObject {

    private static int objectCounter = 0;

    private final int id;

    private String name;


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

}
