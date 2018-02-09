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
import philip.com.hackernews.mvvm.model.remote.FetchTopStoriesTask;
import philip.com.hackernews.mvvm.model.remote.FetchTopStoryIdsTask;
import philip.com.hackernews.mvvm.view.main.PostFragment;

/**
 * Repository for local and remote.
 */
@Singleton
public class Repository {
    private final ApiInterface mApiInterface;
    private final HackerNewsDb mHackerNewsDb;
    private final AppExecutors appExecutors;

    @Inject
    public Repository(HackerNewsDb hackerNewsDb, ApiInterface apiInterface, AppExecutors appExecutors) {
        this.mHackerNewsDb = hackerNewsDb;
        this.mApiInterface = apiInterface;
        this.appExecutors = appExecutors;
    }

    /**
     * Fetch Top story ids.
     */
    public LiveData<Resource<int[]>> getTopStoryIds() {
        FetchTopStoryIdsTask fetchTopStoryIdsTask = new FetchTopStoryIdsTask(mApiInterface);
        appExecutors.networkIO().execute(fetchTopStoryIdsTask);
        return fetchTopStoryIdsTask.getmLiveData();
    }

    /**
     * Get Stories by id array. Sync network connection.
     *
     * @param ids set in {@link PostFragment#getTopStories(int)}
     */
    public LiveData<Resource<List<StoryEntity>>> getStories(int[] ids, boolean isFrist) {
        FetchTopStoriesTask fetchTopStoriesTask = new FetchTopStoriesTask(mApiInterface, ids, mHackerNewsDb, isFrist);
        appExecutors.networkIO().execute(fetchTopStoriesTask);
        return fetchTopStoriesTask.getmLiveData();
    }

    /**
     * Get comment. Async connection.
     */
    public LiveData<Resource<List<CommentEntity>>> getComment(final int parent, final int id) {
        return new NetworkBoundResource<List<CommentEntity>, CommentEntity>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull CommentEntity commentEntity) {
                mHackerNewsDb.commentDao().insertComment(commentEntity);
            }

            /**
             * If comment is exist in DB, then not fetch. I know that comment can be fixed, but be simple.
             */
            @Override
            protected boolean shouldFetch(@Nullable List<CommentEntity> data) {
                if (data == null)
                    return true;

                boolean isExist = false;
                for (CommentEntity commentEntity : data) {
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

    /**
     * Get Bio Information.
     */
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
