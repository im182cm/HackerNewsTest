package philip.com.hackernews.mvvm.model.local;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Data Access Object for comments db.
 */
@Dao
public interface CommentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertComment(CommentEntity commentEntity);

    @Query("SELECT * from comments where parent = :id")
    LiveData<List<CommentEntity>> loadComments(int id);
}
