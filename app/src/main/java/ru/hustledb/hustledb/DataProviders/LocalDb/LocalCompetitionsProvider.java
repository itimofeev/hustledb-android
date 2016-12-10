package ru.hustledb.hustledb.DataProviders.LocalDb;

import android.database.sqlite.SQLiteDatabase;

import com.squareup.sqlbrite.BriteDatabase;

import java.util.List;

import javax.inject.Inject;

import ru.hustledb.hustledb.App;
import ru.hustledb.hustledb.ValueClasses.Competition;
import ru.hustledb.hustledb.Dagger.LocalDbModel;
import ru.hustledb.hustledb.Events.OnCompetitionsLoadCompleteEvent;
import ru.hustledb.hustledb.RxBus;
import rx.Observable;
import rx.Observer;


public class LocalCompetitionsProvider extends LocalDbModel implements Observer<List<Competition>> {
    @Inject
    BriteDatabase briteDatabase;
    @Inject
    RxBus bus;
    private List<Competition> competitions;

    public LocalCompetitionsProvider() {
        super();
        App.getAppComponent().inject(this);
    }

    private void addEntry(Competition competition) {
        briteDatabase.insert(Competition.DATABASE_NAME, competition.getContentValues(), SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void addEntry(int id, String url, String title, String date, String description, String city) {
        briteDatabase.insert(Competition.DATABASE_NAME,
                (new Competition(id, url, title, date, description, city)).getContentValues(), SQLiteDatabase.CONFLICT_REPLACE);
    }

    private void addEntries(List<Competition> competitions, boolean isReplacing) {
        BriteDatabase.Transaction transaction = briteDatabase.newTransaction();
        try {
            if(isReplacing){
                deleteAllEntries();
            }
            for (Competition competition : competitions) {
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

    private void deleteAllEntries() {
        briteDatabase.delete(Competition.DATABASE_NAME, null, (String[])null);
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

    // TODO: 19.11.16 Change the ugly fix over here by adding a proper error handling to @InternetCompetitionsProvider
    @Override
    public void onNext(List<Competition> competitions) {
        this.competitions = competitions;
        addEntries(this.competitions, true);
        if (bus.hasObservers()) {
            bus.send(new OnCompetitionsLoadCompleteEvent(competitions.size() == 0));
        }
    }
}
