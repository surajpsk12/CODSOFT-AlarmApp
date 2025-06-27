package com.surajvanshsv.alarmapp.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import java.util.List;

@Dao
public interface AlarmDao {

    @Insert
    void insert(AlarmEntity alarm);

    @Update
    void update(AlarmEntity alarm);

    @Delete
    void delete(AlarmEntity alarm);

    @Query("DELETE FROM alarms WHERE id = :id")
    void deleteById(int id);

    @Query("SELECT * FROM alarms ORDER BY triggerMillis ASC")
    LiveData<List<AlarmEntity>> getAllAlarms();

    // 🔴 New Method: Update isEnabled field by alarm ID
    @Query("UPDATE alarms SET isEnabled = :isEnabled WHERE id = :alarmId")
    void updateEnableState(int alarmId, boolean isEnabled);
}
