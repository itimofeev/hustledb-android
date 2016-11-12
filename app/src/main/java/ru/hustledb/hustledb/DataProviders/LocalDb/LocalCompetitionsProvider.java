package ru.hustledb.hustledb.DataProviders.LocalDb;

import android.database.sqlite.SQLiteDatabase;

import com.squareup.otto.Bus;
import com.squareup.sqlbrite.BriteDatabase;

import java.util.List;

import javax.inject.Inject;

import ru.hustledb.hustledb.App;
import ru.hustledb.hustledb.Competition;
import ru.hustledb.hustledb.Dagger.LocalDbModel;
import ru.hustledb.hustledb.Events.OnLoadCompleteEvent;
import rx.Observable;
import rx.Observer;


public class LocalCompetitionsProvider extends LocalDbModel implements Observer<List<Competition>> {
    @Inject
    BriteDatabase briteDatabase;
    @Inject
    Bus bus;
    private List<Competition> competitions;
    public LocalCompetitionsProvider() {
        super();
        App.getAppComponent().inject(this);
    }
    private void addEntry(Competition competition){
        briteDatabase.insert(Competition.DATABASE_NAME, competition.getContentValues(), SQLiteDatabase.CONFLICT_REPLACE);
    }
    public void addEntry(int id, String url, String title, String date, String description, String city) {
        briteDatabase.insert(Competition.DATABASE_NAME,
                (new Competition(id, url, title, date, description, city)).getContentValues(), SQLiteDatabase.CONFLICT_REPLACE);
    }
    private void addEntries(List<Competition> competitions){
        BriteDatabase.Transaction transaction = briteDatabase.newTransaction();
        try {
            for(Competition competition: competitions){
                addEntry(competition);
            }
            transaction.markSuccessful();
        } finally {
            transaction.end();
        }
    }

    public void deleteEntry(int id) {
        briteDatabase.delete(Competition.DATABASE_NAME, Competition.ID + " = ?", String.valueOf(id));
    }

    public Observable<List<Competition>> getCompetitionsObservable() {
        return briteDatabase.createQuery(Competition.DATABASE_NAME, Competition.ALL_VALUES_QUERY)
                .mapToList(Competition.MAP)
                .compose(applySchedulers());
    }

    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable e) {
        competitions.clear();
    }

    @Override
    public void onNext(List<Competition> competitions) {
        this.competitions = competitions;
        addEntries(this.competitions);
        bus.post(new OnLoadCompleteEvent());
    }
}
