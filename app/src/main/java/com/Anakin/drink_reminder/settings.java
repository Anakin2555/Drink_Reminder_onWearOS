package com.Anakin.drink_reminder;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;

import com.heytap.wearable.support.widget.HeyBackTitleBar;

import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class settings extends WearableActivity {
    private Button button_volumeofcup;
    private Button button_goal;
    private Button button_reminder;
    private Button button_introduction;
    private Button button_suggest;
    private HeyBackTitleBar heyBackTitleBar;
    private ScrollView scrollView;
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
        setContentView(R.layout.activity_settings);
        Activities.getInstance().addActivity(settings.this);

        heyBackTitleBar=findViewById(R.id.heybacktitle_7);
        heyBackTitleBar.getTextClock().setFormat12Hour("HH:mm");
        Log.d("",":"+heyBackTitleBar.getTextClock().is24HourModeEnabled());
        button_volumeofcup=findViewById(R.id.button_settings_1);
        button_goal=findViewById(R.id.button_settings_2);
        button_reminder=findViewById(R.id.button_settings_3);
        button_introduction=findViewById(R.id.button_settings_5);
        heyBackTitleBar.getTitleTextView().setTextSize(16);
        heyBackTitleBar.getTextClock().setTextSize(17);


        heyBackTitleBar.setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                /*overridePendingTransition(0,R.anim.close_activity);*/



            }
        }, settings.this);



        button_introduction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(settings.this, introduction.class);
                startActivity(intent);
                /*overridePendingTransition(R.anim.enter_activity, 0);*/
            }
        });

        button_volumeofcup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(settings.this, setting_VolumeOfCup.class);
                startActivity(intent);
                /*overridePendingTransition(R.anim.enter_activity, 0);*/
            }
        });

        button_goal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(settings.this,setting_goals.class);
                startActivity(intent);
                /*overridePendingTransition(R.anim.enter_activity, 0);*/
            }
        });

        button_reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(settings.this,setting_time.class);
                startActivity(intent);
                /*overridePendingTransition(R.anim.enter_activity, 0);*/
            }
        });
        setAmbientEnabled();


    }
}