package org.freakz.pmud.common.message;

import java.io.Serializable;

public class PMudQuitClientMessage extends ToClientsMessageBase implements Serializable {

    public PMudQuitClientMessage(long pid) {
        setReplyToPid(pid);
    }
}
