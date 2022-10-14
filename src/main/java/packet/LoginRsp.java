package packet;

import java.io.Serializable;

public class LoginRsp implements Serializable {

    public String username;
    public int uid;

    public LoginRsp(String username, int uid){
        this.username = username;
        this.uid = uid;
    }

}
