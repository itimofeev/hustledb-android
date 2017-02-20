package ru.hustledb.hustledb;

import android.util.SparseArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.hustledb.hustledb.DataProviders.Retrofit.InternetPreregistrationProvider;
import ru.hustledb.hustledb.ValueClasses.Contest;
import rx.Observable;
import rx.Observer;

public class ContestsCache implements Observer<List<Contest>>{

    private List<Contest> contests;
    private Contest selectedContest;
    private SparseArray<List<Contest>> contestsByYears;
    private List<Observer<List<Contest>>> observers;
    private static ContestsCache INSTANCE = new ContestsCache();

    private ContestsCache() {
        contests = new ArrayList<>();
        observers = new ArrayList<>();
        contestsByYears = new SparseArray<>();
    }

    public static ContestsCache getInstance() {
        return INSTANCE;
    }

    void registerObserver(Observer<List<Contest>> observer) {
        observer.onNext(this.contests);
        observers.add(observer);
    }

    void unregisterObserver(Observer<List<Contest>> observer) {
        observers.remove(observer);
    }

    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(List<Contest> contests) {
        this.contests.clear();
        this.contests.addAll(contests);
        Collections.sort(this.contests, Collections.reverseOrder());
        Observable.from(observers)
                .forEach(observer -> observer.onNext(this.contests));
    }
    public void onYearSelected(int year){

    }

    Contest getCompetitionById(int id) {
        for (Contest c : contests) {
            if (c.getId() == id) {
                selectedContest = c;
                return c;
            }
        }
        throw new AssertionError();
    }

    public Contest getSelectedContest() {
        return selectedContest;
    }
}
