package philip.com.hackernews.mvvm.model.local;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.Date;

import philip.com.hackernews.util.ArrayTypeConverter;

/**
 * Database structure of users.
 */
@Entity(tableName = "users")
public class UserEntity implements Parcelable {
    @PrimaryKey
    @NonNull
    private String id;
    public int created;
    private int karma;
    private String about;
    @TypeConverters(ArrayTypeConverter.class)
    private int[] submitted;

    public UserEntity(String id, int created, int karma, String about, int[] submitted) {
        this.id = id;
        this.created = created;
        this.karma = karma;
        this.about = about;
        this.submitted = submitted;
    }

    protected UserEntity(Parcel in) {
        id = in.readString();
        created = in.readInt();
        karma = in.readInt();
        about = in.readString();
        submitted = in.createIntArray();
    }

    public static final Creator<UserEntity> CREATOR = new Creator<UserEntity>() {
        @Override
        public UserEntity createFromParcel(Parcel in) {
            return new UserEntity(in);
        }

        @Override
        public UserEntity[] newArray(int size) {
            return new UserEntity[size];
        }
    };

    public String getId() {
        return id;
    }

    public int getKarma() {
        return karma;
    }

    public String getAbout() {
        return about;
    }

    public String getCreated() {
        Date date = new Date(created * 1000);
        return date.toString();
    }

    public int[] getSubmitted() {
        return submitted;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeInt(created);
        dest.writeInt(karma);
        dest.writeString(about);
        dest.writeIntArray(submitted);
    }
}
