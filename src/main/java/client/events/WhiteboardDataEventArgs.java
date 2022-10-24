package client.events;

import server.WhiteboardData;

public class WhiteboardDataEventArgs extends EventArgs {

    public final WhiteboardData whiteboardData;

    public WhiteboardDataEventArgs(WhiteboardData whiteboardData) {
        this.whiteboardData = whiteboardData;
    }
}
