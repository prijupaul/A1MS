package uk.com.a1ms.messages.fcm;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import android.util.Log;

import uk.com.a1ms.util.SharedPreferenceManager;

/**
 * Created by priju.jacobpaul on 16/08/16.
 */
public class FCMInstanceIDService extends FirebaseInstanceIdService {

    private final String TAG = FCMInstanceIDService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        // sendRegistrationToServer(refreshedToken);

        // TODO: /user/:id
        // Do a put on this end point with the token
        // the token id should be "fcmPushId"

        SharedPreferenceManager.savePushNotificationToken(refreshedToken,getApplicationContext());
    }
}
