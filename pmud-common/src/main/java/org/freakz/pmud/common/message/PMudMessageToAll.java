package org.freakz.pmud.common.message;

import java.io.Serializable;

public class PMudMessageToAll implements Serializable {

    private String message;

    private long exceptPid;

    public PMudMessageToAll(String message, long exceptPid) {
        this.message = message;
        this.exceptPid = exceptPid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getExceptPid() {
        return exceptPid;
    }

    public void setExceptPid(long exceptPid) {
        this.exceptPid = exceptPid;
    }
}
