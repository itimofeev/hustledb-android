package ru.hustledb.hustledb.DataProviders.LocalDb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ru.hustledb.hustledb.Competition;

public class CompetitionsOpenHelper extends SQLiteOpenHelper{
    private static final int DB_VERSION = 1;
    private static final String CREATE_TABLE = "" +
            "CREATE TABLE " + Competition.DATABASE_NAME + "(" +
            Competition.ID + " INTEGER PRIMARY KEY," +
            Competition.URL + " TEXT," +
            Competition.TITLE + " TEXT," +
            Competition.DATE + " TEXT," +
            Competition.DESCRIPTION + " TEXT," +
            Competition.CITY + " TEXT" + ")";

    public CompetitionsOpenHelper(Context context){
        super(context, Competition.DATABASE_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + Competition.DATABASE_NAME);
        onCreate(db);
    }
}
