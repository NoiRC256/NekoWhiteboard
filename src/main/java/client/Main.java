package client;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

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

    public JFrame launcherFrame;
    public JFrame mainFrame;

    ClientThread client;

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

        switch (mode) {
            case Offline:
                break;
            case HostServer:
                startServerProcess(new String[]{serverAddress, Integer.toString(serverPort)});
                client = new ClientThread(serverAddress, serverPort);
                break;
            case JoinServer:
                client = new ClientThread(serverAddress, serverPort);
                break;
        }
        this.launcherFrame.setVisible(false);
        this.mainFrame.setVisible(true);
    }

    private void startServerProcess(String[] args) {
        try {
            ProcessBuilder pb = new ProcessBuilder("java", "-jar", "server-jar-with-dependencies.jar");
            serverProcess = pb.start();
            System.out.println("Run host server jar.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setProgramMode(ProgramMode mode) {
        this.mode = mode;
    }
}
