package philip.com.hackernews.mvvm.model.local;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.util.Date;

import philip.com.hackernews.util.ArrayTypeConverter;

/**
 * Database structure of stories
 */
@SuppressWarnings("CanBeFinal")
@Entity(tableName = "stories")
public class StoryEntity {
    private String by;
    @PrimaryKey
    private int id;
    private String title;
    private String type;
    private String url;
    private int score;
    public long time;
    @TypeConverters(ArrayTypeConverter.class)
    private int[] kids;

    public StoryEntity(String by, int id, String title, String type, String url, int score, long time, int[] kids) {
        this.by = by;
        this.id = id;
        this.title = title;
        this.type = type;
        this.url = url;
        this.score = score;
        this.time = time;
        this.kids = kids;
    }

    public String getBy() {
        return by;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public int getScore() {
        return score;
    }

    /**
     * Convert Unix time to date before return.
     */
    public String getTime() {
        Date date = new Date(time * 1000);
        return date.toString();
    }

    public int[] getKids() {
        return kids;
    }

    @Override
    public String toString() {
        return "StoryEntity{" +
                ", id=" + id +
                '}';
    }
}
