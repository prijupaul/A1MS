package a1ms.uk.a1ms.util;

import a1ms.uk.a1ms.A1MSApplication;

/**
 * Created by priju.jacobpaul on 14/06/16.
 */
public class AndroidUtils {

    private static final Object smsLock = new Object();
    private static boolean waitingForSms = false;

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

    public static boolean isWaitingForSms() {
        boolean value;
        synchronized (smsLock) {
            value = waitingForSms;
        }
        return value;
    }

    public static void setWaitingForSms(boolean value) {
        synchronized (smsLock) {
            waitingForSms = value;
        }
    }
}
