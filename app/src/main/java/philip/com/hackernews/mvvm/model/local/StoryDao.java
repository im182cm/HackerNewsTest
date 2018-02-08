package philip.com.hackernews.mvvm.model.local;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertStory(StoryEntity storyEntity);

    @Query("SELECT * from stories")
    LiveData<List<StoryEntity>> loadStories();
}
