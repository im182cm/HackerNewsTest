package philip.com.hackernews.mvvm.view.main;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

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
    private final LiveData<Resource<int[]>> mNewStoryIds;
    private LiveData<Resource<List<StoryEntity>>> mNewStories;
    private LiveData<Resource<UserEntity>> mUser;

    @Inject
    Repository mRepository;

    @SuppressWarnings("unchecked")
    @Inject
    public MainViewModel(Repository repository) {
        mNewStoryIds = repository.getTopStoryIds();
    }

    public LiveData<Resource<int[]>> getmTopStoryIds() {
        return mNewStoryIds;
    }

    public LiveData<Resource<List<StoryEntity>>> getmTopStories(int[] ids) {
        mNewStories = mRepository.getStories(ids);
        return mNewStories;
    }

    public LiveData<Resource<UserEntity>> getmUser(String id) {
        mUser = mRepository.getUser(id);
        return mUser;
    }
}
