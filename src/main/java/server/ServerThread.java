package server;

import packet.UserJoinReq;
import packet.Message;
import packet.UserLoginReq;
import packet.UserLoginRsp;

import java.io.*;
import java.net.Socket;

public class ServerThread implements Runnable {
    private final Socket socket;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            while (true) {
                Message msg = (Message) in.readObject();
                handleMessage(msg, out);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleMessage(Message msg, ObjectOutputStream out) throws IOException {
        if (msg.getClass().equals(UserJoinReq.class)) {
            UserLoginReq userLoginReq = (UserLoginReq) msg;
            handleUserLoginReq(userLoginReq, out);
        }
    }

    private void handleUserLoginReq(UserLoginReq req, ObjectOutputStream out) throws IOException {
        int uid = Server.uidCounter.getAndIncrement();
        UserLoginRsp userLoginRsp = new UserLoginRsp(uid);
        out.writeObject(userLoginRsp);
        out.flush();
    }

    private void handleJoinReq(UserJoinReq req) {
    }
}
