package com.surajvanshsv.alarmapp.data.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {AlarmEntity.class}, version = 2, exportSchema = false)
public abstract class AlarmDatabase extends RoomDatabase {

    private static volatile AlarmDatabase INSTANCE;

    // ✅ Thread pool for background DB operations
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(4); // You can change 4 based on load

    public abstract AlarmDao alarmDao();

    public static AlarmDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AlarmDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AlarmDatabase.class, "alarm_db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
