package ru.hustledb.hustledb.DataProviders.LocalDb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ru.hustledb.hustledb.App;
import ru.hustledb.hustledb.ValueClasses.Club;
import ru.hustledb.hustledb.ValueClasses.Contest;
import ru.hustledb.hustledb.ValueClasses.Dancer;
import ru.hustledb.hustledb.ValueClasses.Nomination;
import ru.hustledb.hustledb.ValueClasses.Preregistration;
import ru.hustledb.hustledb.ValueClasses.Record;

public class ContestsOpenHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;

    private static final String DANCER_CLUB_TABLE_NAME = "dancer_club$";
    private static final String NOMINATION_PREREGISTRATION_TABLE_NAME = "nomination_preregistration$";
    private static final String RECORD_NOMINATION_TABLE_NAME = "record_nomination$";

    private static final String CREATE_CONTESTS_TABLE = "" +
            "CREATE TABLE " + Contest.TABLE_NAME + "(" +
            Contest.ID + " INTEGER PRIMARY KEY," +
            Contest.TITLE + " TEXT," +
            Contest.DATE + " TEXT," +
            Contest.DATE_STR + " TEXT," +
            Contest.CITY_NAME + " TEXT," +
            Contest.FORUM_URL + " TEXT," +
            Contest.VK_LINK + " TEXT," +
            Contest.PREREG_LINK + " TEXT," +
            Contest.COMMON_INFO + " TEXT," +
            Contest.AVATAR_FILE + " TEXT," +
            Contest.UPDATE_DATE + " TEXT," +
            Contest.LAST_SYNC_DATE + " TEXT," +
            Contest.RESULTS_LINK + " TEXT," +
            Contest.VIDEOS_LINK + " TEXT," +
            Contest.PHOTOS_LINK + " TEXT" + ")";
    private static final String CREATE_CLUB_TABLE = "" +
            "CREATE TABLE " + Club.TABLE_NAME + "(" +
            Club.TITLE + "TEXT PRIMARY KEY" + ")";
    private static final String CREATE_DANCER_CLUB_TABLE = "" +
            "CREATE TABLE " + DANCER_CLUB_TABLE_NAME + "(" +
            Dancer.CODE_ASH + " TEXT PRIMARY KEY REFERENCES " + Dancer.TABLE_NAME +
            "(" + Dancer.CODE_ASH + ") ON UPDATE CASCADE ON DELETE CASCADE," +
            Club.TITLE + " TEXT REFERENCES " + Club.TABLE_NAME +
            "(" + Club.TITLE + ") ON UPDATE CASCADE ON DELETE CASCADE);" +
            "CREATE INDEX titleIndex ON " + DANCER_CLUB_TABLE_NAME + "(" + Club.TITLE + ");";
    private static final String CREATE_DANCER_TABLE = "" +
            "CREATE TABLE " + Dancer.TABLE_NAME + "(" +
            Dancer.CODE_ASH + "INT PRIMARY KEY," +
            Dancer.DANCER_CLASS + "TEXT," +
            Dancer.TITLE + "TEXT NOT NULL" + ")";
    private static final String CREATE_NOMINATION_TABLE = "" +
            "CREATE TABLE " + Nomination.TABLE_NAME + "(" +
            Nomination.TITLE + "TEXT PRIMARY KEY" + ")";
    private static final String CREATE_PREREGISTRATION_TABLE = "" +
            "CREATE TABLE " + Preregistration.TABLE_NAME + "(" +
            Preregistration.ID + "INTEGER PRIMARY KEY," +
            Preregistration.F_COMPETITION_ID + "INTEGER NOT NULL" + ")";
    private static final String CREATE_RECORD_TABLE = "" +
            "CREATE TABLE " + Record.TABLE_NAME + "(" +
            Record.INDEX + "INTEGER PRIMARY KEY," +
            Record.DANCER_1 + "TEXT," +
            Record.DANCER_2 + "TEXT" + ")";
    private static final String CREATE_NOMINATION_PREREGISTRATION_TABLE = "" +
            "CREATE TABLE " + NOMINATION_PREREGISTRATION_TABLE_NAME + "(" +
            Preregistration.ID + " INTEGER PRIMARY KEY," +
            Nomination.TITLE + " TEXT," +
            "FOREIGN KEY(" + Preregistration.ID + ") REFERENCES " + Preregistration.TABLE_NAME +
            "(" + Preregistration.ID + ") ON UPDATE CASCADE ON DELETE CASCADE," +
            "FOREIGN KEY(" + Nomination.TITLE + ") REFERENCES " + Nomination.TABLE_NAME +
            "(" + Nomination.TITLE + ") ON UPDATE CASCADE ON DELETE CASCADE" + ");" +
            "CREATE INDEX titleIndex ON " + NOMINATION_PREREGISTRATION_TABLE_NAME + "(" + Nomination.TITLE + ");";
    private static final String CREATE_RECORD_NOMINATION_TABLE = "" +
            "CREATE TABLE " + RECORD_NOMINATION_TABLE_NAME + "(" +
            Record.INDEX + " INTEGER NOT NULL REFERENCES " + Record.TABLE_NAME +
            "(" + Record.INDEX + ") ON UPDATE CASCADE ON DELETE CASCADE," +
            Nomination.TITLE + " TEXT PRIMARY KEY REFERENCES " + Nomination.TABLE_NAME +
            "(" + Nomination.TITLE + ") ON UPDATE CASCADE ON DELETE CASCADE);" +
            "CREATE INDEX indexIndex ON " + RECORD_NOMINATION_TABLE_NAME + "(" + Record.INDEX + ");";

    public ContestsOpenHelper(Context context) {
        super(context, App.DATABASE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CONTESTS_TABLE);
        db.execSQL(CREATE_DANCER_TABLE);
        db.execSQL(CREATE_CLUB_TABLE);
        db.execSQL(CREATE_DANCER_CLUB_TABLE);
        db.execSQL(CREATE_NOMINATION_TABLE);
        db.execSQL(CREATE_PREREGISTRATION_TABLE);
        db.execSQL(CREATE_NOMINATION_PREREGISTRATION_TABLE);
        db.execSQL(CREATE_RECORD_TABLE);
        db.execSQL(CREATE_RECORD_NOMINATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + Contest.TABLE_NAME);
        db.execSQL("DROP TABLE " + Club.TABLE_NAME);
        db.execSQL("DROP TABLE " + Dancer.TABLE_NAME);
        db.execSQL("DROP TABLE " + DANCER_CLUB_TABLE_NAME);
        db.execSQL("DROP TABLE " + Nomination.TABLE_NAME);
        db.execSQL("DROP TABLE " + Preregistration.TABLE_NAME);
        db.execSQL("DROP TABLE " + NOMINATION_PREREGISTRATION_TABLE_NAME);
        db.execSQL("DROP TABLE " + Record.TABLE_NAME);
        db.execSQL("DROP TABLE " + RECORD_NOMINATION_TABLE_NAME);
        onCreate(db);
    }
}
