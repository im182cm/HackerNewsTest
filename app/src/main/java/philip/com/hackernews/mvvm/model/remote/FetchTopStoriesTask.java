package philip.com.hackernews.mvvm.model.remote;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import philip.com.hackernews.mvvm.model.Resource;
import philip.com.hackernews.mvvm.model.local.HackerNewsDb;
import philip.com.hackernews.mvvm.model.local.StoryEntity;
import retrofit2.Response;

/**
 * Runnable class to fetch 30 data at once, and save in DB.
 */
public class FetchTopStoriesTask implements Runnable {
    private final MutableLiveData<Resource<List<StoryEntity>>> mLiveData = new MutableLiveData<>();
    private final ApiInterface mApiInterface;
    private final int[] mIds;
    private final HackerNewsDb mDb;
    private final boolean mIsFirst;

    public FetchTopStoriesTask(ApiInterface apiInterface, int[] ids, HackerNewsDb db, boolean isFirst) {
        this.mApiInterface = apiInterface;
        this.mIds = ids;
        this.mDb = db;
        this.mIsFirst = isFirst;
    }

    @Override
    public void run() {
        List<StoryEntity> storyEntities = new ArrayList<>();

        if (mIsFirst) {
            List<StoryEntity> local = mDb.storyDAO().loadStories();
            Collections.reverse(local);
            if (mIds == null || mIds.length == 0) {
                mLiveData.postValue(Resource.success(local));
            } else {
                mLiveData.postValue(Resource.loading(local));
            }
        }

        if (mIds != null && mIds.length != 0) {
            call(storyEntities, 0);
            mLiveData.postValue(Resource.success(storyEntities));
        }
    }

    /**
     * Recursive function to call API one by one.
     */
    private void call(List<StoryEntity> storyEntities, int index) {
        try {
            Response<StoryEntity> response = mApiInterface.getStory(mIds[index]).execute();
            if (response.isSuccessful()) {
                storyEntities.add(response.body());
            }
        } catch (IOException e) {
            //mLiveData.postValue(Resource.error(e.getMessage(), storyEntities));
        }
        if (index < mIds.length - 1) {
            call(storyEntities, index + 1);
        } else {
            // If fectching is done.
            try {
                mDb.beginTransaction();
                mDb.storyDAO().insertStories(storyEntities);
                mDb.setTransactionSuccessful();
            } finally {
                mDb.endTransaction();
            }
        }
    }

    public LiveData<Resource<List<StoryEntity>>> getmLiveData() {
        return mLiveData;
    }
}
