package philip.com.hackernews.mvvm.model.remote;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import philip.com.hackernews.mvvm.model.Resource;
import philip.com.hackernews.mvvm.model.local.StoryEntity;
import retrofit2.Response;

public class FetchNewStoriesTask implements Runnable {
    private final MutableLiveData<Resource<List<StoryEntity>>> liveData = new MutableLiveData<>();
    private final ApiInterface apiInterface;
    private final int[] ids;

    public FetchNewStoriesTask(ApiInterface apiInterface, int[] ids) {
        this.apiInterface = apiInterface;
        this.ids = ids;
    }

    @Override
    public void run() {
        List<StoryEntity> storyEntities = new ArrayList<>();

        call(storyEntities, 0);
        liveData.postValue(Resource.success(storyEntities));
    }

    private void call(List<StoryEntity> storyEntities, int index) {
        try {
            Response<StoryEntity> response = apiInterface.getStory(ids[index]).execute();
            if (response.isSuccessful()) {
                storyEntities.add(response.body());
            }
        } catch (IOException e) {
            liveData.postValue(Resource.error(e.getMessage(), storyEntities));
        }
        if (index < ids.length -1)
            call(storyEntities, index+1);
    }

    public LiveData<Resource<List<StoryEntity>>> getLiveData() {
        return liveData;
    }
}
