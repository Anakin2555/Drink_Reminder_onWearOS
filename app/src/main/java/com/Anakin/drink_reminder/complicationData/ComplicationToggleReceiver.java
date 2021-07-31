package com.Anakin.drink_reminder.complicationData;

import android.app.PendingIntent;
        import android.content.BroadcastReceiver;
        import android.content.ComponentName;
        import android.content.Context;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.os.Bundle;
import android.os.Parcelable;
import android.support.wearable.complications.ProviderUpdateRequester;

import com.Anakin.drink_reminder.MainActivity;
import com.Anakin.drink_reminder.welcome;

/** Receives intents on tap and causes complication states to be toggled and updated. */
public class ComplicationToggleReceiver extends BroadcastReceiver {
    private static final String EXTRA_PROVIDER_COMPONENT = "providerComponent";
    private static final String EXTRA_COMPLICATION_ID = "complicationId";

    static final String PREFERENCES_NAME = "ComplicationTestSuite";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        ComponentName provider = extras.getParcelable(EXTRA_PROVIDER_COMPONENT);
        int complicationId = extras.getInt(EXTRA_COMPLICATION_ID);

        String preferenceKey = getPreferenceKey(provider, complicationId);
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(preferenceKey, 1); // Increase value by 1
        editor.apply();


        // Request an update for the complication that has just been toggled.
        ProviderUpdateRequester requester = new ProviderUpdateRequester(context, provider);
        requester.requestUpdate(complicationId);


    }

    /**
     * Returns a pending intent, suitable for use as a tap intent, that causes a complication to be
     * toggled and updated.
     */
    static PendingIntent getToggleIntent(
            Context context, ComponentName provider, int complicationId) {
        Intent intent = new Intent(context, ComplicationToggleReceiver.class);
        intent.putExtra(EXTRA_PROVIDER_COMPONENT, provider);
        intent.putExtra(EXTRA_COMPLICATION_ID, complicationId);
        return PendingIntent.getBroadcast(
                context, complicationId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * Returns the key for the shared preference used to hold the current state of a given
     * complication.
     */
    static String getPreferenceKey(ComponentName provider, int complicationId) {
        return provider.getClassName() + complicationId;
    }
}
