package uk.com.a1ms;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;

import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

import uk.com.a1ms.db.A1MSDbHelper;
import uk.com.a1ms.db.dto.A1MSUser;
import uk.com.a1ms.services.ServiceConnection;
import uk.com.a1ms.util.SharedPreferenceManager;

/**
 * Created by priju.jacobpaul on 26/05/16.
 */
public class A1MSApplication extends Application {

    private Activity mActivity;
    public static Context applicationContext;
    public static volatile Handler applicationHandler;
    public static A1MSDbHelper dbHelper;
    private static SQLiteDatabase sqLiteDatabase;
    public static ServiceConnection mServiceConnection;
    private static A1MSUser mCurrentActiveUser;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = getApplicationContext();
        applicationHandler = new Handler(getMainLooper());
        mServiceConnection = new uk.com.a1ms.services.ServiceConnection();
        SharedPreferenceManager.setFilePath(this);

        if(BuildConfig.DEBUG) {
            Logger.init("A1MS")
                    .methodCount(3)
                    .hideThreadInfo()
                    .logLevel(LogLevel.FULL)        // default LogLevel.FULL
                    .methodOffset(2);
        }
        else {
            Logger.init().logLevel(LogLevel.NONE);
        }

        dbHelper = A1MSDbHelper.getInstance(applicationContext);
        sqLiteDatabase = dbHelper.getWritableDatabase();

        mServiceConnection.doBindService();
    }


    public void setCurrentActivity(Activity activity){
        this.mActivity = activity;
    }

    public Activity getCurrentActivity(){
        return this.mActivity;
    }

    public static A1MSDbHelper getDatabaseAdapter()
    {
        return dbHelper;
    }

    public static SQLiteDatabase getSqLiteDatabase(){
        return sqLiteDatabase;
    }

    public static ServiceConnection getServiceConnection() { return  mServiceConnection; }

    public static A1MSUser getCurrentActiveUser() {
        return mCurrentActiveUser;
    }

    public static void setCurrentActiveUser(A1MSUser currentActiveUser) {
        mCurrentActiveUser = currentActiveUser;
    }
}
