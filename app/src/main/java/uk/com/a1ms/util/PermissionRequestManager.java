package uk.com.a1ms.util;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import uk.com.a1ms.R;
import uk.com.a1ms.dialogutil.DialogCallBackListener;
import uk.com.a1ms.dialogutil.DialogUtil;

/**
 * Created by priju.jacobpaul on 26/05/16.
 */
public class PermissionRequestManager {


    public static boolean checkPermission(Activity activity,String permission){

        if(BuildUtils.isVersionLesserThanM()) {
            return true;
        }

        int permissionStatus = ContextCompat.checkSelfPermission(activity,permission);
        return  (permissionStatus == PackageManager.PERMISSION_GRANTED) ? true : false;
    }

    public static void requestPermission(final Activity activity,String[] permissions, final int code){
        if(BuildUtils.isVersionLesserThanM()) {
            return;
        }
        ActivityCompat.requestPermissions(activity, permissions,code);

    }

    public static void checkAndRequestPermission(final Activity activity, final String permission,final int code){

        if(BuildUtils.isVersionLesserThanM()) {
            return;
        }

        if(!checkPermission(activity,permission)){
            if(ActivityCompat.shouldShowRequestPermissionRationale(activity,permission)){
                DialogUtil.showOKDialog(activity,
                        activity.getString(R.string.permission_title),
                        activity.getString(R.string.permission_message_contacts),
                        activity.getString(android.R.string.ok),
                        new DialogCallBackListener() {
                            @Override
                            public void run() {
                                ActivityCompat.requestPermissions(activity,
                                        new String[]{permission},
                                        code);
                                return;
                            }
                        },false);

            }
            else {
                ActivityCompat.requestPermissions(activity, new String[]{permission},code);
            }
        }
    }
}
