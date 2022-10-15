package packet;

import java.io.Serializable;

public class UserJoinReq extends Message implements Serializable  {

    public int uid;
    public String username;

    public UserJoinReq(int uid, String username) {
        this.uid = uid;
        this.username = username;
    }
}
