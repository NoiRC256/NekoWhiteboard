package packet;

import client.users.UserData;

import java.io.Serializable;

public class UserListNotify extends Message implements Serializable {

    public final UserData[] userDatas;

    public UserListNotify(UserData[] userDatas) {
        this.userDatas =userDatas;
    }

}
