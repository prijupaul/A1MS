package uk.com.a1ms.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import uk.com.a1ms.A1MSApplication;
import uk.com.a1ms.db.dto.A1MSGroup;
import uk.com.a1ms.db.dto.A1MSUser;

/**
 * Created by priju.jacobpaul on 6/06/16.
 */
public class A1MSGroupsFieldsDataSource extends BaseFields {


    private SQLiteDatabase sqLiteDatabase = null;
    private A1MSDbHelper a1MSDbHelper;


    public static abstract class A1MSGroupsEntry implements BaseColumns {
        public static final String TABLE_NAME = "A1MSGroups";
        public static final String COLUMN_NAME_NULLABLE = "nullhack";
        public static final String COLUMN_NAME_A1MS_GROUPS_USER_NAME = "name";
        public static final String COLUMN_NAME_A1MS_GROUPS_ADMIN_ID = "adminId";
        public static final String COLUMN_NAME_A1MS_GROUPS_USER_AVATAR = "avatar";
        public static final String COLUMN_NAME_A1MS_GROUPS_MEMBERS_ID = "membersId";
        public static final String COLUMN_NAME_A1MS_GROUPS_ID = "groupId";
        public static final String COLUMN_NAME_A1MS_GROUPS_IS_ACTIVE = "isActive";

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + A1MSGroupsEntry.TABLE_NAME + " (" +
                        A1MSGroupsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        A1MSGroupsEntry.COLUMN_NAME_A1MS_GROUPS_USER_NAME + TEXT_TYPE + COMMA_SEP +
                        A1MSGroupsEntry.COLUMN_NAME_A1MS_GROUPS_ADMIN_ID + TEXT_TYPE + COMMA_SEP +
                        A1MSGroupsEntry.COLUMN_NAME_A1MS_GROUPS_MEMBERS_ID + TEXT_TYPE + COMMA_SEP +
                        A1MSGroupsEntry.COLUMN_NAME_A1MS_GROUPS_ID + TEXT_TYPE + COMMA_SEP +
                        A1MSGroupsEntry.COLUMN_NAME_A1MS_GROUPS_IS_ACTIVE + TEXT_TYPE + COMMA_SEP +
                        A1MSGroupsEntry.COLUMN_NAME_A1MS_GROUPS_USER_AVATAR + TEXT_TYPE + COMMA_SEP +
                        "PRIMARY KEY (" + A1MSGroupsEntry.COLUMN_NAME_A1MS_GROUPS_USER_NAME + "," + A1MSGroupsEntry.COLUMN_NAME_A1MS_GROUPS_ID + ")" + " );";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + A1MSGroupsEntry.TABLE_NAME;

        private static String[] allColumns = {
                A1MSGroupsEntry._ID,
                A1MSGroupsEntry.COLUMN_NAME_A1MS_GROUPS_USER_NAME,
                A1MSGroupsEntry.COLUMN_NAME_A1MS_GROUPS_MEMBERS_ID,
                A1MSGroupsEntry.COLUMN_NAME_A1MS_GROUPS_ID,
                A1MSGroupsEntry.COLUMN_NAME_A1MS_GROUPS_ADMIN_ID,
                A1MSGroupsEntry.COLUMN_NAME_A1MS_GROUPS_IS_ACTIVE,
                A1MSGroupsEntry.COLUMN_NAME_A1MS_GROUPS_USER_AVATAR
        };
    }


    public A1MSGroupsFieldsDataSource(Context context) {
        a1MSDbHelper = A1MSDbHelper.getInstance(context);
    }


    public void open() throws SQLException {
        sqLiteDatabase = A1MSApplication.sqLiteDatabase;
    }

    public void close() {

//        if (a1MSDbHelper != null) {
//            a1MSDbHelper.close();
//        }
    }

    public void createDb(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(A1MSGroupsEntry.SQL_CREATE_ENTRIES);
    }

    private String computeGroupMembersList(A1MSGroup a1MSGroup) {

        StringBuffer members = new StringBuffer();
        int index = 0;
        for (String member : a1MSGroup.getMembersList()) {
            index++;
            members.append(member);
            if (index != a1MSGroup.getMembersList().size()) {
                members.append("&");
            }
        }
        return members.toString();
    }

