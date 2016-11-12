package ru.hustledb.hustledb.DataProviders.LocalDb;

import android.database.Cursor;

import java.sql.Date;

public final class Db {

    public static String getString(Cursor cursor, String columnName) {
        String string = cursor.getString(cursor.getColumnIndexOrThrow(columnName));
        return string == null? "": string;
    }

    public static int getInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndexOrThrow(columnName));
    }

    public static Integer getInteger(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndexOrThrow(columnName));
    }

    private Db() {
        throw new AssertionError("No instances.");
    }

    public static Date getDate(Cursor cursor, String date) {
        return null;
    }
}