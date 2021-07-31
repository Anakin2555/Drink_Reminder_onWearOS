package com.Anakin.drink_reminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Objects;

public class homelistener extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO: This method is called when the BroadcastReceiver is receiving
            // an Intent broadcast.


            if (Objects.equals(intent.getAction(), Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra("reason");
                Log.i("received", "reason:" + reason);
                if ("homekey".equals(reason)) { // 短按Home键
                    //可以在这里实现关闭程序操作。。。
                    Log.i("received_2", "homekey");
                    Activities.getInstance().exit(context);

                }
            }

        }
    }
