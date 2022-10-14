package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class LauncherController {

    private LauncherFrame view;

    public LauncherController(LauncherFrame view) {
        this.view = view;
    }

    public void init() {
        Main main = Main.getInstance();
        main.setProgramMode(ProgramMode.Offline);
        view.serverInfoPanel.setVisible(false);

        view.modeBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (view.modeBox.getSelectedIndex()) {
                    case 0:
                        main.setProgramMode(ProgramMode.Offline);
                        view.serverInfoPanel.setVisible(false);
                        break;
                    case 1:
                        main.setProgramMode(ProgramMode.HostServer);
                        view.serverInfoPanel.setVisible(true);
                        break;
                    case 2:
                        main.setProgramMode(ProgramMode.JoinServer);
                        view.serverInfoPanel.setVisible(true);
                        break;
                }
            }
        });

        view.startBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main main = Main.getInstance();
                switch (main.mode) {
                    case Offline:
                        Main.getInstance().start();
                        break;
                    case HostServer:
                    case JoinServer:
                        main.serverAddress = view.serverAddressField.getText();
                        try {
                            main.serverPort = Integer.parseInt(view.serverPortField.getText());
                            Main.getInstance().start();
                        } catch (NumberFormatException ex) {
                            System.out.println("Invalid port number format");
                        }
                        break;
                }
            }
        });
    }

}
