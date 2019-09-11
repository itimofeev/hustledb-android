package ru.hustledb.hustledb.DataProviders.Retrofit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.hustledb.hustledb.ValueClasses.Club;
import ru.hustledb.hustledb.ValueClasses.Dancer;
import ru.hustledb.hustledb.ValueClasses.Nomination;
import ru.hustledb.hustledb.ValueClasses.Preregistration;
import ru.hustledb.hustledb.ValueClasses.Record;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by sergey on 20.11.16.
 */

public class FakeIPP extends InternetPreregistrationProvider{
    @Override
    public Observable<Preregistration> getPreregistrationObservable(int f_competition_id) {
        List<Record> eRecords = new ArrayList<>();
        List<Club> vClubs = new ArrayList<>();
        vClubs.add(new Club("Танцоры"));
        vClubs.add(new Club("Победители"));
        List<Club> iClubs = new ArrayList<>();
        iClubs.add(new Club("Любители кофе"));
        iClubs.add(new Club("АА"));
        eRecords.add(new Record(1, new Dancer("1515", "E", "Иванов Иван Иванович", vClubs),
                new Dancer("1414", "E", "Васильева Василика Васильевна", iClubs)));
        eRecords.add(new Record(2, new Dancer("2525", "D", "Петров Петр Петрович", iClubs),
                new Dancer("2424", "D", "Валентинова Валентина Валентиновна", vClubs)));
        List<Nomination> nominations = new ArrayList<>();
        nominations.add(new Nomination("Хастл E класс", eRecords));
        nominations.add(new Nomination("Хастл D класс", eRecords));
        nominations.add(new Nomination("Хастл C класс", eRecords));
        nominations.add(new Nomination("Хастл B класс", eRecords));
        nominations.add(new Nomination("Хастл A класс", eRecords));
        Preregistration preregistration = new Preregistration("someId", f_competition_id,
                nominations, null);
        return Observable.just(preregistration)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
