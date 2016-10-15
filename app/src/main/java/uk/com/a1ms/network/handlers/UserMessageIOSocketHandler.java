package uk.com.a1ms.network.handlers;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import uk.com.a1ms.A1MSApplication;
import uk.com.a1ms.util.ExecutorUtils;
import uk.com.a1ms.util.SharedPreferenceManager;

//import com.github.nkzawa.emitter.Emitter;
//import com.github.nkzawa.socketio.client.IO;
//import com.github.nkzawa.socketio.client.Socket;

/**
 * Created by priju.jacobpaul on 3/08/16.
 */
public class UserMessageIOSocketHandler {


    private static Socket mSocket;
    private UserMessageIOSocketListener listener;

    private String TAG = UserMessageIOSocketHandler.class.getSimpleName();

    private final String AUTHENTICATION = "authentication";
    private final String AUTHENTICATION_REPLY = "authStatus";

    private final String ECHOMESSAGE = "echoMessage";
    private final String ECHOMESSAGE_REPLY = "eresMessage";

    private final String PRIVATEMESSAGE = "privateMessage";
    private final String PRIVATEMESSAGE_REPLY = "presMessage";

    private final String GROUPMESSAGE = "groupMessage";
    private final String GROUPMESSAGE_REPLY = "grespMessage";

    private final String USERS = "users";


    public interface UserMessageIOSocketListener {
        void onMessageReceived(String message,String shortMessage);
    }

    public UserMessageIOSocketHandler(UserMessageIOSocketListener listener) {

        this.listener = listener;

    }

    public void connect() {
        try {

            IO.Options opts = new IO.Options();
            opts.query = "token=" + SharedPreferenceManager.getUserToken(A1MSApplication.applicationContext);
            mSocket = IO.socket("http://chat.voltric.io:8181",opts);//http://163.172.137.155:8181"); //"http://chat.socket.io");

            mSocket.on(Socket.EVENT_CONNECT,onConnected);
            mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
            mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
            mSocket.on(AUTHENTICATION_REPLY,onAuthenticated);
            mSocket.on(ECHOMESSAGE_REPLY,onNewMessage);
            mSocket.on(PRIVATEMESSAGE_REPLY,onNewPrivateMessage);
            mSocket.on(GROUPMESSAGE_REPLY,onGroupMessage);
            mSocket = mSocket.connect();


        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

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

    public void sendMessage(final String message) {

        Log.d(TAG,"sendMessage..: " + mSocket.connected());

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
            mSocket.off(ECHOMESSAGE_REPLY,onNewMessage);
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

        }
    };


    private Emitter.Listener onNewMessage = new Emitter.Listener() {
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

                Log.d(TAG, "onTextMessage" + " " + data);
                if (listener != null) {
                    listener.onMessageReceived(data,data);
                }
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
                if (listener != null) {
                    listener.onMessageReceived(data,data);
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

    };

}
