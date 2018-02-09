package philip.com.hackernews.mvvm.view.story;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;
import philip.com.hackernews.R;
import philip.com.hackernews.mvvm.model.Resource;
import philip.com.hackernews.mvvm.model.local.CommentEntity;
import philip.com.hackernews.util.Constant;

public class StoryActivity extends DaggerAppCompatActivity {
    private CommentRecyclerViewAdapter mCommentRecyclerViewAdapter;
    @Inject
    ViewModelProvider.Factory mViewModelFactory;

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        initLayout();

        StoryViewModel storyViewModel = ViewModelProviders.of(this, mViewModelFactory).get(StoryViewModel.class);
        storyViewModel.getmComments(getIntent().getIntExtra(Constant.EXTRA_PARENT, -1)).observe(this, new Observer<Resource<List<CommentEntity>>>() {
            @Override
            public void onChanged(@Nullable Resource<List<CommentEntity>> listResource) {
                if (listResource == null || listResource.data == null) {
                    return;
                }
                mCommentRecyclerViewAdapter.setmCommentEntities(listResource.data);
            }
        });

        int[] kids = getIntent().getIntArrayExtra(Constant.EXTRA_KIDS);
        if (kids != null) {
            for (int kid : kids) {
                storyViewModel.getmComments(getIntent().getIntExtra(Constant.EXTRA_PARENT, -1), kid).observe(this, new Observer<Resource<List<CommentEntity>>>() {
                    @Override
                    public void onChanged(@Nullable Resource<List<CommentEntity>> listResource) {
                        if (listResource == null || listResource.data == null)
                            return;

                        mCommentRecyclerViewAdapter.setmCommentEntities(listResource.data);
                    }
                });
            }
        } else {
            mRecyclerView.setVisibility(View.GONE);
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initLayout() {
        WebView webView = findViewById(R.id.webview_url);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        webView.loadUrl(getIntent().getStringExtra(Constant.EXTRA_URL));

        mRecyclerView = findViewById(R.id.recycler_comment);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mCommentRecyclerViewAdapter = new CommentRecyclerViewAdapter();
        mRecyclerView.setAdapter(mCommentRecyclerViewAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mRecyclerView.setAdapter(null);
    }
}
