package ru.hustledb.hustledb.DataProviders.Retrofit;

import java.util.List;

import retrofit2.http.GET;
import ru.hustledb.hustledb.Competition;
import rx.Observable;


public interface HustleService {
    @GET("api/v1/forum/competitions")
    Observable<List<Competition>> getCompetitions();
}
