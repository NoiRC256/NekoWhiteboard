package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class MainFrame extends JFrame {

    private JPanel mainPanel;
    private JPanel whiteboardPanel;
    private JLabel mouseCoordLabel;
    private JPanel usersPanel;

    public MainFrame(String appName) {
        super(appName);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);

        whiteboardPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                onMouseMoved(e);
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new MainFrame("Neko Whiteboard");
        frame.setVisible(true);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        whiteboardPanel = new WhiteboardPanel();

    }

    public void onMouseMoved(MouseEvent e){
        if(e.getSource() == whiteboardPanel){
            Point mouseLoc = e.getPoint();
            mouseCoordLabel.setText("x: " + mouseLoc.x + " y: " + mouseLoc.y);
        }
    }
}
