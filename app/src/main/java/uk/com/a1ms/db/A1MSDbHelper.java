package uk.com.a1ms.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import uk.com.a1ms.BuildConfig;
import uk.com.a1ms.db.dto.A1MSUser;
import uk.com.a1ms.db.mock.AddMockUsers;

/**
 * Created by priju.jacobpaul on 6/06/16.
 */
public class A1MSDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "a1ms.db";
    private Context mContext;
    private static A1MSDbHelper mDbHelper;

    public static A1MSDbHelper getInstance(Context context){
        if(mDbHelper == null){
            mDbHelper = new A1MSDbHelper(context);
        }
        return mDbHelper;
    }

    private A1MSDbHelper(Context context){
        super(new A1MSDatabaseContext(context),DATABASE_NAME,null,DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
       A1MSUsersFieldsDataSource dataSource =  new A1MSUsersFieldsDataSource(mContext);
        dataSource.createDb(sqLiteDatabase);
        if(BuildConfig.DEBUG){
            AddMockUsers.addMockA1MSUsers(dataSource,sqLiteDatabase);
        }

        addEchomate(dataSource,sqLiteDatabase);

        A1MSGroupsFieldsDataSource groupsDataSource =  new A1MSGroupsFieldsDataSource(mContext);
        groupsDataSource.createDb(sqLiteDatabase);

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

    public void closeDb(){

        if(mDbHelper != null){
            mDbHelper.close();
        }
    }

    private void addEchomate(A1MSUsersFieldsDataSource dataSource,SQLiteDatabase sqLiteDatabase){

        A1MSUser a1MSUser = new A1MSUser();
        a1MSUser.setEmail("info@a1ms-uk.com");
        a1MSUser.setMobile("01234 567 8901");
        a1MSUser.setUserId("priju-echos-0000-0000-0000");
        a1MSUser.setName("Echo Mate");
        a1MSUser.setEchomate(true);
        a1MSUser.setEditable(false);
        dataSource.insertA1MSUser(sqLiteDatabase,a1MSUser);
    }
}
