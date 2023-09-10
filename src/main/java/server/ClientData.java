package server;

import client.users.UserData;

import java.net.Socket;

public class ClientData {

    /**
     * Client user data.
     */
    public final UserData userData;

    /**
     * The server thread handling the client.
     */
    public final ServerRunnable serverRunnable;

    public ClientData(UserData userData, ServerRunnable serverRunnable) {
        this.userData = userData;
        this.serverRunnable = serverRunnable;
    }
}
