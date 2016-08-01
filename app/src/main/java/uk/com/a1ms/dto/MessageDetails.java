package uk.com.a1ms.dto;

/**
 * Created by priju.jacobpaul on 31/07/16.
 */
public class MessageDetails {

    int startPosition;
    int endPosition;
    StringType stringType;

    enum StringType {
        TYPE_CUSTOM,
        TYPE_DEFAULT,
        TYPE_NONE
    }

    public int getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(int startPosition) {
        this.startPosition = startPosition;
    }

    public int getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(int endPosition) {
        this.endPosition = endPosition;
    }

    public StringType getStringType() {
        return stringType;
    }

    public void setStringType(StringType stringType) {
        this.stringType = stringType;
    }
}
