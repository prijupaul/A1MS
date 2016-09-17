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
import uk.com.a1ms.db.dto.A1MSUser;

/**
 * Created by priju.jacobpaul on 6/06/16.
 */
public class A1MSUsersFieldsDataSource extends BaseFields {


    private SQLiteDatabase sqLiteDatabase = null;
    private A1MSDbHelper a1MSDbHelper;


    public static abstract class A1MSUsersEntry implements BaseColumns {
        public static final String TABLE_NAME = "A1MSUsers";
        public static final String COLUMN_NAME_NULLABLE = "nullhack";
        public static final String COLUMN_NAME_A1MS_USER_NAME = "username";
        public static final String COLUMN_NAME_A1MS_USER_MOB = "mobileno";
        public static final String COLUMN_NAME_A1MS_USER_EMAIL = "emailid";
        public static final String COLUMN_NAME_A1MS_USER_AVATAR = "avatar";
        public static final String COLUMN_NAME_A1MS_USER_TOKEN = "token";
        public static final String COLUMN_NAME_A1MS_USER_ID = "userid";
        public static final String COLUMN_NAME_A1MS_USER_EDITABLE = "editable";
        public static final String COLUMN_NAME_A1MS_IS_GROUP = "isGroup";
        public static final String COLUMN_NAME_A1MS_IS_ECHOMATE = "isEchomate";

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + A1MSUsersEntry.TABLE_NAME + " (" +
                        A1MSUsersEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        A1MSUsersEntry.COLUMN_NAME_A1MS_USER_NAME + TEXT_TYPE + COMMA_SEP +
                        A1MSUsersEntry.COLUMN_NAME_A1MS_USER_MOB + TEXT_TYPE + COMMA_SEP +
                        A1MSUsersEntry.COLUMN_NAME_A1MS_USER_EMAIL + TEXT_TYPE + COMMA_SEP +
                        A1MSUsersEntry.COLUMN_NAME_A1MS_USER_TOKEN + TEXT_TYPE + COMMA_SEP +
                        A1MSUsersEntry.COLUMN_NAME_A1MS_USER_ID + TEXT_TYPE + COMMA_SEP +
                        A1MSUsersEntry.COLUMN_NAME_A1MS_USER_EDITABLE + TEXT_TYPE + COMMA_SEP +
                        A1MSUsersEntry.COLUMN_NAME_A1MS_IS_GROUP + TEXT_TYPE + COMMA_SEP +
                        A1MSUsersEntry.COLUMN_NAME_A1MS_USER_AVATAR + TEXT_TYPE + COMMA_SEP +
                        "PRIMARY KEY (" + A1MSUsersEntry.COLUMN_NAME_A1MS_USER_MOB + "," + A1MSUsersEntry.COLUMN_NAME_A1MS_USER_ID + ")" + " );";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + A1MSUsersEntry.TABLE_NAME;

        private static String[] allColumns = {
                A1MSUsersEntry._ID,
                A1MSUsersEntry.COLUMN_NAME_A1MS_USER_NAME,
                A1MSUsersEntry.COLUMN_NAME_A1MS_USER_MOB,
                A1MSUsersEntry.COLUMN_NAME_A1MS_USER_EMAIL,
                A1MSUsersEntry.COLUMN_NAME_A1MS_USER_TOKEN,
                A1MSUsersEntry.COLUMN_NAME_A1MS_USER_ID,
                A1MSUsersEntry.COLUMN_NAME_A1MS_USER_EDITABLE,
                A1MSUsersEntry.COLUMN_NAME_A1MS_IS_GROUP,
                A1MSUsersEntry.COLUMN_NAME_A1MS_USER_AVATAR
        };
    }


    public A1MSUsersFieldsDataSource(Context context) {
        a1MSDbHelper = A1MSDbHelper.getInstance(context);
    }


    public void open() throws SQLException {

        sqLiteDatabase = A1MSApplication.getSqLiteDatabase();

//        ExecutorUtils.runInBackgroundThread(new Runnable() {
//            @Override
//            public void run() {
//                sqLiteDatabase = a1MSDbHelper.getWritableDatabase();
//
//                ExecutorUtils.runOnUIThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        NotificationController.getInstance().postNotificationName(NotificationController.didOpenDatabase,null);
//                    }
//                },0);
//
//            }
//        });
    }

    public void close() {

//        if(a1MSDbHelper!= null){
//            a1MSDbHelper.close();
//        }
    }

    public void createDb(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(A1MSUsersFieldsDataSource.A1MSUsersEntry.SQL_CREATE_ENTRIES);
    }

