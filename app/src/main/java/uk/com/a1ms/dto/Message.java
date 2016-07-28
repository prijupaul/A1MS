package uk.com.a1ms.dto;

import uk.com.a1ms.db.dto.A1MSUser;

/**
 * Created by priju.jacobpaul on 29/07/16.
 */
public class Message {

    private int messageId;
    private String shortMessage;
    private String message;
    private A1MSUser idToUser;
    private A1MSUser idUser;
    private boolean isSelf;

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public String getShortMessage() {
        return shortMessage;
    }

    public void setShortMessage(String shortMessage) {
        this.shortMessage = shortMessage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
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
}
