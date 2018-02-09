package philip.com.hackernews.mvvm.model.local;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
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
    @PrimaryKey(autoGenerate = true)
    private int index;
    private int id;
    private String title;
    private String type;
    private String url;
    private int score;
    public long time;
    @TypeConverters(ArrayTypeConverter.class)
    private int[] kids;

    public StoryEntity(String by, int index, int id, String title, String type, String url, int score, long time, int[] kids) {
        this.by = by;
        this.index = index;
        this.id = id;
        this.title = title;
        this.type = type;
        this.url = url;
        this.score = score;
        this.time = time;
        this.kids = kids;
    }

    @Ignore
    public StoryEntity(int id) {
        this.id = id;
    }

    public String getBy() {
        return by;
    }

    public int getIndex() {
        return index;
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
        return "{" +
                "index="+index+
                "id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        boolean isEqual = false;

        if (obj instanceof StoryEntity){
            StoryEntity storyEntity = (StoryEntity) obj;
            isEqual = storyEntity.id == this.id;
        }

        return isEqual;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + this.id;
        return hash;
    }
}
