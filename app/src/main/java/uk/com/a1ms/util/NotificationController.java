package uk.com.a1ms.util;

import android.util.SparseArray;

import java.util.ArrayList;

import uk.com.a1ms.A1MSApplication;
import uk.com.a1ms.BuildConfig;

/**
 * Created by priju.jacobpaul on 14/06/16.
 */
public class NotificationController {

    private static int totalEvents = 1;

    public static final int contactsDidLoaded = totalEvents++;
    public static final int didReceiveSmsCode = totalEvents++;
    public static final int didReceiveLocation = totalEvents++;
    public static final int didOpenDatabase = totalEvents++;


    private int broadcasting = 0;

    private SparseArray<ArrayList<Object>> observers = new SparseArray<>();
    private SparseArray<ArrayList<Object>> removeAfterBroadcast = new SparseArray<>();
    private SparseArray<ArrayList<Object>> addAfterBroadcast = new SparseArray<>();

    public interface NotificationListener {
        void onNotificationReceived(int id, Object... args);
    }

    private static volatile NotificationController Instance = null;

    public static NotificationController getInstance() {
        NotificationController localInstance = Instance;
        if (localInstance == null) {
            synchronized (NotificationController.class) {
                localInstance = Instance;
                if (localInstance == null) {
                    Instance = localInstance = new NotificationController();
                }
            }
        }
        return localInstance;
    }

    public void addObserver(Object observer, int id) {

        if (BuildConfig.DEBUG) {
            if (Thread.currentThread() != A1MSApplication.applicationHandler.getLooper().getThread()) {
                throw new RuntimeException("addObserver allowed only from MAIN thread");
            }
        }

        if (broadcasting != 0) {
            ArrayList<Object> arrayList = addAfterBroadcast.get(id);
            if (arrayList == null) {
                arrayList = new ArrayList<>();
                addAfterBroadcast.put(id, arrayList);
            }
            arrayList.add(observer);
            return;
        }

        ArrayList<Object> objects = observers.get(id);
        if (objects == null) {
            observers.put(id, (objects = new ArrayList<>()));
        }
        if (objects.contains(observer)) {
            return;
        }
        objects.add(observer);
    }

    public void removeObserver(Object observer, int id) {
        if (BuildConfig.DEBUG) {
            if (Thread.currentThread() != A1MSApplication.applicationHandler.getLooper().getThread()) {
                throw new RuntimeException("removeObserver allowed only from MAIN thread");
            }
        }
        if (broadcasting != 0) {
            ArrayList<Object> arrayList = removeAfterBroadcast.get(id);
            if (arrayList == null) {
                arrayList = new ArrayList<>();
                removeAfterBroadcast.put(id, arrayList);
            }
            arrayList.add(observer);
            return;
        }
        ArrayList<Object> objects = observers.get(id);
        if (objects != null) {
            objects.remove(observer);
        }
    }

    public void postNotificationName(int id, Object... args) {

        if (BuildConfig.DEBUG) {
            if (Thread.currentThread() != A1MSApplication.applicationHandler.getLooper().getThread()) {
                throw new RuntimeException("postNotificationName allowed only from MAIN thread");
            }
        }

        broadcasting++;
        ArrayList<Object> objects = observers.get(id);
        if (objects != null && !objects.isEmpty()) {
            for (int a = 0; a < objects.size(); a++) {
                Object obj = objects.get(a);
                ((NotificationListener) obj).onNotificationReceived(id, args);
            }
        }
        broadcasting--;
        if (broadcasting == 0) {
            if (removeAfterBroadcast.size() != 0) {
                for (int a = 0; a < removeAfterBroadcast.size(); a++) {
                    int key = removeAfterBroadcast.keyAt(a);
                    ArrayList<Object> arrayList = removeAfterBroadcast.get(key);
                    for (int b = 0; b < arrayList.size(); b++) {
                        removeObserver(arrayList.get(b), key);
                    }
                }
                removeAfterBroadcast.clear();
            }
            if (addAfterBroadcast.size() != 0) {
                for (int a = 0; a < addAfterBroadcast.size(); a++) {
                    int key = addAfterBroadcast.keyAt(a);
                    ArrayList<Object> arrayList = addAfterBroadcast.get(key);
                    for (int b = 0; b < arrayList.size(); b++) {
                        addObserver(arrayList.get(b), key);
                    }
                }
                addAfterBroadcast.clear();
            }
        }
    }
}
