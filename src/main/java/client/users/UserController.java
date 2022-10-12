package client.users;

import client.MainFrame;

import javax.swing.*;

public class UserController {

    public MainFrame view = MainFrame.getInstance();
    public User user;

    public UserController(){
        user = new User();
    }

    public void init(){

    }

    public void setName(String name){
        user.name = name;
    }
}
