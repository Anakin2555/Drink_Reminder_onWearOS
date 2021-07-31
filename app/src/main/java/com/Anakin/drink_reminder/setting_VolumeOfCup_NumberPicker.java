package com.Anakin.drink_reminder;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.ResultReceiver;
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

public class setting_VolumeOfCup_NumberPicker extends WearableActivity {
private HeyBackTitleBar heyBackTitleBar;
private HeyNumberPicker heyNumberPicker;
private Button button;
private MotionEvent motionEvent;
Water water=Water.getInstance();
private homelistener homelistener=new homelistener();
    @Override
    //字体全局设置
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
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
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting__volume_of_cup__number_picker);
        Activities.getInstance().addActivity(setting_VolumeOfCup_NumberPicker.this);

        heyBackTitleBar=findViewById(R.id.heybacktitle_3);
        heyBackTitleBar.getTitleTextView().setTextSize(16);
        heyBackTitleBar.getTextClock().setTextSize(17);
        heyBackTitleBar.getTextClock().setFormat12Hour("HH:mm");
        button=findViewById(R.id.setting_volume_input_confirm);
        heyNumberPicker=findViewById(R.id.setting_volume_input);
        heyNumberPicker.setContentTextTypeface(Typeface.createFromAsset(getAssets(),"font/ds.ttf"));
        heyNumberPicker.da=new SoundPool(1, AudioManager.STREAM_MUSIC,3);
        heyNumberPicker.requestFocus();


        heyNumberPicker.setOnGenericMotionListener( new View.OnGenericMotionListener() {
            @Override
            public boolean onGenericMotion(View v, MotionEvent ev) {
                if (ev.getAction() == MotionEvent.ACTION_SCROLL && RotaryEncoder.isFromRotaryEncoder(ev)) {
                    // Don't forget the negation here
                    Log.d("scroll","!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    float delta = (-RotaryEncoder.getRotaryAxisValue(ev) * RotaryEncoder.getScaledScrollFactor(setting_VolumeOfCup_NumberPicker.this));

                    // Swap these axes if you want to do horizontal scrolling instead
                    List<MotionEvent> motionEventList=new ArrayList<>();
                    long downTime=SystemClock.uptimeMillis();
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


                    
                    


                    Log.d("motionevent",String.valueOf(ev.getAction()));
                       /* v.scrollBy(0, Math.round(delta));*/




                    return true;
                }
                return false;
            }
        });


        //从上一个活动获取第几个按钮
        Intent intent=getIntent();
        final int number=intent.getIntExtra("numberOfButton",0);;
        Log.d("number",String.valueOf(number));
        heyBackTitleBar.setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                /*overridePendingTransition(0,R.anim.close_activity);*/
            }
        },setting_VolumeOfCup_NumberPicker.this);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String volume=heyNumberPicker.getContentByCurrValue();  //选中的容量


                String volumeNumber=volume.substring(0,volume.length()-2);
                Log.d("chosen volume",volume);
                SharedPreferences sharedPreferences=getSharedPreferences("VolumeOfCup",MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                switch (number){
                    case 1:
                        water.setVolume_1(volumeNumber);
                        editor.putString("Volume_1",water.getVolume_1());
                        editor.commit();
                        break;
                    case 2:
                        water.setVolume_2(volumeNumber);
                        editor.putString("Volume_2",water.getVolume_2());
                        editor.commit();
                        break;
                    case 3:
                        water.setVolume_3(volumeNumber);
                        editor.putString("Volume_3",water.getVolume_3());
                        editor.commit();
                        break;
                    case 4:
                        water.setVolume_4(volumeNumber);
                        editor.putString("Volume_4",water.getVolume_4());
                        editor.commit();
                        break;
                }








                finish();
                /*overridePendingTransition(0,R.anim.close_activity);*/
                }

        });
        setAmbientEnabled();

    }

}