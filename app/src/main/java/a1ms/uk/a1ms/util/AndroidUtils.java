package a1ms.uk.a1ms.util;

import a1ms.uk.a1ms.A1MSApplication;

/**
 * Created by priju.jacobpaul on 14/06/16.
 */
public class AndroidUtils {

    public static void runOnUIThread(Runnable runnable) {
        runOnUIThread(runnable, 0);
    }

    public static void runOnUIThread(Runnable runnable, long delay) {
        if (delay == 0) {
            A1MSApplication.applicationHandler.post(runnable);
        } else {
            A1MSApplication.applicationHandler.postDelayed(runnable, delay);
        }
    }

    public static void cancelRunOnUIThread(Runnable runnable) {
        A1MSApplication.applicationHandler.removeCallbacks(runnable);
    }
}
