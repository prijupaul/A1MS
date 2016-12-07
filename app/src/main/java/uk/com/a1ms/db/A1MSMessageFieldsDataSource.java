package uk.com.a1ms.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.text.SpannableString;
import android.text.TextUtils;

import uk.com.a1ms.db.dto.A1MSUser;
import uk.com.a1ms.dto.LongMessage;
import uk.com.a1ms.dto.Message;
import uk.com.a1ms.dto.ShortMessage;
import uk.com.a1ms.util.DateTime;
import uk.com.a1ms.util.ExecutorUtils;
import uk.com.a1ms.util.NotificationController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by priju.jacobpaul on 6/06/16.
 */
public class A1MSMessageFieldsDataSource extends BaseFields{


    private SQLiteDatabase sqLiteDatabase = null;
    private A1MSDbHelper   a1MSDbHelper;


    public static abstract class A1MSMessageEntry implements BaseColumns{
        public static final String TABLE_NAME = "Messages";
        public static final String COLUMN_NAME_NULLABLE = "nullhack";
        public static final String COLUMN_NAME_A1MS_USER_ID = "userid";
        public static final String COLUMN_NAME_A1MS_TO_USER_ID = "toUserId";

        // This should be the viewing id. for private message, if the message is send
        // then this should be the touser id and if the message is received, it sud be the
        // from user id.

        // for groups, always the group id.
        public static final String COLUMN_NAME_A1MS_VIEWING_USER_ID = "viewUserId";

        public static final String COLUMN_NAME_A1MS_MESSAGE = "longMessage";
        public static final String COLUMN_NAME_A1MS_SHORT_MESSAGE = "shortMessage";
        public static final String COLUMN_NAME_A1MS_MESSAGE_ID = "messageId";
        public static final String COLUMN_NAME_A1MS_DATE_TIME = "dateTime";
        public static final String COLUMN_NAME_A1MS_ISREAD = "IsRead";

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + A1MSMessageEntry.TABLE_NAME + " (" +
                        A1MSMessageEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        A1MSMessageEntry.COLUMN_NAME_A1MS_USER_ID + TEXT_TYPE + COMMA_SEP +
                        A1MSMessageEntry.COLUMN_NAME_A1MS_TO_USER_ID + TEXT_TYPE + COMMA_SEP +
                        A1MSMessageEntry.COLUMN_NAME_A1MS_DATE_TIME + TEXT_TYPE + COMMA_SEP +
                        A1MSMessageEntry.COLUMN_NAME_A1MS_VIEWING_USER_ID + TEXT_TYPE + COMMA_SEP +
                        A1MSMessageEntry.COLUMN_NAME_A1MS_MESSAGE_ID + TEXT_TYPE + COMMA_SEP +
                        A1MSMessageEntry.COLUMN_NAME_A1MS_MESSAGE + TEXT_TYPE + COMMA_SEP +
                        A1MSMessageEntry.COLUMN_NAME_A1MS_SHORT_MESSAGE + TEXT_TYPE + COMMA_SEP +
                        A1MSMessageEntry.COLUMN_NAME_A1MS_ISREAD + BOOL_TYPE  +" );";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + A1MSMessageEntry.TABLE_NAME;

        private static String[] allColumns = {
                A1MSMessageEntry._ID,
                A1MSMessageEntry.COLUMN_NAME_A1MS_USER_ID,
                A1MSMessageEntry.COLUMN_NAME_A1MS_TO_USER_ID,
                A1MSMessageEntry.COLUMN_NAME_A1MS_MESSAGE,
                A1MSMessageEntry.COLUMN_NAME_A1MS_SHORT_MESSAGE,
                A1MSMessageEntry.COLUMN_NAME_A1MS_MESSAGE_ID,
                A1MSMessageEntry.COLUMN_NAME_A1MS_DATE_TIME,
                A1MSMessageEntry.COLUMN_NAME_A1MS_ISREAD,
                A1MSMessageEntry.COLUMN_NAME_A1MS_USER_ID
        };
    }


    public A1MSMessageFieldsDataSource(Context context){
        a1MSDbHelper = A1MSDbHelper.getInstance(context);
    }


