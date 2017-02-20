package ru.hustledb.hustledb.DataProviders.Retrofit;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.hustledb.hustledb.ValueClasses.Preregistration;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by sergey on 19.11.16.
 */

public class InternetPreregistrationProvider {
    private static final String API_URL = "http://188.166.26.165/";

    public Observable<Preregistration> getPreregistrationObservable(int f_competition_id) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(HustleService.class)
                .getPreregistration(f_competition_id)
                .onExceptionResumeNext(Observable.just(null))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