    public long insertA1MSUser(SQLiteDatabase sqLiteDatabase, A1MSUser a1MSUser) {

        ContentValues values = new ContentValues();
        values.put(A1MSUsersEntry.COLUMN_NAME_A1MS_USER_NAME, a1MSUser.getName());
        values.put(A1MSUsersEntry.COLUMN_NAME_A1MS_USER_MOB, a1MSUser.getMobile());
        values.put(A1MSUsersEntry.COLUMN_NAME_A1MS_USER_EMAIL, a1MSUser.getEmail());
        values.put(A1MSUsersEntry.COLUMN_NAME_A1MS_USER_AVATAR, a1MSUser.getAvatar());
        values.put(A1MSUsersEntry.COLUMN_NAME_A1MS_USER_TOKEN, a1MSUser.getToken());
        values.put(A1MSUsersEntry.COLUMN_NAME_A1MS_USER_ID, a1MSUser.getUserId());
        values.put(A1MSUsersEntry.COLUMN_NAME_A1MS_USER_EDITABLE, a1MSUser.isEditable());
        values.put(A1MSUsersEntry.COLUMN_NAME_A1MS_IS_GROUP, a1MSUser.isGroup());

        long newRowId = sqLiteDatabase.insert(
                A1MSUsersEntry.TABLE_NAME,
                A1MSUsersEntry.COLUMN_NAME_NULLABLE,
                values);

        return newRowId;
    }

    public void deleteA1MSUsers(List<A1MSUser> a1MSUser) {

        if (a1MSUser == null) {
            return;
        }

        ArrayList<String> users = new ArrayList<>();
        for (A1MSUser user : a1MSUser) {
            users.add("\'" + user.getName() + "\'");
        }

        String args = TextUtils.join(", ", users);
        String statement = "DELETE FROM " + A1MSUsersEntry.TABLE_NAME + " WHERE " +
                A1MSUsersEntry.COLUMN_NAME_A1MS_USER_NAME + " IN" + " ( " + args + " )";

        sqLiteDatabase.execSQL(statement);

    }


    public void deleteA1MSUser(A1MSUser a1MSUser) {

        if (a1MSUser == null) {
            return;
        }

        String selection = A1MSUsersEntry.COLUMN_NAME_A1MS_USER_NAME + " LIKE ?";
        String[] selectionArgs = {String.valueOf(a1MSUser.getName())};
        sqLiteDatabase.delete(A1MSUsersEntry.TABLE_NAME, selection, selectionArgs);

    }

    public ArrayList<A1MSUser> getA1MSUsersDetails(ArrayList<String> usersList) {

        ArrayList<A1MSUser> a1MSUsers = new ArrayList<>();

        StringBuffer whereClause = new StringBuffer();
        whereClause.append(A1MSUsersEntry.COLUMN_NAME_A1MS_USER_ID  + " = ?" );
        String[] whereArgs = new String[usersList.size()];
        int index = 0;
        for (String userId : usersList) {
            whereArgs[index] = userId;
            index++;

            if(index != usersList.size()){
                whereClause.append(" OR ");
                whereClause.append(A1MSUsersEntry.COLUMN_NAME_A1MS_USER_ID  + " = ?" );
            }
        }


        String[] projection = {
                A1MSUsersEntry._ID,
                A1MSUsersEntry.COLUMN_NAME_A1MS_USER_NAME,
                A1MSUsersEntry.COLUMN_NAME_A1MS_USER_MOB,
                A1MSUsersEntry.COLUMN_NAME_A1MS_USER_EMAIL,
                A1MSUsersEntry.COLUMN_NAME_A1MS_USER_TOKEN,
                A1MSUsersEntry.COLUMN_NAME_A1MS_USER_ID,
                A1MSUsersEntry.COLUMN_NAME_A1MS_USER_EDITABLE,
                A1MSUsersEntry.COLUMN_NAME_A1MS_IS_GROUP,
                A1MSUsersEntry.COLUMN_NAME_A1MS_USER_AVATAR
        };

        String sortOrder = A1MSUsersEntry.COLUMN_NAME_A1MS_USER_NAME + " ASC";
        Cursor c = sqLiteDatabase.query(
                A1MSUsersEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                whereClause.toString(),                                // The columns for the WHERE clause
                whereArgs,                               // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        if (c != null) {
            c.moveToFirst();

            while (!c.isAfterLast()) {
                A1MSUser a1MSUser = cursorToUser(c);
                a1MSUsers.add(a1MSUser);
                c.moveToNext();
            }

        }
        c.close();

        return a1MSUsers;
    }


