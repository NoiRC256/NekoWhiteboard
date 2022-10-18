package client;

import client.events.*;
import client.users.UserData;
import packet.Message;
import packet.*;

import java.awt.*;
import java.io.*;
import java.net.Socket;

public class ClientRunnable implements Runnable {

    Main main = Main.getInstance();
    private volatile boolean shutdown;
    private String serverAddress;
    private int serverPort;
    private ObjectOutputStream globalOut;

    /**
     * Self has logged in.
     */
    public final EventHandler<LoginEventArgs> loggedInEvt = new EventHandler();
    /**
     * Self has joined session.
     */
    public final EventHandler joinedSessionEvt = new EventHandler();
    /**
     * Other user is requesting to join session.
     */
    public final EventHandler<UserEventArgs> guestRequestJoinEvt = new EventHandler();
    /**
     * Other user has joined session.
     */
    public final EventHandler<UserEventArgs> guestJoinedEvt = new EventHandler();
    /**
     * Should add shape to whiteboard.
     */
    public final EventHandler<ShapeEventArgs> addShapeEvt = new EventHandler();
    /**
     * Should clear whiteboard.
     */
    public final EventHandler clearWhiteboardEvt = new EventHandler();

    public ClientRunnable(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    @Override
    public void run() {
        try (Socket socket = new Socket(serverAddress, serverPort)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            this.globalOut = out;

            System.out.println("Client: Logging in on server " + socket.getInetAddress() + ":" + serverPort);
            UserLoginReq userLoginReq = new UserLoginReq();
            out.writeObject(userLoginReq);
            out.flush();

//            new java.util.Timer().schedule(
//                    new java.util.TimerTask() {
//                        @Override
//                        public void run() {
//                            for(int i = 0; i < 30; i++){
//                                System.out.println("Add dummy user button " + i);
//                                guestJoinedEvt.invoke(this, new UserEventArgs(new UserData(999, "DummyUser")));
//                            }
//                        }
//                    },
//                    2000
//            );

            while (!shutdown) {
                Message msg = (Message) in.readObject();
                handleMessage(msg, out);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    //region Receive

    private void handleMessage(Message msg, ObjectOutputStream out) throws IOException {

        System.out.println("Client: Received message " + msg.getClass());

        // Self login response.
        if (msg.getClass().equals(UserLoginRsp.class)) {
            handleUserLoginRsp((UserLoginRsp) msg, out);
        }
        // Self join response.
        else if (msg.getClass().equals(UserJoinRsp.class)) {
            handleUserJoinRsp((UserJoinRsp) msg, out);
        }
        // Guest join notify.
        else if (msg.getClass().equals(UserJoinNotify.class)){
            handleUserJoinNotify((UserJoinNotify) msg, out);
        }
        // Whiteboard add shape notify.
        else if (msg.getClass().equals(WhiteboardShapeNotify.class)){
            handleWhiteboardShapeNotify((WhiteboardShapeNotify) msg, out);
        }
        // Whiteboard clear notify.
        else if (msg.getClass().equals(WhiteboardClearNotify.class)){
            handleWhiteboardClearNotify();
        }
    }

    //region User

    // Login response.
    private void handleUserLoginRsp(UserLoginRsp rsp, ObjectOutputStream out) throws IOException {
        int uid = rsp.uid;
        UserData userData = new UserData(uid, main.username);

        // Logged in.
        System.out.println("Client: Logged in with UID: " + uid);
        Main.getInstance().userData = userData;
        loggedInEvt.invoke(this, new LoginEventArgs(userData));

        // Send join session request.
        UserJoinReq userJoinReq = new UserJoinReq(userData);
        out.writeObject(userJoinReq);
        out.flush();
    }

    // Join session response.
    private void handleUserJoinRsp(UserJoinRsp rsp, ObjectOutputStream out) throws IOException {
        if(rsp.isApproved){
            System.out.println("Client: Joined session.");
            Main.getInstance().userRole = rsp.userRole;
            joinedSessionEvt.invoke(this, EventArgs.empty);
        }
    }

    // Other user joined session.
    private void handleUserJoinNotify(UserJoinNotify msg, ObjectOutputStream out) throws IOException{
        System.out.println("Client: User " + msg.userData.username + " joined session.");
        guestJoinedEvt.invoke(this, new UserEventArgs(msg.userData));
    }

    //endregion

    //region Drawing

    // Receive add shape notify.
    private void handleWhiteboardShapeNotify(WhiteboardShapeNotify msg, ObjectOutputStream out) throws IOException{
        addShapeEvt.invoke(this, new ShapeEventArgs(msg.shape));
    }

    // Receive clear whiteboard notify.
    private void handleWhiteboardClearNotify() {
        clearWhiteboardEvt.invoke(this, EventArgs.empty);
    }

    //endregion

    //endregion

    //region Send

    /**
     * Request the server to broadcast a message
     */
    public void broadcastMessage(BroadcastReq req){
        if(globalOut == null) return;
        try{
            globalOut.writeObject(req);
            globalOut.flush();
            System.out.println("Client: request to broadcast message " + req);
        }catch (IOException e){
            System.out.println("Client: cannot broadcast message.");
            //throw new RuntimeException(e);
        }
    }

    //endregion

    public void shutdown() {
        shutdown = true;
    }
}
