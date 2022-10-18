package packet;

import client.shapes.IShape;

import java.io.Serializable;

/**
 * Message used for syncing new shapes.
 */
public class WhiteboardShapeNotify extends Message implements Serializable {

    public final IShape shape;

    public WhiteboardShapeNotify(IShape shape){
        this.shape = shape;
    }
}