    public void open() throws SQLException {

        ExecutorUtils.runInBackgroundThread(new Runnable() {
            @Override
            public void run() {
                sqLiteDatabase = a1MSDbHelper.getWritableDatabase();

                ExecutorUtils.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        NotificationController.getInstance().postNotificationName(NotificationController.didOpenDatabase,null);
                    }
                },0);

            }
        });
    }

    public void close(){

        if(a1MSDbHelper!= null){
            a1MSDbHelper.close();
        }
    }

    public void createDb(SQLiteDatabase sqLiteDatabase){
        sqLiteDatabase.execSQL(A1MSMessageEntry.SQL_CREATE_ENTRIES);
    }

    public  long insertMessage(SQLiteDatabase sqLiteDatabase,Message message,boolean isMessageReceived,boolean isGroup,boolean isRead){

        ContentValues values = new ContentValues();
        values.put(A1MSMessageEntry.COLUMN_NAME_A1MS_TO_USER_ID,message.getIdToUser().getUserId());
        values.put(A1MSMessageEntry.COLUMN_NAME_A1MS_USER_ID,message.getIdUser().getUserId());
        values.put(A1MSMessageEntry.COLUMN_NAME_A1MS_DATE_TIME, DateTime.getDateTime());
        values.put(A1MSMessageEntry.COLUMN_NAME_A1MS_MESSAGE_ID, message.getMessageId());
        if(isGroup) {
         values.put(A1MSMessageEntry.COLUMN_NAME_A1MS_VIEWING_USER_ID,message.getIdToUser().getUserId());
        }
        else {
            values.put(A1MSMessageEntry.COLUMN_NAME_A1MS_VIEWING_USER_ID, (isMessageReceived == true) ? message.getIdUser().getUserId() : message.getIdToUser().getUserId());
        }
        values.put(A1MSMessageEntry.COLUMN_NAME_A1MS_ISREAD,isRead);
        values.put(A1MSMessageEntry.COLUMN_NAME_A1MS_MESSAGE,message.getMessage().getLongMessage().toString());
        values.put(A1MSMessageEntry.COLUMN_NAME_A1MS_SHORT_MESSAGE,message.getShortMessage().getShortMessage().toString());

        long newRowId = sqLiteDatabase.insert(
                A1MSMessageEntry.TABLE_NAME,
                A1MSMessageEntry.COLUMN_NAME_NULLABLE,
                values);

        return newRowId;
    }




    public  void deleteMessages(List<Message> messages){

        if (messages == null) {
            return;
        }

        ArrayList<String> messagesArray = new ArrayList<>();
        for (Message message : messages) {
            messagesArray.add("\'" + message.getMessageId() + "\'");
        }

        String args = TextUtils.join(", ", messagesArray);
        String statement = "DELETE FROM " + A1MSMessageFieldsDataSource.A1MSMessageEntry.TABLE_NAME + " WHERE " +
                A1MSMessageEntry.COLUMN_NAME_A1MS_MESSAGE_ID + " IN" + " ( " + args + " )";
        sqLiteDatabase.execSQL(statement);

    }


    public  void deleteMessage(Message message){

        if(message == null){
            return;
        }

        String selection = A1MSMessageEntry.COLUMN_NAME_A1MS_MESSAGE_ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(message.getMessageId())};
        sqLiteDatabase.delete(A1MSMessageEntry.TABLE_NAME,selection,selectionArgs);

    }

    public List<Message> getAllMessagesBetweenUsers(A1MSUser fromUserId, A1MSUser toUserId){

        List<Message> messageList = new ArrayList<>();

        String[] projection = {
                A1MSMessageEntry.COLUMN_NAME_A1MS_TO_USER_ID,
                A1MSMessageEntry.COLUMN_NAME_A1MS_USER_ID,
                A1MSMessageEntry.COLUMN_NAME_A1MS_DATE_TIME,
                A1MSMessageEntry.COLUMN_NAME_A1MS_MESSAGE_ID,
                A1MSMessageEntry.COLUMN_NAME_A1MS_ISREAD,
                A1MSMessageEntry.COLUMN_NAME_A1MS_MESSAGE,
                A1MSMessageEntry.COLUMN_NAME_A1MS_SHORT_MESSAGE
        };

        String whereClause = "( " + A1MSMessageEntry.COLUMN_NAME_A1MS_USER_ID + " = ?" +
                            " OR " + A1MSMessageEntry.COLUMN_NAME_A1MS_USER_ID + " = ?" + " ) " +
                            "AND " + "( " +A1MSMessageEntry.COLUMN_NAME_A1MS_TO_USER_ID + " = ?" +
                            " OR " + A1MSMessageEntry.COLUMN_NAME_A1MS_TO_USER_ID + " = ?" + " )";
        String[] whereArgs = new String[] {
                fromUserId.getUserId(),toUserId.getUserId(),
                toUserId.getUserId(),fromUserId.getUserId()
        };

        String sortOrder = A1MSMessageEntry.COLUMN_NAME_A1MS_DATE_TIME + " ASC";
        Cursor c = sqLiteDatabase.query(A1MSMessageEntry.TABLE_NAME,
                projection,
                whereClause,
                whereArgs,
                null,
                null,
                sortOrder);

        if(c != null) {
            c.moveToFirst();


            while (!c.isAfterLast()){
                Message message = cursorToUser(c);
                messageList.add(message);
                }
                c.moveToNext();
            }
            c.close();

        return messageList;
    }


