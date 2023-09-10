package server;

import client.shapes.IShape;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Data class that contains whiteboard buffered image and pending shapes.
 */
public class WhiteboardData implements Serializable {

    public BufferedImage bufferedImage;
    public ConcurrentLinkedQueue<IShape> pendingShapes = new ConcurrentLinkedQueue<IShape>();

    public WhiteboardData(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
    }

    public WhiteboardData(BufferedImage bufferedImage, ConcurrentLinkedQueue<IShape> pendingShapes) {
        this.bufferedImage = bufferedImage;
        this.pendingShapes = pendingShapes;
    }

    /**
     * Convert buffered image to byte array.
     * @param bufferedImage
     * @param format
     * @return
     * @throws IOException
     */
    public static byte[] toByteArray(BufferedImage bufferedImage, String format) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, format, out);
        byte[] bytes = out.toByteArray();
        return bytes;
    }

    /**
     * Get buffered image from byte array.
     * @param bytes
     * @return
     * @throws IOException
     */
    public static BufferedImage toBufferedImage(byte[] bytes) throws IOException{
        InputStream in = new ByteArrayInputStream(bytes);
        BufferedImage bufferedImage = ImageIO.read(in);
        return bufferedImage;
    }

    /**
     * Update whiteboard data.
     * Usually called before sending.
     */
    public synchronized void update(){
        drawPendingShapesToBuffer();
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
