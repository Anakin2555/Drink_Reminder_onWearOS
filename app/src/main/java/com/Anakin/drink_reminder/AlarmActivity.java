package com.Anakin.drink_reminder;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.wearable.activity.WearableActivity;
import android.text.Html;
import android.util.AtomicFile;
import android.util.Log;
import android.view.KeyEvent;
import android.view.KeyboardShortcutGroup;
import android.view.View;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.Anakin.drink_reminder.view.Circle_small;
import com.heytap.wearable.support.widget.HeyMainTitleBar;

import java.lang.reflect.Field;
import java.sql.Time;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AlarmActivity extends Activity {
    private Vibrator vibrator;
    Water water=Water.getInstance();

    /*private Thread newThread; //声明一个子线程*/
    private Calendar calendarOfDate=Calendar.getInstance();



    @Override
    //字体全局设置
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("onstart","111");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("onresume","!!!");




    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.d("onstoped","!!!");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("onPaused","!!!");



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("destroyed","!!!");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        int time = intent.getIntExtra("time", 0);
        final int hour = time / 100;
        final int minute = time - hour * 100;
        int Month = calendarOfDate.get(Calendar.MONTH) + 1;
        int Day = calendarOfDate.get(Calendar.DATE);
        int Year = calendarOfDate.get(Calendar.YEAR);
        String current_date;
        if (Month < 10) {
            if (Day < 10) {
                current_date = Year + "0" + Month + "0" + Day;
            } else
                current_date = Year + "0" + Month + Day;
        } else {
            if (Day < 10) {
                current_date = Year + String.valueOf(Month) + "0" + Day;
            } else
                current_date = Year + String.valueOf(Month) + String.valueOf(Day);
        }

        Log.d("date", current_date);
        water.load_Water(AlarmActivity.this, current_date);
        super.onCreate(savedInstanceState);

        Activities.getInstance().addActivity(AlarmActivity.this);
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        assert pm != null;
        final PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK,"drinkreminder:mywakelocktag");
        wakeLock.acquire(20000);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            assert vibrator != null;
            vibrator.vibrate(VibrationEffect.createWaveform(new long[]{100, 640, 150, 640,150}, -1));
        setContentView(R.layout.activity_alarm);




        TextView textView_1 = findViewById(R.id.tv_0);
        TextView textView_2 = findViewById(R.id.tv_2);
        Button button_open = findViewById(R.id.button_remindtodrink);
        Button button_cancel = findViewById(R.id.button_cancel);
        Circle_small circle_small = findViewById(R.id.circle_small);
        Log.d("angle", circle_small.getAngle() + "    " + water.getHaveDrunk());
        circle_small.setAngle(270 * (Float.parseFloat(water.getHaveDrunk()) / Float.parseFloat(water.getTotalWater())));
        circle_small.invalidate();
        int Hour = calendarOfDate.get(Calendar.HOUR_OF_DAY);
        int Minute = calendarOfDate.get(Calendar.MINUTE);
        if (Hour < 10) {
            if (Minute < 10) {
                textView_1.setText("0" + Hour + ":" + "0" + Minute);
            } else {
                textView_1.setText("0" + Hour + ":" + Minute);
            }

        } else {
            if (Minute < 10) {
                textView_1.setText(Hour + ":" + "0" + Minute);
            } else {
                textView_1.setText(Hour + ":" + Minute);
            }
        }
        if (water.getRemainWater().equals("0")) {
            textView_2.setText("今日喝水量已达标");
        } else
            textView_2.setText("剩余" + water.getRemainWater() + "ml" + "水量");



        final Handler mHandler = new Handler();
        final Runnable r = new Runnable() {
            @Override
            public void run() {
                vibrator.cancel();
                /*if(water.getRemainWater().equals("0"))
                { }
                else
                    sendNotify(hour,minute);*/
                wakeLock.release();
                finish();
            }
        };

        mHandler.postDelayed(r, 19000);//延时

        /*newThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(30000);

                } catch (InterruptedException e) {
                    e.printStackTrace();

                }
            }
        });
        newThread.start(); //启动线程*/


        button_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHandler.removeCallbacks(r);
                vibrator.cancel();
                wakeLock.release();
                Intent intent=new Intent(AlarmActivity.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);
                finish();
            }
        });
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*newThread.interrupt();*/
                mHandler.removeCallbacks(r);
                vibrator.cancel();
                wakeLock.release();
                finish();
                /*overridePendingTransition(0,R.anim.close_activity);*/

            }
        });



        /*Handler handler=new Handler();
        Runnable runnable=new Runnable() {
            @Override
            public void run() {

            }
        };
        handler.postDelayed(runnable,15000);
        handler.*/




    }
    /*public void sendNotify(int hour,int minute){
        int id_int=Integer.parseInt(String.valueOf(hour)+minute);
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


        Context applicationContext=getApplicationContext();

        Intent intent=new Intent(applicationContext, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent activity=PendingIntent.getActivity(applicationContext,id_int,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        String id="channel_1";
        String description="喝水提醒";
        int importance= NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel notificationChannel=new NotificationChannel(id,description,importance);
        NotificationManager notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        assert notificationManager != null;
        notificationManager.createNotificationChannel(notificationChannel);
        Notification.Builder builder=new Notification.Builder(AlarmActivity.this,"channel_1");
        builder.setAutoCancel(true);
        builder.setContentTitle("该喝水啦"  )
                .setContentText(contentText)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(activity)
                .setWhen(System.currentTimeMillis())
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
        Notification notification = builder.build();
        notificationManager.notify(id_int, notification);

    }*/
}