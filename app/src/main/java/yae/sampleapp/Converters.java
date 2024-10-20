package yae.sampleapp;

import androidx.room.TypeConverter;

import java.util.Date;

public class Converters {

    @TypeConverter
    public static Date fromTimestamp(Long val) {
        return val == null ? null : new Date(val);
    }

    @TypeConverter
    public static Long toTimeStamp(Date date) {
        return date == null ? null: date.getTime();
    }
}
