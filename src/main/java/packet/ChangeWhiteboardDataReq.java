package packet;

import client.users.UserRole;
import server.WhiteboardData;

import java.io.Serializable;

/**
 * Request to set new server-side whiteboard data.
 */
public class ChangeWhiteboardDataReq extends Message implements Serializable {

    public final int uid;
    public final byte[] bufferBytes;

    public ChangeWhiteboardDataReq(int uid, byte[] bufferBytes) {
        this.uid = uid;
        this.bufferBytes = bufferBytes;
    }
}
