package ru.hustledb.hustledb;

import com.jakewharton.rxrelay.PublishRelay;
import com.jakewharton.rxrelay.Relay;

import rx.Observable;

public class RxBus {
    private final Relay<Object, Object> bus = PublishRelay.create().toSerialized();
    public void send(Object o){
        bus.call(o);
    }
    public Observable<Object> asObservable() {
        return bus.asObservable();
    }

    public boolean hasObservers() {
        return bus.hasObservers();
    }
}
