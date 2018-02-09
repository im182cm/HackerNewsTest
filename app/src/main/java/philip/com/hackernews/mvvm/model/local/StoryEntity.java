package philip.com.hackernews.mvvm.model.local;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.util.Arrays;
import java.util.Date;

import philip.com.hackernews.util.ArrayTypeConverter;

@Entity(tableName = "stories")
public class StoryEntity {
    public String by;
    @PrimaryKey
    public int id;
    public String title;
    public String type;
    public String url;
    public int score;
    public long time;
    @TypeConverters(ArrayTypeConverter.class)
    public int[] kids;

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
                "by='" + by + '\'' +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", url='" + url + '\'' +
                ", score=" + score +
                ", time=" + time +
                ", kids=" + Arrays.toString(kids) +
                '}';
    }
}
