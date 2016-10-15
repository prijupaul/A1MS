package uk.com.a1ms.services;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import uk.com.a1ms.A1MSApplication;
import uk.com.a1ms.R;
import uk.com.a1ms.messages.MessageImpl;
import uk.com.a1ms.util.ExecutorUtils;
import uk.com.a1ms.util.SharedPreferenceManager;

/**
 * Created by priju.jacobpaul on 6/10/2016.
 */

public class MessagingService extends Service {

    private static Socket mSocket;
    private final String TAG = MessagingService.class.getSimpleName();

    private final String AUTHENTICATION = "authentication";
    private final String AUTHENTICATION_REPLY = "authStatus";

    private final String ECHOMESSAGE = "echoMessage";
    private final String ECHOMESSAGE_REPLY = "eresMessage";

    private final String PRIVATEMESSAGE = "privateMessage";
    private final String PRIVATEMESSAGE_REPLY = "presMessage";

    private final String GROUPMESSAGE = "groupMessage";
    private final String GROUPMESSAGE_REPLY = "grespMessage";

    private final String USERS = "users";

    public static final int MSG_SEND_PRIVATE_MESSAGE = 1;
    public static final int MSG_SEND_GROUP_MESSAGE = 2;
    public static final int MSG_SEND_ECHO_MESSAGE = 3;
    public static final int MSG_FROM_SERVER_PRIVATE_REPLY = 4;
    public static final int MSG_FROM_SERVER_GROUP_REPLY = 5;
    public static final int MSG_FROM_SERVER_ECHO_REPLY = 6;

    private final Messenger mMessenger = new Messenger(new IncomingMessageHandler());
    private Messenger mUIMessenger = new Messenger(new MessageImpl.IncomingMessageHandler());
    private static boolean isRunning = false;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return Service.START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if(getBaseContext().checkCallingOrSelfPermission(getString(R.string.message_permission)) == PackageManager.PERMISSION_GRANTED){
            isRunning = true;
            connectToWebSocket();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        disconnect();
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
    }


