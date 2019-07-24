package com.app.devchat.data.SqlDatabase;

import java.util.Date;

import androidx.room.TypeConverter;

import com.app.devchat.data.DataModels.Message;

public class TypeConverters {
    private static final int TEXT = 0;
    private static final int MULTI_MEDIA = 1;

    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static Message.MessageType fromInteger(int value){
        switch (value) {
            case TEXT:
                return Message.MessageType.TEXT;
            case  MULTI_MEDIA:
                return Message.MessageType.MULTIMEDIA;
        }
        return null;
    }

    @TypeConverter
    public static int messageTypeToInt(Message.MessageType value){
        switch (value) {
            case MULTIMEDIA:
                return MULTI_MEDIA;
            case TEXT:
                return TEXT;
        }
        return -1;
    }
}
