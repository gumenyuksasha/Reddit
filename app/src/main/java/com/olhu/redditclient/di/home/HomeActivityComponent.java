package com.olhu.redditclient.di.home;

import com.olhu.redditclient.activity.HomeActivity;

import dagger.BindsInstance;
import dagger.BindsOptionalOf;
import dagger.Subcomponent;

@Subcomponent(modules = HomeActivityModule.class)
public interface HomeActivityComponent {
    void inject(HomeActivity homeActivity);

    @Subcomponent.Builder
    interface Builder {
        Builder homeActivityModule(HomeActivityModule module);

        HomeActivityComponent build();
    }
}