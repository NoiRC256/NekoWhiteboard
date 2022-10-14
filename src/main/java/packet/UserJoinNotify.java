package packet;

public class UserJoinNotify {

    public String username;
    public int uid;

    public UserJoinNotify(String username, int uid){
        this.username = username;
        this.uid = uid;
    }
}
