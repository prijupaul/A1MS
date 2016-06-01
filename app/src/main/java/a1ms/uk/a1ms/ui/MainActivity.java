package a1ms.uk.a1ms.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import a1ms.uk.a1ms.A1MSApplication;
import a1ms.uk.a1ms.R;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ((A1MSApplication)getApplication()).setCurrentActivity(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_chats,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_deals_notification:{
                return true;
            }
            case R.id.action_contacts:{
                startContactsGroupsActivity(null,false);
                return true;
            }
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case R.integer.MY_PERMISSIONS_REQUEST_READ_CONTACTS:{
//                FetchContactsLoader contactsLoader = new FetchContactsLoader(getApplicationContext(),this);
//                contactsLoader.loadContactsWithSMS();
                break;
            }
        }
    }

//    @Override
//    public void onContactsLoadComplete(Map<String, Contacts> contactsList) {
//
//    }

    @Override
    public void updateUi(Object object) {

    }
}
