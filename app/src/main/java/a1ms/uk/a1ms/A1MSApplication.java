package a1ms.uk.a1ms;

import android.app.Activity;
import android.app.Application;

/**
 * Created by priju.jacobpaul on 26/05/16.
 */
public class A1MSApplication extends Application {

    private Activity mActivity;

    @Override
    public void onCreate() {
        super.onCreate();
    }


    public void setCurrentActivity(Activity activity){
        this.mActivity = activity;
    }

    public Activity getCurrentActivity(){
        return this.mActivity;
    }
}
