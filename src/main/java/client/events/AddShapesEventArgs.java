package client.events;

import client.shapes.IShape;

public class AddShapesEventArgs extends EventArgs {

    public final IShape[] shapes;

    public AddShapesEventArgs(IShape[] shapes) {
        this.shapes = shapes;
    }

}
