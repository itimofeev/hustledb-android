package ru.hustledb.hustledb;

import android.app.Application;

import ru.hustledb.hustledb.Dagger.AppComponent;
import ru.hustledb.hustledb.Dagger.AppModule;
import ru.hustledb.hustledb.Dagger.DaggerAppComponent;

public class App extends Application {

    public static final String DATABASE_NAME = "vHustle";
    private static AppComponent APP_COMPONENT;

    @Override
    public void onCreate(){
        super.onCreate();
        APP_COMPONENT = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }
    public static AppComponent getAppComponent(){
        return APP_COMPONENT;
    }

}
