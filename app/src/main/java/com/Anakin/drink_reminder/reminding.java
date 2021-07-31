package com.Anakin.drink_reminder;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.util.Calendar;
public class reminding {
    private int hour;
    private int minute;
    private boolean isEnable=true;
    private int code=-1;
    public reminding(){}
    public reminding(int hour,int minute,boolean isEnable,int code){
        this.hour=hour;
        this.minute=minute;
        this.isEnable=isEnable;
        this.code=code;
    }


    public long getTimeInMills() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 10);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }
    public int getId(){
        if(minute<10)
        return Integer.parseInt(String.valueOf(this.hour)+"0"+this.minute);
        else
            return Integer.parseInt(String.valueOf(this.hour)+this.minute);
    }


    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }



    public int getHour() {
        return this.hour;
    }
    public void setHour(int Hour_picked){
        this.hour=Hour_picked;
    }

    public int getMinute() {
        return this.minute;
    }
    public void setMinute(int Minute_picked){
        this.minute=Minute_picked;
    }

}
