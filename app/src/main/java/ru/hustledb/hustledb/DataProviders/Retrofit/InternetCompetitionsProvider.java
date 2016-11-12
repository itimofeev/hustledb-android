package ru.hustledb.hustledb.DataProviders.Retrofit;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.hustledb.hustledb.Competition;
import ru.hustledb.hustledb.DataProviders.Retrofit.HustleService;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class InternetCompetitionsProvider{
    private static final String API_URL = "http://188.166.26.165:18080/";

    public InternetCompetitionsProvider(){}
    public Observable<List<Competition>> getCompetitionsObservable() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(HustleService.class)
                .getCompetitions()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
