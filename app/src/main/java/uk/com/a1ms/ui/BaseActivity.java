package uk.com.a1ms.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import uk.com.a1ms.A1MSApplication;
import uk.com.a1ms.BuildConfig;

/**
 * Created by priju.jacobpaul on 27/05/16.
 */
public abstract class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (BuildConfig.DEBUG) {
            // Enable strict mode checks when in debug modes
//            StrictModeUtils.enableStrictMode();
        }
        ((A1MSApplication)getApplication()).setCurrentActivity(this);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        ((A1MSApplication)getApplication()).setCurrentActivity(this);
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

    public void startActivity(Class<?> clas ,Bundle bundle,boolean finish){

        Intent intent = new Intent(this,clas);
        if(bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);

        if(finish){
            finish();
        }
    }
    public void startCreateGroupsActivity(Bundle bundle, boolean finish){

        Intent intent = new Intent(this,CreateGroupsActivity.class);
        if(bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);

        if(finish){
            finish();
        }
    }

    protected void startRegistrationActivity(Bundle bundle,boolean finish){

        Intent intent = new Intent(this,MainActivity.class);
        if(bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);

        if(finish){
            finish();
        }
    }

}
