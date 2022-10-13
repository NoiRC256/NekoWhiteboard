package client;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        JFrame launcherFrame = new LauncherFrame("Neko Whiteboard Launcher");
        JFrame mainFrame = new MainFrame("Neko Whiteboard");

        launcherFrame.setSize(LauncherFrame.DEFAULT_WIDTH, LauncherFrame.DEFAULT_HEIGHT);
        mainFrame.setSize(MainFrame.DEFAULT_WIDTH, MainFrame.DEFAULT_HEIGHT);
        launcherFrame.setVisible(true);
        mainFrame.setVisible(false);
    }
}
