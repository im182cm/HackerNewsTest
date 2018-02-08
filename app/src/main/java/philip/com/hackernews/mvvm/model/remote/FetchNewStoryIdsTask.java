package philip.com.hackernews.mvvm.model.remote;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.io.IOException;

import philip.com.hackernews.mvvm.model.Resource;
import retrofit2.Response;

public class FetchNewStoryIdsTask implements Runnable {
    private final MutableLiveData<Resource<int[]>> liveData = new MutableLiveData<>();
    private final ApiInterface apiInterface;

    public FetchNewStoryIdsTask(ApiInterface apiInterface) {
        this.apiInterface = apiInterface;
    }

    @Override
    public void run() {
        try {
            Response<int[]> response = apiInterface.getNewStories().execute();
            ApiResponse<int[]> apiResponse = new ApiResponse<>(response);
            if (apiResponse.isSuccessful()) {
                liveData.postValue(Resource.success(apiResponse.body));
            } else {
                liveData.postValue(Resource.error(apiResponse.errorMessage, new int[1]));
            }
        } catch (IOException e) {
            liveData.postValue(Resource.error(e.getMessage(), new int[1]));
        }
    }

    public LiveData<Resource<int[]>> getLiveData() {
        return liveData;
    }
}
