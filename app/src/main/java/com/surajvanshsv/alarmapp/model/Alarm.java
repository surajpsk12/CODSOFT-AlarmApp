package com.surajvanshsv.alarmapp.model;

public class Alarm {
    private int id;
    private String time;
    private String days;
    private boolean isEnabled;
    private String toneUri;
    private long triggerMillis;
    private String label;         // 🆕 Alarm Label
    private String repeatDays;    // 🆕 Repeat Days

    public Alarm(int id, String time, String days, boolean isEnabled) {
        this.id = id;
        this.time = time;
        this.days = days;
        this.isEnabled = isEnabled;
    }

    // ✅ Setters
    public void setToneUri(String toneUri) {
        this.toneUri = toneUri;
    }

    public void setTriggerMillis(long triggerMillis) {
        this.triggerMillis = triggerMillis;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setRepeatDays(String repeatDays) {
        this.repeatDays = repeatDays;
    }

    public void setEnabled(boolean enabled) {
        this.isEnabled = enabled;
    }

    // ✅ Getters
    public int getId() {
        return id;
    }

    public String getTime() {
        return time;
    }

    public String getDays() {
        return days;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public String getToneUri() {
        return toneUri;
    }

    public long getTriggerMillis() {
        return triggerMillis;
    }

    public String getLabel() {
        return label;
    }

    public String getRepeatDays() {
        return repeatDays;
    }
}
