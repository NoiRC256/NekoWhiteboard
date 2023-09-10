package client.events;

public interface ICallback<T extends EventArgs> {

    void invoke(Object source, T args);

}
