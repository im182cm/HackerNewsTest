package philip.com.hackernews.mvvm.view.main;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import philip.com.hackernews.R;
import philip.com.hackernews.mvvm.model.Resource;
import philip.com.hackernews.mvvm.model.local.StoryEntity;
import philip.com.hackernews.mvvm.model.local.UserEntity;
import philip.com.hackernews.mvvm.view.bio.BioActivity;
import philip.com.hackernews.mvvm.view.story.StoryActivity;
import philip.com.hackernews.util.Constant;

/**
 * A fragment representing a list of Items.
 */
public class PostFragment extends DaggerFragment {
    @Inject
    ViewModelProvider.Factory mViewModelFactory;
    private MainViewModel mMainViewModel;
    private PostRecyclerViewAdapter mPostRecyclerViewAdapter;
    private RecyclerView mRecyclerView;

    // RecyclerView scroll threshold.
    private final int mVisibleThreshold = 30;
    private boolean mIsLoading = false;
    private boolean mIsStoryIdsLoaded = false;

    @SuppressWarnings("CanBeFinal")
    private List<StoryEntity> mStoryEntities = new ArrayList<>();

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    @Inject
    public PostFragment() {
    }

    @SuppressWarnings({"ConstantConditions", "NullableProblems"})
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
            public void onClick(@NonNull StoryEntity storyEntity, boolean isPost) {
                if (isPost) {
                    // this is for story and comments.
                    Intent intent = new Intent(getContext(), StoryActivity.class);
                    intent.putExtra(Constant.EXTRA_URL, storyEntity.getUrl());
                    intent.putExtra(Constant.EXTRA_KIDS, storyEntity.getKids());
                    intent.putExtra(Constant.EXTRA_PARENT, storyEntity.getId());
                    startActivity(intent);
                } else {
                    // this is for bio.
                    getUser(storyEntity.getBy());
                }
            }
        });
        mRecyclerView.setAdapter(mPostRecyclerViewAdapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int totalItemCount = linearLayoutManager.getItemCount();
                int lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                // if it is not loading data from API AND currently looking item + threshold > total Item count.
                if (!mIsLoading && totalItemCount <= (lastVisibleItem + mVisibleThreshold)) {
                    // if scroll before fetch story ids from API.
                    if (!mIsStoryIdsLoaded) {
                        return;
                    }
                    // Let's say it is started.
                    mIsLoading = true;

                    getTopStories(mVisibleThreshold);
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMainViewModel = ViewModelProviders.of(this, mViewModelFactory).get(MainViewModel.class);
        mMainViewModel.getmTopStoryIds().observe(this, new Observer<Resource<int[]>>() {
            @Override
            public void onChanged(@Nullable Resource<int[]> listResource) {
                if (listResource == null || listResource.data == null) {
                    return;
                }

                mIsStoryIdsLoaded = true;

                int fetchingDataCount;
                // If mVisibleThreshold is bigger than Top stories array. Maybe This will not going to happen in this app.
                if (mVisibleThreshold > listResource.data.length) {
                    fetchingDataCount = listResource.data.length;
                } else {
                    fetchingDataCount = mVisibleThreshold;
                }

                // First fetching.
                getTopStories(fetchingDataCount);
            }
        });
    }

    private void getTopStories(int fetchingDataCount) {
        mMainViewModel.getmTopStories(fetchingDataCount).observe(this, new Observer<Resource<List<StoryEntity>>>() {
            @Override
            public void onChanged(@Nullable Resource<List<StoryEntity>> listResource) {
                if (listResource == null || listResource.data == null)
                    return;

                mStoryEntities.addAll(listResource.data);
                mPostRecyclerViewAdapter.setmStoryEntities(mStoryEntities);
                mIsLoading = false;
            }
        });
    }

    private void getUser(String by) {
        mMainViewModel.getmUser(by).observe(this, new Observer<Resource<UserEntity>>() {
            @Override
            public void onChanged(@Nullable Resource<UserEntity> userEntityResource) {
                if (userEntityResource == null || userEntityResource.data == null) {
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

        mRecyclerView.setAdapter(null);
    }
}
