package uk.com.a1ms.dto;

import org.json.JSONException;
import org.json.JSONObject;

import uk.com.a1ms.A1MSApplication;
import uk.com.a1ms.db.dto.A1MSGroup;
import uk.com.a1ms.db.dto.A1MSUser;
import uk.com.a1ms.util.SharedPreferenceManager;

/**
 * Created by priju.jacobpaul on 29/07/16.
 */
public class Message {

    private int messageId;
    private ShortMessage shortMessage;
    private LongMessage message;
    private A1MSUser idToUser;
    private A1MSUser idUser;
    private boolean isSelf;
    private String time;

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public ShortMessage getShortMessage() {
        return shortMessage;
    }

    public void setShortMessage(ShortMessage shortMessage) {
        this.shortMessage = shortMessage;
    }

    public LongMessage getMessage() {
        return message;
    }

    public void setMessage(LongMessage message) {
        this.message = message;
    }

    public A1MSUser getIdToUser() {
        return idToUser;
    }

    public void setIdToUser(A1MSUser idToUser) {
        this.idToUser = idToUser;
    }

    public A1MSUser getIdUser() {
        return idUser;
    }

    public void setIdUser(A1MSUser idUser) {
        this.idUser = idUser;
    }

    public boolean isSelf() {
        return isSelf;
    }

    public void setSelf(boolean self) {
        isSelf = self;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public JSONObject convertToJson(String token, A1MSUser user, A1MSGroup group) {

        try {

//            jsonObject.put("token", token);

            JSONObject messageObject = new JSONObject();
            messageObject.put("longMessage", getMessage().getLongMessage().toString());
            messageObject.put("shortMessage", getShortMessage().getShortMessage().toString());
            messageObject.put("userId", SharedPreferenceManager.getUserId(A1MSApplication.applicationContext));

            if (user.isGroup()) {
                messageObject.put("groupId", group.getGroupId());
                messageObject.put("groupArray", group.getMembersList());
            } else {
                messageObject.put("toUserId", getIdToUser().getUserId());
            }

            return messageObject;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;

    }
}
