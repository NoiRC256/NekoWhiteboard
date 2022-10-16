package client;

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

    public Process serverProcess;
    public ProgramMode mode = ProgramMode.Offline;
    public String serverAddress;
    public int serverPort;

    public LauncherFrame launcherFrame;
    public MainFrame mainFrame;

    public ClientRunnable client;

    public static void main(String[] args) {

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
                main.mainFrame = new MainFrame("Neko Whiteboard");
                main.launcherFrame.setSize(LauncherFrame.DEFAULT_WIDTH, LauncherFrame.DEFAULT_HEIGHT);
                main.launcherFrame.setMinimumSize(new Dimension(LauncherFrame.MIN_WIDTH, LauncherFrame.MIN_HEIGHT));
                main.mainFrame.setSize(MainFrame.DEFAULT_WIDTH, MainFrame.DEFAULT_HEIGHT);
                main.mainFrame.setMinimumSize(new Dimension(MainFrame.MIN_WIDTH, MainFrame.MIN_HEIGHT));

                main.launcherFrame.setVisible(true);
                main.mainFrame.setVisible(false);
            }
        });
    }

    public void start() {

        this.launcherFrame.setVisible(false);
        this.mainFrame.setVisible(true);

        switch (mode) {
            case Offline:
                break;
            case HostServer:
                //startServerProcess(new String[]{serverAddress, Integer.toString(serverPort)});
                client = new ClientRunnable(serverAddress, serverPort);
                new Thread(client).start();
                break;
            case JoinServer:
                client = new ClientRunnable(serverAddress, serverPort);
                new Thread(client).start();
                break;
        }

        mainFrame.enableCallbacks();
    }

    public void quit() {

        mainFrame.disableCallbacks();

        switch (mode) {
            case Offline:
                break;
            case HostServer:
                //endServerProcess();
                client.shutdown();
                break;
            case JoinServer:
                client.shutdown();
                break;
        }

        this.mainFrame.setVisible(false);
        this.launcherFrame.setVisible(true);
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
}
