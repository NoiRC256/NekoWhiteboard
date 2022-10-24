package client;

import client.shapes.IShape;
import client.users.UserData;
import client.users.UserRole;
import packet.*;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class Main {
    private static Main instance;

    public static Main getInstance() {
        if (instance == null) {
            instance = new Main();
        }
        return instance;
    }

    public static final String DEFAULT_USERNAME = "username";
    public UserData userData;
    public UserRole userRole = UserRole.None;

    public Process serverProcess;
    public ProgramMode mode = ProgramMode.Offline;
    public static String serverAddress;
    public static int serverPort;

    public LauncherFrame launcherFrame;
    public MainFrame mainFrame;

    public String username = DEFAULT_USERNAME;
    public ClientRunnable client;

    public static void main(String[] args) {

        parseArgs(args);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Main main = Main.getInstance();
                try {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Create frames.
                main.launcherFrame = new LauncherFrame("Neko Whiteboard Launcher");
                main.launcherFrame.setSize(LauncherFrame.DEFAULT_WIDTH, LauncherFrame.DEFAULT_HEIGHT);
                main.launcherFrame.setMinimumSize(new Dimension(LauncherFrame.MIN_WIDTH, LauncherFrame.MIN_HEIGHT));
                main.launcherFrame.setVisible(true);
            }
        });
    }

    private static void parseArgs(String[] args) {

        if (args.length <= 0) return;

        // Port number arg.
        if (args[0] != null) {
            serverAddress = args[0];
        }

        // Port number arg.
        if (args[1] != null) {
            try {
                serverPort = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid port number format.");
            }
        }
    }

    public void start(String username) {

        this.launcherFrame.setVisible(false);

        createMainFrame();
        this.mainFrame.setVisible(true);
        this.mainFrame.colorChooser.getSelectionModel().setSelectedColor(Color.BLACK);

        switch (mode) {
            case Offline:
                userData = new UserData(-1, username);
                userRole = UserRole.None;
                break;
            case Online:
                //startServerProcess(new String[]{serverAddress, Integer.toString(serverPort)});
                userRole = UserRole.Manager;
                client = new ClientRunnable(serverAddress, serverPort);
                new Thread(client).start();
                break;
        }

        mainFrame.enableCallbacks();
    }

    public void quit() {

        broadcastQuit();

        mainFrame.disableCallbacks();

        switch (mode) {
            case Offline:
                break;
            case Online:
                //endServerProcess();
                client.shutdown();
                break;
        }

        userData = null;
        userRole = UserRole.None;

        this.mainFrame.setVisible(false);
        this.launcherFrame.setVisible(true);
    }

    private void createMainFrame() {
        mainFrame = new MainFrame("Neko Whiteboard");
        mainFrame.setSize(MainFrame.DEFAULT_WIDTH, MainFrame.DEFAULT_HEIGHT);
        mainFrame.setMinimumSize(new Dimension(MainFrame.MIN_WIDTH, MainFrame.MIN_HEIGHT));
        mainFrame.setVisible(false);
    }

    private void startServerProcess(String[] args) {
        try {
            ProcessBuilder pb = new ProcessBuilder("java", "-jar", "server-jar-with-dependencies.jar");
            serverProcess = pb.start();
            InputStream in = serverProcess.getInputStream();
            InputStream err = serverProcess.getErrorStream();
            System.out.println("Run host server jar.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void endServerProcess() {
        serverProcess.destroy();
    }

    public void setProgramMode(ProgramMode mode) {
        this.mode = mode;
    }

    //region Request to broadcast message

    public void broadcastMessage(Message message) {
        broadcastMessage(message, true);
    }

    public void broadcastMessage(Message message, boolean ignoreSource) {
        BroadcastReq msg = new BroadcastReq(message, true);
        client.broadcastMessage(msg);
    }

    public void reqRemoveUser(UserData userData) {
        if (userRole != UserRole.Manager) return;
        client.sendMessage(new UserRemoveReq(this.userData, userData));
    }

    public void broadcastShape(IShape shape) {
        if (client == null) return;
        // Put a new shape notify message into a broadcast request, let the client ask server to broadcast it.
        WhiteboardShapeNotify message = new WhiteboardShapeNotify(shape);
        BroadcastReq msg = new BroadcastReq(message, true);
        client.broadcastMessage(msg);
    }

    public void broadcastChatEntry(UserData sender, String content) {
        if (client == null) return;
        ChatEntryNotify message = new ChatEntryNotify(sender, content);
        BroadcastReq msg = new BroadcastReq(message, true);
        client.broadcastMessage(msg);
    }

    public void broadcastClearWhiteboard() {
        if (client == null) return;
        if (userRole == UserRole.Guest) {
            System.out.println("Guests cannot request to broadcast clear whiteboard message.");
            return;
        }

        WhiteboardClearNotify message = new WhiteboardClearNotify();
        BroadcastReq msg = new BroadcastReq(message, true);
        client.broadcastMessage(msg);
    }

    public void broadcastQuit() {
        if (client == null) return;
        if (userData == null) return;
        UserLeaveNotify message = new UserLeaveNotify(userData);
        broadcastMessage(message);
    }

    //endregion
}
