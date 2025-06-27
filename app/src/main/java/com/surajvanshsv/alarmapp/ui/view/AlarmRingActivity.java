package com.surajvanshsv.alarmapp.ui.view;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.surajvanshsv.alarmapp.R;

public class AlarmRingActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private Uri alarmUri;
    private PowerManager.WakeLock wakeLock;

    private static final String PREFS_NAME = "theme_prefs";
    private static final String KEY_IS_DARK_MODE = "is_dark";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 🔁 Apply theme from preferences
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isDark = prefs.getBoolean(KEY_IS_DARK_MODE, false);
        AppCompatDelegate.setDefaultNightMode(isDark ?
                AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_ring);

        // 🧱 Show over lock screen (API 27+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true);
            setTurnScreenOn(true);
        }

        // ✅ Backward compatibility
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        // 🔓 Dismiss keyguard
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        if (keyguardManager != null) {
            keyguardManager.requestDismissKeyguard(this, null);
        }

        // 🔋 Acquire wakelock (30 seconds)
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        if (pm != null) {
            wakeLock = pm.newWakeLock(
                    PowerManager.SCREEN_DIM_WAKE_LOCK |
                            PowerManager.ACQUIRE_CAUSES_WAKEUP |
                            PowerManager.ON_AFTER_RELEASE,
                    "alarmapp:wakelock"
            );
            wakeLock.acquire(30_000);
        }

        // 🔊 Get tone URI
        String uriString = getIntent().getStringExtra("toneUri");
        alarmUri = uriString != null ? Uri.parse(uriString) :
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        playAlarmTone(alarmUri);

        // 🎚 Button setup
        Button btnSnooze = findViewById(R.id.btnSnooze);
        Button btnDismiss = findViewById(R.id.btnDismiss);

        btnDismiss.setOnClickListener(v -> {
            stopAlarm();
            finish();
        });

        btnSnooze.setOnClickListener(v -> {
            // Optional: schedule snooze logic (e.g., +10 mins)
            stopAlarm();
            finish();
        });
    }

    private void playAlarmTone(Uri uri) {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(this, uri);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
            mediaPlayer.setLooping(true);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
            mediaPlayer = MediaPlayer.create(this,
                    RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
            if (mediaPlayer != null) {
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
            }
        }
    }

    private void stopAlarm() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        if (wakeLock != null && wakeLock.isHeld()) {
            wakeLock.release();
        }
    }

    @Override
    protected void onDestroy() {
        stopAlarm();
        super.onDestroy();
    }
}
