package com.surajvanshsv.alarmapp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.surajvanshsv.alarmapp.ui.view.AlarmRingActivity;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("AlarmReceiver", "Alarm triggered!");

        if (context == null || intent == null) {
            Log.e("AlarmReceiver", "Context or Intent is null");
            return;
        }

        // 🔊 Get tone URI and label
        String toneUri = intent.getStringExtra("toneUri");
        String label = intent.getStringExtra("label");

        if (toneUri == null) {
            Log.w("AlarmReceiver", "toneUri is null. Default tone will be used.");
        }

        if (label == null || label.trim().isEmpty()) {
            label = "Alarm is ringing";
        }

        // 📢 Launch AlarmRingActivity
        Intent ringIntent = new Intent(context, AlarmRingActivity.class);
        ringIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ringIntent.putExtra("toneUri", toneUri);
        ringIntent.putExtra("label", label);

        try {
            context.startActivity(ringIntent);
        } catch (Exception e) {
            Log.e("AlarmReceiver", "Failed to start AlarmRingActivity: " + e.getMessage());
        }
    }
}
