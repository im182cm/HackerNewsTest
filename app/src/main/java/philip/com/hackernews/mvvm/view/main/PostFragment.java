package philip.com.hackernews.mvvm.view.main;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import philip.com.hackernews.R;
import philip.com.hackernews.mvvm.model.Resource;
import philip.com.hackernews.mvvm.model.local.StoryEntity;

/**
 * A fragment representing a list of Items.
 * <p/>
 * interface.
 */
public class PostFragment extends DaggerFragment {
    private static final String LOG_TAG = PostFragment.class.getSimpleName();

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private MainViewModel mainViewModel;
    private PostRecyclerViewAdapter mPostRecyclerViewAdapter;
    private RecyclerView mRecyclerView;
    private RequestManager mRequestManager;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    @Inject
    public PostFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRequestManager = Glide.with(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post, container, false);

        Context context = view.getContext();

        mRecyclerView = view.findViewById(R.id.list);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mPostRecyclerViewAdapter = new PostRecyclerViewAdapter(mRequestManager, new PostRecyclerListener() {
            @Override
            public void onClick(@Nullable String url, @Nullable String by) {
                if (!TextUtils.isEmpty(url) && !TextUtils.isEmpty(by))
                    return;

                if (!TextUtils.isEmpty(url)) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } else{
                    Log.d(LOG_TAG, by);
                }
            }
        });
        mRecyclerView.setAdapter(mPostRecyclerViewAdapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mainViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel.class);
        mainViewModel.getmNewStoryIds().observe(this, new Observer<Resource<int[]>>() {
            @Override
            public void onChanged(@Nullable Resource<int[]> listResource) {
                Log.d(LOG_TAG, listResource.status.name());

                if (listResource.data == null) {
                    return;
                }
                Log.d(LOG_TAG, listResource.data.toString());
                for (int id : listResource.data) {
                    getNewStories(id);

                }
            }
        });
        getNewStories(-1);
    }

    private void getNewStories(int id) {
        mainViewModel.getmNewStories(id).observe(this, new Observer<Resource<List<StoryEntity>>>() {
            @Override
            public void onChanged(@Nullable Resource<List<StoryEntity>> listResource) {
                if (listResource.data == null) {
                    return;
                }

                mPostRecyclerViewAdapter.setmStoryEntities(listResource.data);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Unregister the adapter.
        // Because the RecyclerView won't unregister the adapter, the
        // ViewHolders are very likely leaked.
        mRecyclerView.setAdapter(null);
    }
}
