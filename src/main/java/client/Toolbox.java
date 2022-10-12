package client;

import client.shapes.*;

import java.awt.*;

import java.awt.*;

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
}
