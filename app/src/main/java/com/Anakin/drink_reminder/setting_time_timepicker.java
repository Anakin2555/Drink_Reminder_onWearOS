package com.Anakin.drink_reminder;


import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.input.RotaryEncoder;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.heytap.wearable.support.widget.HeyBackTitleBar;
import com.heytap.wearable.support.widget.HeyTimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class setting_time_timepicker extends WearableActivity {
    private HeyBackTitleBar heyBackTitleBar;
    private HeyTimePicker heyTimePicker;
    private Button button;
    private Button button_again;
    private int insertPosition;

    private reminding reminding = new reminding();
    Reminding_list reminding_list = Reminding_list.getInstance();
    private String array[] = {"12", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11"};
    private homelistener homelistener = new homelistener();

    /*private String ALARM_EVENT = "com.Anakin.drink_reminder.AlarmReceiver";
    // 声明一个闹钟的广播接收器
    private AlarmReceiver alarmReceiver;*/

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(homelistener, new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(homelistener);
    }

    @Override
    public void onStart() {
        super.onStart();
        /*// 从Android9.0开始，系统不再支持静态广播，应用广播只能通过动态注册
        // 创建一个闹钟的广播接收器
        alarmReceiver = new AlarmReceiver();
        // 创建一个意图过滤器，只处理指定事件来源的广播
        IntentFilter filter = new IntentFilter(ALARM_EVENT);
        // 注册广播接收器，注册之后才能正常接收广播
        registerReceiver(alarmReceiver, filter);*/


    }
    @Override
    //字体全局设置
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    public void onStop() {
        super.onStop();
        // 注销广播接收器，注销之后就不再接收广播
        /* unregisterReceiver(alarmReceiver);*/

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_time_timepicker);
        Activities.getInstance().addActivity(setting_time_timepicker.this);


        heyBackTitleBar = findViewById(R.id.heybacktitle_6);
        heyBackTitleBar.getTitleTextView().setTextSize(16);
        heyBackTitleBar.getTextClock().setTextSize(17);
        heyBackTitleBar.getTextClock().setFormat12Hour("HH:mm");
        heyBackTitleBar.setBackListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                /*overridePendingTransition(0, R.anim.close_activity);*/
            }
        }), setting_time_timepicker.this);
        heyTimePicker = findViewById(R.id.time_pickers);
        Calendar currentTime = Calendar.getInstance();


        heyTimePicker.b.setFocusable(true);
        heyTimePicker.b.setFocusableInTouchMode(true);
        heyTimePicker.c.setFocusableInTouchMode(true);
        heyTimePicker.c.setFocusable(true);
        heyTimePicker.b.requestFocus();
        heyTimePicker.b.setContentTextTypeface(Typeface.createFromAsset(getAssets(),"font/ds.ttf"));
        heyTimePicker.c.setContentTextTypeface(Typeface.createFromAsset(getAssets(),"font/ds.ttf"));
        heyTimePicker.b.da=new SoundPool(1, AudioManager.STREAM_MUSIC,3);
        heyTimePicker.c.da=new SoundPool(1, AudioManager.STREAM_MUSIC,3);

        heyTimePicker.b.setOnGenericMotionListener(new View.OnGenericMotionListener() {
            @Override
            public boolean onGenericMotion(View v, MotionEvent ev) {
                if (ev.getAction() == MotionEvent.ACTION_SCROLL && RotaryEncoder.isFromRotaryEncoder(ev)) {
                    // Don't forget the negation here
                    Log.d("b_scroll", "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    // Swap these axes if you want to do horizontal scrolling instead
                    List<MotionEvent> motionEventList = new ArrayList<>();
                    long downTime = SystemClock.uptimeMillis();
                    long eventTime = downTime;
                    float px = v.getX() / 2-80;
                    float py = v.getY() / 2;
                    MotionEvent motionEvent_down = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, px, py, 0);
                    v.onTouchEvent(motionEvent_down);
                    //向下滑
                    if (ev.getAxisValue(MotionEvent.AXIS_SCROLL) < 0) {
                        Log.d("scroll down", "0");
                        for (int i = 0; i < 50; i++) {
                            eventTime += 10;
                            py -= 1.2;
                            MotionEvent motionEvent_move = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_MOVE, px, py, 0);
                            motionEventList.add(motionEvent_move);
                            v.onTouchEvent(motionEvent_move);

                        }
                    }
                    //向上滑
                    else {
                        Log.d("scroll up", "1");
                        for (int i = 0; i < 50; i++) {
                            eventTime += 10;
                            py += 1.2;
                            MotionEvent motionEvent_move = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_MOVE, px, py, 0);
                            motionEventList.add(motionEvent_move);
                            v.onTouchEvent(motionEvent_move);

                        }
                    }
                    MotionEvent motionEvent_up = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_UP, px, py, 0);
                    v.onTouchEvent(motionEvent_up);
                    //回收
                    motionEvent_down.recycle();
                    for (int i = 0; i < motionEventList.size(); i++) {
                        motionEventList.get(i).recycle();
                    }
                    motionEvent_up.recycle();


                    Log.d("motionevent", String.valueOf(ev.getAction()));



                    return true;
                }
                return false;
            }
        });
        heyTimePicker.b.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                heyTimePicker.b.setFocusableInTouchMode(true);
                heyTimePicker.b.requestFocus();
                heyTimePicker.b.setOnGenericMotionListener(new View.OnGenericMotionListener() {
                    @Override
                    public boolean onGenericMotion(View v, MotionEvent ev) {
                        if (ev.getAction() == MotionEvent.ACTION_SCROLL && RotaryEncoder.isFromRotaryEncoder(ev)) {
                            // Don't forget the negation here
                            Log.d("b_scroll", "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                            // Swap these axes if you want to do horizontal scrolling instead
                            List<MotionEvent> motionEventList = new ArrayList<>();
                            long downTime = SystemClock.uptimeMillis();
                            long eventTime = downTime;
                            float px = v.getX() / 2-80;
                            float py = v.getY() / 2;
                            MotionEvent motionEvent_down = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, px, py, 0);
                            v.onTouchEvent(motionEvent_down);
                            //向下滑
                            if (ev.getAxisValue(MotionEvent.AXIS_SCROLL) < 0) {
                                Log.d("scroll down", "0");
                                for (int i = 0; i < 50; i++) {
                                    eventTime += 10;
                                    py -= 1.2;
                                    MotionEvent motionEvent_move = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_MOVE, px, py, 0);
                                    motionEventList.add(motionEvent_move);
                                    v.onTouchEvent(motionEvent_move);

                                }
                            }
                            //向上滑
                            else {
                                Log.d("scroll up", "1");
                                for (int i = 0; i < 50; i++) {
                                    eventTime += 10;
                                    py += 1.2;
                                    MotionEvent motionEvent_move = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_MOVE, px, py, 0);
                                    motionEventList.add(motionEvent_move);
                                    v.onTouchEvent(motionEvent_move);

                                }
                            }
                            MotionEvent motionEvent_up = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_UP, px, py, 0);
                            v.onTouchEvent(motionEvent_up);
                            //回收
                            motionEvent_down.recycle();
                            for (int i = 0; i < motionEventList.size(); i++) {
                                motionEventList.get(i).recycle();
                            }
                            motionEvent_up.recycle();


                            Log.d("motionevent", String.valueOf(ev.getAction()));



                            return true;
                        }
                        return false;
                    }
                });

                return false;
            }
        });
        heyTimePicker.c.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                heyTimePicker.c.requestFocus();
                heyTimePicker.c.setOnGenericMotionListener(new View.OnGenericMotionListener() {
                    @Override
                    public boolean onGenericMotion(View v, MotionEvent ev) {
                        if (ev.getAction() == MotionEvent.ACTION_SCROLL && RotaryEncoder.isFromRotaryEncoder(ev)) {
                            // Don't forget the negation here
                            Log.d("C_scroll", "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                            // Swap these axes if you want to do horizontal scrolling instead
                            List<MotionEvent> motionEventList = new ArrayList<>();
                            long downTime = SystemClock.uptimeMillis();
                            long eventTime = downTime;
                            float px = v.getX() / 2 + 100;
                            float py = v.getY() / 2;
                            MotionEvent motionEvent_down = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, px, py, 0);
                            v.onTouchEvent(motionEvent_down);
                            //向下滑
                            if (ev.getAxisValue(MotionEvent.AXIS_SCROLL) < 0) {
                                Log.d("scroll down", "0");
                                for (int i = 0; i < 50; i++) {
                                    eventTime += 10;
                                    py -= 1.2;
                                    MotionEvent motionEvent_move = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_MOVE, px, py, 0);
                                    motionEventList.add(motionEvent_move);
                                    v.onTouchEvent(motionEvent_move);

                                }
                            }
                            //向上滑
                            else {
                                Log.d("scroll up", "1");
                                for (int i = 0; i < 50; i++) {
                                    eventTime += 10;
                                    py += 1.2;
                                    MotionEvent motionEvent_move = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_MOVE, px, py, 0);
                                    motionEventList.add(motionEvent_move);
                                    v.onTouchEvent(motionEvent_move);

                                }
                            }
                            MotionEvent motionEvent_up = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_UP, px, py, 0);
                            v.onTouchEvent(motionEvent_up);
                            //回收
                            motionEvent_down.recycle();
                            for (int i = 0; i < motionEventList.size(); i++) {
                                motionEventList.get(i).recycle();
                            }
                            motionEvent_up.recycle();


                            Log.d("motionevent", String.valueOf(ev.getAction()));



                            return true;
                        }
                        return false;
                    }
                });
                return false;
            }
        });






        //强制转换为24小时制
        heyTimePicker.a.setVisibility(View.INVISIBLE);
        heyTimePicker.b.refreshByNewDisplayedValues(this.getResources().getStringArray(R.array.hour24));

            final Intent intent = getIntent();
            final int ifNew = intent.getIntExtra("ifNew", 1);
            final int position = intent.getIntExtra("position", 0);


            if(ifNew==1)
            heyTimePicker.setInitValue(currentTime.get(Calendar.HOUR_OF_DAY), 0);
            else
                heyTimePicker.setInitValue(reminding_list.remindingArrayList.get(position).getHour(),
                        reminding_list.remindingArrayList.get(position).getMinute());
            button_again=findViewById(R.id.setting_reminding_time_again);
            if(ifNew==0)
                button_again.setText("返回");
            button_again.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(ifNew==0){
                        finish();
                    }else {
                        boolean ifRepeat = false;
                        for (int i = 0; i < reminding_list.remindingArrayList.size(); i++) {
                            if (heyTimePicker.getCurrentHour() == reminding_list.remindingArrayList.get(i).getHour()
                                    && heyTimePicker.getCurrentMinute() == reminding_list.remindingArrayList.get(i).getMinute()) {
                                ifRepeat = true;
                                Toast.makeText(setting_time_timepicker.this, "该提醒时间已存在", Toast.LENGTH_SHORT).show();
                            }
                        }


                        //未重复
                        if (!ifRepeat) {

                            //保存提醒数据
                            SharedPreferences sharedPreferences = getSharedPreferences("remindings", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            if (ifNew == 0) {
                                Intent intent1 = new Intent(setting_time_timepicker.this, AlarmReceiver.class);
                                intent1.setAction("RemindingTime");
                                PendingIntent pendingIntent = PendingIntent.getBroadcast(setting_time_timepicker.this, reminding_list.remindingArrayList.get(position).getId(), intent1, PendingIntent.FLAG_UPDATE_CURRENT);
                                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                                assert alarmManager != null;
                                alarmManager.cancel(pendingIntent);
                                Log.d(reminding_list.remindingArrayList.get(position).getId() + ":" + position, "!!!");
                                editor.remove(String.valueOf(position));
                                reminding_list.remindingArrayList.remove(position);
                                for (int i = position; i < reminding_list.remindingArrayList.size(); i++) {
                                    editor.remove(String.valueOf(i + 1));
                                    reminding_list.remindingArrayList.get(i).setCode(i);
                                    editor.putString(String.valueOf(reminding_list.remindingArrayList.get(i).getCode()),
                                            reminding_list.remindingArrayList.get(i).getCode() + "_" +
                                                    reminding_list.remindingArrayList.get(i).getHour() + "_" +
                                                    reminding_list.remindingArrayList.get(i).getMinute() + "_" +
                                                    reminding_list.remindingArrayList.get(i).isEnable());

                                }


                            }
                            reminding.setHour(heyTimePicker.getCurrentHour());
                            reminding.setMinute(heyTimePicker.getCurrentMinute());
                            reminding.setCode(reminding_list.remindingArrayList.size());
                            reminding.setEnable(true);


                            if (reminding_list.remindingArrayList.size() == 0) {
                                reminding_list.add(reminding);
                                insertPosition = 0;
                            } else if (reminding.getTimeInMills() > reminding_list.remindingArrayList.get(reminding_list.remindingArrayList.size() - 1).getTimeInMills()) {
                                reminding_list.remindingArrayList.add(reminding);
                                insertPosition = reminding_list.remindingArrayList.size() - 1;
                            } else if (reminding.getTimeInMills() < reminding_list.remindingArrayList.get(0).getTimeInMills()) {
                                reminding_list.remindingArrayList.add(0, reminding);
                                insertPosition = 0;
                            } else {
                                for (int i = 0; i < reminding_list.remindingArrayList.size(); i++) {
                                    if (reminding.getTimeInMills() > reminding_list.remindingArrayList.get(i).getTimeInMills() &&
                                            reminding.getTimeInMills() < reminding_list.remindingArrayList.get(i + 1).getTimeInMills()) {
                                        reminding_list.remindingArrayList.add(i + 1, reminding);
                                        insertPosition = 1 + i;
                                        break;
                                    }

                                }
                            }
                            for (int i = 0; i < reminding_list.remindingArrayList.size(); i++) {
                                reminding_list.remindingArrayList.get(i).setCode(i);
                            }
                            for (int j = 0; j < reminding_list.remindingArrayList.size() - 1; j++) {
                                Intent intent1 = new Intent(setting_time_timepicker.this, AlarmReceiver.class);
                                intent1.setAction("RemindingTime");
                                PendingIntent pendingIntent = PendingIntent.getBroadcast(setting_time_timepicker.this, reminding_list.remindingArrayList.get(j).getId(), intent1, PendingIntent.FLAG_UPDATE_CURRENT);
                                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                                assert alarmManager != null;
                                alarmManager.cancel(pendingIntent);
                            }

                            for (int i = 0; i < reminding_list.remindingArrayList.size(); i++) {
                                editor.putString(String.valueOf(reminding_list.remindingArrayList.get(i).getCode()),
                                        reminding_list.remindingArrayList.get(i).getCode() + "_" +
                                                reminding_list.remindingArrayList.get(i).getHour() + "_" +
                                                reminding_list.remindingArrayList.get(i).getMinute() + "_" +
                                                reminding_list.remindingArrayList.get(i).isEnable());

                                if (reminding_list.remindingArrayList.get(i).isEnable()) {
                                    long time = reminding_list.remindingArrayList.get(i).getTimeInMills();
                                    Intent intent = new Intent(setting_time_timepicker.this, AlarmReceiver.class);
                                    intent.setAction("RemindingTime");
                                    intent.putExtra("time", reminding_list.remindingArrayList.get(i).getId());
                                    PendingIntent sender = PendingIntent.getBroadcast(setting_time_timepicker.this, reminding_list.remindingArrayList.get(i).getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                                    if (time < System.currentTimeMillis()) {
                                        assert alarmManager != null;
                                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, time + AlarmManager.INTERVAL_DAY, sender);
                                    } else {
                                        assert alarmManager != null;
                                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, sender);
                                    }
                                }

                            }
                        /*long time = reminding_list.remindingArrayList.get(insertPosition).getTimeInMills();
                        Intent intent = new Intent(setting_time_timepicker.this, AlarmReceiver.class);
                        intent.setAction("RemindingTime");
                        intent.putExtra("position", insertPosition);
                        PendingIntent sender = PendingIntent.getBroadcast(setting_time_timepicker.this, reminding_list.remindingArrayList.get(insertPosition).getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        AlarmManager alarmManager2 = (AlarmManager) getSystemService(ALARM_SERVICE);
                        if (time <= System.currentTimeMillis()) {
                            assert alarmManager2 != null;
                            alarmManager2.setExact(AlarmManager.RTC_WAKEUP, time + AlarmManager.INTERVAL_DAY, sender);
                            Log.d("tomorrow",insertPosition+"!!!");
                        } else {
                            assert alarmManager2 != null;
                            alarmManager2.setExact(AlarmManager.RTC_WAKEUP, time, sender);
                            Log.d(reminding_list.remindingArrayList.get(insertPosition).getId()+":"+insertPosition,"time："+time+"!!!!");
                        }*/


                            editor.commit();


                            finish();
                            Intent intent1 = new Intent(setting_time_timepicker.this, setting_time_timepicker.class);
                            startActivity(intent1);
                            /*overridePendingTransition(0, R.anim.close_activity);*/
                        }
                    }
                }
            });




            button = findViewById(R.id.setting_reminding_time_confirm);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean ifRepeat=false;
                    for(int i=0;i<reminding_list.remindingArrayList.size();i++){
                        if(heyTimePicker.getCurrentHour()==reminding_list.remindingArrayList.get(i).getHour()
                            &&heyTimePicker.getCurrentMinute()==reminding_list.remindingArrayList.get(i).getMinute()){
                            ifRepeat=true;
                            Toast.makeText(setting_time_timepicker.this,"该提醒时间已存在",Toast.LENGTH_SHORT).show();
                        }
                    }







                    //未重复
                    if(!ifRepeat) {
                        reminding_list.load_reminding(setting_time_timepicker.this);

                        //保存提醒数据
                        SharedPreferences sharedPreferences = getSharedPreferences("remindings", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        if (ifNew == 0) {

                            Intent intent1 = new Intent(setting_time_timepicker.this, AlarmReceiver.class);
                            intent1.setAction("RemindingTime");
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(setting_time_timepicker.this, reminding_list.remindingArrayList.get(position).getId(), intent1, PendingIntent.FLAG_UPDATE_CURRENT);
                            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                            assert alarmManager != null;
                            alarmManager.cancel(pendingIntent);
                            Log.d(reminding_list.remindingArrayList.get(position).getId()+":"+position,"!!!");
                            editor.remove(String.valueOf(position));
                            reminding_list.remindingArrayList.remove(position);
                            for(int i=position;i<reminding_list.remindingArrayList.size();i++) {
                                editor.remove(String.valueOf(i + 1));
                                reminding_list.remindingArrayList.get(i).setCode(i);
                                editor.putString(String.valueOf(reminding_list.remindingArrayList.get(i).getCode()),
                                        reminding_list.remindingArrayList.get(i).getCode() + "_" +
                                                reminding_list.remindingArrayList.get(i).getHour() + "_" +
                                                reminding_list.remindingArrayList.get(i).getMinute() + "_" +
                                                reminding_list.remindingArrayList.get(i).isEnable());

                            }


                        }
                        reminding.setHour(heyTimePicker.getCurrentHour());
                        reminding.setMinute(heyTimePicker.getCurrentMinute());
                        reminding.setCode(reminding_list.remindingArrayList.size());
                        reminding.setEnable(true);


                        if(reminding_list.remindingArrayList.size()==0){
                            reminding_list.add(reminding);
                            insertPosition=0;
                        }
                        else if(reminding.getTimeInMills()>reminding_list.remindingArrayList.get(reminding_list.remindingArrayList.size()-1).getTimeInMills()){
                            reminding_list.remindingArrayList.add(reminding);
                            insertPosition=reminding_list.remindingArrayList.size()-1;
                        }
                        else if(reminding.getTimeInMills()<reminding_list.remindingArrayList.get(0).getTimeInMills()){
                            reminding_list.remindingArrayList.add(0,reminding);
                            insertPosition=0;
                        }else {
                            for (int i = 0; i < reminding_list.remindingArrayList.size(); i++) {
                                if (reminding.getTimeInMills() > reminding_list.remindingArrayList.get(i).getTimeInMills() &&
                                        reminding.getTimeInMills() < reminding_list.remindingArrayList.get(i + 1).getTimeInMills()) {
                                    reminding_list.remindingArrayList.add(i + 1, reminding);
                                    insertPosition=1+i;
                                    break;
                                }

                            }
                        }
                        for (int i=0;i<reminding_list.remindingArrayList.size();i++){
                            reminding_list.remindingArrayList.get(i).setCode(i);
                        }
                        for(int j=0;j<reminding_list.remindingArrayList.size()-1;j++) {
                            Intent intent1 = new Intent(setting_time_timepicker.this, AlarmReceiver.class);
                            intent1.setAction("RemindingTime");
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(setting_time_timepicker.this, reminding_list.remindingArrayList.get(j).getId(), intent1, PendingIntent.FLAG_UPDATE_CURRENT);
                            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                            assert alarmManager != null;
                            alarmManager.cancel(pendingIntent);
                        }

                        for(int i=0;i<reminding_list.remindingArrayList.size();i++){

                            editor.putString(String.valueOf(reminding_list.remindingArrayList.get(i).getCode()),
                                    reminding_list.remindingArrayList.get(i).getCode() + "_" +
                                            reminding_list.remindingArrayList.get(i).getHour() + "_" +
                                            reminding_list.remindingArrayList.get(i).getMinute() + "_" +
                                            reminding_list.remindingArrayList.get(i).isEnable());

                            if(reminding_list.remindingArrayList.get(i).isEnable()) {
                                long time = reminding_list.remindingArrayList.get(i).getTimeInMills();
                                Intent intent = new Intent(setting_time_timepicker.this, AlarmReceiver.class);
                                intent.setAction("RemindingTime");
                                intent.putExtra("time", reminding_list.remindingArrayList.get(i).getId());
                                PendingIntent sender = PendingIntent.getBroadcast(setting_time_timepicker.this, reminding_list.remindingArrayList.get(i).getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                                if (time < System.currentTimeMillis()) {
                                    assert alarmManager != null;
                                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, time + AlarmManager.INTERVAL_DAY, sender);
                                } else {
                                    assert alarmManager != null;
                                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, sender);
                                }
                            }

                        }
                        /*long time = reminding_list.remindingArrayList.get(insertPosition).getTimeInMills();
                        Intent intent = new Intent(setting_time_timepicker.this, AlarmReceiver.class);
                        intent.setAction("RemindingTime");
                        intent.putExtra("position", insertPosition);
                        PendingIntent sender = PendingIntent.getBroadcast(setting_time_timepicker.this, reminding_list.remindingArrayList.get(insertPosition).getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        AlarmManager alarmManager2 = (AlarmManager) getSystemService(ALARM_SERVICE);
                        if (time <= System.currentTimeMillis()) {
                            assert alarmManager2 != null;
                            alarmManager2.setExact(AlarmManager.RTC_WAKEUP, time + AlarmManager.INTERVAL_DAY, sender);
                            Log.d("tomorrow",insertPosition+"!!!");
                        } else {
                            assert alarmManager2 != null;
                            alarmManager2.setExact(AlarmManager.RTC_WAKEUP, time, sender);
                            Log.d(reminding_list.remindingArrayList.get(insertPosition).getId()+":"+insertPosition,"time："+time+"!!!!");
                        }*/










                        editor.commit();


                        finish();
                        /*overridePendingTransition(0, R.anim.close_activity);*/
                    }
                }
            });
            setAmbientEnabled();




    }
}