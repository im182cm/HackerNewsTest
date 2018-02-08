package philip.com.hackernews.mvvm.view.story;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import philip.com.hackernews.mvvm.model.Repository;
import philip.com.hackernews.mvvm.model.Resource;
import philip.com.hackernews.mvvm.model.local.CommentEntity;
import philip.com.hackernews.mvvm.model.local.StoryEntity;

public class StoryViewModel extends ViewModel {
    private LiveData<Resource<List<CommentEntity>>> mComments;

    @Inject
    Repository mRepository;

    @SuppressWarnings("unchecked")
    @Inject
    public StoryViewModel(Repository repository) {
    }

    public LiveData<Resource<List<CommentEntity>>> getmComments(int parent, int id) {
        mComments = mRepository.getComment(parent, id);
        return mComments;
    }

    public LiveData<Resource<List<CommentEntity>>> getmComments(int parent) {
        return mComments = mRepository.getComment(parent, -1);
    }
}
