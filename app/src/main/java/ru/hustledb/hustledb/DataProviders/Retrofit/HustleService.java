package ru.hustledb.hustledb.DataProviders.Retrofit;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import ru.hustledb.hustledb.ValueClasses.Contest;
import ru.hustledb.hustledb.ValueClasses.Preregistration;
import rx.Observable;

public interface HustleService {
    @GET("/api/v1/contests")
    Observable<List<Contest>> getContests();
    @GET("/api/v1/prereg/{fContId}")
    Observable<Preregistration> getPreregistration(@Path("fContId") int fContId);
    @GET("/api/v1/prereg")
    Observable<List<Preregistration>> getPreregistrations();
}
