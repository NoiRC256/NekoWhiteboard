package server;

import client.shapes.IShape;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Data class that contains whiteboard buffered image and pending shapes.
 */
public class WhiteboardData {

    public BufferedImage bufferedImage;
    private ConcurrentLinkedQueue<IShape> pendingShapes;

    public WhiteboardData(BufferedImage bufferedImage, ConcurrentLinkedQueue<IShape> pendingShapes) {
        this.bufferedImage = bufferedImage;
        this.pendingShapes = pendingShapes;
    }

    /**
     * Draw pending shapes to the buffered image.
     */
    public synchronized void drawPendingShapesToBuffer() {
        // Draw any pending shapes to buffered image.
        while (!pendingShapes.isEmpty()) {
            IShape shape = pendingShapes.poll();
            drawToBuffer(shape);
        }
    }

    /**
     * Draw a shape to the buffered image.
     * @param shape
     */
    public synchronized void drawToBuffer(IShape shape) {
        Graphics2D g2d = bufferedImage.createGraphics();
        shape.draw(g2d);
        g2d.dispose();
    }

    public void addPendingShape(IShape shape){
        pendingShapes.add(shape);
    }

}
