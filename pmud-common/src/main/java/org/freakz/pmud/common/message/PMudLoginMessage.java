package org.freakz.pmud.common.message;

import java.io.Serializable;

public class PMudLoginMessage implements Serializable {

    private long pid;
    private String player;
    private String password;

    public PMudLoginMessage(String player, String password, long myPid) {
        this.pid = myPid;
        this.player = player;
        this.password = password;
    }

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
