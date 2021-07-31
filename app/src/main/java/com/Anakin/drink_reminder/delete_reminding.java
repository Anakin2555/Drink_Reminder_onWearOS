package com.Anakin.drink_reminder;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.util.Calendar;

public class delete_reminding extends WearableActivity {
private Button cancel;
private Button delete;
Reminding_list reminding_list=Reminding_list.getInstance();

private homelistener homelistener=new homelistener();

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(homelistener,new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(homelistener);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_reminding);
        Activities.getInstance().addActivity(delete_reminding.this);
        cancel=findViewById(R.id.cancel);
        delete=findViewById(R.id.delete);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消闹钟

                Intent intent=getIntent();
                int position=intent.getIntExtra("position",0);
                Log.d("deleted position",position+"!!!");
                    Intent intent1 = new Intent(delete_reminding.this, AlarmReceiver.class);
                    intent1.setAction("RemindingTime");
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(delete_reminding.this, reminding_list.remindingArrayList.get(position).getId(), intent1, PendingIntent.FLAG_UPDATE_CURRENT);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    assert alarmManager != null;
                    alarmManager.cancel(pendingIntent);

                //取消sharedpreference数据
                SharedPreferences sharedPreferences=getSharedPreferences("remindings",MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.remove(String.valueOf(position));
                reminding_list.remove(reminding_list.remindingArrayList.get(position));
                //删除后补位
                for(int i=position;i<reminding_list.remindingArrayList.size();i++) {
                    editor.remove(String.valueOf(i+1));
                    reminding_list.remindingArrayList.get(i).setCode(i);
                    editor.putString(String.valueOf(reminding_list.remindingArrayList.get(i).getCode()),
                            reminding_list.remindingArrayList.get(i).getCode()+"_"+
                            reminding_list.remindingArrayList.get(i).getHour() + "_" +
                            reminding_list.remindingArrayList.get(i).getMinute() + "_" +
                            reminding_list.remindingArrayList.get(i).isEnable());
                    /*if(reminding_list.remindingArrayList.get(i).isEnable()) {
                        long time = reminding_list.remindingArrayList.get(i).getTimeInMills();
                        Intent intent2 = new Intent(delete_reminding.this, AlarmReceiver.class);
                        intent2.setAction("RemindingTime");
                        intent2.putExtra("position", i);
                        PendingIntent sender = PendingIntent.getBroadcast(delete_reminding.this, reminding_list.remindingArrayList.get(i).getId(), intent2, PendingIntent.FLAG_UPDATE_CURRENT);
                        AlarmManager alarmManager2 = (AlarmManager) getSystemService(ALARM_SERVICE);
                        if (time < System.currentTimeMillis()) {
                            assert alarmManager2 != null;
                            alarmManager2.setExact(AlarmManager.RTC_WAKEUP, time + AlarmManager.INTERVAL_DAY, sender);
                        } else {
                            assert alarmManager2 != null;
                            alarmManager2.setExact(AlarmManager.RTC_WAKEUP, time, sender);
                        }
                        Log.d("alarmset", i + "!!!");
                    }*/
                }

                editor.commit();

                finish();
            }
        });
        setAmbientEnabled();
    }
}