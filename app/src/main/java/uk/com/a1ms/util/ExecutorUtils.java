package uk.com.a1ms.util;

import android.os.Handler;
import android.os.Process;

import uk.com.a1ms.A1MSApplication;

/**
 * Created by priju.jacobpaul on 21/07/16.
 */
public class ExecutorUtils {

    static Handler handler = new Handler(A1MSApplication.applicationContext.getMainLooper());

    public static void runOnUIThread(Runnable runnable,long delay){
        if(delay == 0) {
         handler.post(runnable);
        }
        else {
            handler.postDelayed(runnable, delay);
        }
    }

    public static void cancelRunOnUIThread(Runnable runnable){
        handler.removeCallbacks(runnable);
    }

    public static void runInBackgroundThread(final Runnable runnable){

        new Thread(runnable){
            @Override
            public void run() {
                android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                runnable.run();
            }
        }.start();
    }

    public static void runInBackgroundThreadWithDelay(final Runnable runnable,final int delay){

        new Thread(runnable){
            @Override
            public void run() {
                try {
                    Thread.sleep(delay);
                    android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                    runnable.run();
                }
                catch (InterruptedException e){

                }
            }
        }.start();
    }
}
