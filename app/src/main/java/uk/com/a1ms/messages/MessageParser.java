package uk.com.a1ms.messages;

import android.content.res.Resources;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;

import uk.com.a1ms.A1MSApplication;
import uk.com.a1ms.R;

/**
 * Created by priju.jacobpaul on 2/08/16.
 */
public class MessageParser {

    private String originalString;
    private StringBuffer parsedString;
    private String[] parsedOriginalArray;
    private boolean isCapsFound;
    private SpannableStringBuilder spannableStringBuilder;
    private Resources resources;
    private int newAcroColor;

    public MessageParser(){

        parsedString = new StringBuffer();
        spannableStringBuilder = new SpannableStringBuilder();
        newAcroColor = (A1MSApplication.applicationContext).getResources().getColor(R.color.bg_msg_custom_acronym);

    }

    public SpannableString Parse(String string) {

        parsedString.delete(0,parsedString.length());
        spannableStringBuilder.clear();

        parsedOriginalArray = string.split("\\s+");

        if(parsedOriginalArray.length > 0){

            for(int i=0;i<parsedOriginalArray.length;i++) {

                String str = parsedOriginalArray[i];
                Character firstCharStr = getFirstChar(str);
                if(isFirstCharCaps(str)){

                    // Check whether the word is the last word in the sentence.
                    if( (i+1) != parsedOriginalArray.length) {
                        String nextStr = parsedOriginalArray[i+1];
                        if(isFirstCharCaps(nextStr)){
                            Character firstCharNxtStr = getFirstChar(nextStr);

                            parsedString.append(firstCharStr);
                            parsedString.append(firstCharNxtStr);
                            isCapsFound = true;
                            i++;

                            spannableStringBuilder = spannableStringBuilder.append(firstCharStr);
                            spannableStringBuilder = spannableStringBuilder.append(firstCharNxtStr);

                            spannableStringBuilder.setSpan(new BackgroundColorSpan(newAcroColor),
                                    spannableStringBuilder.length()-2,spannableStringBuilder.length(),
                                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                        else {
                            parsedString.append(str + " ");
                            spannableStringBuilder = spannableStringBuilder.append(str + " ");
                        }
                    }
                    else {
                        // If its the last word, check whether the previous word was caps.
                        String prevStr = parsedOriginalArray[i-1];
                        if(isFirstCharCaps(prevStr)){
                            parsedString.append(getFirstChar(str));

                            spannableStringBuilder = spannableStringBuilder.append(firstCharStr);
                            spannableStringBuilder.setSpan(new BackgroundColorSpan(newAcroColor),
                                    spannableStringBuilder.length()-1,spannableStringBuilder.length(),
                                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                            // If the last character of the last word is alpha numeric, append that.
                            Character lastChar = str.charAt(str.length()-1);
                            if(!lastChar.isDigit(lastChar) && !lastChar.isLetter(lastChar)){
                                parsedString.append(lastChar);
                                spannableStringBuilder = spannableStringBuilder.append(lastChar);
                            }
                        }
                        else {
                            // if the previous word was'nt caps, just add the word
                            // add a space before the word.
                            if(isCapsFound){
                                parsedString.append(" ");
                                spannableStringBuilder = spannableStringBuilder.append(" ");
                                isCapsFound = false;
                            }
                            parsedString.append(str + " ");
                            spannableStringBuilder =  spannableStringBuilder.append(str + " ");
                        }
                    }
                }
                else {
                    if(isCapsFound) {
                        parsedString.append(" ");
                        spannableStringBuilder = spannableStringBuilder.append(" ");
                        isCapsFound = false;
                    }
                    parsedString.append(str + " ");
                    spannableStringBuilder = spannableStringBuilder.append(str + " ");
                }
            }
        }
        return new SpannableString(spannableStringBuilder);
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

}
