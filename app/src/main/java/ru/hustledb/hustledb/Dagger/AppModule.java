package ru.hustledb.hustledb.Dagger;
import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.hustledb.hustledb.RxBus;

@Module
public class AppModule {
    private Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public Context provideContext() {
        return application;
    }
    @Provides
    @Singleton
    public RxBus provideBus(){return new RxBus();}
}
