package ru.hustledb.hustledb;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ru.hustledb.hustledb.Events.OnPreregistrationLoadCompleteEvent;
import ru.hustledb.hustledb.ValueClasses.Nomination;
import ru.hustledb.hustledb.ValueClasses.Preregistration;
import rx.Observable;
import rx.Observer;

/**
 * Created by sergey on 20.11.16.
 */

public class PreregistrationCache implements Observer<Preregistration> {

    private Preregistration preregistration;
    private List<Observer<Preregistration>> preregistrationObservers;
    private List<Observer<Nomination>> nominationObservers;

    @Inject
    RxBus bus;

    private static PreregistrationCache INSTANCE = new PreregistrationCache();
    private String selectedNominationTitle;

    private PreregistrationCache() {
        App.getAppComponent().inject(this);
        preregistrationObservers = new ArrayList<>();
        nominationObservers = new ArrayList<>();
    }

    public static PreregistrationCache getInstance() {
        return INSTANCE;
    }

    public void registerPreregistrationObserver(Observer<Preregistration> observer) {
        if(preregistration != null) {
            observer.onNext(preregistration);
        }
        preregistrationObservers.add(observer);

    }
    public void registerNominationObserver(Observer<Nomination> observer){
        observer.onNext(getSelectedNomination());
        nominationObservers.add(observer);
    }
    public void unregisterPreregistrationObserver(Observer<Preregistration> observer) {
        preregistrationObservers.remove(observer);
    }
    public void unregisterNominationObserver(Observer<Nomination> observer){
        nominationObservers.remove(observer);
    }

    @Override
    public void onCompleted() {


    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(Preregistration preregistration) {
        this.preregistration = preregistration;
        Observable.from(preregistrationObservers)
                .forEach(observer -> observer.onNext(this.preregistration));
        Observable.from(nominationObservers)
                .forEach(observer -> observer.onNext(this.getSelectedNomination()));
        if(bus.hasObservers()){
            if(preregistration != null) {
                bus.send(new OnPreregistrationLoadCompleteEvent(false));
            } else {
                bus.send(new OnPreregistrationLoadCompleteEvent(true));
            }
        }
    }
    public Integer getCurrentPreregistrationId(){
        if(preregistration != null){
            return preregistration.getF_competition_id();
        }
        return null;
    }
    public void selectNomination(String nominationTitle) {
        this.selectedNominationTitle = nominationTitle;
    }

    public Nomination getSelectedNomination() {
        for(Nomination nomination: preregistration.getNominations()){
            if(nomination.getTitle().equals(selectedNominationTitle)){
                return nomination;
            }
        }
        return null;
    }
}
