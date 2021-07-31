package com.Anakin.drink_reminder;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.icu.text.UnicodeSetSpanner;
import android.os.Bundle;
import android.provider.Settings;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.input.RotaryEncoder;
import android.text.Html;
import android.util.Log;
import android.util.TimeUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.Anakin.drink_reminder.utils.NoDoubleClickListener;
import com.Anakin.drink_reminder.view.Circle;
import com.heytap.wearable.support.recycler.widget.LinearLayoutManager;
import com.heytap.wearable.support.recycler.widget.RecyclerView;
import com.heytap.wearable.support.widget.HeyBackTitleBar;
import com.heytap.wearable.support.widget.HeyMainTitleBar;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.channels.Channel;
import java.util.Calendar;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends WearableActivity {



    private ScrollView scrollView;
    private Circle circle;
    private TextView text_DrunkWater;
    private TextView text_TotalWater;
    private Button button_VolumeOfGlass_1;
    private Button button_VolumeOfGlass_2;
    private Button button_VolumeOfGlass_3;
    private Button button_VolumeOfGlass_4;
    private Button button_history;
    private Button button_return;
    private Button button_MoreSettings;
    private TextView tv;
    private HeyBackTitleBar heyMainTitleBar;
    private float angle;
    private  homelistener homelistener=new homelistener();
    private String current_date;
    Calendar calendarOfDate=Calendar.getInstance();
    Water water=Water.getInstance();
    Reminding_list reminding_list=Reminding_list.getInstance();


    @Override
    //字体全局设置
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume(){
        super.onResume();
        registerReceiver(homelistener,new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
        Log.d("mainActivity has resumed","!!!!");
        refresh();



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.exit(0);
        Log.d("mainActivity has finished","!!!!");
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(homelistener);
        Log.d("mainActivity has paused","!!!!");

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Activities.getInstance().addActivity(MainActivity.this);


        resetWater();
        sendNotify();

        Log.d("oncreatedate",current_date);
        water.load_VolumeOfCup(MainActivity.this);
        water.load_Water(MainActivity.this,current_date);
        reminding_list.load_reminding(MainActivity.this);


        SharedPreferences sharedPreferences =getSharedPreferences("Update_first_boot",MODE_PRIVATE);
        if(sharedPreferences.getInt("if_first_boot", 5)!=6){

            for(int position=0;position<reminding_list.size;position++){
                Intent intent1 = new Intent(MainActivity.this, AlarmReceiver.class);
                intent1.setAction("RemindingTime");
                PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this,reminding_list.remindingArrayList.get(position).getId(), intent1, PendingIntent.FLAG_UPDATE_CURRENT);
                if(pendingIntent!=null) {
                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    assert alarmManager != null;
                    alarmManager.cancel(pendingIntent);
                }
            }

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("if_first_boot", 6);
            Log.d("iffirst_boot:"+sharedPreferences.getInt("if_first_boot", 1),"!!!!");
            editor.commit();
            SharedPreferences sharedPreferences_1=getSharedPreferences("remindings",MODE_PRIVATE);
            SharedPreferences.Editor editor1=sharedPreferences_1.edit();

            reminding_list.remindingArrayList.clear();
            for (int i = 0; i < 16; i++) {

                    reminding_list.add(new reminding(i+7,30,false,i));
                    editor1.putString(String.valueOf(reminding_list.remindingArrayList.get(i).getCode()),
                        reminding_list.remindingArrayList.get(i).getCode() + "_" +
                                reminding_list.remindingArrayList.get(i).getHour() + "_" +
                                reminding_list.remindingArrayList.get(i).getMinute() + "_" +
                                reminding_list.remindingArrayList.get(i).isEnable());
                    Log.d("add completed","!!!");
            }


            editor1.commit();
        }




        scrollView=findViewById(R.id.scroll_view);
        circle=findViewById(R.id.circle_view);
        circle.setLayerType(View.LAYER_TYPE_SOFTWARE,null);
        text_DrunkWater=findViewById(R.id.drinkedwater);
        text_TotalWater=findViewById(R.id.totalwater);
        button_VolumeOfGlass_1=findViewById(R.id.button_1);
        button_VolumeOfGlass_2=findViewById(R.id.button_2);
        button_VolumeOfGlass_3=findViewById(R.id.button_3);
        button_VolumeOfGlass_4=findViewById(R.id.button_4);
        button_return=findViewById(R.id.button_5);
        button_MoreSettings=findViewById(R.id.button_6);
        button_history=findViewById(R.id.button_7);
        heyMainTitleBar=findViewById(R.id.app_titleBar);



        //scrollview支持表冠滑动
        scrollView.requestFocus();

        //标题栏文字颜色大小
        heyMainTitleBar.getTitleTextView().setTextSize(16);
        /*heyMainTitleBar.getTitleTextView().setTextColor(getResources().getColor(R.color.SkyBlue));*/
        heyMainTitleBar.getTextClock().setFormat12Hour("HH:mm");
        heyMainTitleBar.getTextClock().setTextSize(17);
        heyMainTitleBar.setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activities.getInstance().exit(MainActivity.this);
               /* overridePendingTransition(0,R.anim.close_activity);*/
            }
        },MainActivity.this);

        //点击后可增加已喝水量
        View.OnClickListener onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    SharedPreferences sharedPreferences = getSharedPreferences("Water", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    switch (v.getId()) {
                        case R.id.button_1:
                            water.setHaveDrunk(String.valueOf(Integer.parseInt(water.getHaveDrunk()) + Integer.parseInt(water.getVolume_1())));

                            break;
                        case R.id.button_2:
                            water.setHaveDrunk(String.valueOf(Integer.parseInt(water.getHaveDrunk()) + Integer.parseInt(water.getVolume_2())));

                            break;
                        case R.id.button_3:
                            water.setHaveDrunk(String.valueOf(Integer.parseInt(water.getHaveDrunk()) + Integer.parseInt(water.getVolume_3())));

                            break;
                        case R.id.button_4:
                            water.setHaveDrunk(String.valueOf(Integer.parseInt(water.getHaveDrunk()) + Integer.parseInt(water.getVolume_4())));

                            break;
                        //重置
                        case R.id.button_5:
                            water.setHaveDrunk("0");
                            water.setReachGoal(false);
                            editor.remove(current_date + "ifReach");

                            break;


                    }

                    editor.putString(current_date, water.getHaveDrunk());


                    String DrunkWater = water.getHaveDrunk() + "<font color='#00BFFF'><small>ml</small></font color>";

                    text_DrunkWater.setText(Html.fromHtml(DrunkWater));
                    text_DrunkWater.invalidate();
                    //绘制圆环
                    circle.setAngle(angle = 270 * (Float.parseFloat(water.getHaveDrunk()) / Float.parseFloat(water.getTotalWater())));
                    circle.invalidate();
                    if (angle >= 270 && !water.getReachGoal()) {
                        water.setReachGoal(true);
                        editor.putBoolean(current_date + "ifReach", true);
                        editor.commit();
                        Intent intent = new Intent(MainActivity.this, reach_goal_congratulate.class);
                        startActivity(intent);
                       /* overridePendingTransition(R.anim.enter_activity_frombotton, 0);*/
                    } else
                        editor.commit();

                }

        };
        button_VolumeOfGlass_1.setOnClickListener(onClickListener);
        button_VolumeOfGlass_2.setOnClickListener(onClickListener);
        button_VolumeOfGlass_3.setOnClickListener(onClickListener);
        button_VolumeOfGlass_4.setOnClickListener(onClickListener);
        button_return.setOnClickListener(onClickListener);



        //跳转设置界面
        button_MoreSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    button_MoreSettings.setPressed(true);
                    Intent intent = new Intent(MainActivity.this, settings.class);
                    startActivity(intent);
                    /*overridePendingTransition(R.anim.enter_activity, 0);*/



            }
        });
        button_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent = new Intent(MainActivity.this, History.class);
                    intent.putExtra("currentdate", current_date);
                    startActivity(intent);
                    /*overridePendingTransition(R.anim.enter_activity, 0);*/

            }
        });




        // Enables Always-on
        setAmbientEnabled();


    }



    //返回主页刷新水杯容量和目标水量
