package org.freakz.pmud.common.message;

import java.io.Serializable;

public class PMudMessage implements Serializable {

    private final long pid;
    private String message;
    private String player;


    public PMudMessage(String message, String player) {
        this.pid = ProcessHandle.current().pid();
        this.message = message;
        this.player = player;
    }

    @Override
    public String toString() {
        return String.format("[%06d] Player: %s - Message: %s", pid, player, message);
    }

    public long getPid() {
        return pid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

}
