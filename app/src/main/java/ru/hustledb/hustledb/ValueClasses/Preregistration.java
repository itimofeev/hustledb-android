package ru.hustledb.hustledb.ValueClasses;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sergey on 18.11.16.
 */

public class Preregistration {
    public static final String TABLE_NAME = "preregistrations$";
    public static final String ID = "id";
    public static final String F_COMPETITION_ID = "f_competition_id";

    private final String id;
    private final int f_competition_id;
    private List<Nomination> nominations;
    private Contest contest;

    public Preregistration(String id, int f_competition_id, @NonNull List<Nomination> nominations,
                           @Nullable Contest contest){
        this.id = id;
        this.f_competition_id = f_competition_id;
        this.nominations = new ArrayList<>(nominations.size());
        this.nominations.addAll(nominations);
        this.contest = contest;
    }

    public void setContest(Contest contest){
        this.contest = contest;
    }
    public void setNominations(@NonNull List<Nomination> nominations){
        this.nominations.clear();
        this.nominations.addAll(nominations);
    }
    public int getF_competition_id(){
        return f_competition_id;
    }
    public List<Nomination> getNominations() {
        return nominations;
    }
}
