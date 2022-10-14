package client.events;

public class UserJoinEventArgs {

    public final int uid;
    public final String username;

    public UserJoinEventArgs(int uid, String username){
        this.uid = uid;
        this.username = username;
    }

}
