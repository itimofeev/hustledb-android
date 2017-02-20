package ru.hustledb.hustledb.Dagger;

import javax.inject.Singleton;

import dagger.Component;
import ru.hustledb.hustledb.ContestDetailsFragment;
import ru.hustledb.hustledb.ContestsListFragment;
import ru.hustledb.hustledb.DataProviders.LocalDb.LocalContestsProvider;
import ru.hustledb.hustledb.DataProviders.LocalDb.LocalDbModel;
import ru.hustledb.hustledb.DataProviders.LocalDb.LocalPreregistrationProvider;
import ru.hustledb.hustledb.DataProviders.Retrofit.InternetContestsProvider;
import ru.hustledb.hustledb.MainActivity;
import ru.hustledb.hustledb.NominationFragment;
import ru.hustledb.hustledb.NominationRecyclerAdapter;
import ru.hustledb.hustledb.PreregistrationCache;
import ru.hustledb.hustledb.PreregistrationFragment;

@Singleton
@Component(modules = {AppModule.class, PersistenceModule.class, ThreadsModule.class})
public interface AppComponent {
    void inject(MainActivity mainActivity);
    void inject(ContestsListFragment contestsListFragment);
    void inject(PreregistrationFragment preregistrationFragment);
    void inject(LocalContestsProvider localContestsProvider);
    void inject(LocalPreregistrationProvider localPreregistrationProvider);
    void inject(LocalDbModel localDbModel);
    void inject(InternetContestsProvider internetContestsProvider);
    void inject(PreregistrationCache preregistrationCache);
    void inject(NominationFragment nominationFragment);
    void inject(ContestDetailsFragment contestDetailsFragment);

    void inject(NominationRecyclerAdapter nominationRecyclerAdapter);
}
