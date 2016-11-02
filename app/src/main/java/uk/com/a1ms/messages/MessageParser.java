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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

            if(isSentenceDottedAcronym(sentence)){

                String[] words = sentence.split(" ");
                StringBuffer dottedAcronym = new StringBuffer();
                for (int i=0;i<words.length;i++){
                    String word = words[i];
                    String subString = word;
                    if(i == 0) {
                        subString = word.substring(2);
                    }
                    else if(i == words.length -1 ){
                        subString = word.substring(0,word.length()-2);
                    }
                    dottedAcronym.append(subString.substring(0,1).toUpperCase());
                }
                dottedAcronym.append(" ");

                appendString(dottedAcronym.toString());
                addSpan(new BackgroundColorSpan(newAcroColor),
                        spannableStringBuilder.length() - (dottedAcronym.length()), spannableStringBuilder.length() , spannableStringBuilder);

                longSpannableStringBuilder.append(sentence);
                longSpannableStringBuilder.setSpan(new BackgroundColorSpan(newAcroColor),
                        (longSpannableStringBuilder.length()) - (sentence.length()),
                        longSpannableStringBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            else {
                parsedOriginalArray = breakIntoWords(sentence); // sentence.split("\\s+");
                isCapsFound = false;

                if (parsedOriginalArray.size() > 0) {

                    for (int i = 0; i < parsedOriginalArray.size(); i++) {

                        currentStr = "";
                        nextStr = "";
                        prevStr = "";

                        currentStr = parsedOriginalArray.get(i);
                        customAcronymCreation(currentStr, i);

                    }
                }
            }
        }
        return new SpannableString(spannableStringBuilder);
    }

    private void customAcronymCreation(String currentStr,int index){

        String acronym = getAcryonym(currentStr);
        if(currentStr.trim().isEmpty()) {
            if(!isCapsFound){
                appendString(currentStr);
            }
            longSpannableStringBuilder.append(currentStr);
        }
        else if(acronym != null) {

            appendString(acronym);
            addSpan(new BackgroundColorSpan(existAcryColor),
                    spannableStringBuilder.length() - (acronym.length()), spannableStringBuilder.length() , spannableStringBuilder);

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

                    nextStr = getNextWord(index);

                    if (isFirstCharCaps(nextStr)) {

//                    Character firstCharNxtStr = getFirstChar(nextStr);
                        isCapsFound = true;

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
                        prevStr = getPreviousWord(index);
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


    private boolean isWordOnlyDot(String word){

        if(word == null || word.isEmpty()) {
            return false;
        }
        if(word.length() == 1 && isCharacterDot(word.charAt(0))){
            return true;
        }
        return false;
    }

    private Character getFirstChar(String word){

        if(word == null || word.isEmpty()) {
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
            String subString = source.substring(start, end);

            //(\s\.\..*\b\.\.)
            // \.\.\b((?!=|\,|\.).)+(.)\b\.\.
            Pattern pattern = Pattern.compile("\\.\\.\\b((?=[\\w\\'\\s]+).)+(.)\\b\\.\\.");
            Matcher matcher = pattern.matcher(subString);
            int count = 0;
            int startIndex = 0;

            while (matcher.find())
            {
                count++;
                System.out.println("found: " + count + " : "
                        + matcher.start() + " - " + matcher.end());
                String group = matcher.group();
                String start_end = subString.substring(startIndex, matcher.start());
                if(!start_end.trim().isEmpty()){
                    sentenceArray.add(start_end);
                }
                sentenceArray.add(subString.substring(matcher.start(),matcher.end()));
                startIndex = matcher.end();

            }

            if(startIndex != subString.length()){
                sentenceArray.add(subString.substring(startIndex,subString.length()));
            }

        }

        return sentenceArray;
    }

    private boolean isSentenceDottedAcronym(String sentence){

        if(sentence != null && !sentence.isEmpty()){
            String[] words = sentence.split(" ");
            // 4 makes sure there are atleast there are
            // two words other than .. ..
            if(words.length >= 2){
                String lastWord = words[words.length-1];
                String firstWord = words[0].substring(0,2);
                if(firstWord.equalsIgnoreCase("..") &&
                        lastWord.substring(lastWord.length() -2, lastWord.length()).equalsIgnoreCase("..")){
                    return true;
                }
            }
        }

        return false;
    }

    private ArrayList<String> breakIntoWords(String source) {

        ArrayList<String>sentenceArray = new ArrayList<>();

        BreakIterator breakIterator = BreakIterator.getWordInstance(Locale.getDefault());
        breakIterator.setText(source);

        int start = breakIterator.first();
        for (int end = breakIterator.next(); end != BreakIterator.DONE;
             start = end, end = breakIterator.next()) {

                String subString = source.substring(start,end);
                sentenceArray.add(subString);
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

    private String getPreviousWord(int index){
        --index;
        for(;index >= 0;index--){
            String prevString = parsedOriginalArray.get(index);
            if(prevString.trim().isEmpty()){
                continue;
            }
            else {
                this.prevStr = prevString;
                break;
            }
        }
        return prevStr;
    }

    private String getNextWord(int index){

        index++;
        for(;index<parsedOriginalArray.size();index++) {
            String nextString = parsedOriginalArray.get(index);
            if(nextString.trim().isEmpty()) {
                continue;
            }
            else {
                this.nextStr = nextString;
                break;
            }
        }
        return nextStr;
    }

}
