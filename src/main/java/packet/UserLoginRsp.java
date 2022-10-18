package packet;

import java.io.Serializable;

/**
 * Response from server to user trying to login.
 */
public class UserLoginRsp extends Message implements Serializable {

    public int uid;

    public UserLoginRsp(int uid) {
        this.uid = uid;
    }
}
