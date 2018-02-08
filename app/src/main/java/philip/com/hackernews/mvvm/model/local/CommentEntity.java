package philip.com.hackernews.mvvm.model.local;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.util.Date;

import philip.com.hackernews.util.ArrayTypeConverter;

/**
 * Created by 1000140 on 2018. 1. 22..
 */

@Entity(tableName = "comments")
public class CommentEntity {
    public String by;
    @PrimaryKey
    public int id;
    public String text;
    public String type;
    public long time;

    public CommentEntity(String by, int id, String text, String type, long time) {
        this.by = by;
        this.id = id;
        this.text = text;
        this.type = type;
        this.time = time;
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
}
