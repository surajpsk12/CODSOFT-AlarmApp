package com.surajvanshsv.alarmapp.ui.view;

import android.app.AlarmManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.chip.Chip;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.snackbar.Snackbar;
import com.surajvanshsv.alarmapp.R;
import com.surajvanshsv.alarmapp.data.db.AlarmEntity;
import com.surajvanshsv.alarmapp.handler.AlarmScheduler;
import com.surajvanshsv.alarmapp.ui.viewmodel.AlarmViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SetAlarmActivity extends AppCompatActivity {

    private TextView tvPickedTime, tvPickedTone;
    private EditText etAlarmLabel;
    private Uri pickedToneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
    private Calendar selectedTime = Calendar.getInstance();

    private final int RINGTONE_PICKER = 1;
    private AlarmViewModel alarmViewModel;

    private Chip chipMon, chipTue, chipWed, chipThu, chipFri, chipSat, chipSun;

    private static final String PREFS_NAME = "theme_prefs";
    private static final String KEY_IS_DARK_MODE = "is_dark";

    private boolean isEditMode = false;
    private int alarmId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isDark = prefs.getBoolean(KEY_IS_DARK_MODE, false);
        AppCompatDelegate.setDefaultNightMode(isDark ?
                AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm);

        tvPickedTime = findViewById(R.id.tvPickedTime);
        tvPickedTone = findViewById(R.id.tvPickedTone);
        etAlarmLabel = findViewById(R.id.etAlarmLabel);
        Button btnPickTime = findViewById(R.id.btnPickTime);
        Button btnPickTone = findViewById(R.id.btnPickTone);
        Button btnSaveAlarm = findViewById(R.id.btnSaveAlarm);
        Button btnDeleteAlarm = findViewById(R.id.btnDeleteAlarm);

        chipMon = findViewById(R.id.chipMon);
        chipTue = findViewById(R.id.chipTue);
        chipWed = findViewById(R.id.chipWed);
        chipThu = findViewById(R.id.chipThu);
        chipFri = findViewById(R.id.chipFri);
        chipSat = findViewById(R.id.chipSat);
        chipSun = findViewById(R.id.chipSun);

        alarmViewModel = new ViewModelProvider(this).get(AlarmViewModel.class);

        Intent intent = getIntent();
        isEditMode = intent.getBooleanExtra("edit_mode", false);

        if (isEditMode) {
            alarmId = intent.getIntExtra("alarm_id", -1);
            String time = intent.getStringExtra("time");
            String label = intent.getStringExtra("label");
            String tone = intent.getStringExtra("toneUri");
            long triggerMillis = intent.getLongExtra("triggerMillis", System.currentTimeMillis());
            String repeatDays = intent.getStringExtra("repeatDays");

            etAlarmLabel.setText(label);
            tvPickedTime.setText(time);
            tvPickedTone.setText(tone);
            pickedToneUri = Uri.parse(tone);
            selectedTime.setTimeInMillis(triggerMillis);
            setRepeatChips(repeatDays);

            btnDeleteAlarm.setVisibility(View.VISIBLE);
        } else {
            btnDeleteAlarm.setVisibility(View.GONE);
        }

        btnPickTime.setOnClickListener(v -> {
            int hour = selectedTime.get(Calendar.HOUR_OF_DAY);
            int minute = selectedTime.get(Calendar.MINUTE);
            new TimePickerDialog(this, (view, hourOfDay, minute1) -> {
                selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                selectedTime.set(Calendar.MINUTE, minute1);
                selectedTime.set(Calendar.SECOND, 0);
                selectedTime.set(Calendar.MILLISECOND, 0);

                // ✅ Display in 12-hour format with AM/PM
                String formatted = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(selectedTime.getTime());
                tvPickedTime.setText(formatted);
            }, hour, minute, false).show(); // false = 12-hour format
        });

        btnPickTone.setOnClickListener(v -> {
            Intent ringtoneIntent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
            ringtoneIntent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
            ringtoneIntent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Alarm Tone");
            ringtoneIntent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, pickedToneUri);
            startActivityForResult(ringtoneIntent, RINGTONE_PICKER);
        });

        btnSaveAlarm.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                if (alarmManager != null && !alarmManager.canScheduleExactAlarms()) {
                    Intent intent1 = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                    startActivity(intent1);
                    return;
                }
            }

            long triggerTime = selectedTime.getTimeInMillis();
            String time = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(selectedTime.getTime());

            String label = etAlarmLabel.getText().toString().trim();
            if (label.isEmpty()) label = "Alarm";

            String repeatDays = getRepeatDaysFromChips();

            AlarmEntity alarm = new AlarmEntity(
                    time,
                    repeatDays,
                    true,
                    pickedToneUri.toString(),
                    triggerTime,
                    label,
                    repeatDays
            );

            if (isEditMode && alarmId != -1) {
                alarm.id = alarmId;
                alarmViewModel.update(alarm);
                AlarmScheduler.cancelAlarm(this, alarmId);
                AlarmScheduler.scheduleAlarm(this, alarm);
                Snackbar.make(btnSaveAlarm, "Alarm updated!", Snackbar.LENGTH_SHORT).show();
            } else {
                alarmViewModel.insert(alarm);
                AlarmScheduler.scheduleAlarm(this, alarm);
                Snackbar.make(btnSaveAlarm, "Alarm saved & scheduled!", Snackbar.LENGTH_SHORT).show();
            }

            finish();
        });

        btnDeleteAlarm.setOnClickListener(v -> {
            if (alarmId != -1) {
                alarmViewModel.deleteById(alarmId);
                AlarmScheduler.cancelAlarm(this, alarmId);
                Snackbar.make(btnDeleteAlarm, "Alarm deleted!", Snackbar.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void setRepeatChips(String repeatDays) {
        if (repeatDays == null || repeatDays.length() < 7) return;
        chipMon.setChecked(repeatDays.charAt(0) == 'M');
        chipTue.setChecked(repeatDays.charAt(1) == 'T');
        chipWed.setChecked(repeatDays.charAt(2) == 'W');
        chipThu.setChecked(repeatDays.charAt(3) == 'T');
        chipFri.setChecked(repeatDays.charAt(4) == 'F');
        chipSat.setChecked(repeatDays.charAt(5) == 'S');
        chipSun.setChecked(repeatDays.charAt(6) == 'S');
    }

    private String getRepeatDaysFromChips() {
        return (chipMon.isChecked() ? "M" : "-") +
                (chipTue.isChecked() ? "T" : "-") +
                (chipWed.isChecked() ? "W" : "-") +
                (chipThu.isChecked() ? "T" : "-") +
                (chipFri.isChecked() ? "F" : "-") +
                (chipSat.isChecked() ? "S" : "-") +
                (chipSun.isChecked() ? "S" : "-");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RINGTONE_PICKER && resultCode == RESULT_OK && data != null) {
            pickedToneUri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            if (pickedToneUri != null) {
                tvPickedTone.setText(pickedToneUri.toString());
            } else {
                tvPickedTone.setText("Default");
                pickedToneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            }
        }
    }
}
