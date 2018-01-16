package com.olhu.redditclient.di.home;

import com.olhu.redditclient.activity.HomeActivity;
import com.olhu.redditclient.network.RedditApiClient;
import com.olhu.redditclient.mvp.presenter.HomePresenterImpl;
import com.olhu.redditclient.mvp.contract.HomeContract.HomePresenter;
import com.olhu.redditclient.mvp.contract.HomeContract.HomeView;

import dagger.Module;
import dagger.Provides;

@Module
public class HomeActivityModule {
    private HomeActivity homeActivity;

    public HomeActivityModule(HomeActivity homeActivity) {
        this.homeActivity = homeActivity;
    }

    @Provides
    HomeView provideHomeView() {
        return homeActivity;
    }

    @Provides
    HomePresenter provideHomePresenter(RedditApiClient apiClient, HomeView homeView) {
        return new HomePresenterImpl(apiClient, homeView);
    }
}
