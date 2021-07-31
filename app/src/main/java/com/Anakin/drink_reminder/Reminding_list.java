package com.Anakin.drink_reminder;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Reminding_list extends Application {
    private  static Reminding_list instance=null;
    public int size;
    public boolean all=true;
    private Reminding_list(){}
    public static Reminding_list getInstance(){
        if (instance==null)
            instance=new Reminding_list();
        return instance;
    }

    public ArrayList<reminding> remindingArrayList=new ArrayList<reminding>();
    public void add(reminding reminding){
        remindingArrayList.add(reminding);
    }
    public void remove(reminding reminding){
        remindingArrayList.remove(reminding);
    }
    public void load_reminding(Context context){
        SharedPreferences sharedPreferences_size=context.getSharedPreferences("size",Context.MODE_PRIVATE);
        remindingArrayList.clear();
        size=sharedPreferences_size.getInt("number",0);
        SharedPreferences sharedPreferences=context.getSharedPreferences("remindings",Context.MODE_PRIVATE);
        all=sharedPreferences.getBoolean("all",true);
        for(int i=0;i<size;i++){
            reminding reminding = new reminding();
            remindingArrayList.add(reminding);
            String s=sharedPreferences.getString(String.valueOf(i),"-2_4_4_false");
            StringTokenizer stringTokenizer=new StringTokenizer(s,"_");
            reminding.setCode(Integer.parseInt(stringTokenizer.nextToken()));
            reminding.setHour(Integer.parseInt(stringTokenizer.nextToken()));
            reminding.setMinute(Integer.parseInt(stringTokenizer.nextToken()));
            reminding.setEnable(Boolean.parseBoolean(stringTokenizer.nextToken()));

        }
    }
}
