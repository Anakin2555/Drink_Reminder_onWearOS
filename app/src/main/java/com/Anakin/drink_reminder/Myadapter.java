package com.Anakin.drink_reminder;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.heytap.wearable.support.widget.HeyMultipleItemWithSwitch;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.StringTokenizer;

public class Myadapter extends RecyclerView.Adapter {
private Context mContext;
private ArrayList<reminding> mRemindingArrayList;
private View mFooterView;
private View mHeadView;
public static final int TYPE_FOOTER = 1;
public static final int TYPE_NORMAL = 2;
public static final int TYPE_HEADER=0;
private int Hour;
private int Minute;
private AdapterView.OnItemClickListener mOnItemClickListener;
Reminding_list reminding_list=Reminding_list.getInstance();




    public View getFooterView() {
        return mFooterView;
    }
    public void setFooterView(View footerView) {
        mFooterView = footerView;
        notifyItemInserted(getItemCount()-1);
    }

    public View getHeadView(){return mHeadView;}
    public void setHeadView(View headView){
        mHeadView=headView;
        notifyItemInserted(0);
    }
    public Myadapter(Context context,ArrayList<reminding> remindingArrayList) {
        mContext = context;
        mRemindingArrayList = remindingArrayList;

    }
public interface OnItemClickListener{
        void onButtonClicked(View view,int position);
    }
    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.mOnItemClickListener = (AdapterView.OnItemClickListener) clickListener;
    }