public void refresh(){
        String DrunkWater=water.getHaveDrunk()+"<font color='#00BFFF'><small>ml</small></font color>";


        text_DrunkWater.setText(Html.fromHtml(DrunkWater));

        String TotalWater="/"+water.getTotalWater()+"<font color='#9F9B9B'><small>ML</small></font color>";
        text_TotalWater.setText(Html.fromHtml(TotalWater));

        String VolumeOfGlass_1=water.getVolume_1()+"<font color='#9F9B9B'><small><small><small>ml<small><small></small></font color>";
        button_VolumeOfGlass_1.setText(Html.fromHtml(VolumeOfGlass_1));
        button_VolumeOfGlass_1.invalidate();
        String VolumeOfGlass_2=water.getVolume_2()+"<font color='#9F9B9B'><small><small><small>ml<small><small></small></font color>";
        button_VolumeOfGlass_2.setText(Html.fromHtml(VolumeOfGlass_2));
        button_VolumeOfGlass_2.invalidate();
        String VolumeOfGlass_3=water.getVolume_3()+"<font color='#9F9B9B'><small><small><small>ml<small><small></small></font color>";
        button_VolumeOfGlass_3.setText(Html.fromHtml(VolumeOfGlass_3));
        button_VolumeOfGlass_3.invalidate();
        String VolumeOfGlass_4=water.getVolume_4()+"<font color='#9F9B9B'><small><small><small>ml<small><small></small></font color>";
        button_VolumeOfGlass_4.setText(Html.fromHtml(VolumeOfGlass_4));
        button_VolumeOfGlass_4.invalidate();
        circle.setAngle((float)270*(Float.parseFloat(water.getHaveDrunk())/Float.parseFloat(water.getTotalWater())));
        circle.invalidate();
    }

