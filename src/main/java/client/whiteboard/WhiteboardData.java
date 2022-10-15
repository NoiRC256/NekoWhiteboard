package client.whiteboard;

import client.shapes.IShape;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;

public class WhiteboardData implements Serializable {

    public BufferedImage bufferedImage;
    public IShape previewShape;

}
