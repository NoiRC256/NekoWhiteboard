package packet;

import client.users.UserData;

import java.io.Serializable;

public class UserLeaveNotify extends Message implements Serializable {

    public final int uid;

    public UserLeaveNotify(int uid) {
        this.uid = uid;
    }
}
