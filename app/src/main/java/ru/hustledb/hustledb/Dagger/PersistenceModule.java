package ru.hustledb.hustledb.Dagger;

import android.content.Context;
import android.util.Log;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.hustledb.hustledb.CompetitionsCache;
import ru.hustledb.hustledb.DataProviders.LocalDb.CompetitionsOpenHelper;
import ru.hustledb.hustledb.DataProviders.Retrofit.InternetCompetitionsProvider;
import ru.hustledb.hustledb.DataProviders.LocalDb.LocalCompetitionsProvider;
import rx.schedulers.Schedulers;

@Module
public class PersistenceModule {
    @Provides
    @Singleton
    CompetitionsCache provideCompetitionsCache(){
        return CompetitionsCache.getInstance();
    }
    @Provides
    InternetCompetitionsProvider provideInternerCompetitionsProvider(){
        return new InternetCompetitionsProvider();
    }
    @Provides
    @Singleton
    LocalCompetitionsProvider localCompetitionsProvider(){
        return new LocalCompetitionsProvider();
    }
    @Provides
    @Singleton
    SqlBrite provideSqlBrite(){
        return SqlBrite.create(message -> Log.d("SqlBrite", message));
    }
    @Provides
    @Singleton
    BriteDatabase provideBriteDatabase(SqlBrite sqlBrite, CompetitionsOpenHelper competitionsOpenHelper){
        BriteDatabase db = sqlBrite.wrapDatabaseHelper(competitionsOpenHelper, Schedulers.io());
        db.setLoggingEnabled(true);
        return db;
    }
    @Provides
    @Singleton
    CompetitionsOpenHelper provideCompetitionsOpenHelper(Context context){
        return new CompetitionsOpenHelper(context);
    }
}
