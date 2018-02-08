package philip.com.hackernews.mvvm.model.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {StoryEntity.class, CommentEntity.class}, version = 1, exportSchema = false)
public abstract class HackerNewsDb extends RoomDatabase {
    abstract public StoryDao storyDAO();

    abstract public CommentDao commentDao();
}
