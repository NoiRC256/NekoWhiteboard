package client;

import client.shapes.AdjustableShapeBase;
import client.shapes.FreehandShape;
import client.shapes.OvalShape;
import client.shapes.RectangleShape;

import java.awt.*;

/**
 * Class that provides tools for drawing shapes onto a whiteboard panel.
 */
public class Toolbox {

    /**
     * Current tool type.
     */
    ToolType toolType = ToolType.Freehand;
    Color color = Color.BLACK;
    int thickness = 5;

    // Active shape previews.
    public AdjustableShapeBase activeRectShape;
    public AdjustableShapeBase activeOvalShape;

    public void draw(WhiteboardPanel whiteboardPanel, MouseData mouseData){
        Point mousePos = mouseData.mousePos;
        Point mousePressPos = mouseData.mousePressPos;
        Point prevMouseDragPos = mouseData.prevMouseDragPos;

        switch (toolType) {
            case Freehand:
                FreehandShape freehandShape = new FreehandShape(prevMouseDragPos, mousePos, color, thickness);
                whiteboardPanel.addShape(freehandShape);
                break;
            case Rectangle:
                if(activeRectShape == null){
                    RectangleShape rect = new RectangleShape(mousePos, color, thickness,
                            1, 1);
                    whiteboardPanel.addShape(rect);
                    activeRectShape = rect;
                }
                activeRectShape.adjustAnchor(mousePos, mousePressPos);
                break;
            case Oval:
                if(activeRectShape == null){
                    OvalShape oval = new OvalShape(mousePos, color, thickness,
                            1, 1);
                    whiteboardPanel.addShape(oval);
                    activeRectShape = oval;
                }
                activeRectShape.adjustAnchor(mousePos, mousePressPos);
                break;
        }
    }
}
