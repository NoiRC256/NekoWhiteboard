package packet;

import client.users.UserData;

import java.io.Serializable;

public class ChatEntryNotify extends Message implements Serializable {

    public final UserData sender;
    public final String content;

    public ChatEntryNotify(UserData sender, String content){
        this.sender = sender;
        this.content = content;
    }
}
