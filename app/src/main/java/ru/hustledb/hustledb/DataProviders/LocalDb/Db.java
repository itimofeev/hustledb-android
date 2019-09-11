package ru.hustledb.hustledb.DataProviders.LocalDb;

import android.database.Cursor;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import ru.hustledb.hustledb.ValueClasses.InfoLink;

public final class Db {

    public static String getString(Cursor cursor, String columnName) {
        String string = cursor.getString(cursor.getColumnIndexOrThrow(columnName));
        return string == null ? "" : string;
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

    public static <T> List<T> getList(Cursor cursor, String columnName) {
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        Type collectionType = new TypeToken<List<T>>() {
        }.getType();
        JsonArray jsonArray = parser.parse(cursor.getString(cursor.getColumnIndexOrThrow(columnName))).getAsJsonArray();
        return gson.fromJson(jsonArray, collectionType);
    }
}