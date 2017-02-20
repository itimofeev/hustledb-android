package ru.hustledb.hustledb.ValueClasses;

import android.support.annotation.Nullable;

/**
 * Created by sergey on 18.11.16.
 */

public class Record {
    public static final String TABLE_NAME = "records$";
    public static final String INDEX = "indeks";
    public static final String DANCER_1 = "dancer_1";
    public static final String DANCER_2 = "dancer_2";

    private final int index;
    private final Dancer dancer_1;
    private final Dancer dancer_2;

    public Record(int index, @Nullable Dancer dancer_1, @Nullable Dancer dancer_2) {
        this.index = index;
        this.dancer_1 = dancer_1;
        this.dancer_2 = dancer_2;
    }

    public int getIndex() {
        return index;
    }

    public Dancer getDancer_1() {
        return dancer_1;
    }

    public Dancer getDancer_2() {
        return dancer_2;
    }
}
