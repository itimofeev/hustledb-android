package ru.hustledb.hustledb.ValueClasses;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sergey on 18.11.16.
 */

public class Preregistration {

    private final String id;
    private final int f_competition_id;
    private List<Nomination> nominations;
    private Competition competition;

    public Preregistration(String id, int f_competition_id, @NonNull List<Nomination> nominations,
                           @Nullable Competition competition){
        this.id = id;
        this.f_competition_id = f_competition_id;
        this.nominations = new ArrayList<>(nominations.size());
        this.nominations.addAll(nominations);
        this.competition = competition;
    }

    public void setCompetition(Competition competition){
        this.competition = competition;
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
