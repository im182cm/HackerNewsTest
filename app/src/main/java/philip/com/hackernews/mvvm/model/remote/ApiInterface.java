package philip.com.hackernews.mvvm.model.remote;

import android.arch.lifecycle.LiveData;

import philip.com.hackernews.mvvm.model.local.StoryEntity;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiInterface {
    @GET("newstories.json?print=pretty")
    Call<int[]> getNewStories();

    @GET("item/{id}.json?print=pretty")
    LiveData<ApiResponse<StoryEntity>> getStory(@Path("id") int id);
}
