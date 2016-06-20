package a1ms.uk.a1ms.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;

import a1ms.uk.a1ms.A1MSApplication;
import a1ms.uk.a1ms.R;
import a1ms.uk.a1ms.ui.fragments.RegistrationAcceptActivationFragment;
import a1ms.uk.a1ms.ui.fragments.RegistrationAcceptPhoneFragment;
import a1ms.uk.a1ms.util.AndroidUtils;
import a1ms.uk.a1ms.util.SharedPreferenceManager;

public class MainActivity extends BaseActivity implements RegistrationAcceptPhoneFragment.OnRegoAcceptPhoneFragmentInteractionListener,
        RegistrationAcceptActivationFragment.OnRegoActivationFragmentInteractionListener
{

    private FrameLayout mFrameLayoutHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if(SharedPreferenceManager.isFirstTimeLaunch(this)) {
            setContentView(R.layout.mainactivity);
            mFrameLayoutHolder = (FrameLayout)findViewById(R.id.framelayout_holder);
            launchRegoAcceptPhoneFragment();
        }
        else {

            Intent messagingActivity = new Intent(getApplicationContext(),MessagingActivity.class);
            startActivity(messagingActivity);
            finish();
        }

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setCollapsible(false);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("Activation");

        ((A1MSApplication)getApplication()).setCurrentActivity(this);

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

                break;
            }
        }
    }

    @Override
    public void updateUi(Object object) {

    }

    private void launchRegoAcceptPhoneFragment(){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        Fragment fr = RegistrationAcceptPhoneFragment.newInstance();
        fragmentTransaction.replace(R.id.framelayout_holder, fr);
        fragmentTransaction.commit();
    }

    @Override
    public void onNextPressed(String countryCode, String phoneNo) {
        if(!countryCode.isEmpty() && !phoneNo.isEmpty()){

            // TODO:
            // Send the phone number to the server
            AndroidUtils.setWaitingForSms(true);
            launchRegoInputActivationCodeFragment("+" + countryCode + phoneNo);
        }
    }

    private void launchRegoInputActivationCodeFragment(String phoneNumber){

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        Fragment fr = RegistrationAcceptActivationFragment.newInstance(phoneNumber);
        fragmentTransaction.replace(R.id.framelayout_holder, fr);
        fragmentTransaction.commit();

    }

    @Override
    public void onSendActivationCode(String activationCode) {
        // TODO:
        // Send the activation code to the server
        AndroidUtils.setWaitingForSms(false);
        startContactsGroupsActivity(null,true);
    }

}
