package client.whiteboard;

import client.Main;
import client.shapes.*;
import client.users.UserRole;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.ConcurrentLinkedQueue;

public class WhiteboardPanel extends JComponent {

    public BufferedImage bufferedImage;
    public ConcurrentLinkedQueue<IShape> pendingShapes = new ConcurrentLinkedQueue<IShape>();
    private IShape shapePreview;

    public WhiteboardPanel() {
        super();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        if (bufferedImage == null) {
            initBuffer();
        }

        // Draw any pending shapes received from remote to buffered image.
        synchronized (pendingShapes){
            while(!pendingShapes.isEmpty()){
                IShape shape = pendingShapes.poll();
                drawToBuffer(shape);
            }
        }

        // Draw buffered image to whiteboard.
        g2d.drawImage(bufferedImage, 0, 0, null);

        // Update any temporary shape preview. This is not drawn onto the buffered image.
        if (shapePreview != null) {
            shapePreview.draw(g2d);
        }
    }

    /**
     * Draw a shape to the whiteboard buffer.
     * @param shape
     */
    public void drawToBuffer(IShape shape) {
        Graphics2D g2d = bufferedImage.createGraphics();
        shape.draw(g2d);
        g2d.dispose();
    }

    /**
     * If has active shape preview, draw it to whiteboard buffer.
     */
    public void finalizeShapePreview(){
        if(hasActiveShapePreview()){
            drawToBuffer(shapePreview);
            Main.getInstance().broadcastShape(shapePreview);
            clearShapePreview();
        }
    }

    private void initBuffer() {
        int w = 1280;
        int h = 720;
        //bufferedImage = (BufferedImage) (this.createImage(w, h));
        bufferedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, w, h);
        g2d.dispose();
    }

    public void clearBuffer(){
        int w = 1280;
        int h = 720;
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, w, h);
        g2d.dispose();
        bufferedImage = null;
    }

    public Graphics2D createGraphics() {
        return bufferedImage.createGraphics();
    }

    public Graphics getGraphics() {
        return bufferedImage.getGraphics();
    }

    public Boolean hasActiveShapePreview(){
        return this.shapePreview != null;
    }

    public void clearShapePreview() {
        this.shapePreview = null;
    }

    public void setShapePreview(IShape shape) {
        this.shapePreview = shape;
    }

    public void addPendingShape(IShape shape){
        this.pendingShapes.add(shape);
    }

    public void clearPendingShapes(){
        this.pendingShapes.clear();
    }

    public void clearAll(){
        clearBuffer();
        clearShapePreview();
        clearPendingShapes();
        repaint();
    }
}
