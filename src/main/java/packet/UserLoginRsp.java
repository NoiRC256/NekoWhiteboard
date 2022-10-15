package packet;

import java.io.Serializable;

public class UserLoginRsp extends Message implements Serializable  {

    public int uid;

    public UserLoginRsp(int uid){
        this.uid = uid;
    }
}
