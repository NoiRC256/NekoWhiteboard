package client.shapes;

import java.awt.*;

public class FreehandShape implements IShape{

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
                BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_ROUND);
        g.setStroke(stroke);
        g.drawLine(point1.x, point1.y, point2.x, point2.y);
    }
}
