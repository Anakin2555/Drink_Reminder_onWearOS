package com.Anakin.drink_reminder;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.wearable.activity.WearableActivity;
import android.text.Html;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class reach_goal_congratulate extends WearableActivity {
private TextView goalvolume;
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
        setContentView(R.layout.activity_reach_goal_congratulate);
        Activities.getInstance().addActivity(reach_goal_congratulate.this);
        goalvolume=findViewById(R.id.text_goalvolume);
        final TextView reachgoal = findViewById(R.id.text_reachgoal);
        Animation animation= AnimationUtils.loadAnimation(reach_goal_congratulate.this,R.anim.alpha_800ms);
        final String totalWater=water.getTotalWater()+"<font color=#C0C0C0><small>ml</small></font color>";
        reachgoal.setText(" 今日喝水目标\n       已完成");
        reachgoal.setAnimation(animation);
        final Handler mHandler = new Handler();
             Runnable r = new Runnable() {
              @Override
                  public void run() {

                  goalvolume.setText(Html.fromHtml(totalWater));
                  Animation animation1=AnimationUtils.loadAnimation(reach_goal_congratulate.this,R.anim.alpha);
                  goalvolume.setAnimation(animation1);

                }
         };

        mHandler.postDelayed(r, 3000);//延时
        setAmbientEnabled();



    }
}