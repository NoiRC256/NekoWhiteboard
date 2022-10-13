package client.shapes;

import java.awt.*;
import java.io.Serializable;

/**
 * Draws oval shape.
 */
public class OvalShape extends AdjustableShapeBase implements IShape, Serializable {

    public OvalShape(Point position, Color color, int thickness, int width, int height) {
        this.position = position;
        this.color = color;
        this.thickness = thickness;
        this.width = width;
        this.height = height;
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(color);
        Stroke stroke = new BasicStroke(thickness,
                BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_ROUND);
        g.setStroke(stroke);
        g.drawOval(position.x, position.y, width, height);
    }

}
