package philip.com.hackernews.mvvm.view.main;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import philip.com.hackernews.R;
import philip.com.hackernews.mvvm.model.Resource;
import philip.com.hackernews.mvvm.model.local.StoryEntity;
import philip.com.hackernews.mvvm.model.local.UserEntity;
import philip.com.hackernews.mvvm.view.story.StoryActivity;
import philip.com.hackernews.util.Constant;

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

    private int visibleThreshold = 30;
    private int lastVisibleItem = 0;
    private int totalItemCount = 0;
    private boolean isLoading = false;

    private int[] storyIds;
    private int index = 0;

    private int networkCount = 0;

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
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_post, container, false);

        Context context = view.getContext();

        mRecyclerView = view.findViewById(R.id.list);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mPostRecyclerViewAdapter = new PostRecyclerViewAdapter(new PostRecyclerListener() {
            @Override
            public void onClick(@Nullable String url, @Nullable String by, @Nullable int[] kids, int parent) {
                if (!TextUtils.isEmpty(by)) {
                    getUser(by);
                } else {
                    Intent intent = new Intent(getContext(), StoryActivity.class);
                    intent.putExtra(Constant.EXTRA_URL, url);
                    intent.putExtra(Constant.EXTRA_KIDS, kids);
                    intent.putExtra(Constant.EXTRA_PARENT, parent);
                    startActivity(intent);
                }
            }
        });
        mRecyclerView.setAdapter(mPostRecyclerViewAdapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                Log.d(LOG_TAG, "isLoading="+isLoading +"&&"+totalItemCount + "<=" + lastVisibleItem+"+"+visibleThreshold);
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (storyIds == null)
                        return;
                    isLoading = true;
                    Log.d(LOG_TAG, storyIds.length + "<" + index+"+"+visibleThreshold);
                    if (storyIds.length -1 < index + visibleThreshold) {
                        Log.d(LOG_TAG, visibleThreshold + "=" + storyIds.length+"-1");
                        visibleThreshold = storyIds.length - 1 - index;
                        Log.d(LOG_TAG, "visibleThreshold="+visibleThreshold);

                    }

                    Log.d(LOG_TAG, networkCount + "=" + visibleThreshold);
                    networkCount = visibleThreshold;
                    Log.d(LOG_TAG, "networkCount="+networkCount);

                    for (int i = index; i < index+visibleThreshold; i++) {
                        getNewStories(storyIds[i]);
                    }
                    Log.d(LOG_TAG, index + "=" + visibleThreshold+"+"+index);
                    index += visibleThreshold;
                    Log.d(LOG_TAG, "index="+index);
                }
            }
        });

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

                networkCount = visibleThreshold;
                for (int i = 0; i < visibleThreshold; i++) {
                    getNewStories(listResource.data[i]);
                }
                storyIds = listResource.data;
                index = visibleThreshold;
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

                Collections.reverse(listResource.data);
                mPostRecyclerViewAdapter.setmStoryEntities(listResource.data);

                networkCount--;
                if (networkCount == 0) {
                    isLoading = false;
                    Log.d(LOG_TAG, "isLoading="+isLoading);
                }
            }
        });
    }

    private void getUser(String by) {
        mainViewModel.getmUser(by).observe(this, new Observer<Resource<UserEntity>>() {
            @Override
            public void onChanged(@Nullable Resource<UserEntity> userEntityResource) {
                if (userEntityResource.data == null) {
                    return;
                }

                Intent intent = new Intent(getContext(), BioActivity.class);
                intent.putExtra(Constant.EXTRA_USER, userEntityResource.data);
                startActivity(intent);
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
