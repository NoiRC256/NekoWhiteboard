package client;

import client.events.EventHandler;
import client.events.LoginEventArgs;
import packet.Message;
import packet.UserLoginReq;
import packet.UserLoginRsp;

import java.io.*;
import java.net.Socket;

public class ClientRunnable implements Runnable {

    Main main = Main.getInstance();
    private volatile boolean shutdown;
    private String serverAddress;
    private int serverPort;

    public final EventHandler<LoginEventArgs> loggedInEvt = new EventHandler();
    public final EventHandler joinedServerEvt = new EventHandler();
    public final EventHandler otherUserRequestJoinEvt = new EventHandler();
    public final EventHandler otherUserJoinedEvt = new EventHandler();
    public final EventHandler addShapeEvt = new EventHandler();

    public ClientRunnable(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    @Override
    public void run() {
        try (Socket socket = new Socket(serverAddress, serverPort)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            System.out.println("Client: Logging in on server " + serverAddress + ":" + serverPort);
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

    private void handleUserLoginRsp(UserLoginRsp rsp, ObjectOutputStream out) throws IOException{
        int uid = rsp.uid;
        System.out.println("Client: Logged in with UID: " + uid);
        loggedInEvt.invoke(this, new LoginEventArgs(uid));
    }

    public void shutdown(){
        shutdown = true;
    }
}
