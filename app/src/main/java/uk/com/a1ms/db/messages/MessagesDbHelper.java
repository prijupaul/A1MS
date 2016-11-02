package uk.com.a1ms.db.messages;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import uk.com.a1ms.db.A1MSDatabaseContext;

import static uk.com.a1ms.db.A1MSDbHelper.DATABASE_NAME;
import static uk.com.a1ms.db.A1MSDbHelper.DATABASE_VERSION;

/**
 * Created by priju.jacobpaul on 28/10/2016.
 */

public class MessagesDbHelper extends SQLiteOpenHelper{

    public static final String MESSGES_DATABASE_NAME = "oats.db";
    private Context context;
    private static MessagesDbHelper mMessagesDbHelper;

    public static MessagesDbHelper getInstance(Context context){
        if(mMessagesDbHelper == null){
            mMessagesDbHelper = new MessagesDbHelper(context);
        }
        return mMessagesDbHelper;
    }

    private MessagesDbHelper(Context context){
        super(new A1MSDatabaseContext(context),DATABASE_NAME,null,DATABASE_VERSION);
        this.context = context;
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