    private void connectToWebSocket(){
        ExecutorUtils.runInBackgroundThread(new Runnable() {
            @Override
            public void run() {
                try {

                    IO.Options opts = new IO.Options();
                    opts.query = "token=" + SharedPreferenceManager.getUserToken(A1MSApplication.applicationContext);
                    mSocket = IO.socket("http://chat.voltric.io:8181",opts);//http://163.172.137.155:8181"); //"http://chat.socket.io");

                    mSocket.on(Socket.EVENT_CONNECT,onConnected);
                    mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
                    mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
                    mSocket.on(AUTHENTICATION_REPLY,onAuthenticated);
                    mSocket.on(ECHOMESSAGE_REPLY, onNewEchoMessage);
                    mSocket.on(PRIVATEMESSAGE_REPLY,onNewPrivateMessage);
                    mSocket.on(GROUPMESSAGE_REPLY,onGroupMessage);
                    mSocket = mSocket.connect();


                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void sendEchoMessage(final String message) {

        Log.d(TAG,"sendEchoMessage..: " + mSocket.connected());

        ExecutorUtils.runInBackgroundThread(new Runnable() {
            @Override
            public void run() {
                if ( mSocket != null) {
                    Log.d(TAG,message);
                    mSocket.emit(ECHOMESSAGE, message);
                }
            }
        });

    }

    public void sendPrivateMessage(final String message) {

        Log.d(TAG,"sendPrivateMessage..: " + mSocket.connected());

        ExecutorUtils.runInBackgroundThread(new Runnable() {
            @Override
            public void run() {
                if ( mSocket != null) {
                    Log.d(TAG,message);
                    mSocket.emit(PRIVATEMESSAGE, message);
                }
            }
        });

    }

    public void sendGroupMessage(final String message) {

        Log.d(TAG,"sendGroupMessage..: " + mSocket.connected());

        ExecutorUtils.runInBackgroundThread(new Runnable() {
            @Override
            public void run() {
                if ( mSocket != null) {
                    Log.d(TAG,message);
                    mSocket.emit(GROUPMESSAGE, message);
                }
            }
        });

    }

    public void disconnect() {

        if(mSocket != null) {

            mSocket.disconnect();

            mSocket.off(Socket.EVENT_CONNECT,onConnected);
            mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
            mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
            mSocket.off(AUTHENTICATION_REPLY,onAuthenticated);
            mSocket.off(ECHOMESSAGE_REPLY, onNewEchoMessage);
            mSocket.off(PRIVATEMESSAGE_REPLY,onNewPrivateMessage);
            mSocket.off(GROUPMESSAGE_REPLY,onGroupMessage);
        }

    }

    private Emitter.Listener onConnected = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            Log.d(TAG,"Connected..: " + mSocket.connected());

        }
    };

    private Emitter.Listener onAuthenticated = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d(TAG,"onAuthenticated");

        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            ExecutorUtils.runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(A1MSApplication.applicationContext,
                            "error", Toast.LENGTH_LONG).show();
                }
            },0);
        }
    };


    private Emitter.Listener onGroupMessage = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            try {
                String data = "";
                Object object = args[0];
                if (object instanceof JSONObject) {
                    data = object.toString();
                } else if (object instanceof String) {
                    data = (String) object;
                }

                Log.d(TAG, "onTextGroupMessage" + " " + data);
//                if (listener != null) {
//                    listener.onMessageReceived(data);
//                }
                sendMessageToUI(MSG_FROM_SERVER_GROUP_REPLY,data);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    };


    private Emitter.Listener onNewEchoMessage = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            try {
                String data = "";
                Object object = args[0];
                if (object instanceof JSONObject) {
                    data = object.toString();
                } else if (object instanceof String) {
                    data = (String) object;
                }

                Log.d(TAG, "onTextEchoMessage" + " " + data);
//                if (listener != null) {
//                    listener.onMessageReceived(data);
//                }
                sendMessageToUI(MSG_FROM_SERVER_ECHO_REPLY,data);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

    };


    private Emitter.Listener onNewPrivateMessage = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            Log.d(TAG, "onNewPrivateMessage");

            try {
                String data = "";
                Object object = args[0];
                if (object instanceof JSONObject) {
                    data = object.toString();
                } else if (object instanceof String) {
                    data = (String) object;
                }

                Log.d(TAG, "onNewPrivateMessage" + " " + data);
//                if (listener != null) {
//                    listener.onMessageReceived(data);
//                }
                sendMessageToUI(MSG_FROM_SERVER_PRIVATE_REPLY,data);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

    };

    private void sendMessageToUI(int type, String message){

        try {
            Bundle bundle = new Bundle();
            bundle.putString("message", message);
            Message msg = Message.obtain(null, MSG_FROM_SERVER_PRIVATE_REPLY);
            msg.what = type;
            msg.setData(bundle);
            mUIMessenger.send(msg);
        }
        catch (RemoteException e){
            e.printStackTrace();
        }

    }

    private void sendBroadcast(String data){
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(A1MSApplication.applicationContext);
        Intent intent = new Intent();
        intent.putExtra("data",data);
        localBroadcastManager.sendBroadcast(intent);
    }

    public static boolean isRunning(){
        return isRunning;
    }

    private class IncomingMessageHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG,"handleMessage: " + msg.what );

            switch (msg.what){
                case MSG_SEND_ECHO_MESSAGE:{
                    String message = msg.getData().getString("message");
//                    mUIMessenger = msg.replyTo;
                    sendEchoMessage(message);
                    break;
                }
                case MSG_SEND_GROUP_MESSAGE:{
                    String message = msg.getData().getString("message");
//                    mUIMessenger = msg.replyTo;
                    sendGroupMessage(message);
                    break;
                }
                case MSG_SEND_PRIVATE_MESSAGE:{
                    String message = msg.getData().getString("message");
//                    mUIMessenger = msg.replyTo;
                    sendPrivateMessage(message);
                    break;
                }
                default:{
                    super.handleMessage(msg);
                }
            }
        }
    }


}
