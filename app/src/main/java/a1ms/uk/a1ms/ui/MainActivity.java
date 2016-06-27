package a1ms.uk.a1ms.ui;

import android.Manifest;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import java.util.ArrayList;

import a1ms.uk.a1ms.R;
import a1ms.uk.a1ms.dialogutil.DialogUtil;
import a1ms.uk.a1ms.network.dto.UserDetails;
import a1ms.uk.a1ms.network.handlers.UserActivationNetworkHandler;
import a1ms.uk.a1ms.ui.fragments.RegistrationAcceptActivationFragment;
import a1ms.uk.a1ms.ui.fragments.RegistrationAcceptPhoneFragment;
import a1ms.uk.a1ms.util.AndroidUtils;
import a1ms.uk.a1ms.util.PermissionRequestManager;
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

            Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
            if(toolbar != null) {
                toolbar.setCollapsible(false);
                setSupportActionBar(toolbar);
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                getSupportActionBar().setTitle("Activation");
            }

            launchRegoAcceptPhoneFragment();
        }
        else {
            startContactsGroupsActivity(null,true);
        }

    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.action_deals_notification:{
//                return true;
//            }
//            case R.id.action_contacts:{
//                startContactsGroupsActivity(null,false);
//                return true;
//            }
//        }
//        return false;
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case 1:{
                DialogUtil.showOKDialog(this,
                        getString(R.string.permission_title),
                        getString(R.string.permission_receive_sms),
                        getString(android.R.string.ok),
                        null, false);
                break;
            }
        }
    }

    @Override
    public void updateUi(Object object) {

    }

    @TargetApi(Build.VERSION_CODES.M)
    private void launchRegoAcceptPhoneFragment(){


        if (!PermissionRequestManager.checkPermission(this,Manifest.permission.RECEIVE_SMS)) {
            ArrayList<String> permissons = new ArrayList<>();
            permissons.add(Manifest.permission.RECEIVE_SMS);
            String[] items = permissons.toArray(new String[permissons.size()]);
            PermissionRequestManager.requestPermission(this,items,1);
        }


        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        Fragment fr = RegistrationAcceptPhoneFragment.newInstance();
        fragmentTransaction.replace(R.id.framelayout_holder, fr);
        fragmentTransaction.commit();
    }

    @Override
    public void onNextPressed(final String countryCode, final String phoneNo) {
        if(!countryCode.isEmpty() && !phoneNo.isEmpty()){

            UserActivationNetworkHandler builder = new UserActivationNetworkHandler.UserActivationNetworkHandlerBuilder()
                    .setMobileNumber(phoneNo)
                    .setName("deprecated")
                    .setPassword("password124")
                    .build();
            builder.doRegisterUserWithMobileNumber(new UserActivationNetworkHandler.UserActivationListener() {
                @Override
                public void onUserActivationResponse(UserDetails details) {
                    if(details != null){
                        SharedPreferenceManager.saveUserToken(details.getToken(),MainActivity.this);
                    }

                    AndroidUtils.setWaitingForSms(true);
                    launchRegoInputActivationCodeFragment("+" + countryCode + phoneNo);
                }

                @Override
                public void onUserActivationError() {

                }
            });



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
