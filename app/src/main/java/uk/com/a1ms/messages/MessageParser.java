package uk.com.a1ms.messages;

import android.content.res.Resources;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import uk.com.a1ms.A1MSApplication;
import uk.com.a1ms.R;

/**
 * Created by priju.jacobpaul on 2/08/16.
 */
public class MessageParser {

    private String originalString;
    private StringBuffer parsedString;
    private ArrayList<String> parsedOriginalArray = new ArrayList<>();
    private ArrayList<String> sentenceArray = new ArrayList<>();
    private String currentStr;
    private String nextStr;
    private String prevStr;
    private boolean isNewSentence;
    private boolean isCapsFound;
    private SpannableStringBuilder spannableStringBuilder;
    private SpannableStringBuilder longSpannableStringBuilder;

    private Resources resources;
    private int newAcroColor;
    private int existAcryColor;

    public MessageParser(){

        parsedString = new StringBuffer();
        sentenceArray.clear();
        spannableStringBuilder = new SpannableStringBuilder();
        longSpannableStringBuilder = new SpannableStringBuilder();

        if((A1MSApplication.applicationContext) != null) {
            newAcroColor = (A1MSApplication.applicationContext).getResources().getColor(R.color.bg_msg_custom_acronym);
            existAcryColor = (A1MSApplication.applicationContext).getResources().getColor(R.color.bg_msg_existing_acronym);
        }
        else {
            newAcroColor = R.color.bg_msg_custom_acronym;
            existAcryColor = R.color.bg_msg_existing_acronym;
        }

    }


    public SpannableString Parse(String string) {

        sentenceArray.clear();
        sentenceArray = breakIntoSentence(string);

        BreakIterator breakIterator = BreakIterator.getSentenceInstance(Locale.getDefault());
//        count(breakIterator,string);

        parsedString.delete(0,parsedString.length());
        spannableStringBuilder.clear();
        longSpannableStringBuilder.clear();


        for(String sentence: sentenceArray) {

            parsedOriginalArray = breakIntoWords(sentence); // sentence.split("\\s+");
            isCapsFound = false;

            if (parsedOriginalArray.size() > 0) {

                for (int i = 0; i < parsedOriginalArray.size(); i++) {

                    currentStr = "";
                    nextStr = "";
                    prevStr = "";

                    currentStr = parsedOriginalArray.get(i);
                    customAcronymCreation(currentStr,i);

                }
            }
        }
        return new SpannableString(spannableStringBuilder);
    }

