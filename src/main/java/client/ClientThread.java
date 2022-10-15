package client;

import client.events.EventHandler;
import client.events.LoginEventArgs;
import client.users.User;
import packet.Message;
import packet.UserLoginReq;
import packet.UserLoginRsp;

import java.awt.*;
import java.io.*;
import java.net.Socket;

public class ClientThread implements Runnable {

    Main main = Main.getInstance();
    private volatile boolean shutdown;
    private String serverAddress;
    private int serverPort;

    public final EventHandler<LoginEventArgs> loggedInEvt = new EventHandler();
    public final EventHandler joinedServerEvt = new EventHandler();
    public final EventHandler otherUserRequestJoinEvt = new EventHandler();
    public final EventHandler otherUserJoinedEvt = new EventHandler();
    public final EventHandler addShapeEvt = new EventHandler();

    public ClientThread(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    @Override
    public void run() {
        try (Socket socket = new Socket(serverAddress, serverPort)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            UserLoginReq userLoginReq = new UserLoginReq();
            out.writeObject(userLoginReq);
            out.flush();

            while (!shutdown) {
                Message msg = (Message) in.readObject();
                handleMessage(msg, out);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e){
            throw new RuntimeException(e);
        }
    }

    private void handleMessage(Message msg, ObjectOutputStream out) throws IOException{
        if(msg.getClass().equals(UserLoginRsp.class)){
            handleUserLoginRsp((UserLoginRsp) msg, out);
        }
    }

    private void handleUserLoginRsp(UserLoginRsp userLoginRsp, ObjectOutputStream out) throws IOException{
        int uid = userLoginRsp.uid;
        loggedInEvt.invoke(this, new LoginEventArgs(uid));
    }

    public void shutdown(){
        shutdown = true;
    }
}
