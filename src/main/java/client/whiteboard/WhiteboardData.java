package client.whiteboard;

import client.shapes.IShape;

import java.io.Serializable;
import java.util.ArrayList;

public class WhiteboardData implements Serializable {

    public ArrayList<IShape> shapes = new ArrayList<IShape>();

}
