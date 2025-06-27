package com.surajvanshsv.alarmapp.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.surajvanshsv.alarmapp.data.db.AlarmDao;
import com.surajvanshsv.alarmapp.data.db.AlarmDatabase;
import com.surajvanshsv.alarmapp.data.db.AlarmEntity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AlarmRepository {

    private final AlarmDao alarmDao;
    private final LiveData<List<AlarmEntity>> allAlarms;

    private final ExecutorService executorService;

    public AlarmRepository(Application application) {
        AlarmDatabase database = AlarmDatabase.getInstance(application);
        alarmDao = database.alarmDao();
        allAlarms = alarmDao.getAllAlarms();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<AlarmEntity>> getAllAlarms() {
        return allAlarms;
    }

    public void insert(AlarmEntity alarm) {
        executorService.execute(() -> alarmDao.insert(alarm));
    }

    public void update(AlarmEntity alarm) {
        executorService.execute(() -> alarmDao.update(alarm));
    }
    public void deleteById(int id) {
        AlarmDatabase.databaseWriteExecutor.execute(() -> {
            alarmDao.deleteById(id);
        });
    }

    public void delete(AlarmEntity alarm) {
        executorService.execute(() -> alarmDao.delete(alarm));
    }

    // 🔴 New method to update enable status
    public void updateEnableState(int alarmId, boolean isEnabled) {
        executorService.execute(() -> alarmDao.updateEnableState(alarmId, isEnabled));
    }
}
