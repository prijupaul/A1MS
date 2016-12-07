package uk.com.a1ms.db.messages;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import uk.com.a1ms.db.A1MSMessageFieldsDataSource;


/**
 * Created by priju.jacobpaul on 28/10/2016.
 */

public class MessagesDbHelper extends SQLiteOpenHelper{

    public static final String MESSGES_DATABASE_NAME = "oats.db";
    private static final int DATABASE_VERSION = 1;
    private Context context;
    private static MessagesDbHelper mMessagesDbHelper;

    public static MessagesDbHelper getInstance(Context context){
        if(mMessagesDbHelper == null){
            mMessagesDbHelper = new MessagesDbHelper(context);
        }
        return mMessagesDbHelper;
    }

    private MessagesDbHelper(Context context){
        super(new A1MSMessagesDatabaseContext(context),MESSGES_DATABASE_NAME,null,DATABASE_VERSION);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        A1MSMessageFieldsDataSource dataSource = new A1MSMessageFieldsDataSource(context);
        dataSource.createDb(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void closeDb(){

        if(mMessagesDbHelper != null){
            mMessagesDbHelper.close();
        }
    }
}
