package client.toolbox;

import client.*;
import client.shapes.*;
import client.whiteboard.MouseData;
import client.whiteboard.WhiteboardPanel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ToolboxController {

    public MainFrame view = MainFrame.getInstance();
    public Toolbox toolbox;

    public ToolboxController(){
        toolbox = new Toolbox();
    }

    public void init(){

        // Toolbox button listeners.

        view.freehandToolBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toolbox.toolType = ToolType.Freehand;
            }
        });
        view.rectToolBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toolbox.toolType = ToolType.Rectangle;
            }
        });
        view.ovalToolBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toolbox.toolType = ToolType.Oval;
            }
        });
        view.thicknessSlider.setValue(toolbox.thickness);
        view.thicknessSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int value = view.thicknessSlider.getValue();
                toolbox.thickness = value;
            }
        });
        view.clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.whiteboardPanel.clearShapes();
                view.whiteboardPanel.repaint();
            }
        });
    }

    public void draw(WhiteboardPanel whiteboardPanel, MouseData mouseData){
        Point mousePos = mouseData.mousePos;
        Point mousePressPos = mouseData.mousePressPos;
        Point prevMouseDragPos = mouseData.prevMouseDragPos;

        switch (toolbox.toolType) {
            case Freehand:
                FreehandShape freehandShape = new FreehandShape(prevMouseDragPos, mousePos,
                        toolbox.color, toolbox.thickness);
                whiteboardPanel.addShape(freehandShape);
                break;
            case Rectangle:
                if(toolbox.activeRectShape == null){
                    RectangleShape rect = new RectangleShape(mousePos, toolbox.color,
                            toolbox.thickness, 1, 1);
                    whiteboardPanel.addShape(rect);
                    toolbox.activeRectShape = rect;
                }
                toolbox.activeRectShape.adjustAnchor(mousePos, mousePressPos);
                break;
            case Oval:
                if(toolbox.activeRectShape == null){
                    OvalShape oval = new OvalShape(mousePos, toolbox.color,
                            toolbox.thickness, 1, 1);
                    whiteboardPanel.addShape(oval);
                    toolbox.activeRectShape = oval;
                }
                toolbox.activeRectShape.adjustAnchor(mousePos, mousePressPos);
                break;
        }
    }
}
