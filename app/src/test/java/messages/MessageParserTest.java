package messages;

import android.text.SpannableString;
import android.util.Log;

import uk.com.a1ms.messages.MessageParser;

/**
 * Created by priju.jacobpaul on 2/08/16.
 */
public class MessageParserTest {

    @org.junit.Before
    public void setUp() throws Exception {

    }

    @org.junit.Test
    public void testParse() throws Exception {
        MessageParser messageParser = new MessageParser();
        SpannableString parsedString = messageParser.Parse("Mum and I are getting Compact Disc and Black Biker Gloves");
        Log.d("Test",parsedString.toString());

    }

    @org.junit.Test
    public void testParse1() throws Exception {
        MessageParser messageParser = new MessageParser();
        SpannableString parsedString = messageParser.Parse("Mum and I are getting Compact Disc and Black Biker gloves");
        Log.d("Test",parsedString.toString());
    }

    @org.junit.Test
    public void testParse2() throws Exception {
        MessageParser messageParser = new MessageParser();
        SpannableString parsedString = messageParser.Parse("Mum and I Love Black gloves");
        Log.d("Test",parsedString.toString());
    }

    @org.junit.Test
    public void testParse3() throws Exception {
        MessageParser messageParser = new MessageParser();
        SpannableString parsedString = messageParser.Parse("Mum and I are getting CD and Black Biker Gloves...");
        Log.d("Test",parsedString.toString());
    }

    @org.junit.Test
    public void testParse4() throws Exception {
        MessageParser messageParser = new MessageParser();
        SpannableString parsedString = messageParser.Parse("Thanks again For");
        Log.d("Test",parsedString.toString());
    }
}