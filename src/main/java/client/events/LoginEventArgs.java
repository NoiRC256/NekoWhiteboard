package client.events;

import client.users.User;
import client.users.UserData;

public class LoginEventArgs extends EventArgs{

    public final UserData userData;

    public LoginEventArgs(UserData userData){
        this.userData = userData;
    }
}
