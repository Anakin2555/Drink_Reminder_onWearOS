package com.Anakin.drink_reminder;

import androidx.appcompat.app.AppCompatActivity;

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
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.heytap.wearable.support.widget.HeyBackTitleBar;
import com.heytap.wearable.support.widget.HeyNumberPicker;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class
setting_goals extends WearableActivity {
    Water water=Water.getInstance();
private HeyNumberPicker heyNumberPicker;
private HeyBackTitleBar heyBackTitleBar;
private Button button;
private homelistener homelistener=new homelistener();
    @Override
    //字体全局设置
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_goals);
        Activities.getInstance().addActivity(setting_goals.this);
        heyNumberPicker=findViewById(R.id.setting_goal_input);
        int goal=Integer.parseInt(water.getTotalWater());
        int goal_num=goal/100-5;
        heyNumberPicker.setValue(goal_num);
        heyBackTitleBar=findViewById(R.id.heybacktitle_4);
        button=findViewById(R.id.setting_goal_input_confirm);
        heyNumberPicker.da=new SoundPool(1, AudioManager.STREAM_MUSIC,3);
        heyNumberPicker.requestFocus();
        heyNumberPicker.setContentTextTypeface(Typeface.createFromAsset(getAssets(),"font/ds.ttf"));
        heyNumberPicker.setOnGenericMotionListener( new View.OnGenericMotionListener() {
            @Override
            public boolean onGenericMotion(View v, MotionEvent ev) {
                if (ev.getAction() == MotionEvent.ACTION_SCROLL && RotaryEncoder.isFromRotaryEncoder(ev)) {
                    // Don't forget the negation here
                    // Swap these axes if you want to do horizontal scrolling instead
                    List<MotionEvent> motionEventList=new ArrayList<>();
                    long downTime= SystemClock.uptimeMillis();
                    long eventTime=downTime;
                    float px=v.getX()/2;
                    float py=v.getY()/2;
                    MotionEvent motionEvent_down=MotionEvent.obtain( downTime, eventTime,MotionEvent.ACTION_DOWN,px,py,0);
                    v.onTouchEvent(motionEvent_down);
                    //向下滑
                    if(ev.getAxisValue(MotionEvent.AXIS_SCROLL)<0){
                        Log.d("scroll down","0");
                        for(int i=0;i<50;i++){
                            eventTime+=10;
                            py-=1.2;
                            MotionEvent motionEvent_move=MotionEvent.obtain(downTime,eventTime,MotionEvent.ACTION_MOVE,px,py,0);
                            motionEventList.add(motionEvent_move);
                            v.onTouchEvent(motionEvent_move);

                        }
                    }
                    //向上滑
                    else {
                        Log.d("scroll up", "1");
                        for(int i=0;i<50;i++){
                            eventTime+=10;
                            py+=1.2;
                            MotionEvent motionEvent_move=MotionEvent.obtain(downTime,eventTime,MotionEvent.ACTION_MOVE,px,py,0);
                            motionEventList.add(motionEvent_move);
                            v.onTouchEvent(motionEvent_move);

                        }
                    }
                    MotionEvent motionEvent_up=MotionEvent.obtain(downTime,eventTime,MotionEvent.ACTION_UP,px,py,0);
                    v.onTouchEvent(motionEvent_up);
                    //回收
                    motionEvent_down.recycle();
                    for(int i=0;i<motionEventList.size();i++){
                        motionEventList.get(i).recycle();
                    }
                    motionEvent_up.recycle();

                    /* v.scrollBy(0, Math.round(delta));*/




                    return true;
                }
                return false;
            }
        });
        heyBackTitleBar.getTitleTextView().setTextSize(17);
        heyBackTitleBar.getTextClock().setFormat12Hour("HH:mm");
        heyBackTitleBar.getTextClock().setTextSize(17);
        heyBackTitleBar.setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                /*overridePendingTransition(0,R.anim.close_activity);*/
            }
        },setting_goals.this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String volume=heyNumberPicker.getContentByCurrValue();  //选中的容量
                Log.d("chosen volume",volume);
                String volume_number =volume.substring(0,volume.length()-2);
                water.setTotalWater(volume_number);
                SharedPreferences sharedPreferences=getSharedPreferences("Water",MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("TotalWater",volume_number);
                editor.commit();
                finish();
                /*overridePendingTransition(0,R.anim.close_activity);*/
            }
        });
        setAmbientEnabled();
    }



}