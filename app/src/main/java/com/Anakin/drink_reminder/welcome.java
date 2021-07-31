package com.Anakin.drink_reminder;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.wearable.activity.WearableActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class welcome extends WearableActivity {
    private homelistener homelistener=new homelistener();
    private ImageView imageView;
    private TextView textView;


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
        Activities.getInstance().addActivity(welcome.this);
        setContentView(R.layout.activity_welcome);
        Animation animation= AnimationUtils.loadAnimation(welcome.this,R.anim.alpha_translate);
        Animation animation1=AnimationUtils.loadAnimation(welcome.this,R.anim.alpha);
        textView=findViewById(R.id.tv_8);
        /*textView.setAnimation(animation1);*/
        imageView=findViewById(R.id.logo_3);
        imageView.setAnimation(animation1);

        Handler handler=new Handler();
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(welcome.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        };
        handler.postDelayed(runnable,250);



    }
}