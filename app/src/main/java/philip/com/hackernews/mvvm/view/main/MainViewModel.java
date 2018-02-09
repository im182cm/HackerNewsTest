package philip.com.hackernews.mvvm.view.main;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import philip.com.hackernews.mvvm.model.Repository;
import philip.com.hackernews.mvvm.model.Resource;
import philip.com.hackernews.mvvm.model.local.StoryEntity;
import philip.com.hackernews.mvvm.model.local.UserEntity;

/**
 * ViewModel for {@link MainActivity}
 */
public class MainViewModel extends ViewModel {
    private final LiveData<Resource<int[]>> mTopStoryIds;
    private LiveData<Resource<List<StoryEntity>>> mTopStories;
    private LiveData<Resource<UserEntity>> mUser;

    private int mIdsIndex = 0;
    private final int mVisibleThreshold = 30;

    @Inject
    Repository mRepository;

    @SuppressWarnings("unchecked")
    @Inject
    public MainViewModel(Repository repository) {
        mTopStoryIds = repository.getTopStoryIds();
    }

    public LiveData<Resource<int[]>> getmTopStoryIds() {
        return mTopStoryIds;
    }

    public LiveData<Resource<List<StoryEntity>>> getmTopStories(int fetchingDataCount) {
        // If it exceeds Top story ids array size, then fetch amount of remain count.
        if (mTopStoryIds.getValue().data.length - 1 < mIdsIndex + mVisibleThreshold) {
            fetchingDataCount = mTopStoryIds.getValue().data.length - 1 - mIdsIndex;
        }

        boolean isFirst = mIdsIndex == 0;
        // Copy array with range. This will be handed over for data fetching.
        int[] ids = Arrays.copyOfRange(mTopStoryIds.getValue().data, mIdsIndex, mIdsIndex + fetchingDataCount);
        Log.d("HackerNews", "size:"+ids.length);
        // move index.
        mIdsIndex = mIdsIndex + fetchingDataCount;

        mTopStories = mRepository.getStories(ids, isFirst);
        return mTopStories;
    }

    public LiveData<Resource<UserEntity>> getmUser(String id) {
        mUser = mRepository.getUser(id);
        return mUser;
    }
}
