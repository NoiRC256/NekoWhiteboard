package server;

import javax.net.ServerSocketFactory;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

public class Server {

    public static Boolean isActive = true;
    private static String host = "localhost";
    private static int port = 3001;
    public static AtomicInteger uidCounter = new AtomicInteger(0);

    private static ExecutorService threadPool;
    public static ConcurrentHashMap<Integer, Socket> activeClients = new ConcurrentHashMap<Integer, Socket>();

    public static void main(String[] args) {

        parseArgs(args);

        threadPool = Executors.newCachedThreadPool();
        ServerSocketFactory factory = ServerSocketFactory.getDefault();
        try (ServerSocket serverSocket = factory.createServerSocket(port)) {
            System.out.println("Server started.");

            while (isActive) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client Connecting...");
                threadPool.execute(new ServerThread(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void parseArgs(String[] args){

    }

}
