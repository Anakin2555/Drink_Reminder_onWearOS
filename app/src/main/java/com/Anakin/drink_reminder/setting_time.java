package com.Anakin.drink_reminder;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.heytap.wearable.support.widget.HeyBackTitleBar;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class setting_time extends WearableActivity {
private HeyBackTitleBar heyBackTitleBar;
private Button button;
private androidx.recyclerview.widget.RecyclerView recyclerView;
private Myadapter adapter;
private androidx.recyclerview.widget.RecyclerView.LayoutManager layoutManager;
Reminding_list reminding_list= Reminding_list.getInstance();
private homelistener homelistener=new homelistener();
@Override
protected void onResume(){
    super.onResume();
    adapter.notifyDataSetChanged();
    registerReceiver(homelistener,new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
}
    @Override
    //字体全局设置
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(homelistener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("setting_time has finished","0");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_time);
        Activities.getInstance().addActivity(setting_time.this);

        //列表设置
        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.requestFocus();
        layoutManager=new androidx.recyclerview.widget.LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new Myadapter(setting_time.this,reminding_list.remindingArrayList);
        recyclerView.setAdapter(adapter);
        setFooterView(recyclerView);
        setHeadView(recyclerView);
        heyBackTitleBar=findViewById(R.id.heybacktitle_5);
        heyBackTitleBar.getTitleTextView().setTextSize(16);
        heyBackTitleBar.getTextClock().setTextSize(17);
        heyBackTitleBar.getTextClock().setFormat12Hour("HH:mm");
        heyBackTitleBar.setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                /*overridePendingTransition(0,R.anim.close_activity);*/

            }
        }, setting_time.this);
        setAmbientEnabled();


    }

    private void setFooterView(RecyclerView view){
        View footer=LayoutInflater.from(this).inflate(R.layout.view_footer,view,false);
        adapter.setFooterView(footer);
    }
    private void setHeadView(RecyclerView view){
    View header=LayoutInflater.from(this).inflate(R.layout.view_head,view,false);
    adapter.setHeadView(header);
}
}