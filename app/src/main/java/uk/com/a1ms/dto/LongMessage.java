package uk.com.a1ms.dto;

import android.text.SpannableString;

import java.util.ArrayList;

/**
 * Created by priju.jacobpaul on 31/07/16.
 */
public class LongMessage {

    SpannableString longMessage;
    ArrayList<MessageDetails> messageDetails = new ArrayList<>();

    public SpannableString getLongMessage() {
        return longMessage;
    }

    public void setLongMessage(SpannableString longMessage) {
        this.longMessage = longMessage;
    }

    public ArrayList<MessageDetails> getMessageDetails() {
        return messageDetails;
    }

    public void setMessageDetails(ArrayList<MessageDetails> messageDetails) {
        this.messageDetails = messageDetails;
    }
}