@Override
public int getItemViewType(int position){
        if(position==0){
            return TYPE_HEADER;
        }
    if (position == getItemCount()-1){
        //最后一个,应该加载Footer
        return TYPE_FOOTER;
    }
    return TYPE_NORMAL;
}
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if(mFooterView != null && i == TYPE_FOOTER){
            return new ItemHolder(mFooterView, (OnItemClickListener) mOnItemClickListener);
        }
        if(mHeadView!=null&&i==TYPE_HEADER){
            return new ItemHolder(mHeadView,(OnItemClickListener)mOnItemClickListener);
        }
        View v= LayoutInflater.from(mContext).inflate(R.layout.recyclerview_item,viewGroup,false);
        return new ItemHolder(v, (OnItemClickListener) mOnItemClickListener);
    }


    public class ItemHolder extends RecyclerView.ViewHolder{
        public LinearLayout recyclerView;
        public ImageButton imageButton;
        public TextView textView_1;
        public TextView textView_2;
        public Switch aSwitch;
        private Button button;
        public TextView textView_switch_all;
        public Switch switch_all;





        public ItemHolder(View view,final OnItemClickListener onItemClickListener) {
            super(view);
            if (view == mFooterView){
                button=view.findViewById(R.id.create_remindTime);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position=getAdapterPosition();
                        if(position==getItemCount()-1){

                            Intent intent=new Intent(v.getContext(),setting_time_timepicker.class);
                            intent.putExtra("position",position);
                            intent.putExtra("ifNew",1);
                            v.getContext().startActivity(intent);



                        }
                    }
                });
            }
            else if(view==mHeadView){
                textView_switch_all=view.findViewById(R.id.text_switch_all);
                switch_all=view.findViewById(R.id.switch_all);
                switch_all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(!buttonView.isPressed()){
                            return;
                        }
                        if (!isChecked) {
                            textView_switch_all.setText("关闭提醒");

                            reminding_list.all=false;
                            SharedPreferences sharedPreferences=mContext.getSharedPreferences("remindings",Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.putBoolean("all",false);
                            editor.commit();


                            for(int i=0;i<mRemindingArrayList.size();i++){
                                if(mRemindingArrayList.get(i).isEnable()) {
                                    mRemindingArrayList.get(i).setEnable(false);
                                    Intent intent1 = new Intent(mContext, AlarmReceiver.class);
                                    intent1.setAction("RemindingTime");
                                    PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, mRemindingArrayList.get(i).getId(), intent1, PendingIntent.FLAG_UPDATE_CURRENT);
                                    if(pendingIntent!=null) {
                                        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
                                        assert alarmManager != null;
                                        alarmManager.cancel(pendingIntent);
                                    }
                                }
                            }

                            notifyDataSetChanged();







                        } else {
                            textView_switch_all.setText("开启提醒");
                            reminding_list.all=true;



                            SharedPreferences sharedPreferences=mContext.getSharedPreferences("remindings",Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.putBoolean("all",true);
                            editor.commit();
                            for (int i = 0; i < mRemindingArrayList.size(); i++) {
                                String s=sharedPreferences.getString(String.valueOf(i),"-2_4_4_false");
                                StringTokenizer stringTokenizer=new StringTokenizer(s,"_");
                                String s_isEnable = "";
                                while (stringTokenizer.hasMoreTokens()){
                                    s_isEnable=stringTokenizer.nextToken();
                                }
                                boolean isEnable=Boolean.parseBoolean(s_isEnable);

                                if (isEnable) {
                                    mRemindingArrayList.get(i).setEnable(true);
                                    long time = mRemindingArrayList.get(i).getTimeInMills();
                                    Intent intent = new Intent(mContext, AlarmReceiver.class);
                                    intent.setAction("RemindingTime");
                                    intent.putExtra("time", mRemindingArrayList.get(i).getId());
                                    PendingIntent sender = PendingIntent.getBroadcast(mContext, mRemindingArrayList.get(i).getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                    AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
                                    if (time < System.currentTimeMillis()) {
                                        assert alarmManager != null;
                                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, time + AlarmManager.INTERVAL_DAY, sender);
                                    } else {
                                        assert alarmManager != null;
                                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, sender);
                                    }


                                }
                                notifyDataSetChanged();

                            }
                        }
                    }
                });
            }
            else {
                recyclerView = view.findViewById(R.id.recycler_view_item);
                textView_2 = view.findViewById(R.id.time);
                imageButton=view.findViewById(R.id.item_background);
                imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(reminding_list.all) {
                            int position = getAdapterPosition() - 1;
                            Intent intent = new Intent(v.getContext(), setting_time_timepicker.class);
                            intent.putExtra("position", position);
                            intent.putExtra("ifNew", 0);
                            v.getContext().startActivity(intent);
                        }
                        else{
                            Toast.makeText(mContext,"请先开启提醒",Toast.LENGTH_SHORT).show();
                            return;

                        }

                    }
                });
                imageButton.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if(reminding_list.all) {
                            Vibrator vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(150);
                            int position = getAdapterPosition() - 1;
                            Intent intent = new Intent(v.getContext(), delete_reminding.class);
                            intent.putExtra("position", position);


                            v.getContext().startActivity(intent);
                            return true;
                        }
                        else{
                            Toast.makeText(mContext,"请先开启提醒",Toast.LENGTH_SHORT).show();
                            return true;

                        }
                    }
                });


                aSwitch = view.findViewById(R.id.switch_reminding);
                aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(!buttonView.isPressed()){
                            return;
                        }
                        int position = getAdapterPosition()-1;
                        if (isChecked) {
                            if (reminding_list.all) {

                                /*SharedPreferences sharedPreferences = mContext.getSharedPreferences("remindings", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean("all", true);
                                editor.commit();
                                for (int i = 0; i < mRemindingArrayList.size(); i++) {
                                    String s = sharedPreferences.getString(String.valueOf(i), "-2_4_4_false");
                                    StringTokenizer stringTokenizer = new StringTokenizer(s, "_");
                                    String s_isEnable = "";
                                    while (stringTokenizer.hasMoreTokens()) {
                                        s_isEnable = stringTokenizer.nextToken();
                                    }
                                    boolean isEnable = Boolean.parseBoolean(s_isEnable);

                                    if (isEnable) {
                                        mRemindingArrayList.get(i).setEnable(true);
                                        long time = mRemindingArrayList.get(i).getTimeInMills();
                                        Intent intent = new Intent(mContext, AlarmReceiver.class);
                                        intent.setAction("RemindingTime");
                                        intent.putExtra("time", mRemindingArrayList.get(i).getId());
                                        PendingIntent sender = PendingIntent.getBroadcast(mContext, mRemindingArrayList.get(i).getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
                                        if (time < System.currentTimeMillis()) {
                                            assert alarmManager != null;
                                            alarmManager.setExact(AlarmManager.RTC_WAKEUP, time + AlarmManager.INTERVAL_DAY, sender);
                                        } else {
                                            assert alarmManager != null;
                                            alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, sender);
                                        }


                                    }
                                    notifyDataSetChanged();
                                }*/


                                mRemindingArrayList.get(position).setEnable(true);
                                long time = mRemindingArrayList.get(position).getTimeInMills();
                                Intent intent = new Intent(mContext, AlarmReceiver.class);
                                intent.setAction("RemindingTime");
                                intent.putExtra("time", mRemindingArrayList.get(position).getId());
                                PendingIntent sender = PendingIntent.getBroadcast(mContext, mRemindingArrayList.get(position).getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
                                if (time < System.currentTimeMillis()) {
                                    assert alarmManager != null;
                                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, time + AlarmManager.INTERVAL_DAY, sender);
                                } else {
                                    assert alarmManager != null;
                                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, sender);
                                }


                            } else {
                                aSwitch.setChecked(false);
                                Toast.makeText(mContext, "请先开启提醒", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        if (!isChecked) {
                                mRemindingArrayList.get(position).setEnable(false);
                                Intent intent = new Intent(mContext, AlarmReceiver.class);
                                intent.setAction("RemindingTime");
                                PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, mRemindingArrayList.get(position).getId(), intent, 0);
                                AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
                                assert alarmManager != null;
                                alarmManager.cancel(pendingIntent);


                            }
                            SharedPreferences sharedPreferences = mContext.getSharedPreferences("remindings", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("all", reminding_list.all);
                            editor.putString(String.valueOf(position),
                                    mRemindingArrayList.get(position).getCode() + "_" +
                                            mRemindingArrayList.get(position).getHour() + "_" +
                                            mRemindingArrayList.get(position).getMinute() + "_" +
                                            mRemindingArrayList.get(position).isEnable());
                            editor.commit();

                        }

                });
            }

        }
    }


        @SuppressLint("SetTextI18n")
        @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        ItemHolder holder=(ItemHolder) viewHolder;
    if (getItemViewType(i)==TYPE_FOOTER){
        return;
    }
    else if(getItemViewType(i)==TYPE_HEADER){
        holder.switch_all.setChecked(reminding_list.all);
        return;
    }


    SharedPreferences sharedPreferences_size=mContext.getSharedPreferences("size",Context.MODE_PRIVATE);
    SharedPreferences.Editor editor=sharedPreferences_size.edit();
    editor.putInt("number",mRemindingArrayList.size());
    editor.commit();


    holder.aSwitch.setChecked(mRemindingArrayList.get(i-1).isEnable());
    if(!reminding_list.all){
        holder.aSwitch.setChecked(false);
    }
    Hour=mRemindingArrayList.get(i-1).getHour();
    Minute=mRemindingArrayList.get(i-1).getMinute();
    if(Hour<10){
        if(Minute<10){

            holder.textView_2.setText("0"+Hour+":"+"0"+Minute);
        }
        else {

            holder.textView_2.setText("0" + Hour + ":" + Minute);
        }
    }
    else{
        if(Minute<10){
            holder.textView_2.setText(Hour+":"+"0"+Minute);
        }
        else
        holder.textView_2.setText( Hour+":"+Minute);

    }
    }

    @Override
    public int getItemCount() {
        if(mFooterView!=null&&mHeadView!=null) {
            return mRemindingArrayList.size() + 2;
        }
        return mRemindingArrayList.size();
    }

}
