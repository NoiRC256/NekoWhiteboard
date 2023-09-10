package client.users;

import client.Main;
import client.MainFrame;
import com.jgoodies.forms.layout.CellConstraints;

import javax.swing.*;
import javax.swing.plaf.synth.SynthTextAreaUI;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserController {

    public MainFrame view = MainFrame.getInstance();

    public UserController(){
    }

    public void init(){
        view.usersPanel.setLayout(new BoxLayout(view.usersPanel, BoxLayout.Y_AXIS));
    }

    public void addUser(UserData userData){
        System.out.println("Trying to add user button for UID " + userData.uid);
        JButton userBtn = new JButton();
        userBtn.setText(userData.username);
        CellConstraints cc = new CellConstraints();
        int row = view.userBtns.size() + 1;
        view.usersPanel.add(userBtn);
        view.usersPanel.validate();
        view.userBtns.put(userData.uid, userBtn);
        userBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Remove user.
                if(Main.getInstance().userRole == UserRole.Manager){
                    System.out.println("Request to remove user " + userData.uid);
                    Main.getInstance().reqRemoveUser(userData);
                }
            }
        });
    }

    public void removeUser(UserData userData){
        System.out.println("Trying to remove user button for UID " + userData.uid);
        JButton userBtn = view.userBtns.getOrDefault(userData.uid, null);
        if(userBtn != null){
            System.out.println("Removed user button for UID " + userData.uid);
            view.usersPanel.remove(userBtn);
            view.usersPanel.revalidate();
            view.usersPanel.repaint();
            view.usersPanel.validate();
        }
    }
}
