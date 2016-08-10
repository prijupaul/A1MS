package uk.com.a1ms.messages;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.support.v4.os.ResultReceiver;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import uk.com.a1ms.A1MSApplication;

/**
 * Created by priju.jacobpaul on 1/08/16.
 */
public class FileReaderService extends IntentService {

    private String fileName ="";
    ListMultimap<String, String> acronymsArray = ArrayListMultimap.create();
    ListMultimap<String, String> sentenceArrays = ArrayListMultimap.create();
    ArrayList<String> filesToRead = new ArrayList<>();

    public FileReaderService(){
        super(FileReaderService.class.getSimpleName());

    }

    public FileReaderService(String filename) {
        super(filename);
        this.fileName = filename;

    }

    public void populateOfflineFiles(){
        filesToRead.add("M_ShortenedWordsSet1.txt");
        filesToRead.add("M_ShortenedWordsSet2.txt");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final ResultReceiver receiver = intent.getParcelableExtra("receiver");
        if( (fileName == null) || ((fileName!=null) && fileName.isEmpty())) {
            populateOfflineFiles();
        }
        else {
            filesToRead.add(fileName);
        }

        try {
            Context context = A1MSApplication.applicationContext;
            AssetManager am = context.getAssets();
            for(String filename: filesToRead) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open(filename)));
                StringBuilder stringBuilder = new StringBuilder();
                String line = reader.readLine();
                while (line != null) {
                    String[] array = line.split("\\s+", 2);
                    if (array.length == 2) {
                        acronymsArray.put(array[0], array[1]);
                        sentenceArrays.put(array[1], array[0]);
                    }
                    line = reader.readLine();
                }
                reader.close();
            }
            ((A1MSApplication)(A1MSApplication.applicationContext)).setAcronymsArray(acronymsArray);
            ((A1MSApplication)(A1MSApplication.applicationContext)).setSentenceArrays(sentenceArrays);

        } catch (IOException e) {
            e.printStackTrace();
        }

        stopSelf();
    }
}
