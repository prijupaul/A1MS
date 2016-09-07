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
                        A1MSGroupsEntry.COLUMN_NAME_A1MS_GROUPS_USER_AVATAR + TEXT_TYPE + " );";

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

    public long insertA1MSGroup(SQLiteDatabase sqLiteDatabase, A1MSGroup a1MSGroup) {

        StringBuffer members = new StringBuffer();
        int index = 0;
        for(String member:a1MSGroup.getMembersList()){
            index ++;
            members.append(member);
            if(index!= a1MSGroup.getMembersList().size()) {
                members.append("&");
            }
        }

        ContentValues values = new ContentValues();
        values.put(A1MSGroupsEntry.COLUMN_NAME_A1MS_GROUPS_USER_NAME, a1MSGroup.getGroupName());
        values.put(A1MSGroupsEntry.COLUMN_NAME_A1MS_GROUPS_ADMIN_ID, a1MSGroup.getAdminId());
        values.put(A1MSGroupsEntry.COLUMN_NAME_A1MS_GROUPS_MEMBERS_ID, members.toString());
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
            users.add("\'" + user.getGroupName() + "\'");
        }

        String args = TextUtils.join(", ", users);
        String statement = "DELETE FROM " + A1MSGroupsEntry.TABLE_NAME + " WHERE " +
                A1MSGroupsEntry.COLUMN_NAME_A1MS_GROUPS_USER_NAME + " IN" + " ( " + args + " )";

        sqLiteDatabase.execSQL(statement);

    }


    public void deleteA1MSUserFromGroup(A1MSUser a1MSUser, A1MSGroup a1MSGroup) {

        if (a1MSUser == null) {
            return;
        }

        String selection = A1MSGroupsEntry.COLUMN_NAME_A1MS_GROUPS_USER_NAME + " LIKE ?";
        String[] selectionArgs = {String.valueOf(a1MSUser.getName())};
        sqLiteDatabase.delete(A1MSGroupsEntry.TABLE_NAME, selection, selectionArgs);

    }

    public A1MSGroup getDetailsOfGroups(String groupdID,String groupName){

        String whereClause = A1MSGroupsEntry.COLUMN_NAME_A1MS_GROUPS_ID+" =? AND " +
                A1MSGroupsEntry.COLUMN_NAME_A1MS_GROUPS_USER_NAME + " =?";

        String [] whereArgs = new String[2];
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

        if(c != null) {
            c.moveToFirst();
            A1MSGroup group = cursorToUser(c);
            return group;
        }
        return null;
    }

    public A1MSGroup getDetailsOfGroups(String groupId){

        String whereClause = A1MSGroupsEntry.COLUMN_NAME_A1MS_GROUPS_ID+"=?";
        String [] whereArgs = new String[1];
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

        if(c.getCount() > 0) {
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
