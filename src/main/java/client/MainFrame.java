package client;

import client.users.User;
import client.users.UserController;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;

public class MainFrame extends JFrame {

    // Singleton.
    private static MainFrame instance;
    public static MainFrame getInstance(){
        return instance;
    }

    UserController userController;
    ToolboxController toolboxController;

    public JPanel mainPanel;
    public WhiteboardPanel whiteboardPanel;
    public JLabel mouseCoordLabel;

    // User view.
    public JPanel selfUserPanel;
    public JPanel usersPanel;
    public JScrollPane usersScrollPane;

    // Toolbox view.
    public JPanel toolsPanel;
    public JRadioButton freehandToolBtn;
    public JRadioButton rectToolBtn;
    public JRadioButton ovalToolBtn;
    public JColorChooser colorChooser;
    public JButton clearButton;

    public MouseData mouseData;

    public MainFrame(String appName) {
        super(appName);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MainFrame.instance = this;
        this.setContentPane(mainPanel);

        // Setup.

        userController = new UserController();
        userController.init();
        toolboxController = new ToolboxController();
        toolboxController.init();
        mouseData = new MouseData();

        // Event listeners.

        whiteboardPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                Point mousePos = e.getPoint();
                mouseCoordLabel.setText("x: " + mousePos.x + " y: " + mousePos.y);
                mouseData.mousePos = mousePos;
            }
        });

        whiteboardPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                Point mousePos = e.getPoint();
                mouseData.mousePressPos = mousePos;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                // Clear tmp mouse data.
                mouseData.mousePressPos = null;
                mouseData.prevMouseDragPos = null;
                // Clear active adjustable shape refs.
                toolboxController.toolbox.activeRectShape = null;
                toolboxController.toolbox.activeOvalShape = null;
            }
        });

        MouseAdapter mouseDragAdapter = new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);

                // Update mouse data.
                Point mousePos = e.getPoint();
                mouseData.mousePos = mousePos;
                if (mouseData.mousePressPos == null)
                    mouseData.mousePressPos = mousePos;
                if (mouseData.prevMouseDragPos == null)
                    mouseData.prevMouseDragPos = mousePos;

                // Draw on whiteboard.
                toolboxController.draw(whiteboardPanel, mouseData);
                whiteboardPanel.repaint();

                mouseData.prevMouseDragPos = mousePos;
            }
        };
        whiteboardPanel.addMouseListener(mouseDragAdapter);
        whiteboardPanel.addMouseMotionListener(mouseDragAdapter);
    }

    public static void main(String[] args) {
        JFrame frame = new MainFrame("Neko Whiteboard");
        frame.setVisible(true);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        colorChooser = new JColorChooser();
        mainPanel.add(colorChooser);

        colorChooser.getSelectionModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                Color newColor = colorChooser.getColor();
                toolboxController.toolbox.color = newColor;
            }
        });
    }
}
