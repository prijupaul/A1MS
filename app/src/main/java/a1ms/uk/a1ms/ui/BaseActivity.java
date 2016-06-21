package a1ms.uk.a1ms.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import a1ms.uk.a1ms.A1MSApplication;
import a1ms.uk.a1ms.BuildConfig;
import a1ms.uk.a1ms.util.StrictModeUtils;

/**
 * Created by priju.jacobpaul on 27/05/16.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        if (BuildConfig.DEBUG) {
            // Enable strict mode checks when in debug modes
            StrictModeUtils.enableStrictMode();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((A1MSApplication)getApplication()).setCurrentActivity(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        ((A1MSApplication)getApplication()).setCurrentActivity(null);
    }


    protected void startContactsGroupsActivity(Bundle bundle, boolean finish){

        Intent intent = new Intent(this,ContactsGroupsActivity.class);
        if(bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);

        if(finish){
            finish();
        }
    }

    public abstract void updateUi(Object object);
}
