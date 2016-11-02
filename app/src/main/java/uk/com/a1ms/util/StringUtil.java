package uk.com.a1ms.util;

/**
 * Created by priju.jacobpaul on 25/10/2016.
 */

public class StringUtil {

    public static CharSequence noTrailingwhiteLines(CharSequence text) {

        while (text.charAt(text.length() - 1) == '\n') {
            text = text.subSequence(0, text.length() - 1);
        }
        return text;
    }
}
