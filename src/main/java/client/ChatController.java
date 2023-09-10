package client;

import client.users.UserData;
import com.jgoodies.forms.layout.CellConstraints;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChatController {

    MainFrame view = MainFrame.getInstance();

    public void init(){
        view.chatContentPanel.setLayout(new BoxLayout(view.chatContentPanel, BoxLayout.Y_AXIS));

        view.sendChatBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserData sender = Main.getInstance().userData;
                String content = view.charTextPane.getText();
                if(content.isEmpty()) return;
                addChatEntry(sender, content);
                Main.getInstance().broadcastChatEntry(sender, content);
            }
        });
    }

    public void addChatEntry(UserData sender, String content){
        String chatEntry = new String(sender.username + ": " + content);

        JLabel chatEntryLabel = new JLabel(chatEntry);
        view.chatContentPanel.add(chatEntryLabel);
        view.chatContentPanel.validate();
    }

}
