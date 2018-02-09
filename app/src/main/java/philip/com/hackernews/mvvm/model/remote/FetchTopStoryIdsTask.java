package philip.com.hackernews.mvvm.model.remote;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.io.IOException;

import philip.com.hackernews.mvvm.model.Resource;
import retrofit2.Response;

/**
 * Runnable class to fecth top story ids. Do not save in DB.
 * Top 500 stories are changing in real time.
 */
public class FetchTopStoryIdsTask implements Runnable {
    private final MutableLiveData<Resource<int[]>> mLiveData = new MutableLiveData<>();
    private final ApiInterface mApiInterface;

    public FetchTopStoryIdsTask(ApiInterface apiInterface) {
        this.mApiInterface = apiInterface;
    }

    @Override
    public void run() {
        try {
            Response<int[]> response = mApiInterface.getNewStories().execute();
            ApiResponse<int[]> apiResponse = new ApiResponse<>(response);
            if (apiResponse.isSuccessful()) {
                mLiveData.postValue(Resource.success(apiResponse.body));
            } else {
                mLiveData.postValue(Resource.error(apiResponse.errorMessage, new int[1]));
            }
        } catch (IOException e) {
            mLiveData.postValue(Resource.error(e.getMessage(), new int[1]));
        }
    }

    public LiveData<Resource<int[]>> getmLiveData() {
        return mLiveData;
    }
}
