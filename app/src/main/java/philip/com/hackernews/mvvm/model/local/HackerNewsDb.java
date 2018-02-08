package philip.com.hackernews.mvvm.model.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {StoryEntity.class}, version = 1, exportSchema = false)
public abstract class HackerNewsDb extends RoomDatabase {
    abstract public StoryDao storyDAO();
}
