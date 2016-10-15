package uk.com.a1ms.messages;

import org.json.JSONObject;

/**
 * Created by priju.jacobpaul on 12/10/2016.
 */

public interface MessageListerner {

    enum MESSAGETYPE{

        MESSAGE_ECHO,
        MESSAGE_PRIVATE,
        MESSAGE_GROUP

    }

    void sendMessage(MESSAGETYPE messagetype, JSONObject jsonObject);
}
