package com.surajvanshsv.alarmapp.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.SwitchCompat;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.surajvanshsv.alarmapp.R;
import com.surajvanshsv.alarmapp.model.Alarm;

import java.util.List;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder> {

    private List<Alarm> alarmList;
    private final OnAlarmToggleListener toggleListener;
    private final OnAlarmItemClickListener itemClickListener;

    // 🔸 Interface to toggle ON/OFF
    public interface OnAlarmToggleListener {
        void onToggle(Alarm alarm, boolean isEnabled);
    }

    // 🔸 Interface for item click (Edit)
    public interface OnAlarmItemClickListener {
        void onClick(Alarm alarm);
    }

    public AlarmAdapter(List<Alarm> alarmList, OnAlarmToggleListener toggleListener, OnAlarmItemClickListener itemClickListener) {
        this.alarmList = alarmList;
        this.toggleListener = toggleListener;
        this.itemClickListener = itemClickListener;
    }

    public void updateList(List<Alarm> newList) {
        this.alarmList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alarm_item, parent, false);
        return new AlarmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmViewHolder holder, int position) {
        Alarm alarm = alarmList.get(position);

        holder.tvAlarmTime.setText(alarm.getTime());
        holder.tvAlarmDays.setText(alarm.getRepeatDays());
        holder.tvAlarmLabel.setText(alarm.getLabel());

        // Prevent flickering
        holder.switchAlarm.setOnCheckedChangeListener(null);
        holder.switchAlarm.setChecked(alarm.isEnabled());

        holder.switchAlarm.setOnCheckedChangeListener((buttonView, isChecked) -> {
            toggleListener.onToggle(alarm, isChecked);
        });

        // 🟢 Edit alarm on item click (excluding switch)
        holder.itemView.setOnClickListener(v -> itemClickListener.onClick(alarm));
    }

    @Override
    public int getItemCount() {
        return alarmList != null ? alarmList.size() : 0;
    }

    static class AlarmViewHolder extends RecyclerView.ViewHolder {
        TextView tvAlarmTime, tvAlarmDays, tvAlarmLabel;
        SwitchCompat switchAlarm;

        public AlarmViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAlarmTime = itemView.findViewById(R.id.tvAlarmTime);
            tvAlarmDays = itemView.findViewById(R.id.tvAlarmDays);
            tvAlarmLabel = itemView.findViewById(R.id.tvAlarmLabel);
            switchAlarm = itemView.findViewById(R.id.switchAlarm);
        }
    }
}
