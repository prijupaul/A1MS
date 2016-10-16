package uk.com.a1ms.messages;

import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import uk.com.a1ms.A1MSApplication;
import uk.com.a1ms.dto.Message;
import uk.com.a1ms.gsonconvert.MessageConvertor;
import uk.com.a1ms.services.MessagingService;
import uk.com.a1ms.services.ServiceConnection;
import uk.com.a1ms.util.NotificationController;

/**
 * Created by priju.jacobpaul on 12/10/2016.
 */

public class MessageImpl implements MessageListerner {

    @Override
    public void sendMessage(MESSAGETYPE messagetype, JSONObject jsonObject) {

        ServiceConnection serviceConnection = A1MSApplication.getServiceConnection();
        if (serviceConnection != null) {

            switch (messagetype) {
                case MESSAGE_ECHO: {
                    serviceConnection.sendEchoMessage(jsonObject.toString());
                    break;
                }
                case MESSAGE_PRIVATE: {
                    serviceConnection.sendPrivateMessage(jsonObject.toString());
                    break;
                }
                case MESSAGE_GROUP: {
                    serviceConnection.sendGroupMessage(jsonObject.toString());
                    break;
                }
                default: {

                }
            }
        }
    }

    /**
     * Handle incoming messages from MessengerService
     */
    public static class IncomingMessageHandler extends Handler {

        private final String TAG = MessageImpl.class.getSimpleName();

        @Override
        public void handleMessage(android.os.Message msg) {
            Log.d(TAG, "IncomingHandler:handleMessage");
            switch (msg.what) {
                case MessagingService.MSG_FROM_SERVER_ECHO_REPLY: {
                    String message = msg.getData().getString("message");
                    NotificationController.getInstance().postNotificationName(NotificationController.messageReceivedFromServer,
                            "echoMessage",convertToMessage(message));
                    break;
                }
                case MessagingService.MSG_FROM_SERVER_PRIVATE_REPLY: {
                    String message = msg.getData().getString("message");
                    NotificationController.getInstance().postNotificationName(NotificationController.messageReceivedFromServer,
                            "privateMessage",convertToMessage(message));
                    break;
                }
                case MessagingService.MSG_FROM_SERVER_GROUP_REPLY: {
                    String message = msg.getData().getString("message");
                    NotificationController.getInstance().postNotificationName(NotificationController.messageReceivedFromServer,
                            "groupMessage",convertToMessage(message));
                    break;
                }
                default:
                    super.handleMessage(msg);
            }
        }

        private Message convertToMessage(String message){

            Gson gson = new GsonBuilder().registerTypeAdapter(Message.class, new MessageConvertor()).create();
            Message msgObj = gson.fromJson(message,Message.class);
            return msgObj;
        }
    }
}
