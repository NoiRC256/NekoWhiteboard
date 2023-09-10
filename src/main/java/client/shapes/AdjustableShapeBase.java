package client.shapes;

import java.awt.*;
import java.io.Serializable;

public abstract class AdjustableShapeBase implements IShape, Serializable {

    public Color color;
    public Point position;
    public int thickness;
    public int width;
    public int height;

    public AdjustableShapeBase() {
    }

    public void adjustAnchor(Point mousePos, Point mousePressPos) {
        Point position = mousePos;
        int deltaX = mousePos.x - mousePressPos.x;
        int deltaY = mousePos.y - mousePressPos.y;
        if (deltaX > 0)
            position.x = mousePressPos.x;
        if (deltaY > 0)
            position.y = mousePressPos.y;
        this.position = position;
        this.width = Math.abs(deltaX);
        this.height = Math.abs(deltaY);
    }
}
