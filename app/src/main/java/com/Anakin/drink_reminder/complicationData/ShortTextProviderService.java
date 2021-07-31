package com.Anakin.drink_reminder.complicationData;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Icon;
import android.support.wearable.complications.ComplicationData;
import android.support.wearable.complications.ComplicationManager;
import android.support.wearable.complications.ComplicationProviderService;
import android.support.wearable.complications.ComplicationText;
import android.util.Log;
import android.widget.Toast;

import com.Anakin.drink_reminder.MainActivity;
import com.Anakin.drink_reminder.R;
import com.Anakin.drink_reminder.Water;
import com.Anakin.drink_reminder.complicationData.ComplicationToggleReceiver;
import com.Anakin.drink_reminder.welcome;

/**
 * A complication provider that supports only {@link ComplicationData#TYPE_SHORT_TEXT} and cycles
 * through the possible configurations on tap.
 */
public class ShortTextProviderService extends ComplicationProviderService {
    Water water=Water.getInstance();

    @Override
    public void onComplicationUpdate(int complicationId, int type, ComplicationManager manager) {
        if (type != ComplicationData.TYPE_SHORT_TEXT) {
            manager.noUpdateRequired(complicationId);
            return;
        }

        ComponentName thisProvider = new ComponentName(this, getClass());
        PendingIntent complicationTogglePendingIntent =
                ComplicationToggleReceiver.getToggleIntent(this, thisProvider, complicationId);

        SharedPreferences preferences =
                getSharedPreferences(ComplicationToggleReceiver.PREFERENCES_NAME, 0);
        int state =
                preferences.getInt(
                        ComplicationToggleReceiver.getPreferenceKey(thisProvider, complicationId),
                        1);

        ComplicationData data = null;
        Context applicationContext=getApplicationContext();

        Intent intent=new Intent(applicationContext, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent activity=PendingIntent.getActivity(applicationContext,complicationId,intent,PendingIntent.FLAG_UPDATE_CURRENT);







       /* switch (state % 4) {
            case 0:
                data =
                        new ComplicationData.Builder(type)
                                .setShortText(
                                        ComplicationText.plainText(
                                                "2000ml"))

                                .setTapAction(complicationTogglePendingIntent)
                                .build();
                break;
            case 1:*/
       if(state==1) {
           data =
                   new ComplicationData.Builder(type)
                           .setShortText(
                                   ComplicationText.plainText(
                                           water.getHaveDrunk()))
                           .setIcon(Icon.createWithResource(this, R.drawable.logo_bar))
                           .setTapAction(activity)
                           .build();
           /*     break;
            case 2:
                data =
                        new ComplicationData.Builder(type)
                                .setShortText(
                                        ComplicationText.plainText(
                                                "1000"))
                                .setShortTitle(
                                        ComplicationText.plainText(getString(R.string.short_title)))
                                .setTapAction(complicationTogglePendingIntent)
                                .build();
                break;
            case 3:
                // When short text includes both short title and icon, the watch face should only
                // display one of those fields.
                data =
                        new ComplicationData.Builder(type)
                                .setShortText(
                                        ComplicationText.plainText(
                                                "2000"))
                                .setShortTitle(
                                        ComplicationText.plainText(getString(R.string.short_title)))
                                .setIcon(
                                       null)
                                .setTapAction(complicationTogglePendingIntent)
                                .build();
                break;
        }*/
       }
        manager.updateComplicationData(complicationId, data);
    }
}