package uk.com.a1ms.messages;

import com.google.common.collect.ListMultimap;

/**
 * Created by priju.jacobpaul on 15/08/16.
 */
public class FileParseDetails {

    static ListMultimap<String, String> mAcronymsArray;
    static ListMultimap<String, String> mSentenceArrays;

    public static ListMultimap<String, String> getAcronymsArray() {
        return mAcronymsArray;
    }

    public static void setAcronymsArray(ListMultimap<String, String> acronymsArray) {
        mAcronymsArray = acronymsArray;
    }

    public static ListMultimap<String, String> getSentenceArrays() {
        return mSentenceArrays;
    }

    public static void setSentenceArrays(ListMultimap<String, String> sentenceArrays) {
        mSentenceArrays = sentenceArrays;
    }
}
