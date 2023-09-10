package client.chat;

import java.time.LocalDateTime;

public class ChatMessage {

    public final int id;
    public final int uid;
    public final String username;
    public final String content;
    public LocalDateTime dateTime;

    public ChatMessage(int id, int uid, String username, String content, LocalDateTime dateTime){
        this.id = id;
        this.uid = uid;
        this.username = username;
        this.content = content;
        this.dateTime = dateTime;
    }

}
