package com.nnd.popularmovies;

import android.app.Application;

import com.nnd.popularmovies.dependency.AppComponent;
import com.nnd.popularmovies.dependency.DaggerAppComponent;
import com.nnd.popularmovies.dependency.NetworkModule;

import timber.log.Timber;

/**
 * Created by Android dev on 6/17/17.
 */

public class App extends Application {
    private static AppComponent appComponent;

    public static AppComponent getAppComponent() {
        if (appComponent == null) {
            createComponent();
        }
        return appComponent;
    }

    protected static void createComponent() {
        appComponent = DaggerAppComponent.builder()
                .networkModule(new NetworkModule())
                .build();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Timber.plant(new Timber.DebugTree());
        createComponent();
    }
}
