
package uk.com.a1ms.listeners;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import uk.com.a1ms.util.AndroidUtils;
import uk.com.a1ms.util.NotificationController;

public class SmsListener extends BroadcastReceiver {

    private SharedPreferences preferences;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            if (!AndroidUtils.isWaitingForSms()) {
                return;
            }
            Bundle bundle = intent.getExtras();
            SmsMessage[] msgs;
            if (bundle != null) {
                try {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    String wholeString = "";
                    for(int i = 0; i < msgs.length; i++){
                        msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                        wholeString += msgs[i].getMessageBody();
                    }

                    if(!wholeString.contains("a1ms") && !wholeString.contains("A1MS")) {
                        return;
                    }

                    try {
//                        Pattern pattern = Pattern.compile("[0-9]+");
                        Pattern pattern = Pattern.compile("\\d{4}");
                        final Matcher matcher = pattern.matcher(wholeString);
                        if (matcher.find()) {
                            String str = matcher.group(0);
                            if (str.length() >= 3) {
                                AndroidUtils.runOnUIThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        AndroidUtils.setWaitingForSms(false);
                                        NotificationController.getInstance().postNotificationName(NotificationController.didReceiveSmsCode, matcher.group(0));
                                    }
                                });
                            }
                        }
                    } catch (Throwable e) {
//                        FileLog.e("tmessages", e);
                    }

                } catch(Throwable e) {
//                    FileLog.e("tmessages", e);
                }
            }
        }
    }
}
