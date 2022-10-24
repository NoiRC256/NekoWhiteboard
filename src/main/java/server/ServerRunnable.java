package server;

import client.shapes.IShape;
import client.users.UserRole;
import packet.*;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;

public class ServerRunnable implements Runnable {

    public final Socket socket;
    private ObjectOutputStream globalOut;
    public int uid = -1;

    public ServerRunnable(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            this.globalOut = out;

            while (true) {
                Message msg = (Message) in.readObject();
                handleMessage(msg, out);
            }

        } catch (IOException e) {

            System.out.println("Server Thread: IO Exception");
            globalOut = null;
            // Remove client data from server record.
            // Notify other users.
            int removedUid = Server.removeJoinedClient(this);

            if(Server.manager != null && removedUid == Server.manager.userData.uid){
                System.exit(0);
            }

            if(Server.serverRunnableClientDatas.isEmpty() || Server.uidClientDatas.isEmpty()){
                System.exit(0);
            }

            UserLeaveNotify msg = new UserLeaveNotify(removedUid);
            Server.broadcastMessage(msg, this, true);

        } catch (ClassNotFoundException e) {
            System.out.println("Server Thread: Cannot find message class");
        }
    }

    private void handleMessage(Message msg, ObjectOutputStream out) throws IOException {

        System.out.println("Server Thread: Received message " + msg.getClass());

        // Receive request to login.
        if (msg.getClass().equals(UserLoginReq.class)) {
            handleUserLoginReq((UserLoginReq) msg, out);
        }
        // Receive request to join session.
        else if (msg.getClass().equals(UserJoinReq.class)) {
            handleUserJoinReq((UserJoinReq) msg, out);
        }
        // Receive request to broadcast message.
        else if (msg.getClass().equals(BroadcastReq.class)) {
            handleBroadcastReq((BroadcastReq) msg, out);
        }
        // Receive request to set new server whiteboard data.
        else if (msg.getClass().equals(ChangeWhiteboardDataReq.class)) {
            handleChangeWhiteboardDataReq((ChangeWhiteboardDataReq) msg, out);
        }
    }

    // User login.
    private void handleUserLoginReq(UserLoginReq req, ObjectOutputStream out) throws IOException {
        int uid = Server.uidCounter.getAndIncrement();
        ClientData existingClientData = Server.getClientDataByUid(uid);
        // Existing joined user.
        if (existingClientData != null) {
            uid = existingClientData.userData.uid;
        }
        System.out.println("Server Thread: Received user login request. Allocate UID: " + uid);
        UserLoginRsp userLoginRsp = new UserLoginRsp(uid);
        out.writeObject(userLoginRsp);
        out.flush();
    }

    // User request join session.
    private void handleUserJoinReq(UserJoinReq req, ObjectOutputStream out) throws IOException {
        System.out.println("Server Thread: User " + req.userData.username + " requesting to join session.");

        // Register user as manager if no manager.
        boolean isManager = Server.trySetManager(this, req.userData);

        // User join response.
        UserRole assignedRole = isManager ? UserRole.Manager : UserRole.Guest;

        if (assignedRole == UserRole.Manager) {
            UserJoinRsp userJoinRsp = new UserJoinRsp(true, assignedRole);
            out.writeObject(userJoinRsp);
            out.flush();

            System.out.println("Server Thread: User " + req.userData.username + " joined session as " + assignedRole);

            // Register user to session as joined client.
            Server.addJoinedClient(this, req.userData);

            // Ask manager to upload whiteboard data.
            out.writeObject(new ShouldUploadWhiteboardDataNotify());
            out.flush();

            // Notify other users.
            UserJoinNotify userJoinNotify = new UserJoinNotify(req.userData);
            Server.broadcastMessage(userJoinNotify, this, true);

        } else {

            // TODO: If not manager, do the following.
            // TODO: GuestRequestJoinNotify -- send to manager, and wait for manager to approve or not approve.
            // TODO: ApproveGuestJoinNotify -- receive from host.
            UserJoinRsp userJoinRsp = new UserJoinRsp(true, assignedRole);
            out.writeObject(userJoinRsp);
            out.flush();

            System.out.println("Server Thread: User " + req.userData.username + " joined session as " + assignedRole);

            // Register user to session as joined client.
            Server.addJoinedClient(this, req.userData);

            // Send latest server-side whiteboard data to this user.
            if (Server.whiteboardData != null) {
                Server.whiteboardData.update();
                System.out.println("Server Thread: Converting whiteboard buffer to bytes...");
                byte[] bufferBytes = WhiteboardData.toByteArray(Server.whiteboardData.bufferedImage, "png");
                WhiteboardDataNotify whiteboardDataNotify = new WhiteboardDataNotify(bufferBytes);
                System.out.println("Server Thread: Send whiteboard data notify");
                out.writeObject(whiteboardDataNotify);
                out.flush();
            }

            // Notify other users.
            UserJoinNotify userJoinNotify = new UserJoinNotify(req.userData);
            Server.broadcastMessage(userJoinNotify, this, true);
        }

    }

    public void handleChangeWhiteboardDataReq(ChangeWhiteboardDataReq req, ObjectOutputStream out) throws IOException {

        // Check whether sender is manager.
        if (req.uid == Server.manager.userData.uid) {
            System.out.println("Server Thread: Changing whiteboard data....");
            BufferedImage bufferedImage = WhiteboardData.toBufferedImage(req.bufferBytes);
            Server.whiteboardData = new WhiteboardData(bufferedImage);
        } else{
            System.out.println("Server Thread: Sender is not manager. Cannot change whiteboard data.");
        }
    }

    // User request to broadcast message.
    public void handleBroadcastReq(BroadcastReq msg, ObjectOutputStream out) throws IOException{

        // If broadcasting whiteboard shape, also paint the shape on server-side whiteboard data.
        if(msg.message.getClass().equals(WhiteboardShapeNotify.class)){
            handleWhiteboardShapeBroadcastReq((WhiteboardShapeNotify) msg.message, out);
        }

        // Broadcast the message contained in the broadcast request.
        Server.broadcastMessage(msg.message, this, msg.ignoreSource);
    }

    private void handleWhiteboardShapeBroadcastReq(WhiteboardShapeNotify msg, ObjectOutputStream out) throws IOException{
        if(Server.whiteboardData != null){
            Server.whiteboardData.addPendingShape(msg.shape);
            Server.whiteboardData.update();
        }
    }

    //region Send

    /**
     * Send a message to client.
     * @param msg
     */
    public void sendMessage(Message msg) {
        if (globalOut == null) return;
        try {
            globalOut.writeObject(msg);
            globalOut.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //endregion

}
