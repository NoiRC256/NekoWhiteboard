package server;

import javax.net.ServerSocketFactory;
import java.io.IOException;
import java.net.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Server {

    private static String host = "localhost";
    private static int port = 3001;

    public static Boolean isActive = true;
    public static AtomicInteger uidCounter = new AtomicInteger(0);

    private static ExecutorService threadPool;
    public static ConcurrentHashMap<Integer, Socket> activeClients = new ConcurrentHashMap<Integer, Socket>();

    public static void main(String[] args) {

        parseArgs(args);

        threadPool = Executors.newCachedThreadPool();
        ServerSocketFactory factory = ServerSocketFactory.getDefault();
        try (ServerSocket serverSocket = factory.createServerSocket(port)) {
            System.out.println("Server started at " + host + ":" + port);

            while (isActive) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client Connecting...");
                threadPool.execute(new ServerRunnable(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void parseArgs(String[] args){
        if(!args[0].isEmpty()){
            try{
                InetAddress hostAddress = InetAddress.getByName(args[0]);
                host = args[0];
            } catch (UnknownHostException e){
                System.out.println("Invalid host.");
            }
        }

        if(!args[1].isEmpty()){
            try{
                port = Integer.parseInt(args[1]);
            } catch (NumberFormatException e){
                System.out.println("Invalid port number format.");
            }
        }
    }

}
