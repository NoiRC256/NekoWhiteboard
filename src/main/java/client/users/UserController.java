package client.users;

import client.MainFrame;
import com.jgoodies.forms.layout.CellConstraints;

import javax.swing.*;

public class UserController {

    public MainFrame view = MainFrame.getInstance();

    public UserController(){
    }

    public void init(){
        view.usersPanel.setLayout(new BoxLayout(view.usersPanel, BoxLayout.Y_AXIS));

    }

    public void addUser(UserData userData){
        JButton userBtn = new JButton();
        userBtn.setText(userData.username);
        CellConstraints cc = new CellConstraints();
        int row = view.userBtns.size() + 1;
        view.usersPanel.add(userBtn);
        view.usersPanel.validate();
        view.userBtns.add(userBtn);
    }
}
