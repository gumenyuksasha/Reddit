package com.olhu.redditclient.mvp.contract;

import android.os.Parcelable;

import com.olhu.redditclient.model.HomeInstanceState;
import com.olhu.redditclient.model.Topic;

import java.util.List;

public interface HomeContract {
    interface HomeView {

        void showTopics(List<Topic> topics);

        void showCurrentPage(int currentPage, int lastPage);
    }

    interface HomePresenter {

        void firstInit();

        void handlePreviousPageButton();

        void handleNextPageButton();

        Parcelable getState();

        void restoreState(HomeInstanceState state);

        void dettachView();
    }


}