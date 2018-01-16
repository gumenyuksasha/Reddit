package com.olhu.redditclient.mvp.presenter;

import com.olhu.redditclient.network.RedditApiClient;
import com.olhu.redditclient.network.request.GetTopTopicsRequest;
import com.olhu.redditclient.model.HomeInstanceState;
import com.olhu.redditclient.model.Topic;
import com.olhu.redditclient.mvp.contract.HomeContract.HomePresenter;
import com.olhu.redditclient.mvp.contract.HomeContract.HomeView;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HomePresenterImpl implements HomePresenter {
    private static final int FIRST_PAGE_NUMBER = 1;
    private static final int LAST_PAGE_NUMBER = 5;
    private int currentPageNumber;
    private RedditApiClient apiClient;
    private HomeView view;
    private List<Topic> topics;
    private CompositeDisposable compositeDisposable;

    public HomePresenterImpl(RedditApiClient apiClient, HomeView homeView) {
        this.apiClient = apiClient;
        this.view = homeView;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void firstInit() {
        GetTopTopicsRequest request = GetTopTopicsRequest.builder()
                .newPageNumber(FIRST_PAGE_NUMBER)
                .build();
        getTopics(request);
    }

    @Override
    public void restoreState(HomeInstanceState state) {
        this.topics = state.getTopics();
        this.currentPageNumber = state.getCurrentPage();
        view.showCurrentPage(currentPageNumber, LAST_PAGE_NUMBER);
        view.showTopics(topics);
    }

    @Override
    public void handleNextPageButton() {
        if (currentPageNumber != LAST_PAGE_NUMBER) {
            GetTopTopicsRequest request = GetTopTopicsRequest.builder()
                    .after(getLastTopicName())
                    .newPageNumber(currentPageNumber + 1)
                    .build();
            getTopics(request);
        }
    }

    //  check if is going to the first page to fully refresh topics list
    @Override
    public void handlePreviousPageButton() {
        GetTopTopicsRequest request;
        if (currentPageNumber == (FIRST_PAGE_NUMBER + 1)) {
            request = GetTopTopicsRequest.builder()
                    .newPageNumber(currentPageNumber - 1)
                    .build();
        } else if (currentPageNumber > FIRST_PAGE_NUMBER) {
            request = GetTopTopicsRequest.builder()
                    .newPageNumber(currentPageNumber - 1)
                    .before(getFirstTopicName())
                    .build();
        } else {
            return;
        }
        getTopics(request);
    }

    private void getTopics(GetTopTopicsRequest request) {
        Disposable disposable = apiClient.top(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(r -> handleGetTopicsResponse(r, request.getNewPageNumber()), Throwable::printStackTrace);
        compositeDisposable.add(disposable);
    }

    private void handleGetTopicsResponse(List<Topic> response, int nextPage) {
        this.topics = response;
        this.currentPageNumber = nextPage;
        view.showCurrentPage(currentPageNumber, LAST_PAGE_NUMBER);
        view.showTopics(topics);
    }

    @Override
    public HomeInstanceState getState() {
        return HomeInstanceState.builder()
                .currentPage(currentPageNumber)
                .topics(topics)
                .build();
    }

    private String getLastTopicName() {
        return topics.get(topics.size() - 1).getName();
    }

    private String getFirstTopicName() {
        return topics.get(0).getName();
    }

    @Override
    public void dettachView() {
        compositeDisposable.clear();
    }
}
