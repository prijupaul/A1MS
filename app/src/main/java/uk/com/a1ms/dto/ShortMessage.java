package uk.com.a1ms.dto;

import android.text.SpannableString;

import java.util.ArrayList;

/**
 * Created by priju.jacobpaul on 31/07/16.
 */
public class ShortMessage {

    SpannableString shortMessage;
    ArrayList<MessageDetails> messageDetails = new ArrayList<>();

    public SpannableString getShortMessage() {
        return shortMessage;
    }

    public void setShortMessage(SpannableString shortMessage) {
        this.shortMessage = shortMessage;
    }

    public ArrayList<MessageDetails> getMessageDetails() {
        return messageDetails;
    }

    public void setMessageDetails(ArrayList<MessageDetails> messageDetails) {
        this.messageDetails = messageDetails;
    }

    public void formatShortString(){

        String shortMsg = shortMessage.toString();
        String[] arraySplitOnSpace = shortMsg.split(" ");
        for (int i=0;i<arraySplitOnSpace.length;i++){

        }
    }
}
