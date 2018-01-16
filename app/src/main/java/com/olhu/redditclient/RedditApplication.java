package com.olhu.redditclient;

import android.app.Application;

import com.olhu.redditclient.di.AppModule;
import com.olhu.redditclient.di.AppComponent;
import com.olhu.redditclient.di.network.DaggerAppComponent;
import com.olhu.redditclient.di.network.NetworkModule;

public class RedditApplication extends Application {
    private static final String BASE_URL = "https://www.reddit.com";
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .networkModule(new NetworkModule(BASE_URL))
                .build();
    }

    public AppComponent appComponent() {
        return appComponent;
    }
}
