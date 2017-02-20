package ru.hustledb.hustledb.Dagger;

import android.content.Context;
import android.util.Log;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.hustledb.hustledb.ContestsCache;
import ru.hustledb.hustledb.DataProviders.LocalDb.ContestsOpenHelper;
import ru.hustledb.hustledb.DataProviders.Retrofit.FakeIPP;
import ru.hustledb.hustledb.DataProviders.Retrofit.InternetContestsProvider;
import ru.hustledb.hustledb.DataProviders.LocalDb.LocalContestsProvider;
import ru.hustledb.hustledb.DataProviders.Retrofit.InternetPreregistrationProvider;
import ru.hustledb.hustledb.PreregistrationCache;
import rx.schedulers.Schedulers;

@Module
public class PersistenceModule {
    @Provides
    @Singleton
    ContestsCache provideCompetitionsCache(){
        return ContestsCache.getInstance();
    }
    @Provides
    @Singleton
    PreregistrationCache providePreregistrationsCache(){
        return PreregistrationCache.getInstance();
    }
    @Provides
    InternetContestsProvider provideInternetCompetitionsProvider(){
        return new InternetContestsProvider();
    }
//    @Provides
//    InternetContestsProvider provideInternetCompetitions(){
//        return new FakeICP();
//    }
    @Provides
    InternetPreregistrationProvider provideInternetPreregistration(){
        return new FakeIPP();
    }
//    @Provides
//    InternetPreregistrationProvider provideInternetPreregistration(){
//        return new InternetPreregistrationProvider();
//    }
    @Provides
    @Singleton
    LocalContestsProvider provideLocalCompetitions(){
        return new LocalContestsProvider();
    }
    @Provides
    @Singleton
    SqlBrite provideSqlBrite(){
        return SqlBrite.create(message -> Log.d("SqlBrite", message));
    }
    @Provides
    @Singleton
    BriteDatabase provideBriteDatabase(SqlBrite sqlBrite, ContestsOpenHelper contestsOpenHelper){
        BriteDatabase db = sqlBrite.wrapDatabaseHelper(contestsOpenHelper, Schedulers.io());
        db.setLoggingEnabled(true);
        return db;
    }
    @Provides
    @Singleton
    ContestsOpenHelper provideCompetitionsOpenHelper(Context context){
        return new ContestsOpenHelper(context);
    }
}
