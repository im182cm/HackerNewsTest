package philip.com.hackernews.mvvm.view.main;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import philip.com.hackernews.mvvm.model.Repository;
import philip.com.hackernews.mvvm.model.Resource;
import philip.com.hackernews.mvvm.model.local.StoryEntity;

/**
 * Created by 1000140 on 2018. 1. 30..
 */

public class MainViewModel extends ViewModel {
    private final LiveData<Resource<int[]>> mNewStoryIds;
    private LiveData<Resource<List<StoryEntity>>> mNewStories;

    @Inject
    Repository mRepository;

    @SuppressWarnings("unchecked")
    @Inject
    public MainViewModel(Repository repository) {
        mNewStoryIds = repository.getNewStoryIds();
    }

    public LiveData<Resource<int[]>> getmNewStoryIds() {
        return mNewStoryIds;
    }

    public LiveData<Resource<List<StoryEntity>>> getmNewStories(int id) {
        mNewStories = mRepository.getStory(id);
        return mNewStories;
    }
}
