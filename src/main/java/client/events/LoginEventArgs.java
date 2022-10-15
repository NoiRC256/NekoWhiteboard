package client.events;

public class LoginEventArgs extends EventArgs{

    public final int uid;

    public LoginEventArgs(int uid){
        this.uid = uid;
    }
}
