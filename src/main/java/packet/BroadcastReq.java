package packet;

import java.io.Serializable;

/**
 * Request server to broadcast a message.
 */
public class BroadcastReq extends Message implements Serializable {

    public final Message message;
    public final boolean ignoreSource;

    public BroadcastReq(Message message, boolean ignoreSource){
        this.message = message;
        this.ignoreSource = ignoreSource;
    }
}