    public long insertA1MSGroup(SQLiteDatabase sqLiteDatabase, A1MSGroup a1MSGroup) {

        String members = computeGroupMembersList(a1MSGroup);

        ContentValues values = new ContentValues();
        values.put(A1MSGroupsEntry.COLUMN_NAME_A1MS_GROUPS_USER_NAME, a1MSGroup.getGroupName());
        values.put(A1MSGroupsEntry.COLUMN_NAME_A1MS_GROUPS_ADMIN_ID, a1MSGroup.getAdminId());
        values.put(A1MSGroupsEntry.COLUMN_NAME_A1MS_GROUPS_MEMBERS_ID, members);
        values.put(A1MSGroupsEntry.COLUMN_NAME_A1MS_GROUPS_ID, a1MSGroup.getGroupId());
        values.put(A1MSGroupsEntry.COLUMN_NAME_A1MS_GROUPS_USER_AVATAR, a1MSGroup.getAvatar());
        values.put(A1MSGroupsEntry.COLUMN_NAME_A1MS_GROUPS_IS_ACTIVE, a1MSGroup.isActivate());


        long newRowId = sqLiteDatabase.insert(
                A1MSGroupsEntry.TABLE_NAME,
                A1MSGroupsEntry.COLUMN_NAME_NULLABLE,
                values);

        return newRowId;
    }

    public void deleteA1MSGroup(List<A1MSGroup> a1MSGroups) {

        if (a1MSGroups == null) {
            return;
        }

        ArrayList<String> users = new ArrayList<>();
        for (A1MSGroup user : a1MSGroups) {
            users.add("\'" + user.getGroupId() + "\'");
        }

        String args = TextUtils.join(", ", users);
        String statement = "DELETE FROM " + A1MSGroupsEntry.TABLE_NAME + " WHERE " +
                A1MSGroupsEntry.COLUMN_NAME_A1MS_GROUPS_ID + " IN" + " ( " + args + " )";

        sqLiteDatabase.execSQL(statement);

    }

    public void deleteA1MSUsersFromGroup(List<A1MSUser> a1MSUserList) {

        List<A1MSGroup> groupList = new ArrayList<>();
        for (A1MSUser a1MSUser : a1MSUserList) {
            if (a1MSUser.isGroup()) {
                A1MSGroup a1MSGroup = new A1MSGroup();
                a1MSGroup.setGroupId(a1MSUser.getUserId());
                a1MSGroup.setGroupName(a1MSUser.getName());
                groupList.add(a1MSGroup);
            }
        }
        deleteA1MSGroup(groupList);
    }

    public void deleteA1MSUserFromGroup(String userId, A1MSGroup a1MSGroup) {

        if ((userId == null) || (a1MSGroup == null)) {
            return;
        }

        String whereClause = A1MSGroupsEntry.COLUMN_NAME_A1MS_GROUPS_ID + " =? ";
        String[] whereArgs = new String[1];
        whereArgs[0] = a1MSGroup.getGroupId();


        boolean removed = a1MSGroup.getMembersList().remove(userId);
        if (removed) {
            String members = computeGroupMembersList(a1MSGroup);

            ContentValues values = new ContentValues();
            values.put(A1MSGroupsEntry.COLUMN_NAME_A1MS_GROUPS_USER_NAME, a1MSGroup.getGroupName());
            values.put(A1MSGroupsEntry.COLUMN_NAME_A1MS_GROUPS_ADMIN_ID, a1MSGroup.getAdminId());
            values.put(A1MSGroupsEntry.COLUMN_NAME_A1MS_GROUPS_MEMBERS_ID, members);
            values.put(A1MSGroupsEntry.COLUMN_NAME_A1MS_GROUPS_ID, a1MSGroup.getGroupId());
            values.put(A1MSGroupsEntry.COLUMN_NAME_A1MS_GROUPS_USER_AVATAR, a1MSGroup.getAvatar());
            values.put(A1MSGroupsEntry.COLUMN_NAME_A1MS_GROUPS_IS_ACTIVE, a1MSGroup.isActivate());

            sqLiteDatabase.update(A1MSGroupsEntry.TABLE_NAME,
                    values,
                    whereClause, whereArgs);

        }


    }

