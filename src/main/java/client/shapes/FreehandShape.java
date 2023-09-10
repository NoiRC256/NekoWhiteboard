package client.shapes;

import java.awt.*;
import java.io.Serializable;

/**
 * Shape for freehand drawing.
 */
public class FreehandShape implements IShape, Serializable {

    public Color color;
    public Point point1;
    public Point point2;
    public int thickness;

    public FreehandShape(Point point1, Point point2, Color color, int thickness) {
        this.point1 = point1;
        this.point2 = point2;
        this.color = color;
        this.thickness = thickness;
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(color);
        Stroke stroke =  new BasicStroke(thickness,
                BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_MITER,
                10);
        g.setStroke(stroke);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawLine(point1.x, point1.y, point2.x, point2.y);
    }
}
