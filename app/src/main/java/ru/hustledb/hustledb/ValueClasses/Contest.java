package ru.hustledb.hustledb.ValueClasses;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.hustledb.hustledb.DataProviders.LocalDb.Db;
import rx.functions.Func1;

public class Contest implements Comparable<Contest> {
    public static final String TABLE_NAME = "contests$";
    public static final String ALL_VALUES_QUERY = "SELECT * FROM " + TABLE_NAME;
    public static final String ID = "_id";
    public static final String TITLE = "title";
    public static final String DATE = "date";
    public static final String DATE_STR = "date_str";
    public static final String CITY_NAME = "city_name";
    public static final String FORUM_URL = "forum_url";
    public static final String VK_LINK = "vk_link";
    public static final String PREREG_LINK = "prereg_link";
    public static final String COMMON_INFO = "common_info";
    public static final String AVATAR_FILE = "avatar_file";
    public static final String UPDATE_DATE = "update_date";
    public static final String LAST_SYNC_DATE = "last_sync_date";
    public static final String RESULTS_LINK = "results_link";
    public static final String VIDEOS_LINK = "videos_link";
    public static final String PHOTOS_LINK = "photos_link";

    public static final SimpleDateFormat dateFormat;

    static {
        dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss");
    }

    private final int id;
    private final String title;
    private final String date;
    private final String date_str;
    private final String city_name;
    private final String forum_url;
    private final String vk_link;
    private final String prereg_link;
    private final String common_info;
    private final String avatar_file;
    private final String update_date;
    private final String last_sync_date;

    private final List<InfoLink> results_link;
    private final List<InfoLink> videos_link;
    private final List<InfoLink> photos_link;

    public Contest(
            int id,
            @Nullable String title,
            @Nullable String date,
            @Nullable String date_str,
            @Nullable String city_name,
            @Nullable String forum_url,
            @Nullable String vk_link,
            @Nullable String prereg_link,
            @Nullable String common_info,
            @Nullable String avatar_file,
            @Nullable String update_date,
            @Nullable String last_sync_date,
            @NonNull List<InfoLink> results_link,
            @NonNull List<InfoLink> videos_link,
            @NonNull List<InfoLink> photos_link) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.date_str = date_str;
        this.city_name = city_name;
        this.forum_url = forum_url;
        this.vk_link = vk_link;
        this.prereg_link = prereg_link;
        this.common_info = common_info;
        this.avatar_file = avatar_file;
        this.update_date = update_date;
        this.last_sync_date = last_sync_date;
        this.results_link = new ArrayList<>(results_link.size());
        this.results_link.addAll(results_link);
        this.videos_link = new ArrayList<>(videos_link.size());
        this.videos_link.addAll(videos_link);
        this.photos_link = new ArrayList<>(photos_link.size());
        this.photos_link.addAll(photos_link);
    }

    public int getId() {
        return id;
    }

    @Nullable
    public String getTitle() {
        return title;
    }

    @Nullable
    public String getDate() {
        return date;
    }

    public String getPrettyDate() {
        int tIndex = date.indexOf("T");
        if (tIndex == -1) {
            return date.replaceAll("-", ".");
        }
        return date.substring(0, date.indexOf("T")).replaceAll("-", ".");
    }

    @Nullable
    public String getCityName() {
        return city_name;
    }

    @Nullable
    public String getDateStr() {
        return date_str;
    }

    @Nullable
    public String getForumUrl() {
        return forum_url;
    }

    @Nullable
    public String getVkLink() {
        return vk_link;
    }

    @Nullable
    public String getPreregLink() {
        return prereg_link;
    }

    @Nullable
    public String getCommonInfo() {
        return common_info;
    }

    @Nullable
    public String getAvatarFile() {
        return avatar_file;
    }

    @Nullable
    public String getUpdateDate() {
        return update_date;
    }

    @Nullable
    public String getLastSyncDate() {
        return last_sync_date;
    }

    @NonNull
    public List<InfoLink> getResultsLink() {
        return results_link;
    }

    @NonNull
    public List<InfoLink> getPhotosLink() {
        return photos_link;
    }

    @NonNull
    public List<InfoLink> getVideosLink() {
        return videos_link;
    }

    @Override
    public String toString() {
        return "Contest{"
                + "id=" + id + ", "
                + "title=" + title + ", "
                + "date=" + date + ", "
                + "}";
    }

    //my equals is bad and i should feel bad but i dont
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof Contest) {
            Contest that = (Contest) o;
            return (this.id == that.getId())
                    && ((this.last_sync_date == null) ? (that.getLastSyncDate() == null) :
                    this.last_sync_date.equals(that.getLastSyncDate()));
        }
        return false;
    }

    @Override
    public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= this.id;
        h *= 1000003;
        h ^= (last_sync_date == null) ? 0 : this.last_sync_date.hashCode();
        return h;
    }
    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues(6);
        contentValues.put(ID, getId());
        contentValues.put(TITLE, getTitle());
        contentValues.put(DATE, getDate());
        contentValues.put(DATE_STR, getDateStr());
        contentValues.put(CITY_NAME, getCityName());
        contentValues.put(FORUM_URL, getForumUrl());
        contentValues.put(VK_LINK, getVkLink());
        contentValues.put(PREREG_LINK, getPreregLink());
        contentValues.put(COMMON_INFO, getCommonInfo());
        contentValues.put(AVATAR_FILE, getAvatarFile());
        contentValues.put(UPDATE_DATE, getUpdateDate());
        contentValues.put(LAST_SYNC_DATE, getLastSyncDate());
        Gson gson = new Gson();
        contentValues.put(RESULTS_LINK, gson.toJson(getResultsLink()));
        contentValues.put(VIDEOS_LINK, gson.toJson(getVideosLink()));
        contentValues.put(PHOTOS_LINK, gson.toJson(getPhotosLink()));
        return contentValues;
    }

    public static Func1<Cursor, Contest> MAP = cursor -> new Contest(
            Db.getInt(cursor, ID),
            Db.getString(cursor, TITLE),
            Db.getString(cursor, DATE),
            Db.getString(cursor, DATE_STR),
            Db.getString(cursor, CITY_NAME),
            Db.getString(cursor, FORUM_URL),
            Db.getString(cursor, VK_LINK),
            Db.getString(cursor, PREREG_LINK),
            Db.getString(cursor, COMMON_INFO),
            Db.getString(cursor, AVATAR_FILE),
            Db.getString(cursor, UPDATE_DATE),
            Db.getString(cursor, LAST_SYNC_DATE),
            Db.getList(cursor, RESULTS_LINK),
            Db.getList(cursor, VIDEOS_LINK),
            Db.getList(cursor, PHOTOS_LINK)
    );

    @Override
    public int compareTo(@NonNull Contest that) {
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
