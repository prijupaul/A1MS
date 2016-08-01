package uk.com.a1ms;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.google.common.collect.ListMultimap;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

import uk.com.a1ms.util.SharedPreferenceManager;

/**
 * Created by priju.jacobpaul on 26/05/16.
 */
public class A1MSApplication extends Application {

    private Activity mActivity;
    public static Context applicationContext;
    public static volatile Handler applicationHandler;
    ListMultimap<String, String> acronymsArray;
    ListMultimap<String, String> sentenceArrays;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = getApplicationContext();
        applicationHandler = new Handler(getMainLooper());
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
    }


    public void setCurrentActivity(Activity activity){
        this.mActivity = activity;
    }

    public Activity getCurrentActivity(){
        return this.mActivity;
    }

    public ListMultimap<String, String> getAcronymsArray() {
        return acronymsArray;
    }

    public void setAcronymsArray(ListMultimap<String, String> acronymsArray) {
        this.acronymsArray = acronymsArray;
    }

    public ListMultimap<String, String> getSentenceArrays() {
        return sentenceArrays;
    }

    public void setSentenceArrays(ListMultimap<String, String> sentenceArrays) {
        this.sentenceArrays = sentenceArrays;
    }
}
