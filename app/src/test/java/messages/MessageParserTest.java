package messages;

import android.text.SpannableString;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.ArrayList;

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
        SpannableString parsedString = messageParser.Parse("Mum and I are getting CD and ..black biker gloves...");
        Log.d("Test",parsedString.toString());
    }

    @org.junit.Test
    public void testParse4() throws Exception {
        MessageParser messageParser = new MessageParser();
        SpannableString parsedString = messageParser.Parse("Thanks again For");
        Log.d("Test",parsedString.toString());
    }

    @org.junit.Test
    public void testParse5() throws Exception {
        try {
            MessageParser messageParser = new MessageParser();
            Class[] args = new Class[1];
            args[0] = String.class;
            Method method = messageParser.getClass().getDeclaredMethod("breakIntoSentence", args);
            method.setAccessible(true);
            ArrayList<String> arrayList = (ArrayList<String>)method.invoke(messageParser, new String("The fox is on fire.. ..Check the kangaroo too.."));
            for (String str:arrayList){
                System.out.println("found: " + str);
            }
        }
        catch (NoSuchMethodException e){
            e.printStackTrace();
        }

    }

    @org.junit.Test
    public void testParse6() throws Exception {
        try {
            MessageParser messageParser = new MessageParser();
            Class[] args = new Class[1];
            args[0] = String.class;
            Method method = messageParser.getClass().getDeclaredMethod("breakIntoSentence", args);
            method.setAccessible(true);
            ArrayList<String> arrayList = (ArrayList<String>)method.invoke(messageParser, new String("..Again, ..check the kangaroo too..The fox is on fire..Again, ...Kate's birthday.. ..This is good.."));
            for (String str:arrayList){
                System.out.println("found: " + str);
            }
        }
        catch (NoSuchMethodException e){
            e.printStackTrace();
        }

    }

    @org.junit.Test
    public void testParse7() throws Exception {
        try {
            MessageParser messageParser = new MessageParser();
            Class[] args = new Class[1];
            args[0] = String.class;
            Method method = messageParser.getClass().getDeclaredMethod("breakIntoWords", args);
            method.setAccessible(true);
            ArrayList<String> arrayList = (ArrayList<String>)method.invoke(messageParser, new String("hello mate ...test sentence.."));
            for (String str:arrayList){
                System.out.println("found: " + str);
            }
        }
        catch (NoSuchMethodException e){
            e.printStackTrace();
        }

    }

    @org.junit.Test
    public void testSentenceDottedAcronym() throws Exception {
        try {
            MessageParser messageParser = new MessageParser();
            Class[] args = new Class[1];
            args[0] = String.class;
            Method method = messageParser.getClass().getDeclaredMethod("isSentenceDottedAcronym", args);
            method.setAccessible(true);
            boolean isDottedAcryo = (boolean)method.invoke(messageParser, new String("..Kate's jacket.."));
            System.out.println("found: " + isDottedAcryo);
        }
        catch (NoSuchMethodException e){
            e.printStackTrace();
        }

    }
}