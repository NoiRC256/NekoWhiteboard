package client;

import client.events.EventHandler;

import java.io.*;
import java.net.Socket;

public class ClientThread implements Runnable {

    private String serverAddress;
    private int serverPort;

    public EventHandler userJoinEvt = new EventHandler();
    public EventHandler addShapeEvt = new EventHandler();

    public ClientThread(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    @Override
    public void run() {
        try (Socket socket = new Socket(serverAddress, serverPort)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            while (true) {

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
