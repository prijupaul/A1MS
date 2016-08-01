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

import uk.com.a1ms.A1MSApplication;

/**
 * Created by priju.jacobpaul on 1/08/16.
 */
public class FileReaderService extends IntentService {

    private String fileName;
    ListMultimap<String, String> acronymsArray = ArrayListMultimap.create();
    ListMultimap<String, String> sentenceArrays = ArrayListMultimap.create();

    public FileReaderService(){
        super(FileReaderService.class.getSimpleName());
    }

    public FileReaderService(String filename) {
        super(filename);
        this.fileName = filename;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final ResultReceiver receiver = intent.getParcelableExtra("receiver");
        try {
            Context context = A1MSApplication.applicationContext;
            AssetManager am = context.getAssets();
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open("M_ShortenedWordsSet1.txt")));
            StringBuilder stringBuilder = new StringBuilder();
            String line = reader.readLine();
            while (line != null) {
                String[] array = line.split("\\s+",2);
                if(array.length == 2) {
                    acronymsArray.put(array[0], array[1]);
                    sentenceArrays.put(array[1],array[0]);
                }
               line =  reader.readLine();
            }
            reader.close();
            ((A1MSApplication)(A1MSApplication.applicationContext)).setAcronymsArray(acronymsArray);
            ((A1MSApplication)(A1MSApplication.applicationContext)).setSentenceArrays(sentenceArrays);

        } catch (IOException e) {
            e.printStackTrace();
        }

        stopSelf();
    }
}
