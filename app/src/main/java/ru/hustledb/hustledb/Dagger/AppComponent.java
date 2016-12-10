package ru.hustledb.hustledb.Dagger;

import javax.inject.Singleton;

import dagger.Component;
import ru.hustledb.hustledb.CompetitionDetailsFragment;
import ru.hustledb.hustledb.CompetitionsCache;
import ru.hustledb.hustledb.CompetitionsListFragment;
import ru.hustledb.hustledb.DataProviders.LocalDb.LocalCompetitionsProvider;
import ru.hustledb.hustledb.DataProviders.Retrofit.InternetCompetitionsProvider;
import ru.hustledb.hustledb.MainActivity;
import ru.hustledb.hustledb.NominationFragment;
import ru.hustledb.hustledb.NominationRecyclerAdapter;
import ru.hustledb.hustledb.PreregistrationCache;
import ru.hustledb.hustledb.PreregistrationFragment;

@Singleton
@Component(modules = {AppModule.class, PersistenceModule.class, ThreadsModule.class})
public interface AppComponent {
    void inject(MainActivity mainActivity);
    void inject(CompetitionsListFragment competitionsListFragment);
    void inject(PreregistrationFragment preregistrationFragment);
    void inject(LocalCompetitionsProvider localCompetitionsProvider);
    void inject(LocalDbModel localDbModel);
    void inject(InternetCompetitionsProvider internetCompetitionsProvider);
    void inject(PreregistrationCache preregistrationCache);
    void inject(NominationFragment nominationFragment);
    void inject(CompetitionDetailsFragment competitionDetailsFragment);

    void inject(NominationRecyclerAdapter nominationRecyclerAdapter);
}
