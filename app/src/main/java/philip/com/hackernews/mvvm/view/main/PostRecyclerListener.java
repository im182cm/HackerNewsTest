package philip.com.hackernews.mvvm.view.main;

import android.support.annotation.NonNull;

import philip.com.hackernews.mvvm.model.local.StoryEntity;

/**
 * Listener for PostRecyclerView.
 */
interface PostRecyclerListener {
    /**
     * RecyclerView Item click listener.
     */
    void onClick(@NonNull StoryEntity storyEntity, boolean isPost);
}
