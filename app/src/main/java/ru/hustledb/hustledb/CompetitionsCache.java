package ru.hustledb.hustledb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.Observer;

public class CompetitionsCache implements Observer<List<Competition>> {
    private List<Competition> competitions;
    private List<Observer<List<Competition>>> observers;
    private static CompetitionsCache INSTANCE = new CompetitionsCache();
    private CompetitionsCache(){
        competitions = new ArrayList<>();
        observers = new ArrayList<>();
    }
    public static CompetitionsCache getInstance(){
        return INSTANCE;
    }
    void registerObserver(Observer<List<Competition>> observer){
        observer.onNext(this.competitions);
        observers.add(observer);
    }
    void unregisterObserver(Observer<List<Competition>> observer){
        observers.remove(observer);
    }
    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(List<Competition> competitions) {
        this.competitions.clear();
        this.competitions.addAll(competitions);
        Collections.sort(this.competitions, Collections.reverseOrder());
        Observable.from(observers)
                .forEach(observer -> observer.onNext(this.competitions));
    }

    Competition getCompetitionById(int id) {
        for(Competition c: competitions){
            if(c.getId() == id)
                return c;
        }
        throw new AssertionError();
    }
}
