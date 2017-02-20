package ru.hustledb.hustledb.ValueClasses;

/**
 * Created by sergey on 18.11.16.
 */

public class Club {
    public static final String TABLE_NAME = "clubs$";
    public static final String TITLE = "title";

    private final String title;

    public Club(String title){
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return getTitle();
    }
}
