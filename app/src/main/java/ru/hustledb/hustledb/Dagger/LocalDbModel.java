package ru.hustledb.hustledb.Dagger;
import com.squareup.sqlbrite.BriteDatabase;

import javax.inject.Inject;
import javax.inject.Named;

import ru.hustledb.hustledb.App;
import rx.Observable;
import rx.Scheduler;

public class LocalDbModel {
    private final Observable.Transformer schedulersTransformer;

    protected BriteDatabase briteDatabase;

    @Inject
    @Named("UI_THREAD")
    protected Scheduler uiThread;

    @Inject
    @Named("IO_THREAD")
    protected Scheduler ioThread;

    @SuppressWarnings("unchecked")
    protected <T> Observable.Transformer<T, T> applySchedulers() {
        return (Observable.Transformer<T, T>) schedulersTransformer;
    }

    //Doesnt work if you dont do the cast - dont remove!
    public LocalDbModel() {
        App.getAppComponent().inject(this);
        schedulersTransformer = observable -> ((Observable) observable)
                .subscribeOn(ioThread)
                .observeOn(uiThread);
    }
    public void close(){
        if(briteDatabase != null) {
            briteDatabase.close();
        }
    }
}