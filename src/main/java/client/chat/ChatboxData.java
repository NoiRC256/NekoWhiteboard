package client.chat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class ChatboxData {

    private int idCount = 1;
    private ArrayList<ChatMessage> messages = new ArrayList<ChatMessage>();

    public void addMessage(ChatMessage message){
        messages.add(message);
    }

    /**
     * Remove chat message.
     * @param message chat message to remove.
     */
    public void removeMessage(ChatMessage message){
        messages.remove(message);
    }

    /**
     * Remove chat message by message id.
     * @param id int message id.
     */
    public void removeMessageById(int id){
        messages.removeIf(m -> (m.id == id));
    }

    /**
     * Remove chat message by sender uid.
     * @param uid int sender uid.
     */
    public void removeMessageByUid(int uid){
        messages.removeIf(m -> (m.uid == uid));
    }

    public ArrayList<ChatMessage> getMessages(){
        return this.messages;
    }

}