    public List<A1MSUser> getAllA1MSGroups() {

        List<A1MSUser> a1MSUsers = new ArrayList<>();
        String whereClause = A1MSUsersEntry.COLUMN_NAME_A1MS_IS_GROUP + " = ?";
        String[] whereArgs = new String[]{
                "1"
        };

        String[] projection = {
                A1MSUsersEntry._ID,
                A1MSUsersEntry.COLUMN_NAME_A1MS_USER_NAME,
                A1MSUsersEntry.COLUMN_NAME_A1MS_USER_MOB,
                A1MSUsersEntry.COLUMN_NAME_A1MS_USER_EMAIL,
                A1MSUsersEntry.COLUMN_NAME_A1MS_USER_TOKEN,
                A1MSUsersEntry.COLUMN_NAME_A1MS_USER_ID,
                A1MSUsersEntry.COLUMN_NAME_A1MS_USER_EDITABLE,
                A1MSUsersEntry.COLUMN_NAME_A1MS_IS_GROUP,
                A1MSUsersEntry.COLUMN_NAME_A1MS_USER_AVATAR
        };

        String sortOrder = A1MSUsersEntry.COLUMN_NAME_A1MS_USER_NAME + " ASC";
        Cursor c = sqLiteDatabase.query(
                A1MSUsersEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                whereClause,                                // The columns for the WHERE clause
                whereArgs,                               // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        if (c != null) {
            c.moveToFirst();


            boolean isEchomateFound = false;

            while (!c.isAfterLast()) {
                A1MSUser a1MSUser = cursorToUser(c);
                if (!isEchomateFound && a1MSUser.getName().contentEquals("Echo Mate")) {
                    isEchomateFound = true;
                    a1MSUsers.add(0, a1MSUser);
                } else {
                    a1MSUsers.add(a1MSUser);
                }
                c.moveToNext();
            }
            c.close();
        }

        return a1MSUsers;
    }


    public List<A1MSUser> getAllA1MSUsers(boolean removeEchomate, boolean includeGroups) {

        List<A1MSUser> a1MSUsers = new ArrayList<>();

        String[] projection = {
                A1MSUsersEntry._ID,
                A1MSUsersEntry.COLUMN_NAME_A1MS_USER_NAME,
                A1MSUsersEntry.COLUMN_NAME_A1MS_USER_MOB,
                A1MSUsersEntry.COLUMN_NAME_A1MS_USER_EMAIL,
                A1MSUsersEntry.COLUMN_NAME_A1MS_USER_TOKEN,
                A1MSUsersEntry.COLUMN_NAME_A1MS_USER_ID,
                A1MSUsersEntry.COLUMN_NAME_A1MS_USER_EDITABLE,
                A1MSUsersEntry.COLUMN_NAME_A1MS_IS_GROUP,
                A1MSUsersEntry.COLUMN_NAME_A1MS_USER_AVATAR
        };

        String whereClause = null;
        String[] whereArgs = null;

        if (!includeGroups) {
            whereClause = A1MSUsersEntry.COLUMN_NAME_A1MS_IS_GROUP + "=?";
            whereArgs = new String[1];
            whereArgs[0] = "0";
        }


        String sortOrder = A1MSUsersEntry.COLUMN_NAME_A1MS_USER_NAME + " ASC";
        Cursor c = sqLiteDatabase.query(
                A1MSUsersEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                whereClause,                                // The columns for the WHERE clause
                whereArgs,                               // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        if (c != null) {
            c.moveToFirst();
            boolean isEchomateFound = false;

            while (!c.isAfterLast()) {
                A1MSUser a1MSUser = cursorToUser(c);
                if (!isEchomateFound && a1MSUser.getName().contentEquals("Echo Mate")) {
                    isEchomateFound = true;
                    if (!removeEchomate) {
                        a1MSUsers.add(0, a1MSUser);
                    }
                } else {
                    a1MSUsers.add(a1MSUser);
                }
                c.moveToNext();
            }
            c.close();
        }

        return a1MSUsers;
    }

    private A1MSUser cursorToUser(Cursor c) {
        A1MSUser a1MSUser = new A1MSUser();
        a1MSUser.setName(c.getString(1));
        a1MSUser.setMobile(c.getString(2));
        a1MSUser.setEmail(c.getString(3));
        a1MSUser.setToken(c.getString(4));
        a1MSUser.setUserId(c.getString(5));
        String editable = c.getString(6);
        a1MSUser.setEditable(editable.contains("1") ? true : false);
        String isGroup = c.getString(7);
        a1MSUser.setGroup(isGroup.contains("1") ? true : false);
        a1MSUser.setAvatar(c.getString(8));
        return a1MSUser;
    }

}
