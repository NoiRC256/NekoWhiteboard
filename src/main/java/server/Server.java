package server;

import client.users.User;
import client.users.UserData;
import client.users.UserRole;
import packet.Message;

import javax.net.ServerSocketFactory;
import java.io.IOException;
import java.net.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Server {

    private static int port = 3001;

    public static Boolean isActive = true;
    public static AtomicInteger uidCounter = new AtomicInteger(0);
    private static ExecutorService threadPool;

    /**
     * Map of Server Runnable and corresponding client data.
     */
    public static ConcurrentHashMap<ServerRunnable, ClientData> serverRunnableClientDatas;
    /**
     * Map of UID and corresponding client data.
     */
    public static ConcurrentHashMap<Integer, ClientData> uidClientDatas;
    /**
     * Client data of manager user.
     */
    public static ClientData manager;

    /**
     * Single source of truth for syncing remote whiteboard status.
     */
    public static WhiteboardData whiteboardData;

    public static void main(String[] args) {

        parseArgs(args);

        threadPool = Executors.newCachedThreadPool();
        serverRunnableClientDatas = new ConcurrentHashMap<ServerRunnable, ClientData>();
        uidClientDatas = new ConcurrentHashMap<Integer, ClientData>();

        ServerSocketFactory factory = ServerSocketFactory.getDefault();
        try (ServerSocket serverSocket = factory.createServerSocket(port)) {
            System.out.println("Server started at " + serverSocket.getInetAddress() + ":" + port);

            while (isActive) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client Connecting...");
                ServerRunnable serverRunnable = new ServerRunnable(clientSocket);
                threadPool.execute(serverRunnable);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void parseArgs(String[] args) {

        if(args.length <= 0) return;

        // Port number arg.
        if (args[0] != null) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid port number format.");
            }
        }
    }

    /**
     * Set manager user if no manager user.
     * @param serverRunnable
     * @param userData
     */
    public static synchronized boolean trySetManager(ServerRunnable serverRunnable, UserData userData){
        if(manager == null){
            manager = new ClientData(userData, serverRunnable);
            return true;
        }
        return false;
    }

    /**
     * Set manager user with existing client data.
     * @param clientData
     */
    public static synchronized void setManager(ClientData clientData){
        manager = clientData;
    }

    /**
     * Create a client data for the user data. Register as a joined user.
     * @param serverRunnable
     * @param userData
     */
    public static synchronized void addJoinedClient(ServerRunnable serverRunnable, UserData userData) {
        System.out.println("Added client: " + userData.username + " (" + userData.uid + ")");
        ClientData clientData = new ClientData(userData, serverRunnable);

        serverRunnableClientDatas.put(serverRunnable, clientData);
        uidClientDatas.put(userData.uid, clientData);
    }

    public static synchronized UserData removeJoinedClient(int uid){
        UserData removedUserData = new UserData(uid, "username");
        System.out.println("Server: Removing uid " + uid);
        ClientData clientData = getClientDataByUid(uid);
        if(clientData != null){
            System.out.println("Server: Removing client data associated with uid " + clientData.userData.uid);
            removedUserData = clientData.userData;
            uidClientDatas.remove(uid);
        }
        if(manager.userData.uid == uid){
            System.out.println("Server: Removing manager " + uid);
            manager = null;
        }
        return removedUserData;
    }

    /**
     * Broadcast a message.
     * @param msg
     * @param source
     * @param ignoreSource
     */
    public static void broadcastMessage(Message msg, ServerRunnable source, boolean ignoreSource){
        System.out.println("Server: broadcast message " + msg.getClass());
        // For each server runnable for joined client, tell them to send message to their client.
        // If ignore source, skip the socket of the source server runnable.
        for(ClientData clientData : Server.serverRunnableClientDatas.values()){
            ServerRunnable serverRunnable = clientData.serverRunnable;
            Socket clientSocket = serverRunnable.socket;
            if(ignoreSource == true && clientSocket.equals(source.socket)){
                continue;
            }
            System.out.println("Server: Broadcasting message to: " + clientData.userData.username
                    + " (" + clientData.userData.uid + ")");
            serverRunnable.sendMessage(msg);
        }
    }

    public static ClientData getClientDataByUid(int uid){
        return uidClientDatas.getOrDefault(uid, null);
    }

    public static ClientData getClientDataByServerRunnable(ServerRunnable serverRunnable){
        return serverRunnableClientDatas.getOrDefault(serverRunnable, null);
    }

}
