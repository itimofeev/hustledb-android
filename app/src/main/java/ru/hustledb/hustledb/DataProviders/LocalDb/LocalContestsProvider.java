package ru.hustledb.hustledb.DataProviders.LocalDb;

import android.database.sqlite.SQLiteDatabase;

import com.squareup.sqlbrite.BriteDatabase;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import ru.hustledb.hustledb.App;
import ru.hustledb.hustledb.ValueClasses.Contest;
import ru.hustledb.hustledb.Events.OnCompetitionsLoadCompleteEvent;
import ru.hustledb.hustledb.RxBus;
import rx.Observable;
import rx.Observer;


public class LocalContestsProvider extends LocalDbModel implements Observer<List<Contest>> {
    @Inject
    BriteDatabase briteDatabase;
    @Inject
    RxBus bus;
    private List<Contest> contests;

    public LocalContestsProvider() {
        super();
        App.getAppComponent().inject(this);
    }

    private void addEntry(Contest contest) {
        briteDatabase.insert(Contest.TABLE_NAME, contest.getContentValues(), SQLiteDatabase.CONFLICT_REPLACE);
    }

//    public void addEntry(int id, String url, String title, String date, String description, String city) {
//        briteDatabase.insert(Contest.TABLE_NAME,
//                (new Contest(id, url, title, date, description, city)).getContentValues(), SQLiteDatabase.CONFLICT_REPLACE);
//    }

    private void addEntries(List<Contest> contests, boolean isReplacing) {
        BriteDatabase.Transaction transaction = briteDatabase.newTransaction();
        try {
            if(isReplacing){
                deleteAllEntries();
            }
            for (Contest contest : contests) {
                addEntry(contest);
            }
            transaction.markSuccessful();
        } finally {
            transaction.end();
        }
    }

    public void deleteEntry(int id) {
        briteDatabase.delete(Contest.TABLE_NAME, Contest.ID + " = ?", String.valueOf(id));
    }

    private void deleteAllEntries() {
        briteDatabase.delete(Contest.TABLE_NAME, null, (String[])null);
    }

    public Observable<List<Contest>> getCompetitionsObservable() {
        return briteDatabase.createQuery(Contest.TABLE_NAME, Contest.ALL_VALUES_QUERY)
                .mapToList(Contest.MAP)
                .compose(applySchedulers());
    }

    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable e) {
        contests.clear();
    }

    // TODO: 19.11.16 Change the ugly fix over here by adding a proper error handling to @InternetContestsProvider
    @Override
    public void onNext(List<Contest> contests) {
        this.contests = contests;
        addEntries(this.contests, true);
        if (bus.hasObservers()) {
            bus.send(new OnCompetitionsLoadCompleteEvent(contests.size() == 0));
        }
    }
}
