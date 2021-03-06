package philip.com.hackernews.util;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;

/**
 * TypeConvert to save int array in DB.
 */
public class ArrayTypeConverter {
    private final Gson gson = new Gson();

    @TypeConverter
    public int[] stringToSomeObjectList(String data) {
        if (data == null) {
            return new int[0];
        }

        return gson.fromJson(data, int[].class);
    }

    @TypeConverter
    public String someObjectListToString(int[] someObjects) {
        return gson.toJson(someObjects);
    }
}
