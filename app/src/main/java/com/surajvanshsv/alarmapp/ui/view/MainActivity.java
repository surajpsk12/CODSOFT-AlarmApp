package com.surajvanshsv.alarmapp.ui.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.surajvanshsv.alarmapp.R;
import com.surajvanshsv.alarmapp.data.db.AlarmEntity;
import com.surajvanshsv.alarmapp.handler.AlarmScheduler;
import com.surajvanshsv.alarmapp.model.Alarm;
import com.surajvanshsv.alarmapp.ui.adapter.AlarmAdapter;
import com.surajvanshsv.alarmapp.ui.viewmodel.AlarmViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;import com.google.android.material.materialswitch.MaterialSwitch;


public class MainActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "theme_prefs";
    private static final String KEY_IS_DARK_MODE = "is_dark";

    private TextView tvCurrentDate;
    private RecyclerView recyclerViewAlarms;
    private FloatingActionButton fabAddAlarm;
    private AlarmViewModel alarmViewModel;
    private AlarmAdapter alarmAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isDark = prefs.getBoolean(KEY_IS_DARK_MODE, false);
        AppCompatDelegate.setDefaultNightMode(isDark ?
                AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);

        tvCurrentDate = findViewById(R.id.tvCurrentDate);
        recyclerViewAlarms = findViewById(R.id.recyclerViewAlarms);
        fabAddAlarm = findViewById(R.id.fabAddAlarm);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM", Locale.getDefault());
        tvCurrentDate.setText(dateFormat.format(calendar.getTime()));

        alarmViewModel = new ViewModelProvider(this).get(AlarmViewModel.class);
        alarmAdapter = new AlarmAdapter(
                new ArrayList<>(),
                (alarm, isEnabled) -> {
                    alarmViewModel.updateEnableState(alarm.getId(), isEnabled);

                    if (isEnabled) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                            android.app.AlarmManager alarmManager = (android.app.AlarmManager) getSystemService(ALARM_SERVICE);
                            if (alarmManager != null && !alarmManager.canScheduleExactAlarms()) {
                                Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                                startActivity(intent);
                                return;
                            }
                        }

                        AlarmEntity entity = new AlarmEntity(
                                alarm.getTime(),
                                alarm.getDays(),
                                true,
                                alarm.getToneUri(),
                                alarm.getTriggerMillis(),
                                alarm.getLabel(),
                                alarm.getRepeatDays()
                        );
                        entity.id = alarm.getId();

                        AlarmScheduler.scheduleAlarm(MainActivity.this, entity);
                    } else {
                        AlarmScheduler.cancelAlarm(MainActivity.this, alarm.getId());
                    }
                },
                alarm -> {
                    Intent intent = new Intent(MainActivity.this, SetAlarmActivity.class);
                    intent.putExtra("edit_mode", true);
                    intent.putExtra("alarm_id", alarm.getId());
                    intent.putExtra("time", alarm.getTime());
                    intent.putExtra("label", alarm.getLabel());
                    intent.putExtra("toneUri", alarm.getToneUri());
                    intent.putExtra("triggerMillis", alarm.getTriggerMillis());
                    intent.putExtra("repeatDays", alarm.getRepeatDays());
                    intent.putExtra("days", alarm.getDays());
                    startActivity(intent);
                }
        );

        recyclerViewAlarms.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAlarms.setAdapter(alarmAdapter);

        alarmViewModel.getAllAlarms().observe(this, alarms -> {
            List<Alarm> uiAlarms = new ArrayList<>();
            for (AlarmEntity entity : alarms) {
                Alarm a = new Alarm(entity.id, entity.time, entity.days, entity.isEnabled);
                a.setToneUri(entity.toneUri);
                a.setTriggerMillis(entity.triggerMillis);
                a.setLabel(entity.label);
                a.setRepeatDays(entity.repeatDays);
                uiAlarms.add(a);
            }
            alarmAdapter.updateList(uiAlarms);
        });

        fabAddAlarm.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SetAlarmActivity.class);
            startActivity(intent);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_app_bar_menu, menu);
        MenuItem item = menu.findItem(R.id.action_toggle_theme);

        View actionView = item.getActionView();
        if (actionView != null) {
            TextView themeLabel = actionView.findViewById(R.id.themeLabel);
            SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            boolean isDark = prefs.getBoolean(KEY_IS_DARK_MODE, false);

            // Set initial text
            themeLabel.setText(isDark ? "Light Mode" : "Dark Mode");

            // Set click listener on TextView
            actionView.setOnClickListener(v -> {
                boolean newTheme = !isDark;

                // Save new preference
                getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                        .edit()
                        .putBoolean(KEY_IS_DARK_MODE, newTheme)
                        .apply();

                AppCompatDelegate.setDefaultNightMode(newTheme ?
                        AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);

                recreate();
            });
        }

        return true;
    }

}
