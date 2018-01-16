package com.olhu.redditclient.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.olhu.redditclient.R;
import com.olhu.redditclient.RedditApplication;
import com.olhu.redditclient.adapter.TopicAdapter;
import com.olhu.redditclient.di.home.HomeActivityModule;
import com.olhu.redditclient.model.HomeInstanceState;
import com.olhu.redditclient.model.Topic;
import com.olhu.redditclient.mvp.contract.HomeContract;

import java.util.List;

import javax.inject.Inject;

public class HomeActivity extends AppCompatActivity implements HomeContract.HomeView {
    @Inject
    HomeContract.HomePresenter presenter;
    private TopicAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        RedditApplication application = (RedditApplication) getApplication();
        application.appComponent().homeActivityComponentBuilder()
                .homeActivityModule(new HomeActivityModule(this))
                .build()
                .inject(this);

        adapter = new TopicAdapter(this);
        setupView();
        initPresenter(savedInstanceState);
    }

    private void setupView() {
        RecyclerView recyclerView = findViewById(R.id.topics_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(false);
        recyclerView.setAdapter(adapter);

        findViewById(R.id.next_page_btn).setOnClickListener(view -> presenter.handleNextPageButton());
        findViewById(R.id.previous_page_btn).setOnClickListener(view -> presenter.handlePreviousPageButton());
    }

    private void initPresenter(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            HomeInstanceState state = savedInstanceState.getParcelable(HomeInstanceState.TAG);
            presenter.restoreState(state);
        } else {
            presenter.firstInit();
        }
    }

    @Override
    public void showTopics(List<Topic> topics) {
        adapter.setTopics(topics);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showCurrentPage(int currentPage, int lastPageNumber) {
        String pageNumber = getString(R.string.page_number, currentPage, lastPageNumber);
        ((AppCompatTextView) findViewById(R.id.page_number_txt)).setText(pageNumber);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(HomeInstanceState.TAG, presenter.getState());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.dettachView();
    }

}
