package org.freakz.pmud.pmudclient.common.message;

import java.io.Serializable;

public class PMudMessage implements Serializable {

    private final long pid;
    private String message;
    private String target;

    public PMudMessage(String message) {
        this.pid = ProcessHandle.current().pid();
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getPid() {
        return pid;
    }
}
