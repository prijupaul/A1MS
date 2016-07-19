package uk.com.a1ms.util;

import android.Manifest;
import android.app.Activity;
import android.os.Build;

import uk.com.a1ms.A1MSApplication;
import uk.com.a1ms.BuildConfig;

/**
 * Created by priju.jacobpaul on 24/05/16.
 */
public class BuildUtils {

    /**
     * Uses static final constants to detect if the device's platform version is Gingerbread or
     * later.
     */
    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    /**
     * Uses static final constants to detect if the device's platform version is Honeycomb or
     * later.
     */
    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    /**
     * Uses static final constants to detect if the device's platform version is Honeycomb MR1 or
     * later.
     */
    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }

    /**
     * Uses static final constants to detect if the device's platform version is ICS or
     * later.
     */
    public static boolean hasICS() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }

    public static boolean isVersionLesserThanM(){
        return Build.VERSION.SDK_INT <  Build.VERSION_CODES.M;
    }

    public static int getMaxPhoneNumberDigits(){
        return 9;
    }

    public static boolean isDbOnSDCard(){

        Activity activity = ((A1MSApplication)A1MSApplication.applicationContext).getCurrentActivity();
        if(activity != null) {
            if ((BuildConfig.DEBUG) &&
                    (PermissionRequestManager.checkPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE))){
                return true;
            }
        }
        return false;
    }

    public static int getMaxGroupsMemberSize(){
        return 100;
    }
}
