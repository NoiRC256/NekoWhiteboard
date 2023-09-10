package client.toolbox;

import client.shapes.*;

import java.awt.*;

public class Toolbox {
    /**
     * Current tool type.
     */
    public ToolType toolType = ToolType.Freehand;
    public Color color = Color.ORANGE;
    public int thickness = 5;

    // Active shape previews.
    public AdjustableShapeBase previewShape;
}
