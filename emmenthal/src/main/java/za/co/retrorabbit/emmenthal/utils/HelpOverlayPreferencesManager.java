package za.co.retrorabbit.emmenthal.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Werner Scheffer on 14/10/16.
 */
public class HelpOverlayPreferencesManager {

    private static final String PREFERENCES_NAME = "emmenthal_preferences";

    private SharedPreferences sharedPreferences;

    public HelpOverlayPreferencesManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public boolean shouldDisplay(String id) {
        return sharedPreferences.getBoolean(id, true);
    }

    public void setDisplayed(String id) {
        sharedPreferences.edit().putBoolean(id, false).apply();
    }

    public void reset(String id) {
        sharedPreferences.edit().putBoolean(id, false).apply();
    }

    public void resetAll() {
        sharedPreferences.edit().clear().apply();
    }

    public static void resetAll(Context context) {
        context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE).edit().clear().apply();
    }


}
