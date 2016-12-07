package uk.com.a1ms.messages;

import uk.com.a1ms.A1MSApplication;
import uk.com.a1ms.db.A1MSMessageFieldsDataSource;
import uk.com.a1ms.dto.Message;
import uk.com.a1ms.ui.uiutil.NotificationBuilder;
import uk.com.a1ms.util.NotificationController;

import java.util.HashMap;

/**
 * Created by priju.jacobpaul on 16/10/2016.
 */

public class MessageNotificationHandler implements NotificationController.NotificationListener {

    public interface MessageNotificationHandlerListener {
        boolean onNewMessageReceived(String type, Message message);
    }

    public static final int PRIORITY_HIGH = 100;
    public static final int PRIORITY_MEDIUM = 50;
    public static final int PRIORITY_LOW = 20;

    private final int priorityArray[] = {
       PRIORITY_LOW,
       PRIORITY_MEDIUM,
       PRIORITY_HIGH
    };

    private static MessageNotificationHandler messageNotificationHandler;
    private HashMap<Integer, Object> priorityMap = new HashMap<>();


    private MessageNotificationHandler() {

        NotificationController.getInstance().addObserver(this,
                NotificationController.messageReceivedFromServer);
    }

    public static MessageNotificationHandler getInstance() {

        if (messageNotificationHandler == null) {
            messageNotificationHandler = new MessageNotificationHandler();
        }

        return messageNotificationHandler;

    }

    public void detachNotification() {

        NotificationController.getInstance().removeObserver(this,
                NotificationController.messageReceivedFromServer);
    }

    @Override
    public void onNotificationReceived(int id, Object... args) {
        if (id == NotificationController.messageReceivedFromServer) {
            String messageType = (String) args[0];
            Message message = (Message) args[1];
            int priorityArrayIndex = priorityArray.length - 1;

            boolean handled = false;
            while (priorityArrayIndex >= 0) {
                MessageNotificationHandlerListener listener = (MessageNotificationHandlerListener) getNextObjectToHandle(priorityArray[priorityArrayIndex]);
                if (listener != null) {
                    if (listener.onNewMessageReceived(messageType, message)) {
                        handled = true;
                        break;
                    }
                }
                --priorityArrayIndex;
            }

            if(!handled){
                NotificationBuilder.showNotification(A1MSApplication.applicationContext, message.getMessage().getLongMessage().toString());

                // Add to the message database
                A1MSMessageFieldsDataSource dataSource = new A1MSMessageFieldsDataSource(A1MSApplication.applicationContext);
                dataSource.insertMessage(A1MSApplication.getMessagesSqLiteDb(),message,true,messageType.equalsIgnoreCase("groupMessage"),false);
            }

        }
    }

    public void registerForEvents(int priority, Object object) {
        priorityMap.put(priority, object);
    }

    public Object getNextObjectToHandle(int priority) {
        return priorityMap.get(priority);
    }

    public void unregisterForEvents(int priority){
        priorityMap.remove(priority);
    }

}
