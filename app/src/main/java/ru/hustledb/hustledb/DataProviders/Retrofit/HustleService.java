package ru.hustledb.hustledb.DataProviders.Retrofit;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import ru.hustledb.hustledb.ValueClasses.Competition;
import ru.hustledb.hustledb.ValueClasses.Preregistration;
import rx.Observable;


public interface HustleService {
    @GET("api/v1/forum/competitions")
    Observable<List<Competition>> getCompetitions();
    @GET("/api/v1/prereg/{fCompId}")
    Observable<Preregistration> getPreregistration(@Path("fCompId") int fCompId);
    @GET("/api/v1/prereg")
    Observable<List<Preregistration>> getPreregistrations();
}