    private void customAcronymCreation(String currentStr,int index){

        String acryonym = getAcryonym(currentStr);
        if(currentStr.trim().isEmpty()) {
            if(!isCapsFound){
                appendString(currentStr);
            }
            longSpannableStringBuilder.append(currentStr);
        }
        else if(acryonym != null) {

            appendString(acryonym);
            addSpan(new BackgroundColorSpan(existAcryColor),
                    spannableStringBuilder.length() - (acryonym.length()), spannableStringBuilder.length() , spannableStringBuilder);

            longSpannableStringBuilder.append(currentStr);
            longSpannableStringBuilder.setSpan(new BackgroundColorSpan(existAcryColor),
                    (longSpannableStringBuilder.length()) - (currentStr.length()),
                    longSpannableStringBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        else {
            Character firstCharStr = getFirstChar(currentStr);

            if (isFirstCharCaps(currentStr)) {

                // Check whether the word is the last word in the sentence.
                if ((index + 1) != parsedOriginalArray.size()) {
//                    nextStr = parsedOriginalArray.get(index + 1);
                    nextStr = getNextWord(index);

                    if (isFirstCharCaps(nextStr)) {

//                    Character firstCharNxtStr = getFirstChar(nextStr);
                        isCapsFound = true;
//                                i++;

                        appendCharacter(firstCharStr);
                        addSpan(new BackgroundColorSpan(newAcroColor),
                                spannableStringBuilder.length()-1, spannableStringBuilder.length(), spannableStringBuilder);

                        longSpannableStringBuilder.append(currentStr);
                        longSpannableStringBuilder.setSpan(new BackgroundColorSpan(newAcroColor),
                                (longSpannableStringBuilder.length()) - (currentStr.length()),
                                longSpannableStringBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else {
                        if (isCapsFound) {
                            appendCharacter(getFirstChar(currentStr));
                            addSpan(new BackgroundColorSpan(newAcroColor),
                                    spannableStringBuilder.length() - 2, spannableStringBuilder.length(), spannableStringBuilder);

                            longSpannableStringBuilder.append(currentStr);
                            longSpannableStringBuilder.setSpan(new BackgroundColorSpan(newAcroColor),
                                    (longSpannableStringBuilder.length()) - (currentStr.length()),
                                    longSpannableStringBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            isCapsFound = false;

                        } else {
                            appendString(currentStr);
                            longSpannableStringBuilder.append(currentStr);

                        }
                    }
                } else {
                    // If its the last word, check whether the previous word was caps.
                    int prevStrIndex = index - 1;
                    if (prevStrIndex >= 0) {
                        prevStr = parsedOriginalArray.get(prevStrIndex);
                        if (isFirstCharCaps(prevStr)) {
                            appendCharacter(firstCharStr);
                            addSpan(new BackgroundColorSpan(newAcroColor),
                                    spannableStringBuilder.length() - 1, spannableStringBuilder.length(), spannableStringBuilder);

                            longSpannableStringBuilder.append(currentStr);
                            longSpannableStringBuilder.setSpan(new BackgroundColorSpan(newAcroColor),
                                    (longSpannableStringBuilder.length()) - (currentStr.length()),
                                    longSpannableStringBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                            // If the last character of the last word is alpha numeric, append that.
                            Character lastChar = currentStr.charAt(currentStr.length() - 1);
                            if (!lastChar.isDigit(lastChar) && !lastChar.isLetter(lastChar)) {
                                appendCharacter(lastChar);
//                                appendString(" ");
                                longSpannableStringBuilder.append(lastChar);
                            }
                        } else {
                            appendString(currentStr);
                            longSpannableStringBuilder.append(currentStr);

                        }
                    } else {
                        // if the previous word was'nt caps, just add the word
                        // add a space before the word.
                        if (isCapsFound) {
                            isCapsFound = false;
                        }
                        appendString(currentStr);
                        longSpannableStringBuilder.append(currentStr);
                    }
                }
            } else {
                if (isCapsFound) {
                    isCapsFound = false;
                }
                parsedString.append(currentStr);
                appendString(currentStr);
                longSpannableStringBuilder.append(currentStr);
            }
        }
    }


    private Character getFirstChar(String word){

        if(word.isEmpty() || word == null) {
           return null;
        }
        char firstChar = word.charAt(0);
        return firstChar;
    }

    private boolean isFirstCharCaps(String word){

        if(word.isEmpty() || word == null) {
            return false;
        }

        char firstChar = word.charAt(0);
        return Character.isUpperCase(firstChar);
    }

    private Character getLastChar(String word){

        if(word.isEmpty() || word == null) {
            return null;
        }
        char firstChar = word.charAt(word.length());
        return firstChar;
    }

    private boolean isCharacterDot(Character character){
        return character.equals('.');
    }


    //

    private void appendCharacter(Character character){
        spannableStringBuilder = spannableStringBuilder.append(character);
    }

    private void appendString(String str){
        spannableStringBuilder = spannableStringBuilder.append(str);
    }

    private void addSpan(Object what,int start,int end,SpannableStringBuilder stringBuilder){
        stringBuilder.setSpan(what,
                start,end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private ArrayList<String> breakIntoSentence(String source) {

        ArrayList<String>sentenceArray = new ArrayList<>();

        BreakIterator breakIterator = BreakIterator.getSentenceInstance(Locale.getDefault());
        breakIterator.setText(source);

        int start = breakIterator.first();

        for (int end = breakIterator.next(); end != BreakIterator.DONE;
            start = end, end = breakIterator.next()) {
            sentenceArray.add(source.substring(start, end));
        }

        return sentenceArray;
    }

    private ArrayList<String> breakIntoWords(String source) {

        ArrayList<String>sentenceArray = new ArrayList<>();

        BreakIterator breakIterator = BreakIterator.getWordInstance(Locale.getDefault());
        breakIterator.setText(source);

        int previousEnd = 0;

        int start = breakIterator.first();

        for (int end = breakIterator.next(); end != BreakIterator.DONE;
             start = end, end = breakIterator.next()) {
            sentenceArray.add(source.substring(start, end));

//            if(previousEnd !=0){
//                String delimiter = source.substring(previousEnd,end+1);
//                if(!delimiter.isEmpty()) {
//                    sentenceArray.add(delimiter);
//                }
//            }
//            previousEnd = end;
        }

        return sentenceArray;
    }

    private static int count(BreakIterator bi, String source) {
        int counter = 0;
        bi.setText(source);

        int lastIndex = bi.first();
        while (lastIndex != BreakIterator.DONE) {
            int firstIndex = lastIndex;
            lastIndex = bi.next();

            if (lastIndex != BreakIterator.DONE) {
                String sentence = source.substring(firstIndex, lastIndex);
                System.out.println("sentence = " + sentence);
                counter++;
            }
        }
        return counter;
    }

    public SpannableStringBuilder getLongSpannableStringBuilder() {
        return longSpannableStringBuilder;
    }

    public SpannableStringBuilder getSpannableStringBuilder() {
        return spannableStringBuilder;
    }

    private String getAcryonym(String word){

        if(FileParseDetails.getSentenceArrays() != null) {
            List<String> acronymList = FileParseDetails.getSentenceArrays().get(word);
            for(String acronym : acronymList){
                return acronym;
            }
        }
        return null;
    }

    private String getNextWord(int index){

        index++;
        for(;index<parsedOriginalArray.size();index++) {
            String nextString = parsedOriginalArray.get(index);
            if(nextString.trim().isEmpty()) {
                continue;
            }
            else {
                this.nextStr = parsedOriginalArray.get(index);
                break;
            }
        }
        return nextStr;
    }

}
