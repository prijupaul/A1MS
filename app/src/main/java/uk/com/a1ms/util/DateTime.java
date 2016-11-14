package uk.com.a1ms.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by priju.jacobpaul on 15/08/16.
 */
public class DateTime {

    public static String getTimeInAmPm(){

        Calendar now = Calendar.getInstance();
        DateFormat outputFormat = new SimpleDateFormat("h:mm a");
        return outputFormat.format(now.getTime());
    }

    public static String getDateTime() {

        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}
