package uk.com.a1ms.util;

import android.Manifest;
import android.app.Activity;
import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by priju.jacobpaul on 22/07/16.
 */

public class FileUtil {

    public final static String FILE_EXTENSION_SEPARATOR = ".";
    public final static String A1MS_MEDIA_FOLDER = "Media";
    public final static String A1MS_PROFILE_PHOTOS = "A1MS Profile Photos";
    public final static String FILE_SEPERATOR = "/";
    public final static String A1MS_ROOT = "A1MS";


    private FileUtil() {
    }

    public static void createMediaFolder(Activity activity){

        if(!PermissionRequestManager.checkPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            return;
        }

        File rootMediaFolder = new File(Environment.getExternalStorageDirectory(),
                FILE_SEPERATOR + A1MS_ROOT + FILE_SEPERATOR + A1MS_MEDIA_FOLDER + FILE_SEPERATOR + A1MS_PROFILE_PHOTOS);

        if(!rootMediaFolder.exists()){
            rootMediaFolder.mkdirs();
        }

    }

    public static String getA1msProfilePhotosFolder(){
        return Environment.getExternalStorageDirectory() +  FILE_SEPERATOR + A1MS_ROOT + FILE_SEPERATOR + A1MS_MEDIA_FOLDER + FILE_SEPERATOR + A1MS_PROFILE_PHOTOS;
    }


    public static File generateGroupProfilePhotos(){

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        return new File(getA1msProfilePhotosFolder(), "IMG_" + timeStamp + ".jpg");

    }


}


