package client.events;

import client.shapes.IShape;
import org.w3c.dom.events.Event;

public class ShapeEventArgs extends EventArgs {

    public final IShape shape;

    public ShapeEventArgs(IShape shape){
        this.shape = shape;
    }
}
