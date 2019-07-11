package org.freakz.pmud.common.message;

import java.io.Serializable;

public class ToClientsMessageBase implements Serializable {

    private long replyToPid;

    public long getReplyToPid() {
        return replyToPid;
    }

    public void setReplyToPid(long replyToPid) {
        this.replyToPid = replyToPid;
    }

}
