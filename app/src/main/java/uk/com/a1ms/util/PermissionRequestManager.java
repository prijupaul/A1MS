package uk.com.a1ms.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import uk.com.a1ms.R;
import uk.com.a1ms.dialogutil.DialogCallBackListener;
import uk.com.a1ms.dialogutil.DialogUtil;

/**
 * Created by priju.jacobpaul on 26/05/16.
 */
public class PermissionRequestManager {




    public static boolean checkPermission(Context context, String permission) {

        if (BuildUtils.isVersionLesserThanM()) {
            return true;
        }

        int permissionStatus = ContextCompat.checkSelfPermission(context, permission);
        return (permissionStatus == PackageManager.PERMISSION_GRANTED) ? true : false;
    }

    public static boolean checkPermissions(Context context, String[] permissions) {

        if (BuildUtils.isVersionLesserThanM()) {
            return true;
        }

        for (String permission : permissions) {
            if (!checkPermission(context, permission)) {
                return false;
            }
        }
        return true;
    }

    public static void requestPermission(final Activity activity, String[] permissions, final int code) {
        if (BuildUtils.isVersionLesserThanM()) {
            return;
        }
        ActivityCompat.requestPermissions(activity, permissions, code);

    }

    public static void checkAndRequestPermissions(final Activity activity, final String[] permissions, final int code) {

        if (BuildUtils.isVersionLesserThanM()) {
            return;
        }

        boolean shouldShowRequestPermission = false;

        for (String permission : permissions) {
            if (!checkPermission(activity, permission)) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                    shouldShowRequestPermission = true;
                    break;
                }
            }
        }

        if (shouldShowRequestPermission) {
            DialogUtil.showOKDialog(activity,
                    activity.getString(R.string.permission_title),
                    activity.getString(R.string.permission_denied_message),
                    activity.getString(android.R.string.ok),
                    new DialogCallBackListener() {
                        @Override
                        public void run() {
                            ActivityCompat.requestPermissions(activity,
                                    permissions,
                                    code);
                            return;
                        }
                    }, false);

        } else {

//                ActivityCompat.requestPermissions(activity, new String[]{permission}, code);
            Toast.makeText(activity, "You could enable permissions from settings app.", Toast.LENGTH_LONG).show();
        }
    }

    public static void checkAndRequestPermission(final Activity activity, final String permission, final int code) {

        if (BuildUtils.isVersionLesserThanM()) {
            return;
        }

        if (!checkPermission(activity, permission)) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                DialogUtil.showOKDialog(activity,
                        activity.getString(R.string.permission_title),
                        activity.getString(R.string.permission_denied_message),
                        activity.getString(android.R.string.ok),
                        new DialogCallBackListener() {
                            @Override
                            public void run() {
                                ActivityCompat.requestPermissions(activity,
                                        new String[]{permission},
                                        code);
                                return;
                            }
                        }, false);

            } else {

//                ActivityCompat.requestPermissions(activity, new String[]{permission}, code);
                Toast.makeText(activity, "You could enable permissions from settings app.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
