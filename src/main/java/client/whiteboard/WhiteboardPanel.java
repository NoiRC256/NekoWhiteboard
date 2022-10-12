package client.whiteboard;

import client.shapes.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class WhiteboardPanel extends JPanel {

    private ArrayList<IShape> shapes = new ArrayList<IShape>();

    public WhiteboardPanel() {
        super();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        for (IShape shape : shapes) {
            shape.draw(g2d);
        }
    }

    public void addShape(IShape shape) {
        shapes.add(shape);
    }

    public void removeShape(IShape shape) {
        shapes.remove(shape);
    }

    public void removeRecentShape() {
        if (shapes.size() > 0) {
            shapes.remove(shapes.size() - 1);
        }
    }

    public void clearShapes() {
        shapes.clear();
    }

    public void setShapes(ArrayList<IShape> shapes) {
        this.shapes = shapes;
    }
}
