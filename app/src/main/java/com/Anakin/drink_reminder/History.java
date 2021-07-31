package com.Anakin.drink_reminder;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.Anakin.drink_reminder.view.Calendarview;
import com.heytap.wearable.support.widget.HeyBackTitleBar;

import java.util.Calendar;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class History extends WearableActivity {
    private int month;
    private int year;
    private int date;
    private int notnulldays;
    private int reachgoal_days;
    private int monthWatertotal;
    private int days;
    private homelistener homelistener=new homelistener();
    Water water=Water.getInstance();
    float goal_water=Float.parseFloat(water.getTotalWater());
    @Override
    protected void onResume(){
        super.onResume();
        registerReceiver(homelistener,new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
    }
    @Override
    //字体全局设置
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(homelistener);
    }

    public static int getCurrentMonthLastDay(int year,int month)
    {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);//把日期设置为当月第一天
        a.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Activities.getInstance().addActivity(History.this);

        ScrollView scrollView=findViewById(R.id.scroll_view_2);
        scrollView.requestFocus();
        /*TextView textView_1 = findViewById(R.id.tv_10);*/
        final Calendarview calendarview = findViewById(R.id.calendarview);
        HeyBackTitleBar heyBackTitleBar = findViewById(R.id.heybacktitle_8);
        final TextView tv_currentmonth = findViewById(R.id.currentmonth);
        final TextView tv_currentyear=findViewById(R.id.currentyear);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "font/ds.ttf");
        tv_currentyear.setTypeface(typeface);

        Button button_lastmonth=findViewById(R.id.lastmonth);
        Button button_nextmonth=findViewById(R.id.nextmonth);
        TextView tv_goal=findViewById(R.id.goalwater);
        final TextView tv_reachgoaldays=findViewById(R.id.reachgoal);
        final TextView tv_notreach=findViewById(R.id.notreachgoal);
        final TextView tv_average=findViewById(R.id.averagewater);





        reachgoal_days=0;

        final SharedPreferences sharedPreferences=getSharedPreferences("Water",MODE_PRIVATE);
        calendarview.setGoal(goal_water);
        final String currentdate=getIntent().getStringExtra("currentdate");
        Log.d("today",currentdate);

        final Calendar calendar=Calendar.getInstance();
         year=calendar.get(Calendar.YEAR);
         date=Integer.parseInt(currentdate.substring(6));
         month=Integer.parseInt(currentdate.substring(4,6));
         final int currentmonth=calendar.get(Calendar.MONTH)+1;


        Log.d("month date",month+" "+String.valueOf(date));


        button_lastmonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                month=month-1;
                if(month==0){
                    month=12;
                    year=year-1;
                }

                setRecord(year,month,calendarview,date,sharedPreferences,currentdate);
                tv_reachgoaldays.setText(String.valueOf(reachgoal_days));
                tv_notreach.setText(String.valueOf(days-reachgoal_days));
                if(notnulldays==0){
                    tv_average.setText("0");
                }else
                tv_average.setText(String.valueOf((int)monthWatertotal/notnulldays));
                calendarview.invalidate();
                tv_currentmonth.setText(month+"月");
                tv_currentyear.setText(String.valueOf(year));
            }
        });

        button_nextmonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (year == calendar.get(Calendar.YEAR)&&month == currentmonth) {

                    return;
                } else {
                    month = month + 1;
                    if (month == 13) {
                        month = 1;
                        year = year + 1;
                    }
                    setRecord(year, month, calendarview, date, sharedPreferences, currentdate);
                    tv_reachgoaldays.setText(String.valueOf(reachgoal_days));
                    tv_notreach.setText(String.valueOf(days-reachgoal_days));
                    if(notnulldays==0){
                        tv_average.setText("0");
                    }else
                    tv_average.setText(String.valueOf((int)monthWatertotal/notnulldays));
                    calendarview.invalidate();
                    tv_currentmonth.setText(month + "月");
                    tv_currentyear.setText(String.valueOf(year));
                }
            }


        });

        setRecord(year,month,calendarview,date,sharedPreferences,currentdate);
        tv_reachgoaldays.setText(String.valueOf(reachgoal_days));
        tv_notreach.setText(String.valueOf(days-reachgoal_days));
        if(notnulldays==0){
            tv_average.setText("0");
        }else
        tv_average.setText(String.valueOf((int)monthWatertotal/notnulldays));
        tv_goal.setText("喝水目标: "+water.getTotalWater()+"ml");








        String water=sharedPreferences.getString(currentdate,"0");
       /* String water2=sharedPreferences.getString(String.valueOf(Integer.valueOf(currentdate)-1),"0");
        textView_1.setText(water+"  "+water2);*/











        //返回栏
        heyBackTitleBar.getTitleTextView().setTextSize(16);
        heyBackTitleBar.getTextClock().setTextSize(17);
        heyBackTitleBar.getTextClock().setFormat12Hour("HH:mm");
        heyBackTitleBar.setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                /*overridePendingTransition(0,R.anim.close_activity);*/
            }
        },History.this);

        tv_currentmonth.setText(month+"月");
        tv_currentyear.setText(String.valueOf(year));
        setAmbientEnabled();






    }
    public void setRecord(int year,int month,Calendarview calendarview,int date,SharedPreferences sharedPreferences,String currentdate){
        date=1;
        notnulldays=0;
        days=getCurrentMonthLastDay(year,month);
        reachgoal_days=0;
        monthWatertotal=0;
        calendarview.setDays(days);
        float[] record =new float[days];
        for(int i=1;i<=days;i++){
            if(month<10&&date<10){
                record[i-1]=Float.parseFloat(sharedPreferences.getString(
                        year+"0"+String.valueOf(month)+"0"+date,
                        "0"));
            }
            else if(month<10&&date>=10){
                record[i-1]=Float.parseFloat(sharedPreferences.getString(
                        year+"0"+String.valueOf(month)+date,
                        "0"));
            }
            else if(month>=10&&date<10){
                record[i-1]=Float.parseFloat(sharedPreferences.getString(
                        year+String.valueOf(month)+"0"+date,
                        "0"));
            }
            else{
                record[i-1]=Float.parseFloat(sharedPreferences.getString(
                        year+String.valueOf(month)+date,
                        "0"));
            }

            Log.d("record:   "+month+":"+date,String.valueOf(record[date-1]));
            date++;
        }

        for(int i=0;i<days;i++){
            monthWatertotal+=record[i];
            if(record[i]!=0){
                notnulldays++;
            }
            if(record[i]>=goal_water){
                reachgoal_days++;
            }
        }

        calendarview.setRecord(record);
    }
}