    public void updateGroupDetails(A1MSGroup a1MSGroup) {

        if (a1MSGroup == null) {
            return;
        }

        String whereClause = A1MSGroupsEntry.COLUMN_NAME_A1MS_GROUPS_ID + " =? ";
        String[] whereArgs = new String[1];
        whereArgs[0] = a1MSGroup.getGroupId();

        String members = computeGroupMembersList(a1MSGroup);

        ContentValues values = new ContentValues();
        values.put(A1MSGroupsEntry.COLUMN_NAME_A1MS_GROUPS_USER_NAME, a1MSGroup.getGroupName());
        values.put(A1MSGroupsEntry.COLUMN_NAME_A1MS_GROUPS_ADMIN_ID, a1MSGroup.getAdminId());
        values.put(A1MSGroupsEntry.COLUMN_NAME_A1MS_GROUPS_MEMBERS_ID, members);
        values.put(A1MSGroupsEntry.COLUMN_NAME_A1MS_GROUPS_ID, a1MSGroup.getGroupId());
        values.put(A1MSGroupsEntry.COLUMN_NAME_A1MS_GROUPS_USER_AVATAR, a1MSGroup.getAvatar());
        values.put(A1MSGroupsEntry.COLUMN_NAME_A1MS_GROUPS_IS_ACTIVE, a1MSGroup.isActivate());

        sqLiteDatabase.update(A1MSGroupsEntry.TABLE_NAME,
                values,
                whereClause, whereArgs);


    }

    public A1MSGroup getDetailsOfGroups(String groupdID, String groupName) {

        String whereClause = A1MSGroupsEntry.COLUMN_NAME_A1MS_GROUPS_ID + " =? AND " +
                A1MSGroupsEntry.COLUMN_NAME_A1MS_GROUPS_USER_NAME + " =?";

        String[] whereArgs = new String[2];
        whereArgs[0] = groupdID;
        whereArgs[1] = groupName;

        Cursor c = sqLiteDatabase.query(
                A1MSGroupsEntry.TABLE_NAME,  // The table to query
                A1MSGroupsEntry.allColumns,                               // The columns to return
                whereClause,                                // The columns for the WHERE clause
                whereArgs,                               // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        if (c != null) {
            c.moveToFirst();
            A1MSGroup group = cursorToUser(c);
            return group;
        }
        return null;
    }

    public A1MSGroup getDetailsOfGroups(String groupId) {

        String whereClause = A1MSGroupsEntry.COLUMN_NAME_A1MS_GROUPS_ID + "=?";
        String[] whereArgs = new String[1];
        whereArgs[0] = groupId;

        Cursor c = sqLiteDatabase.query(
                A1MSGroupsEntry.TABLE_NAME,  // The table to query
                A1MSGroupsEntry.allColumns,                               // The columns to return
                whereClause,                                // The columns for the WHERE clause
                whereArgs,                               // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        c.moveToFirst();
        A1MSGroup group = cursorToUser(c);
        return group;
    }

    public List<A1MSGroup> getAllA1MSGroups() {

        List<A1MSGroup> a1MSGroups = new ArrayList<>();

        String sortOrder = A1MSGroupsEntry.COLUMN_NAME_A1MS_GROUPS_USER_NAME + " ASC";
        Cursor c = sqLiteDatabase.query(
                A1MSGroupsEntry.TABLE_NAME,  // The table to query
                A1MSGroupsEntry.allColumns,                               // The columns to return
                null,                                   // The columns for the WHERE clause
                null,                                   // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        if (c != null) {
            c.moveToFirst();


            while (!c.isAfterLast()) {
                A1MSGroup a1MSGroup = cursorToUser(c);
                a1MSGroups.add(a1MSGroup);
            }
            c.moveToNext();
        }
        c.close();

        return a1MSGroups;
    }

    private A1MSGroup cursorToUser(Cursor c) {


        A1MSGroup a1MSGroup = new A1MSGroup();

        if (c.getCount() > 0) {
            a1MSGroup.setGroupName(c.getString(1));
            String groupMembersList = c.getString(2);
            ArrayList<String> members = new ArrayList<>();
            String[] membersSplit = groupMembersList.split("&");
            for (String member : membersSplit) {
                members.add(member);
            }
            a1MSGroup.setMembersList(members);

            a1MSGroup.setGroupId(c.getString(3));
            a1MSGroup.setAdminId(c.getString(4));

            String active = c.getString(5);
            a1MSGroup.setActivate(active.contains("1") ? true : false);
            a1MSGroup.setAvatar(c.getString(6));
        }

        return a1MSGroup;
    }

}