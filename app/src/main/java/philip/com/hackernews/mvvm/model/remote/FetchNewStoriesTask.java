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

public class FetchNewStoriesTask implements Runnable {
    private final MutableLiveData<Resource<List<StoryEntity>>> liveData = new MutableLiveData<>();
    private final ApiInterface apiInterface;
    private final int[] ids;
    private final HackerNewsDb db;

    public FetchNewStoriesTask(ApiInterface apiInterface, int[] ids, HackerNewsDb db) {
        this.apiInterface = apiInterface;
        this.ids = ids;
        this.db = db;
    }

    @Override
    public void run() {
        List<StoryEntity> storyEntities = new ArrayList<>();

        List<StoryEntity> local = db.storyDAO().loadStories();
        if (local.size() < ids.length) {
            call(storyEntities, 0);
        } else {
            Collections.reverse(local);
            storyEntities = local;
        }
        liveData.postValue(Resource.success(storyEntities));
    }

    private void call(List<StoryEntity> storyEntities, int index) {
        try {
            Response<StoryEntity> response = apiInterface.getStory(ids[index]).execute();
            if (response.isSuccessful()) {
                storyEntities.add(response.body());
            }
        } catch (IOException e) {
            //liveData.postValue(Resource.error(e.getMessage(), storyEntities));
        }
        if (index < ids.length - 1) {
            call(storyEntities, index + 1);
        } else {
            try {
                db.beginTransaction();
                db.storyDAO().insertStories(storyEntities);
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }
    }

    public LiveData<Resource<List<StoryEntity>>> getLiveData() {
        return liveData;
    }
}
