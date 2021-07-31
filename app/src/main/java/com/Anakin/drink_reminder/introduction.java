package com.Anakin.drink_reminder;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.wearable.activity.WearableActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class introduction extends WearableActivity {
private homelistener homelistener= new homelistener();
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
    protected void onStop() {
        super.onStop();
        unregisterReceiver(homelistener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);
        Activities.getInstance().addActivity(introduction.this);
        ImageView imageView = findViewById(R.id.logo_2);
        final TextView textView_1 = findViewById(R.id.tv_3);
        final TextView textView_2 = findViewById(R.id.tv_4);
        Animation animation_1= AnimationUtils.loadAnimation(introduction.this,R.anim.alpha_translate);
        imageView.setAnimation(animation_1);
        Handler handler=new Handler();
        Runnable runnable=new Runnable() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                textView_1.setText("喝水助手");
                textView_2.setText("V2.2");
                Animation animation_2=AnimationUtils.loadAnimation(introduction.this,R.anim.alpha_800ms);
                textView_1.setAnimation(animation_2);
                textView_2.setAnimation(animation_2);

            }
        };
        handler.postDelayed(runnable,200);
        setAmbientEnabled();



    }
}