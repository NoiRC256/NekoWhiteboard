package packet;

import client.users.UserData;

import java.io.Serializable;

/**
 * Message that contains an array of all active users.
 */
public class ActiveUsersNotify extends Message implements Serializable {

    public final UserData[] userDatas;

    public ActiveUsersNotify(UserData[] userDatas) {
        this.userDatas = userDatas;
    }
}
