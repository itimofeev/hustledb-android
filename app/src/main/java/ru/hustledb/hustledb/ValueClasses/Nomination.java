package ru.hustledb.hustledb.ValueClasses;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sergey on 18.11.16.
 */

public class Nomination {
    private final String title;
    private List<Record> records;
    public Nomination(String title, @NonNull List<Record> records){
        this.title = title;
        this.records = new ArrayList<>(records.size());
        this.records.addAll(records);
    }

    public String getTitle() {
        return title;
    }

    public List<Record> getRecords() {
        return records;
    }
}
