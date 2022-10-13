package server.packet;

public class LoginRsp {

    public String username;
    public int uid;

    public LoginRsp(String username, int uid){
        this.username = username;
        this.uid = uid;
    }

}
