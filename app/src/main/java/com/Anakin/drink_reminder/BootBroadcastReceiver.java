package com.Anakin.drink_reminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class BootBroadcastReceiver extends BroadcastReceiver {

    Reminding_list reminding_list = Reminding_list.getInstance();
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.





            reminding_list.load_reminding(context);
            SharedPreferences sharedPreferences_size = context.getSharedPreferences("size", Context.MODE_PRIVATE);

            int size = sharedPreferences_size.getInt("number", 0);
            SharedPreferences sharedPreferences=context.getSharedPreferences("bootcompleted",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();




            for (int i = 0; i < size; i++) {
                if (reminding_list.remindingArrayList.get(i).isEnable()) {
                    long time=reminding_list.remindingArrayList.get(i).getTimeInMills();

                    Intent intent1 = new Intent(context, AlarmReceiver.class);
                    intent1.setAction("RemindingTime");
                    intent1.putExtra("time", reminding_list.remindingArrayList.get(i).getId());
                    PendingIntent sender = PendingIntent.getBroadcast(context, reminding_list.remindingArrayList.get(i).getId(), intent1, PendingIntent.FLAG_UPDATE_CURRENT);
                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    if (time<=System.currentTimeMillis()) {
                        assert alarmManager != null;
                        editor.putBoolean("otherday"+i,true);

                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, time +AlarmManager.INTERVAL_DAY, sender);
                    } else {
                        assert alarmManager != null;
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, sender);
                        editor.putBoolean("today"+i,true);


                    }


                }
                editor.commit();

        }throw new UnsupportedOperationException("Not yet implemented");
    }
}

