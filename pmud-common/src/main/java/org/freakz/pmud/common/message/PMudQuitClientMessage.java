package org.freakz.pmud.common.message;

import java.io.Serializable;

public class PMudQuitClientMessage extends ToClientsMessageBase implements Serializable {

    private String quitMessage;

    public PMudQuitClientMessage(long pid) {
        setReplyToPid(pid);
    }

    public PMudQuitClientMessage(long pid, String quitMessage) {
        setReplyToPid(pid);
        this.quitMessage = quitMessage;
    }

    public String getQuitMessage() {
        return quitMessage;
    }

    public void setQuitMessage(String quitMessage) {
        this.quitMessage = quitMessage;
    }
}
