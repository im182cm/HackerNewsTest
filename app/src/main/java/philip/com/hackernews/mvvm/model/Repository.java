package philip.com.hackernews.mvvm.model;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import philip.com.hackernews.AppExecutors;
import philip.com.hackernews.mvvm.model.local.CommentEntity;
import philip.com.hackernews.mvvm.model.local.HackerNewsDb;
import philip.com.hackernews.mvvm.model.local.StoryEntity;
import philip.com.hackernews.mvvm.model.local.UserEntity;
import philip.com.hackernews.mvvm.model.remote.ApiInterface;
import philip.com.hackernews.mvvm.model.remote.ApiResponse;
import philip.com.hackernews.mvvm.model.remote.FetchNewStoriesTask;
import philip.com.hackernews.mvvm.model.remote.FetchNewStoryIdsTask;

@Singleton
public class Repository {
    private static final String LOG_TAG = Repository.class.getSimpleName();
    private static Repository INSTANCE = null;
    private final ApiInterface mApiInterface;
    private final HackerNewsDb mHackerNewsDb;
    private final AppExecutors appExecutors;

    @Inject
    public Repository(HackerNewsDb hackerNewsDb, ApiInterface apiInterface, AppExecutors appExecutors) {
        this.mHackerNewsDb = hackerNewsDb;
        this.mApiInterface = apiInterface;
        this.appExecutors = appExecutors;
    }

    public LiveData<Resource<int[]>> getNewStoryIds() {
        FetchNewStoryIdsTask fetchNewStoryIdsTask = new FetchNewStoryIdsTask(mApiInterface);
        appExecutors.networkIO().execute(fetchNewStoryIdsTask);
        return fetchNewStoryIdsTask.getLiveData();
    }

    public LiveData<Resource<List<StoryEntity>>> getStory(int[] ids) {
        FetchNewStoriesTask fetchNewStoriesTask = new FetchNewStoriesTask(mApiInterface, ids);
        appExecutors.networkIO().execute(fetchNewStoriesTask);
        return fetchNewStoriesTask.getLiveData();
        /*return new NetworkBoundResource<List<StoryEntity>, StoryEntity>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull StoryEntity newStory) {
                mHackerNewsDb.storyDAO().insertStory(newStory);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<StoryEntity> data) {
                boolean isExist = false;
                for (StoryEntity storyEntity : data){
                    if (storyEntity.getId() == id) {
                        isExist = true;
                        break;
                    }
                }
                return id > 0 && !isExist;
            }

            @NonNull
            @Override
            protected LiveData<List<StoryEntity>> loadFromDb() {
                return mHackerNewsDb.storyDAO().loadStories();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<StoryEntity>> createCall() {
                return mApiInterface.getStory(id);
            }
        }.asLiveData();*/
    }

    public LiveData<Resource<List<CommentEntity>>> getComment(final int parent, final int id) {
        return new NetworkBoundResource<List<CommentEntity>, CommentEntity>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull CommentEntity commentEntity) {
                mHackerNewsDb.commentDao().insertComment(commentEntity);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<CommentEntity> data) {
                boolean isExist = false;
                for (CommentEntity commentEntity : data){
                    if (commentEntity.getId() == id) {
                        isExist = true;
                        break;
                    }
                }
                return id > 0 && !isExist;
            }

            @NonNull
            @Override
            protected LiveData<List<CommentEntity>> loadFromDb() {
                return mHackerNewsDb.commentDao().loadComments(parent);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<CommentEntity>> createCall() {
                return mApiInterface.getComment(id);
            }
        }.asLiveData();
    }

    public LiveData<Resource<UserEntity>> getUser(final String id) {
        return new NetworkBoundResource<UserEntity, UserEntity>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull UserEntity userEntity) {
                mHackerNewsDb.userDao().insertUser(userEntity);
            }

            @Override
            protected boolean shouldFetch(@Nullable UserEntity data) {
                return !TextUtils.isEmpty(id);
            }

            @NonNull
            @Override
            protected LiveData<UserEntity> loadFromDb() {
                return mHackerNewsDb.userDao().loadUser(id);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<UserEntity>> createCall() {
                return mApiInterface.getUser(id);
            }
        }.asLiveData();
    }
}
