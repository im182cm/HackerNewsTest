package philip.com.hackernews.mvvm.model.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * Database of HackerNews Application.
 */
@Database(entities = {StoryEntity.class, CommentEntity.class, UserEntity.class}, version = 1, exportSchema = false)
public abstract class HackerNewsDb extends RoomDatabase {
    abstract public StoryDao storyDAO();

    abstract public CommentDao commentDao();

    abstract public UserDao userDao();
}
