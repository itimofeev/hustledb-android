package ru.hustledb.hustledb.Dagger;

import javax.inject.Singleton;

import dagger.Component;
import ru.hustledb.hustledb.CompetitionsListFragment;
import ru.hustledb.hustledb.DataProviders.LocalDb.LocalCompetitionsProvider;
import ru.hustledb.hustledb.MainActivity;

@Singleton
@Component(modules = {AppModule.class, PersistenceModule.class, ThreadsModule.class})
public interface AppComponent {
    void inject(MainActivity mainActivity);
    void inject(CompetitionsListFragment competitionsListFragment);
    void inject(LocalCompetitionsProvider localCompetitionsProvider);
    void inject(LocalDbModel localDbModel);
}
