package client.events;

import java.util.ArrayList;

public class EventHandler <T extends EventArgs> {

    private ArrayList<ICallback<T>> callbacks = new ArrayList<ICallback<T>>();

    public void addCallback(ICallback<T> callback){
        callbacks.add(callback);
    }

    public void removeCallback(ICallback<T> callback){
        callbacks.remove(callback);
    }

    public void invoke(Object source, T eventArgs){
        for(int i = 0; i < callbacks.size(); i++){
            callbacks.get(i).invoke(source, eventArgs);
        }
    }

    public void clear(){
        callbacks.clear();
    }

}
