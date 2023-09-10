package packet;

import client.users.UserData;

import java.io.Serializable;

/**
 * Notification when a user has joined the session.
 */
public class UserJoinNotify extends Message implements Serializable {

    public final UserData userData;

    public UserJoinNotify(UserData userData){
        this.userData = userData;
    }
}
