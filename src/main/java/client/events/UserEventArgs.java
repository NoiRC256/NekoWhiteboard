package client.events;

import client.users.UserData;

public class UserEventArgs extends EventArgs{

    public final UserData userData;

    public UserEventArgs(UserData userData){
        this.userData = userData;
    }
}
