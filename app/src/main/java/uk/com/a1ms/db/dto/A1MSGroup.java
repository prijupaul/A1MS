package uk.com.a1ms.db.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by priju.jacobpaul on 4/09/16.
 */
public class A1MSGroup implements Parcelable{

    private String groupName;
    private String adminId;
    private boolean isActivate;
    private String groupId;
    private String avatar;
    private ArrayList<String> membersList = new ArrayList<>();

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public boolean isActivate() {
        return isActivate;
    }

    public void setActivate(boolean activate) {
        isActivate = activate;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public ArrayList<String> getMembersList() {
        return membersList;
    }

    public void setMembersList(ArrayList<String> membersList) {
        this.membersList = membersList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        // TODO: Implement this
    }

    public static final Creator<A1MSGroup> CREATOR = new Creator<A1MSGroup>() {
        @Override
        public A1MSGroup createFromParcel(Parcel parcel) {
            return null;
        }

        @Override
        public A1MSGroup[] newArray(int i) {
            return new A1MSGroup[0];
        }
    };
}
