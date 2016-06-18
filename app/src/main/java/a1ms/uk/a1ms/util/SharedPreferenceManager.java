package a1ms.uk.a1ms.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by priju.jacobpaul on 17/06/16.
 */
public class SharedPreferenceManager {

    private static String mAuthTokenKey;
    private static String mFirstTimeLaunch;
    private static String mIsUserRegistered;
    private static String mFile;

    private static String TAG = SharedPreferenceManager.class.getName();

    public static void setFilePath(Context context) {

        mFile = context.getPackageName();
        mAuthTokenKey = mFile + "." + "authToken";
        mFirstTimeLaunch = mFile + "." + "firstTimeLaunch";
        mIsUserRegistered = mFile + "." + "isUserRegistered";

    }

    public static void saveAuthToken(String authToken, Context context, String keyName) {
        saveStringValue(mAuthTokenKey + keyName, authToken, context);
    }

    public static String getAuthToken(Context context, String keyName) {
        return getStringValue(mAuthTokenKey + keyName, context, "");
    }

    public static void setUserRegistered(boolean userRegistered,Context context){
        saveBooleanValue(mIsUserRegistered,userRegistered, context);
    }

    public static boolean isUserRegistered(Context context){
        return getBooleanValue(mIsUserRegistered,context,false);
    }

    public static void setFirstTimeLaunch(boolean firstTimeLaunch,Context context){
        saveBooleanValue(mFirstTimeLaunch,firstTimeLaunch,context);
    }

    public static boolean isFirstTimeLaunch(Context context){
        return getBooleanValue(mFirstTimeLaunch,context,true);
    }

    private static void saveStringValue(String key, String value, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(mFile, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    private static String getStringValue(String key, Context context, String defaultValue) {
        SharedPreferences preferences = context.getSharedPreferences(mFile, Context.MODE_PRIVATE);
        return preferences.getString(key, defaultValue);
    }

    private static void saveIntValue(String key, int value, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(mFile, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    private static int getIntValue(String key, Context context, int defaultValue) {
        SharedPreferences preferences = context.getSharedPreferences(mFile, Context.MODE_PRIVATE);
        return preferences.getInt(key, defaultValue);
    }

    private static void saveLongValue(String key, long value, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(mFile, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    private static long getLongValue(String key, Context context, long defaultValue) {
        SharedPreferences preferences = context.getSharedPreferences(mFile, Context.MODE_PRIVATE);
        return preferences.getLong(key, defaultValue);
    }

    private static void saveBooleanValue(String key, boolean value, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(mFile, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    private static boolean getBooleanValue(String key, Context context, boolean defaultValue) {
        SharedPreferences preferences = context.getSharedPreferences(mFile, Context.MODE_PRIVATE);
        return preferences.getBoolean(key, defaultValue);
    }
}
