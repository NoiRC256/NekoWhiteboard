package server;

import javax.net.ServerSocketFactory;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Server {

    public static Boolean isActive = true;
    private static String host = "localhost";
    private static int port = 3001;
    private static int uidCounter = 1;

    private static ExecutorService threadPool;
    private static ConcurrentHashMap<Integer, Socket> activeClients = new ConcurrentHashMap<Integer, Socket>();

    public static void main(String[] args) {

        parseArgs(args);

        threadPool = Executors.newCachedThreadPool();
        ServerSocketFactory factory = ServerSocketFactory.getDefault();
        try (ServerSocket serverSocket = factory.createServerSocket(port)) {
            System.out.println("Server started.");

            while (isActive) {
                Socket clientSocket = serverSocket.accept();
                uidCounter += 1;
                System.out.println("Client " + uidCounter + ": Connecting.");
                threadPool.execute(new ServerThread(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void parseArgs(String[] args){

    }

}
