package philip.com.hackernews.mvvm.model.local;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;

import java.util.Date;

import philip.com.hackernews.util.ArrayTypeConverter;

/**
 * Created by 1000140 on 2018. 1. 22..
 */

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
}
