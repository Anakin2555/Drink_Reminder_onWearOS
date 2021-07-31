package com.Anakin.drink_reminder;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import com.heytap.wearable.support.widget.HeyBackTitleBar;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class setting_VolumeOfCup extends WearableActivity {
    private HeyBackTitleBar heyBackTitleBar;
    private Button Volume_edit_1;
    private Button Volume_edit_2;
    private Button Volume_edit_3;
    private Button Volume_edit_4;
    private homelistener homelistener=new homelistener();
    Water water=Water.getInstance();
    @Override
    //字体全局设置
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    protected void onResume(){
        super.onResume();
        onCreate(null);
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
        setContentView(R.layout.activity_settings__volume_of_cup);
        Activities.getInstance().addActivity(setting_VolumeOfCup.this);

        heyBackTitleBar=(HeyBackTitleBar)findViewById(R.id.heybacktitle_2);
        Volume_edit_1=findViewById(R.id.button_edit_1);
        Volume_edit_2=findViewById(R.id.button_edit_2);
        Volume_edit_3=findViewById(R.id.button_edit_3);
        Volume_edit_4=findViewById(R.id.button_edit_4);
        String VolumeOfGlass_1=water.getVolume_1()+"<font color='#A9A9A9'><small><small><small>ml<small><small></small></font color>";
        Volume_edit_1.setText(Html.fromHtml(VolumeOfGlass_1));

        String VolumeOfGlass_2=water.getVolume_2()+"<font color='#A9A9A9'><small><small><small>ml<small><small></small></font color>";
        Volume_edit_2.setText(Html.fromHtml(VolumeOfGlass_2));

        String VolumeOfGlass_3=water.getVolume_3()+"<font color='#A9A9A9'><small><small><small>ml<small><small></small></font color>";
        Volume_edit_3.setText(Html.fromHtml(VolumeOfGlass_3));
        String VolumeOfGlass_4=water.getVolume_4()+"<font color='#A9A9A9'><small><small><small>ml<small><small></small></font color>";
        Volume_edit_4.setText(Html.fromHtml(VolumeOfGlass_4));


        //修改水杯容量大小
        Volume_edit_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(setting_VolumeOfCup.this,setting_VolumeOfCup_NumberPicker.class);
                intent.putExtra("numberOfButton",1);
                startActivity(intent);
                /*overridePendingTransition(R.anim.enter_activity,0);*/
            }
        });

        Volume_edit_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(setting_VolumeOfCup.this,setting_VolumeOfCup_NumberPicker.class);
                intent.putExtra("numberOfButton",2);
                startActivity(intent);
                /*overridePendingTransition(R.anim.enter_activity,0);*/
            }
        });

        Volume_edit_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(setting_VolumeOfCup.this,setting_VolumeOfCup_NumberPicker.class);
                intent.putExtra("numberOfButton",3);
                startActivity(intent);
                /*overridePendingTransition(R.anim.enter_activity,0);*/
            }
        });

        Volume_edit_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(setting_VolumeOfCup.this,setting_VolumeOfCup_NumberPicker.class);
                intent.putExtra("numberOfButton",4);
                startActivity(intent);
                /*overridePendingTransition(R.anim.enter_activity,0);*/
            }
        });


















        heyBackTitleBar.getTitleTextView().setTextSize(16);
        heyBackTitleBar.getTextClock().setTextSize(17);
        heyBackTitleBar.getTextClock().setFormat12Hour("HH:mm");
        heyBackTitleBar.setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                /*overridePendingTransition(0,R.anim.close_activity);*/

            }
        },setting_VolumeOfCup.this);
        setAmbientEnabled();


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String dataString;
        switch (resultCode){
            case 1:
                dataString=data.getStringExtra("chosen_volume");
                water.setVolume_1(dataString);

                break;
            case 2:
                dataString=data.getStringExtra("chosen_volume");
                water.setVolume_2(dataString);

                break;
            case 3:
                dataString=data.getStringExtra("chosen_volume");
                water.setVolume_3(dataString);
                break;
            case 4:
                dataString=data.getStringExtra("chosen_volume");
                water.setVolume_4(dataString);
                break;

        }
    }





}