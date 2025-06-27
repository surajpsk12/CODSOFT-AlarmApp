package com.surajvanshsv.alarmapp.data.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "alarms")
public class AlarmEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String time;            // e.g., 07:30
    public String days;            // legacy support, can be reused
    public boolean isEnabled;
    public String toneUri;
    public long triggerMillis;

    public String label;           // 🆕 Custom label
    public String repeatDays;      // 🆕 Repeat pattern: MTWTFSS

    public AlarmEntity(String time, String days, boolean isEnabled, String toneUri, long triggerMillis, String label, String repeatDays) {
        this.time = time;
        this.days = days;
        this.isEnabled = isEnabled;
        this.toneUri = toneUri;
        this.triggerMillis = triggerMillis;
        this.label = label;
        this.repeatDays = repeatDays;
    }
}
