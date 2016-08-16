package uk.com.a1ms.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by priju.jacobpaul on 15/08/16.
 */
public class DateTime {

    public static String getTimeInAmPm(){

        Calendar now = Calendar.getInstance();
        DateFormat outputFormat = new SimpleDateFormat("KK:mm a");
        return outputFormat.format(now.getTime());
    }
}
