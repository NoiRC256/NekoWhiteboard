package packet;

import java.io.Serializable;

public class UserJoinRsp extends Message implements Serializable {

    public int uid;
    public String username;

    public UserJoinRsp(int uid, String username){
        this.uid = uid;
        this.username = username;
    }
}
