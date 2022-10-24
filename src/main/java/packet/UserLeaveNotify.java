package packet;

import client.users.UserData;
import client.users.UserRole;

import java.io.Serializable;

public class UserLeaveNotify extends Message implements Serializable {

    public final UserData userData;

    public UserLeaveNotify(UserData userData) {
        this.userData = userData;
    }
}
