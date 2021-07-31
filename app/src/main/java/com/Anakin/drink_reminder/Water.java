package com.Anakin.drink_reminder;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class Water extends Application {

    private  static Water instance=null;
    private Water(){}
    public static Water getInstance(){
        if (instance==null)
            instance=new Water();
        return instance;
    }
    private String TotalWater="2000";
    private String HaveDrunk="0";

    private boolean reachGoal=false;
    public boolean getReachGoal(){
        return this.reachGoal;
    }
    public void setReachGoal(boolean reachGoal){
        this.reachGoal=reachGoal;
    }

    public String getRemainWater(){
        if(Integer.parseInt(HaveDrunk)>= Integer.parseInt(TotalWater)){
            return "0";
        }
        else
            return String.valueOf(Integer.parseInt(TotalWater)-Integer.parseInt(HaveDrunk));
    }
    private String Volume[]=new String[]{"250","500","750","1000"};

    public void setVolume_1(String volume){
        this.Volume[0]=volume;
    }
    public void setVolume_2(String volume){
        this.Volume[1]=volume;
    }
    public void setVolume_3(String volume){
        this.Volume[2]=volume;
    }
    public void setVolume_4(String volume){
        this.Volume[3]=volume;
    }
    public String getVolume_1(){ return this.Volume[0]; }
    public String getVolume_2(){
        return this.Volume[1];
    }
    public String getVolume_3(){
        return this.Volume[2];
    }
    public String getVolume_4(){
        return this.Volume[3];
    }

    public String getTotalWater(){
        return TotalWater;
    }

    public String getHaveDrunk(){
        return HaveDrunk;
    }
    public void setTotalWater(String totalWater){
        TotalWater = totalWater;
    }
    public void setHaveDrunk(String haveDrunk){
        HaveDrunk =haveDrunk;
    }


    public void load_VolumeOfCup(Context context){
        SharedPreferences sharedPreferences=context.getSharedPreferences("VolumeOfCup",MODE_PRIVATE);
        setVolume_1(sharedPreferences.getString("Volume_1","250"));
        setVolume_2(sharedPreferences.getString("Volume_2","500"));
        setVolume_3(sharedPreferences.getString("Volume_3","750"));
        setVolume_4(sharedPreferences.getString("Volume_4","1000"));

    }
    public void load_Water(Context context,String currentDate){
        SharedPreferences sharedPreferences=context.getSharedPreferences("Water",MODE_PRIVATE);
        setTotalWater(sharedPreferences.getString("TotalWater","2000"));
        setHaveDrunk(sharedPreferences.getString(currentDate,"0"));
        setReachGoal(sharedPreferences.getBoolean(currentDate+"ifReach",false));


    }


}
