package client.toolbox;

import client.shapes.*;

import java.awt.*;

public class Toolbox {
    /**
     * Current tool type.
     */
    public ToolType toolType = ToolType.Freehand;
    public Color color = Color.BLACK;
    public int thickness = 5;

    // Active shape previews.
    public AdjustableShapeBase activeRectShape;
    public AdjustableShapeBase activeOvalShape;
}
