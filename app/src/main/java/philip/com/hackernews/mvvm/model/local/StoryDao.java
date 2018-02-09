package philip.com.hackernews.mvvm.model.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Data Access Object of stories db.
 */
@Dao
public interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertStories(List<StoryEntity> storyEntities);

    @Query("SELECT * from stories")
    List<StoryEntity> loadStories();
}
