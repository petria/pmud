package org.freakz.pmud.common.message;

import java.io.Serializable;

public class PMudLoginReplyMessage extends ToClientsMessageBase implements Serializable {

    public enum Result {
        OK,
        ERROR_WRONG_PASSWORD;
    }

    private final String player;
    private Result result;

    public PMudLoginReplyMessage(String player) {
        this.player = player;
    }

    public String getPlayer() {
        return player;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }
}