//每日更新
public void resetWater(){
        int Month=calendarOfDate.get(Calendar.MONTH)+1;
        int Day=calendarOfDate.get(Calendar.DATE);
        int Year=calendarOfDate.get(Calendar.YEAR);
    if(Month<10) {
        if(Day<10) {
            current_date = Year + "0" + Month + "0"+Day;
        }
        else
            current_date=Year+"0"+Month+(Day);
    }
    else{
        if(Day<10) {
            current_date = Year  + String.valueOf(Month) + "0"+Day;
        }
        else
            current_date=Year+String.valueOf(Month)+ (Day);
    }

    Log.d("date",current_date);
}
   /* public static int getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(),
                    0);
            String version = info.versionName;
            int versioncode = info.versionCode;
            return versioncode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }*/


public void sendNotify(){
        String id="channel_1";
        String description="channel";
        int importance=NotificationManager.IMPORTANCE_DEFAULT;
    NotificationChannel notificationChannel=new NotificationChannel(id,description,importance);
    notificationChannel.enableVibration(true);
    NotificationManager notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
    notificationManager.createNotificationChannel(notificationChannel);
   Notification.Builder builder=new Notification.Builder(this,"channel_1");
    builder.setContentTitle("该喝水啦！")
            .setContentText("今日还需喝"+String.valueOf(Integer.parseInt(water.getTotalWater())-Integer.parseInt(water.getHaveDrunk()))+"ml")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setDefaults(Notification.DEFAULT_VIBRATE )
//            .setVibrate(new long[]{0, 1000, 1000, 1000})
            .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher));
    Notification notification=builder.build();

    notificationManager.notify(1,notification);

}
}

