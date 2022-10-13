package server;

import javax.net.ServerSocketFactory;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static int port = 3001;

    private static int uidCounter = 1;

    public static void main(String[] args){
        ServerSocketFactory factory = ServerSocketFactory.getDefault();
        try(ServerSocket serverSocket = factory.createServerSocket(port)){
            System.out.println("Server socket created. Waiting for client connection...");

            while(true){
                Socket client = serverSocket.accept();
                uidCounter += 1;
                System.out.println("Client " + uidCounter + ": Connecting.");
                Thread t = new Thread(() -> serveClient(client));
            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private static void serveClient(Socket client){

    }

}
