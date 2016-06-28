package uk.com.a1ms;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;

import uk.com.a1ms.util.SharedPreferenceManager;

/**
 * Created by priju.jacobpaul on 26/05/16.
 */
public class A1MSApplication extends Application {

    private Activity mActivity;
    public static Context applicationContext;
    public static volatile Handler applicationHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = getApplicationContext();
        applicationHandler = new Handler(getMainLooper());
        SharedPreferenceManager.setFilePath(this);
    }


    public void setCurrentActivity(Activity activity){
        this.mActivity = activity;
    }

    public Activity getCurrentActivity(){
        return this.mActivity;
    }

}
