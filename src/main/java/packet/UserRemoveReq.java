package packet;

import client.users.UserData;

import java.io.Serializable;

public class UserRemoveReq extends Message implements Serializable {

    public final UserData senderUserData;
    public final UserData userData;

    public UserRemoveReq(UserData senderUserData, UserData userData) {
        this.senderUserData = senderUserData;
        this.userData = userData;
    }
}
