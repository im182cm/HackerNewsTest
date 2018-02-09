package philip.com.hackernews.mvvm.model.local;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

/**
 * Database structure of comments.
 */

@SuppressWarnings("CanBeFinal")
@Entity(tableName = "comments")
public class CommentEntity {
    private String by;
    @PrimaryKey
    private int id;
    private String text;
    private String type;
    @SuppressWarnings("WeakerAccess")
    public long time;
    private int parent;

    public CommentEntity(String by, int id, String text, String type, long time, int parent) {
        this.by = by;
        this.id = id;
        this.text = text;
        this.type = type;
        this.time = time;
        this.parent = parent;
    }

    public String getBy() {
        return by;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getType() {
        return type;
    }

    public String getTime() {
        Date date = new Date(time * 1000);
        return date.toString();
    }

    public int getParent() {
        return parent;
    }
}
