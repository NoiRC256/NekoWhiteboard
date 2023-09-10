package packet;

import server.WhiteboardData;

import java.io.Serializable;

/**
 * Message that contains the whiteboard buffered image.
 * Typically sent to newly joined users to sync their whiteboard with the current server-side state.
 */
public class WhiteboardDataNotify extends Message implements Serializable {

    public final byte[] bufferBytes;

    public WhiteboardDataNotify(byte[] bufferBytes) {
        this.bufferBytes = bufferBytes;
    }
}
