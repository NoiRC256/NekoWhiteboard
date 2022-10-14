package server;

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

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
