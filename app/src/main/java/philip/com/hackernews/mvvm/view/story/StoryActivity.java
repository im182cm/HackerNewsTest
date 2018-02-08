package philip.com.hackernews.mvvm.view.story;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
    private final String LOG_TAG = StoryActivity.class.getSimpleName();

    private CommentRecyclerViewAdapter mCommentRecyclerViewAdapter;
    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        initLayout();

        StoryViewModel storyViewModel = ViewModelProviders.of(this, viewModelFactory).get(StoryViewModel.class);
        storyViewModel.getmComments().observe(this, new Observer<Resource<List<CommentEntity>>>() {
            @Override
            public void onChanged(@Nullable Resource<List<CommentEntity>> listResource) {
                Log.d(LOG_TAG, listResource.status.name());

                if (listResource.data == null) {
                    return;
                }
                mCommentRecyclerViewAdapter.setmCommentEntities(listResource.data);
            }
        });

        int[] kids = getIntent().getIntArrayExtra(Constant.EXTRA_KIDS);
        if (kids != null){
            for (int kid : kids){
                storyViewModel.getmComments(kid).observe(this, new Observer<Resource<List<CommentEntity>>>() {
                    @Override
                    public void onChanged(@Nullable Resource<List<CommentEntity>> listResource) {
                        if (listResource.data == null)
                            return;

                        mCommentRecyclerViewAdapter.setmCommentEntities(listResource.data);
                    }
                });
            }
        }
    }

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

        RecyclerView recyclerView = findViewById(R.id.recycler_comment);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        mCommentRecyclerViewAdapter = new CommentRecyclerViewAdapter();
        recyclerView.setAdapter(mCommentRecyclerViewAdapter);
    }
}
