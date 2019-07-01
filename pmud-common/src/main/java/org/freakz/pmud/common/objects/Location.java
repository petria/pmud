package org.freakz.pmud.common.objects;

import java.io.Serializable;

public class Location extends PMudObject implements Serializable {

    private String title;
    private String description;

    public Location() {
        super();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
