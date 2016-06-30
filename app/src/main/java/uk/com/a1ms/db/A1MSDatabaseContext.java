package uk.com.a1ms.db;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import com.orhanobut.logger.Logger;
import java.io.File;
import uk.com.a1ms.util.BuildUtils;

/**
 * Created by priju.jacobpaul on 29/06/16.
 */
public class A1MSDatabaseContext extends ContextWrapper {

    public static final String TAG = A1MSDatabaseContext.class.getSimpleName();

    public A1MSDatabaseContext(Context context) {
        super(context);
    }

    @Override
    public File getDatabasePath(String name) {
        if (!BuildUtils.isDbOnSDCard()) {
            return super.getDatabasePath(name);
        }

        File sdCard = Environment.getExternalStorageDirectory();
        if (sdCard != null) {
            String dbFile = sdCard.getAbsolutePath() + File.separator + "A1MS" + File.separator + "Databases" + File.separator + name;
            if (!dbFile.endsWith(".db")) {
                dbFile += ".db";
            }

            File result = new File(dbFile);
            if (!result.getParentFile().exists()) {
                result.getParentFile().mkdirs();
            }

            Logger.w(TAG,"getDatabasePath(" + name + ") = " + result.getAbsolutePath());

            return result;
        }

        return super.getDatabasePath(name);
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory) {
        if(!BuildUtils.isDbOnSDCard()) {
            return super.openOrCreateDatabase(name, mode, factory);
        }

        SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);

        Logger.w(TAG,"openOrCreateDatabase(" + name + ",,) = " + result.getPath());

        return result;
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler errorHandler) {
        if (!BuildUtils.isDbOnSDCard()) {
            return super.openOrCreateDatabase(name,mode,factory,errorHandler);
        }

        return openOrCreateDatabase(name, mode, factory);
    }
}
