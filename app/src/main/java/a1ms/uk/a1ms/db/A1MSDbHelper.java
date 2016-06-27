package a1ms.uk.a1ms.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import a1ms.uk.a1ms.BuildConfig;
import a1ms.uk.a1ms.db.mock.AddMockUsers;

/**
 * Created by priju.jacobpaul on 6/06/16.
 */
public class A1MSDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "a1ms.db";
    private Context mContext;

    public A1MSDbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
       A1MSUsersFieldsDataSource dataSource =  new A1MSUsersFieldsDataSource(mContext);
        dataSource.createDb(sqLiteDatabase);
        if(BuildConfig.DEBUG){
            AddMockUsers.addMockA1MSUsers(dataSource,sqLiteDatabase);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

}
