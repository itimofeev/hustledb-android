package ru.hustledb.hustledb.DataProviders.Retrofit;

import java.util.ArrayList;
import java.util.List;

import ru.hustledb.hustledb.ValueClasses.Competition;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by sergey on 19.11.16.
 */

public class FakeICP extends InternetCompetitionsProvider {
    @Override
    public Observable<List<Competition>> getCompetitionsObservable() {
        List<Competition> competitionList = new ArrayList<>();
        competitionList.add(new Competition(1,
                "https://github.com/square/sqlbrite",
                "Огни Москвы",
                "2016-12-12T20:00:00",
                "Впервые в истории Огни Москвы зажгут Петербург",
                "Санкт-Петербург"));
        return Observable.just(competitionList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
