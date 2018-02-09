package philip.com.hackernews.mvvm.model.remote;

import android.arch.lifecycle.LiveData;

import philip.com.hackernews.mvvm.model.local.CommentEntity;
import philip.com.hackernews.mvvm.model.local.StoryEntity;
import philip.com.hackernews.mvvm.model.local.UserEntity;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiInterface {
    @GET("topstories.json?print=pretty")
    Call<int[]> getNewStories();

    @GET("item/{id}.json?print=pretty")
    Call<StoryEntity> getStory(@Path("id") int id);

    @GET("item/{id}.json?print=pretty")
    LiveData<ApiResponse<CommentEntity>> getComment(@Path("id") int id);

    @GET("user/{id}.json?print=pretty")
    LiveData<ApiResponse<UserEntity>> getUser(@Path("id") String id);
}
