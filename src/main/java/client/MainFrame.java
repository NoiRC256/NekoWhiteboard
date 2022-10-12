package client;

import client.users.User;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;

public class MainFrame extends JFrame {

    User user;
    Toolbox toolbox;

    private JPanel mainPanel;
    private WhiteboardPanel whiteboardPanel;
    private JLabel mouseCoordLabel;
    private JPanel usersPanel;
    private JPanel toolsPanel;
    private JRadioButton freehandToolBtn;
    private JRadioButton rectToolBtn;
    private JScrollPane usersScrollPane;
    private JPanel selfUserPanel;
    private JButton clearButton;
    private JRadioButton ovalToolBtn;
    private JColorChooser colorChooser;

    private MouseData mouseData;

    public MainFrame(String appName) {
        super(appName);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);

        // Setup.
        user = new User();
        toolbox = user.toolbox;
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
                // Clear active previews shape references.
                toolbox.activeRectShape = null;
                toolbox.activeOvalShape = null;
            }
        });

        MouseAdapter mouseDragAdapter = new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                Point mousePos = e.getPoint();
                mouseData.mousePos = mousePos;
                if (mouseData.mousePressPos == null)
                    mouseData.mousePressPos = mousePos;
                if (mouseData.prevMouseDragPos == null)
                    mouseData.prevMouseDragPos = mousePos;

                toolbox.draw(whiteboardPanel, mouseData);

                whiteboardPanel.repaint();
                mouseData.prevMouseDragPos = mousePos;
            }
        };
        whiteboardPanel.addMouseListener(mouseDragAdapter);
        whiteboardPanel.addMouseMotionListener(mouseDragAdapter);

        // Tool Buttons.

        freehandToolBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toolbox.toolType = ToolType.Freehand;
            }
        });

        rectToolBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toolbox.toolType = ToolType.Rectangle;
            }
        });
        ovalToolBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toolbox.toolType = ToolType.Oval;
            }
        });
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                whiteboardPanel.shapes.clear();
                whiteboardPanel.repaint();
            }
        });
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
                toolbox.color = newColor;
            }
        });
    }
}
