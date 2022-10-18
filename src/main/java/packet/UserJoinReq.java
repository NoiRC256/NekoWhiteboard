package packet;

import client.users.UserData;

import java.io.Serializable;

/**
 * Request from user trying to join a session.
 */
public class UserJoinReq extends Message implements Serializable  {

    public final UserData userData;

    public UserJoinReq(UserData userData) {
        this.userData = userData;
    }
}
