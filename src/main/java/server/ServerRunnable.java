package server;

import client.users.UserRole;
import packet.*;

import java.io.*;
import java.net.Socket;

public class ServerRunnable implements Runnable {

    public final Socket socket;
    private ObjectOutputStream globalOut;

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
        else if(msg.getClass().equals(UserJoinReq.class)){
            handleUserJoinReq((UserJoinReq) msg, out);
        }
        // Receive request to broadcast message.
        else if (msg.getClass().equals(BroadcastReq.class)){
            handleBroadcastReq((BroadcastReq) msg);
        }
    }

    // User login.
    private void handleUserLoginReq(UserLoginReq req, ObjectOutputStream out) throws IOException {
        int uid = Server.uidCounter.getAndIncrement();
        ClientData existingClientData = Server.getClientDataByUid(uid);
        // Existing joined user.
        if(existingClientData != null){
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

        // TODO: If not manager, do the following.
        // TODO: GuestRequestJoinNotify -- send to manager, and wait for manager to approve or not approve.
        // TODO: ApproveGuestJoinNotify -- receive from host.

        // User join response.
        UserRole assignedRole = isManager ? UserRole.Manager : UserRole.Guest;
        UserJoinRsp userJoinRsp = new UserJoinRsp(true, assignedRole);
        out.writeObject(userJoinRsp);
        out.flush();

        System.out.println("Server Thread: User " + req.userData.username + " joined session as " + assignedRole);

        // Register user to session as joined client.
        Server.addJoinedClient(this, req.userData);

        // Notify other users.
        UserJoinNotify userJoinNotify = new UserJoinNotify(req.userData);
        Server.broadcastMessage(userJoinNotify, this, true);
    }

    // User request to broadcast message.
    public void handleBroadcastReq(BroadcastReq msg){
        // Broadcast the message contained in the broadcast request.
        Server.broadcastMessage(msg.message, this, msg.ignoreSource);
    }

    //region Send

    /**
     * Send a message to client.
     * @param msg
     */
    public void sendMessage(Message msg){
        if(globalOut == null) return;
        try{
            globalOut.writeObject(msg);
            globalOut.flush();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    //endregion

}
