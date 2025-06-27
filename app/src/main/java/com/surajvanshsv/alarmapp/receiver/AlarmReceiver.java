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

        if (intent == null || context == null) {
            Log.e("AlarmReceiver", "Intent or context is null");
            return;
        }

        String toneUri = intent.getStringExtra("toneUri");
        if (toneUri == null) {
            Log.w("AlarmReceiver", "toneUri is null. Default alarm tone will be used.");
        }

        Intent ringIntent = new Intent(context, AlarmRingActivity.class);
        ringIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ringIntent.putExtra("toneUri", toneUri);

        try {
            context.startActivity(ringIntent);
        } catch (Exception e) {
            Log.e("AlarmReceiver", "Failed to start AlarmRingActivity: " + e.getMessage());
        }
    }
}
