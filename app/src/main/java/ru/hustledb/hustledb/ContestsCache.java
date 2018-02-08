package ru.hustledb.hustledb;

import android.util.SparseArray;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import ru.hustledb.hustledb.ValueClasses.Contest;
import rx.Observable;
import rx.Observer;

public class ContestsCache implements Observer<List<Contest>> {
    public static final int FUTURE_YEAR = 0;
    public static final int ASCENDING_DATE = 100;
    public static final int DESCENDING_DATE = 101;
    public static final int ASCENDING_NAME = 102;
    public static final int DESCENDING_NAME = 103;

    //    private List<Contest> contests;
    private Contest selectedContest;
    private int selectedYear;
    private int sortingOrder;
    private List<Contest> contests;
    private SparseArray<List<Contest>> contestsByYears;
    private List<Observer<List<Contest>>> observers;
    private List<Observer<SparseArray<List<Contest>>>> observersByYear;
    private static ContestsCache INSTANCE = new ContestsCache();

    private ContestsCache() {
        contests = new ArrayList<>();
        observers = new ArrayList<>();
        observersByYear = new ArrayList<>();
        contestsByYears = new SparseArray<>();
        sortingOrder = ContestsCache.DESCENDING_DATE;
        selectedYear = ContestsCache.FUTURE_YEAR;
    }

    public static ContestsCache getInstance() {
        return INSTANCE;
    }

    void registerObserver(Observer<List<Contest>> observer) {
        observer.onNext(contests);
        observers.add(observer);
    }

    void registerObserverByYear(Observer<SparseArray<List<Contest>>> observer) {
        observer.onNext(contestsByYears);
        observersByYear.add(observer);
    }

    void unregisterObserver(Observer<SparseArray<List<Contest>>> observer) {
        observersByYear.remove(observer);
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
        this.contestsByYears.clear();

        Date now = new Date();
        Collections.sort(contests, Collections.reverseOrder());
        Observable.from(contests)
                .forEach(contest -> {
                    try {
                        Date date = Contest.dateFormat.parse(contest.getDate());
                        if(date.compareTo(now) > 0){
                            List<Contest> yearContests = contestsByYears.get(ContestsCache.FUTURE_YEAR);
                            if (yearContests == null) {
                                yearContests = new ArrayList<>();
                                contestsByYears.append(ContestsCache.FUTURE_YEAR, yearContests);
                            }
                            yearContests.add(contest);
                        } else {
                            int year = Integer.valueOf(contest.getDate().substring(0, 4));
                            List<Contest> yearContests = contestsByYears.get(year);
                            if (yearContests == null) {
                                yearContests = new ArrayList<>();
                                contestsByYears.append(year, yearContests);
                            }
                            yearContests.add(contest);
                        }
                    } catch (ParseException e) {

                    }
                });
        for (int i = 0; i < contestsByYears.size(); i++) {
            Collections.sort(contestsByYears.get(contestsByYears.keyAt(i)), Collections.reverseOrder());
        }
        Observable.from(observers)
                .forEach(observer -> observer.onNext(this.contests));
        Observable.from(observersByYear)
                .forEach(observer -> observer.onNext(contestsByYears));
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

    public int getYearsNumber() {
        return contestsByYears.size();
    }

    public String getYearAt(int position) {
        int year = contestsByYears.keyAt(position);
        return year == ContestsCache.FUTURE_YEAR? "Новые": String.valueOf(year);
    }
}
