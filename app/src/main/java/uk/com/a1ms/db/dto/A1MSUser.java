package uk.com.a1ms.db.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by priju.jacobpaul on 6/06/16.
 */
public class A1MSUser implements Parcelable {

    private String name;
    private String mobile;
    private String email;
    private String token;
    private String avatar;
    private boolean isEditable = true;
    private boolean isChecked = false;
    private boolean isGroup = false;
    private boolean isEchomate;
    private String userId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isEditable() {
        return isEditable;
    }

    public void setEditable(boolean editable) {
        isEditable = editable;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public void setGroup(boolean group) {
        isGroup = group;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isEchomate() {
        return isEchomate;
    }

    public void setEchomate(boolean echomate) {
        isEchomate = echomate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(name);
        parcel.writeString(mobile);
        parcel.writeString(email);
        parcel.writeString(token);
        parcel.writeString(avatar);
        parcel.writeInt(isEditable() ? 1 : 0);
        parcel.writeInt(isChecked() ? 1: 0);
        parcel.writeInt(isGroup() ? 1: 0);
        parcel.writeString(getUserId());
        parcel.writeInt(isEchomate() ? 1 : 0);
    }

    public A1MSUser(){

    }

    public A1MSUser(Parcel source){
        setName(source.readString());
        setMobile(source.readString());
        setEmail(source.readString());
        setToken(source.readString());
        setAvatar(source.readString());
        setEditable(source.readInt() == 1 ? true : false);
        setChecked(source.readInt() == 1 ? true : false);
        setGroup(source.readInt() == 1 ? true : false);
        setUserId(source.readString());
        setEchomate(source.readInt() == 1 ? true : false);
    }

    public static final Creator<A1MSUser> CREATOR = new Creator<A1MSUser>() {

        @Override
        public A1MSUser createFromParcel(Parcel source) {
            return new A1MSUser(source);
        }

        @Override
        public A1MSUser[] newArray(int size) {
            return new A1MSUser[size];
        }

    };
}