//    public List<Message> getAllMessagesContaining(String message, A1MS)


//    public List<A1MSUser> getAllA1MSUsers(boolean removeEchomate,boolean includeGroups){
//
//        List<A1MSUser> a1MSUsers = new ArrayList<>();
//
//        String[] projection = {
//                A1MSMessageEntry._ID,
//                A1MSMessageEntry.COLUMN_NAME_A1MS_USER_NAME,
//                A1MSMessageEntry.COLUMN_NAME_A1MS_USER_MOB,
//                A1MSMessageEntry.COLUMN_NAME_A1MS_USER_EMAIL,
//                A1MSMessageEntry.COLUMN_NAME_A1MS_USER_TOKEN,
//                A1MSMessageEntry.COLUMN_NAME_A1MS_USER_ID,
//                A1MSMessageEntry.COLUMN_NAME_A1MS_USER_EDITABLE,
//                A1MSMessageEntry.COLUMN_NAME_A1MS_IS_GROUP,
//                A1MSMessageEntry.COLUMN_NAME_A1MS_USER_AVATAR
//        };
//
//        String whereClause = null;
//        String [] whereArgs = null;
//
//        if(!includeGroups) {
//            whereClause = A1MSMessageEntry.COLUMN_NAME_A1MS_IS_GROUP+"=?";
//            whereArgs = new String[1];
//            whereArgs[0] = "0";
//        }
//
//
//
//        String sortOrder = A1MSMessageEntry.COLUMN_NAME_A1MS_USER_NAME + " ASC";
//        Cursor c = sqLiteDatabase.query(
//                A1MSMessageEntry.TABLE_NAME,  // The table to query
//                projection,                               // The columns to return
//                whereClause,                                // The columns for the WHERE clause
//                whereArgs,                               // The values for the WHERE clause
//                null,                                     // don't group the rows
//                null,                                     // don't filter by row groups
//                sortOrder                                 // The sort order
//        );
//
//        if(c != null) {
//            c.moveToFirst();
//            boolean isEchomateFound = false;
//
//            while (!c.isAfterLast()){
//                A1MSUser a1MSUser = cursorToUser(c);
//                if(!isEchomateFound && a1MSUser.getName().contentEquals("Echo Mate")){
//                    isEchomateFound  = true;
//                    if(!removeEchomate) {
//                        a1MSUsers.add(0, a1MSUser);
//                    }
//                }
//                else {
//                    a1MSUsers.add(a1MSUser);
//                }
//                c.moveToNext();
//            }
//            c.close();
//        }
//
//        return a1MSUsers;
//    }

    private Message cursorToUser(Cursor c){

        Message message = new Message();

        A1MSUser toUser = new A1MSUser();
        toUser.setUserId(c.getString(1));
        message.setIdToUser(toUser);

        A1MSUser fromUser = new A1MSUser();
        fromUser.setUserId(c.getString(2));
        message.setIdUser(fromUser);

        message.setDateTime(c.getString(3));
        message.setMessageId(c.getString(4));

        message.setRead( (c.getInt(5) == 0) ? false : true );

        LongMessage longMessage = new LongMessage();
        longMessage.setLongMessage(new SpannableString(c.getString(6)));
        message.setMessage(longMessage);

        ShortMessage shortMessage = new ShortMessage();
        shortMessage.setShortMessage(new SpannableString(c.getString(7)));
        message.setShortMessage(shortMessage);

        return message;
    }

}
