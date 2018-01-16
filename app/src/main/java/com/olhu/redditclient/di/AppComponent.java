package com.olhu.redditclient.di;

import com.olhu.redditclient.di.AppModule;
import com.olhu.redditclient.di.home.HomeActivityComponent;
import com.olhu.redditclient.di.network.NetworkModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, NetworkModule.class})
public interface AppComponent {
    HomeActivityComponent.Builder homeActivityComponentBuilder();
}
