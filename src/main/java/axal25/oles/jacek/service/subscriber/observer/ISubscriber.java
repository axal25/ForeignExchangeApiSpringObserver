package axal25.oles.jacek.service.subscriber.observer;

public interface ISubscriber<T> {
    void onMessage(final T message);
}
