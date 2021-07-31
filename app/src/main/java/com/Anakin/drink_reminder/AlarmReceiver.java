package com.Anakin.drink_reminder;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver{
    Reminding_list reminding_list= Reminding_list.getInstance();
    Water water=Water.getInstance();


    public void setRepeatAlarm(Context context, int id){
        reminding_list.load_reminding(context);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction("RemindingTime");
        intent.putExtra("time",id);
        int hour=id/100;
        int minute=id-hour*100;
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE,minute);
        calendar.set(Calendar.SECOND,10);
        calendar.set(Calendar.MILLISECOND,0);
        long time=calendar.getTimeInMillis();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        if (alarmManager != null)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, time + AlarmManager.INTERVAL_DAY, pendingIntent);
    }
    public void sendNotify(Context context){
        Calendar calendar=Calendar.getInstance();
        int hour=calendar.get(Calendar.HOUR_OF_DAY);
        int minute=calendar.get(Calendar.MINUTE);
        int id_int=Integer.parseInt(String.valueOf(hour)+minute);
        long[] pattern={100,500,150,500};

        String contentText=null;
        if (minute>=10){
            if(water.getRemainWater().equals("0")){
                contentText=hour + ":" +minute+" - 目标已完成";
            }
            else
                contentText=hour+":"+minute+" - 剩余"+water.getRemainWater()+"ml";
        }
        else {
            if(water.getRemainWater().equals("0")){
                contentText=hour + ":" + "0"+minute+" - 目标已完成";
            }
            else
                contentText=hour+":"+"0"+minute+" - 剩余"+water.getRemainWater()+"ml";
        }


        Context applicationContext=context.getApplicationContext();

        Intent intent=new Intent(applicationContext, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent activity=PendingIntent.getActivity(applicationContext,id_int,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        String id="channel_1";
        String description="喝水提醒";
        int importance= NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel notificationChannel=new NotificationChannel(id,description,importance);

        notificationChannel.setVibrationPattern(pattern);
        NotificationManager notificationManager=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        assert notificationManager != null;
        notificationManager.createNotificationChannel(notificationChannel);
        Notification.Builder builder=new Notification.Builder(context,"channel_1");
        builder.setAutoCancel(true);
                builder.setContentTitle("该喝水啦"  )
                        .setContentText(contentText)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentIntent(activity)
                        .setWhen(System.currentTimeMillis())
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher));
                Notification notification = builder.build();
                notificationManager.notify(id_int, notification);

    }
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO: This method is called when the BroadcastReceiver is receiving
            // an Intent broadcast.
            /*if (*//*intent != null &&*//* intent.getAction().equals("RemindingTime")) {*/
                //若关闭闹钟则不响应
               int id=intent.getIntExtra("time",0);
            Log.d("time",id+"!!!");
                    setRepeatAlarm(context,id);

                    /*sendNotify(context);*/
                    intent= new Intent(context, AlarmActivity.class);
                    intent.putExtra("time",id);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

              /* else {
                   setRepeatAlarm(context,position,intent);
                    SharedPreferences sharedPreferences=context.getSharedPreferences("alarmnotreceived",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putInt("position",4);
                    editor.commit();
                }*/

            }

    }

