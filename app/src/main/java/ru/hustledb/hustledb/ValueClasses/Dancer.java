package ru.hustledb.hustledb.ValueClasses;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sergey on 18.11.16.
 */
public class Dancer {
    public static final String TABLE_NAME = "dancers$";
    public static final String CODE_ASH = "code_ash";
    public static final String DANCER_CLASS = "dancer_class";
    public static final String TITLE = "dancer_title";

    private final String code_ash;
    private final String dancer_class;
    private final String title;
    private List<Club> clubs;
    public Dancer(String code_ash, String dancer_class, String title, @NonNull List<Club> clubs){
        this.code_ash = code_ash;
        this.dancer_class = dancer_class;
        this.title = title;
        this.clubs = new ArrayList<>(clubs.size());
        this.clubs.addAll(clubs);
    }

    public String getTitle() {
        return title;
    }

    public String getCode_ash() {
        return code_ash;
    }

    public String getDancer_class() {
        return dancer_class;
    }

    public List<Club> getClubs() {
        return clubs;
    }
}
