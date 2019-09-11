package ru.hustledb.hustledb.DataProviders.Retrofit;

import java.util.Collections;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.hustledb.hustledb.ValueClasses.Contest;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class InternetContestsProvider {
    private static final String API_URL = "http://188.166.26.165/";

    public Observable<List<Contest>> getContestsObservable() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(HustleService.class)
                .getContests()
                .onExceptionResumeNext(Observable.just((List<Contest>) Collections.EMPTY_LIST))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
