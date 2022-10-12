package client;

import client.toolbox.ToolboxController;
import client.users.UserController;
import client.whiteboard.WhiteboardController;
import client.whiteboard.WhiteboardPanel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class MainFrame extends JFrame {

    // Singleton.
    private static MainFrame instance;
    public static MainFrame getInstance() {
        return instance;
    }

    // Controllers.
    public WhiteboardController whiteboardController;
    public UserController userController;
    public ToolboxController toolboxController;

    // Main view.
    public JPanel mainPanel;

    // Whiteboard view.
    public WhiteboardPanel whiteboardPanel;
    public JLabel mousePosLabel;

    // User view.
    public JPanel usersPanel;
    public JPanel selfUserPanel;
    public JScrollPane remoteUsersScrollPane;

    // Toolbox view.
    public JPanel toolboxPanel;
    public JRadioButton freehandToolBtn;
    public JRadioButton rectToolBtn;
    public JRadioButton ovalToolBtn;
    public JColorChooser colorChooser;
    public JButton clearButton;


    public MainFrame(String appName) {
        super(appName);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MainFrame.instance = this;
        this.setContentPane(mainPanel);

        // Setup controllers.
        userController = new UserController();
        toolboxController = new ToolboxController();
        whiteboardController = new WhiteboardController(toolboxController, userController);

        // Initialize controllers. Adds listeners, etc.
        userController.init();
        toolboxController.init();
        whiteboardController.init();
    }

    public static void main(String[] args) {
        JFrame frame = new MainFrame("Neko Whiteboard");
        frame.setVisible(true);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        colorChooser = new JColorChooser();
        toolboxPanel.add(colorChooser);
        colorChooser.setVisible(true);

        colorChooser.getSelectionModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                Color newColor = colorChooser.getColor();
                toolboxController.toolbox.color = newColor;
            }
        });
    }
}
