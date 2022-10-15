package client.whiteboard;

import client.shapes.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class WhiteboardPanel extends JComponent {

    public BufferedImage bufferedImage;
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

        // Draw main buffered image.
        g2d.drawImage(bufferedImage, 0, 0, null);

        // Update any temporary shape preview.
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
        g2d.dispose();bufferedImage = null;
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
}
