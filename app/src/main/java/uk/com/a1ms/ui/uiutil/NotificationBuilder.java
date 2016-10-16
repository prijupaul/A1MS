package uk.com.a1ms.ui.uiutil;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.NotificationCompat;

import uk.com.a1ms.R;
import uk.com.a1ms.ui.ContactsGroupsActivity;

/**
 * Created by priju.jacobpaul on 16/10/2016.
 */

public class NotificationBuilder {

    public static void showNotification(Context context,String message){

        Intent resultIntent = new Intent(context, ContactsGroupsActivity.class);
        android.support.v4.app.TaskStackBuilder stackBuilder = android.support.v4.app.TaskStackBuilder.create(context);
        stackBuilder.addParentStack(ContactsGroupsActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.a1ms_logo)
                        .setContentTitle(context.getString(R.string.new_message))
                        .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                        .setContentIntent(pendingIntent)
                        .setContentText(message);

        Notification notification = mBuilder.build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(001,notification);
    }
}
