package client;

import client.events.*;
import client.users.UserData;
import client.users.UserRole;
import client.whiteboard.WhiteboardPanel;
import packet.Message;
import packet.*;
import server.WhiteboardData;

import java.awt.image.BufferedImage;
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
    public final EventHandler<UserEventArgs> userJoinedEvt = new EventHandler();
    public final EventHandler<UserEventArgs> userLeavedEvt = new EventHandler();
    /**
     * Should add shape to whiteboard.
     */
    public final EventHandler<ShapeEventArgs> addShapeEvt = new EventHandler();
    /**
     * Should clear whiteboard.
     */
    public final EventHandler clearWhiteboardEvt = new EventHandler();
    public final EventHandler<WhiteboardDataEventArgs> newWhiteboardDataEvt = new EventHandler();
    public final EventHandler disconnectedEvt = new EventHandler();

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
            System.out.println("Client: Disconnected from server.");
            disconnectedEvt.invoke(this, EventArgs.empty);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    //region Receive

    private void handleMessage(Message msg, ObjectOutputStream out) throws IOException {

        System.out.println("Client: Received message " + msg.getClass());

        // Self login response.
        if (msg.getClass().equals(UserLoginRsp.class)) {
            handleSelfLoginRsp((UserLoginRsp) msg, out);
        }
        // Self join response.
        else if (msg.getClass().equals(UserJoinRsp.class)) {
            handleSelfJoinRsp((UserJoinRsp) msg, out);
        }
        // User join notify.
        else if (msg.getClass().equals(UserJoinNotify.class)){
            handleUserJoinNotify((UserJoinNotify) msg, out);
        }
        // User leave notify.
        else if (msg.getClass().equals(UserLeaveNotify.class)){
            handleUserLeaveNotify((UserLeaveNotify) msg, out);
        }
        // User list notify.
        else if (msg.getClass().equals(UserListNotify.class)){
            handleUserListNotify((UserListNotify) msg, out);
        }
        // Whiteboard add shape notify.
        else if (msg.getClass().equals(WhiteboardShapeNotify.class)){
            handleWhiteboardShapeNotify((WhiteboardShapeNotify) msg, out);
        }
        // Whiteboard clear notify.
        else if (msg.getClass().equals(WhiteboardClearNotify.class)){
            handleWhiteboardClearNotify();
        }
        // New whiteboard data notify.
        else if (msg.getClass().equals(WhiteboardDataNotify.class)){
            handleWhiteboardDataNotify((WhiteboardDataNotify) msg);
        }
    }

    private void handleUserListNotify(UserListNotify msg, ObjectOutputStream out) {
        for(UserData userData : msg.userDatas){
            userJoinedEvt.invoke(this, new UserEventArgs(userData));
        }
    }

    private void handleUserLeaveNotify(UserLeaveNotify msg, ObjectOutputStream out) {
        System.out.println("Client: User UID " + msg.userData.uid + " disconnected");
        userLeavedEvt.invoke(this, new UserEventArgs(msg.userData));
    }

    //region User

    // Login response.
    private void handleSelfLoginRsp(UserLoginRsp rsp, ObjectOutputStream out) throws IOException {
        UserData userData = new UserData(rsp.uid, main.username);

        // Logged in.
        System.out.println("Client: Logged in with UID: " + rsp.uid);
        Main.getInstance().userData = userData;
        loggedInEvt.invoke(this, new LoginEventArgs(userData));

        // Send join session request.
        UserJoinReq userJoinReq = new UserJoinReq(userData);
        out.writeObject(userJoinReq);
        out.flush();
    }

    // Join session response.
    private void handleSelfJoinRsp(UserJoinRsp rsp, ObjectOutputStream out) throws IOException {
        if(rsp.isApproved){
            Main.getInstance().userData.role = rsp.userRole;
            Main.getInstance().userRole = rsp.userRole;
            System.out.println("Client: Joined session as " + rsp.userRole);
            joinedSessionEvt.invoke(this, EventArgs.empty);
        }

        // If is manager, set new server whiteboard data.
        Main main = Main.getInstance();
        if(main.userRole == UserRole.Manager){
            WhiteboardPanel whiteboardPanel = main.mainFrame.whiteboardPanel;
            out.writeObject(new ChangeWhiteboardDataReq( Main.getInstance().userData.uid,
                    WhiteboardData.toByteArray(whiteboardPanel.bufferedImage, "png")
            ));
            out.flush();
        }
    }

    // Other user joined session.
    private void handleUserJoinNotify(UserJoinNotify msg, ObjectOutputStream out) throws IOException{
        System.out.println("Client: User " + msg.userData.username + " joined session.");
        userJoinedEvt.invoke(this, new UserEventArgs(msg.userData));
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

    // Receive new whiteboard data notify.
    private void handleWhiteboardDataNotify(WhiteboardDataNotify msg) throws IOException{
        System.out.println("Client: Received whiteboard data notify, converting bytes to whiteboard buffer...");
        BufferedImage bufferedImage = WhiteboardData.toBufferedImage(msg.bufferBytes);
        WhiteboardData whiteboardData = new WhiteboardData(bufferedImage);
        newWhiteboardDataEvt.invoke(this, new WhiteboardDataEventArgs(whiteboardData));
    }

    //endregion

    //endregion

    //region Send

    /**
     * Send a message to server.
     * @param msg
     */
    public void sendMessage(Message msg){
        if(globalOut == null) return;
        try{
            globalOut.writeObject(msg);
            globalOut.flush();
            System.out.println("Client: send message " + msg);
        }catch (IOException e){
            System.out.println("Client: cannot send message.");
            //throw new RuntimeException(e);
        }
    }

    /**
     * Request the server to broadcast a message.
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
