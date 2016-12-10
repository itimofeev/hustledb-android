package ru.hustledb.hustledb.ValueClasses;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ru.hustledb.hustledb.DataProviders.LocalDb.Db;
import rx.functions.Func1;

public class Competition implements Comparable<Competition> {
    public static final String DATABASE_NAME = "competitions$";
    public static final String ALL_VALUES_QUERY = "SELECT * FROM " + DATABASE_NAME;
    public static final String ID = "_id";
    public static final String URL = "url";
    public static final String TITLE = "title";
    public static final String DATE = "date";
    public static final String DESCRIPTION = "desc";
    public static final String CITY = "city";

    public static final SimpleDateFormat dateFormat;
    static {
        dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss");
    }

    private final int id;
    private final String url;
    private final String title;
    private final String date;
    private final String desc;
    private final String city;

    public Competition(
            int id,
            @Nullable String url,
            @Nullable String title,
            @Nullable String date,
            @Nullable String desc,
            @Nullable String city) {
        this.id = id;
        this.url = url;
        this.title = title;
        this.date = date;
        this.desc = desc;
        this.city = city;
    }

    public int getId() {
        return id;
    }

    @Nullable
    public String getUrl() {
        return url;
    }

    @Nullable
    public String getTitle() {
        return title;
    }

    @Nullable
    public String getDate() {
        return date;
    }

    public String getPrettyDate(){
        int tIndex = date.indexOf("T");
        if(tIndex == -1){
            return date.replaceAll("-",".");
        }
        return date.substring(0, date.indexOf("T")).replaceAll("-",".");
    }
    @Nullable
    public String getDesc() {
        return desc;
    }

    @Nullable
    public String getCity() {
        return city;
    }

    @Override
    public String toString() {
        return "Competition{"
                + "id=" + id + ", "
                + "url=" + url + ", "
                + "title=" + title + ", "
                + "date=" + date + ", "
                + "desc=" + desc + ", "
                + "city=" + city
                + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof Competition) {
            Competition that = (Competition) o;
            return (this.id == that.getId())
                    && ((this.url == null) ? (that.getUrl() == null) : this.url.equals(that.getUrl()))
                    && ((this.title == null) ? (that.getTitle() == null) : this.title.equals(that.getTitle()))
                    && ((this.date == null) ? (that.getDate() == null) : this.date.equals(that.getDate()))
                    && ((this.desc == null) ? (that.getDesc() == null) : this.desc.equals(that.getDesc()))
                    && ((this.city == null) ? (that.getCity() == null) : this.city.equals(that.getCity()));
        }
        return false;
    }

    @Override
    public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= this.id;
        h *= 1000003;
        h ^= (url == null) ? 0 : this.url.hashCode();
        h *= 1000003;
        h ^= (title == null) ? 0 : this.title.hashCode();
        h *= 1000003;
        h ^= (date == null) ? 0 : this.date.hashCode();
        h *= 1000003;
        h ^= (desc == null) ? 0 : this.desc.hashCode();
        h *= 1000003;
        h ^= (city == null) ? 0 : this.city.hashCode();
        return h;
    }

    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues(6);
        contentValues.put(ID, getId());
        contentValues.put(URL, getUrl());
        contentValues.put(TITLE, getTitle());
        contentValues.put(DATE, getDate());
        contentValues.put(DESCRIPTION, getDesc());
        contentValues.put(CITY, getCity());
        return contentValues;
    }

    public static Func1<Cursor, Competition> MAP = cursor -> new Competition(
            Db.getInt(cursor, ID),
            Db.getString(cursor, URL),
            Db.getString(cursor, TITLE),
            Db.getString(cursor, DATE),
            Db.getString(cursor, DESCRIPTION),
            Db.getString(cursor, CITY));
    @Override
    public int compareTo(@NonNull Competition that) {
        if (this.equals(that)) {
            return 0;
        }
        if (that.date == null && this.date == null) {
            //alphabetic sort?
            return 1;
        }
        Date thatDate;
        try {
            thatDate = dateFormat.parse(that.date);
        } catch (ParseException e) {
            return 1;
        }
        Date thisDate;
        try {
            thisDate = dateFormat.parse(this.date);
        } catch (ParseException e) {
            return -1;
        }
        return thisDate.compareTo(thatDate);
    }
}
