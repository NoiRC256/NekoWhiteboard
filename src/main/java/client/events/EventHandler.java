package client.events;

import java.util.ArrayList;

public class EventHandler {

    private ArrayList<ICallback> callbacks = new ArrayList<ICallback>();

    public void addCallback(ICallback callback){
        callbacks.add(callback);
    }

    public void removeCallback(ICallback callback){
        callbacks.remove(callback);
    }

    public void clear(){
        callbacks.clear();
    }

}
