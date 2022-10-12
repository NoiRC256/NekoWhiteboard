package client.whiteboard;

import client.MainFrame;
import client.toolbox.ToolboxController;
import client.users.UserController;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class WhiteboardController {

    MainFrame view = MainFrame.getInstance();
    WhiteboardData whiteboardData;
    MouseData mouseData;

    ToolboxController toolboxController;
    UserController userController;

    public WhiteboardController(ToolboxController toolboxController, UserController userController){
        whiteboardData = new WhiteboardData();
        mouseData = new MouseData();
        this.toolboxController = toolboxController;
        this.userController = userController;
    }

    public void init(){
        view.whiteboardPanel.setShapes(whiteboardData.shapes);

        view.whiteboardPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                Point mousePos = e.getPoint();
                view.mousePosLabel.setText("x: " + mousePos.x + " y: " + mousePos.y);
                mouseData.mousePos = mousePos;
            }
        });

        view.whiteboardPanel.addMouseListener(new MouseAdapter() {
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
                view.toolboxController.draw(view.whiteboardPanel, mouseData);
                view.whiteboardPanel.repaint();

                mouseData.prevMouseDragPos = mousePos;
            }
        };
        view.whiteboardPanel.addMouseListener(mouseDragAdapter);
        view.whiteboardPanel.addMouseMotionListener(mouseDragAdapter);
    }

    public void clear(){
        view.whiteboardPanel.clearShapes();
        view.whiteboardPanel.repaint();
    }

}
