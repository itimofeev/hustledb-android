package ru.hustledb.hustledb.DataProviders.LocalDb;

import com.squareup.sqlbrite.BriteDatabase;

import javax.inject.Inject;
import javax.inject.Named;

import ru.hustledb.hustledb.App;
import ru.hustledb.hustledb.RxBus;
import ru.hustledb.hustledb.ValueClasses.Preregistration;
import rx.Observer;

/**
 * Created by sergey on 17.01.17.
 */

public class LocalPreregistrationProvider extends LocalDbModel implements Observer<Preregistration>{
    @Inject
    BriteDatabase briteDatabase;
    @Inject
    RxBus bus;
    public LocalPreregistrationProvider(){
        super();
        App.getAppComponent().inject(this);
    }
    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(Preregistration preregistration) {

    }
}
