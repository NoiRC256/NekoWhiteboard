package client.events;

import client.users.UserData;

public class ChatEntryEventArgs extends EventArgs{

    public final UserData sender;
    public final String content;

    public ChatEntryEventArgs(UserData sender, String content) {
        this.sender = sender;
        this.content = content;
    }
}
