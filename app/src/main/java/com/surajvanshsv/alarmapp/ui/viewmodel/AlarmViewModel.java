package com.surajvanshsv.alarmapp.ui.viewmodel;
import com.google.android.material.materialswitch.MaterialSwitch;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.surajvanshsv.alarmapp.data.db.AlarmEntity;
import com.surajvanshsv.alarmapp.data.repository.AlarmRepository;

import java.util.List;

public class AlarmViewModel extends AndroidViewModel {

    private final AlarmRepository repository;
    private final LiveData<List<AlarmEntity>> allAlarms;

    public AlarmViewModel(@NonNull Application application) {
        super(application);
        repository = new AlarmRepository(application);
        allAlarms = repository.getAllAlarms();
    }

    public LiveData<List<AlarmEntity>> getAllAlarms() {
        return allAlarms;
    }

    public void insert(AlarmEntity alarm) {
        repository.insert(alarm);
    }

    public void update(AlarmEntity alarm) {
        repository.update(alarm);
    }

    public void delete(AlarmEntity alarm) {
        repository.delete(alarm);
    }
    public void deleteById(int id) {
        repository.deleteById(id);
    }

    // 🔴 New method to update enable status
    public void updateEnableState(int alarmId, boolean isEnabled) {
        repository.updateEnableState(alarmId, isEnabled);
    }
}
