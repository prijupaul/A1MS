package uk.com.a1ms.ui;

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
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import uk.com.a1ms.R;
import uk.com.a1ms.network.dto.UserDetails;
import uk.com.a1ms.network.handlers.UserActivationNetworkHandler;
import uk.com.a1ms.ui.fragments.RegistrationAcceptActivationFragment;
import uk.com.a1ms.ui.fragments.RegistrationAcceptPhoneFragment;
import uk.com.a1ms.ui.uiutil.ProgressView;
import uk.com.a1ms.util.AndroidUtils;
import uk.com.a1ms.util.LocationService;
import uk.com.a1ms.util.NotificationController;
import uk.com.a1ms.util.PermissionRequestManager;
import uk.com.a1ms.util.SharedPreferenceManager;

public class MainActivity extends BaseActivity implements RegistrationAcceptPhoneFragment.OnRegoAcceptPhoneFragmentInteractionListener,
        RegistrationAcceptActivationFragment.OnRegoActivationFragmentInteractionListener, NotificationController.NotificationListener {

    private FrameLayout mFrameLayoutHolder;
    private final String TAG = MainActivity.class.getSimpleName();
    private LocationService mLocationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

//        SharedPreferenceManager.setFirstTimeLaunch(false,this);
        setContentView(R.layout.mainactivity);

        mFrameLayoutHolder = (FrameLayout) findViewById(R.id.framelayout_holder);
        mLocationService = new LocationService(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setCollapsible(false);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setTitle("Activation");
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        NotificationController.getInstance().addObserver(this,NotificationController.didReceiveLocation);

        if (SharedPreferenceManager.isFirstTimeLaunch(this)) {
            launchRegoAcceptPhoneFragment();
        } else {
            startContactsGroupsActivity(null, true);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();
        NotificationController.getInstance().removeObserver(this,NotificationController.didReceiveLocation);
        mLocationService.stopLocationListener();
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


        switch (requestCode) {
            case 1: {
//                if ((grantResults.length > 1) && (grantResults[0] != PackageManager.PERMISSION_GRANTED)) {
//                    DialogUtil.showOKDialog(this,
//                            getString(R.string.permission_title),
//                            getString(R.string.permission_receive_sms),
//                            getString(android.R.string.ok),
//                            null, false);
//                }
                break;
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void launchRegoAcceptPhoneFragment() {

//        if (!PermissionRequestManager.checkPermission(this, Manifest.permission.RECEIVE_SMS)) {
//            ArrayList<String> permissons = new ArrayList<>();
//            permissons.add(Manifest.permission.RECEIVE_SMS);
//            permissons.add(Manifest.permission.ACCESS_COARSE_LOCATION);
//            permissons.add(Manifest.permission.ACCESS_FINE_LOCATION);
//
//            String[] items = permissons.toArray(new String[permissons.size()]);
//            PermissionRequestManager.requestPermission(this, items, 1);
//        }

        PermissionRequestManager.checkAndRequestPermission(this, Manifest.permission.RECEIVE_SMS,1);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        Fragment fr = RegistrationAcceptPhoneFragment.newInstance();
        fragmentTransaction.replace(R.id.framelayout_holder, fr);
        fragmentTransaction.commit();

    }

    @Override
    public void onNextPressed(final String countryCode, final String phoneNo) {
        if (!countryCode.isEmpty() && !phoneNo.isEmpty()) {

            // TODO: remove the deprecated fields.


            mFrameLayoutHolder.setEnabled(false);
            ProgressView.addProgressView(mFrameLayoutHolder, "Registering +" + countryCode + phoneNo);

            UserActivationNetworkHandler builder = new UserActivationNetworkHandler.UserActivationNetworkHandlerBuilder()
                    .setMobileNumber(phoneNo)
                    .setName("deprecated")
                    .setPassword("password124")
                    .build();
            builder.doRegisterUserWithMobileNumber(new UserActivationNetworkHandler.UserActivationListener() {
                @Override
                public void onUserActivationResponse(UserDetails details) {

                    ProgressView.removeProgressView(mFrameLayoutHolder);

                    if ((details != null) && (details.getToken() != null)) {
                        SharedPreferenceManager.saveUserToken(details.getToken(), MainActivity.this);
                        SharedPreferenceManager.saveUserId(details.getUser().getId(),MainActivity.this);

                    }

                    AndroidUtils.setWaitingForSms(true);
                    launchRegoInputActivationCodeFragment("+" + countryCode + phoneNo);
                }

                @Override
                public void onUserActivationError() {
                    ProgressView.removeProgressView(mFrameLayoutHolder);
                }
            });
        }
    }

    private void launchRegoInputActivationCodeFragment(String phoneNumber) {

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

        UserActivationNetworkHandler activationNetworkHandler = new UserActivationNetworkHandler.UserActivationNetworkHandlerBuilder()
                .setActivationCode(activationCode)
                .setUserID(SharedPreferenceManager.getUserId(this))
                .build();
        activationNetworkHandler.doActivateUserWithCode(new UserActivationNetworkHandler.UserActivationListener() {
            @Override
            public void onUserActivationResponse(UserDetails userDetails) {
                AndroidUtils.setWaitingForSms(false);
                startContactsGroupsActivity(null, true);
            }

            @Override
            public void onUserActivationError() {
                Toast.makeText(MainActivity.this,"Activation error",Toast.LENGTH_LONG).show();
            }
        });


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        UserActivationNetworkHandler.cancelNetworkOperation();
        ProgressView.removeProgressView(mFrameLayoutHolder);
    }

    @Override
    public void onNotificationReceived(int id, Object... args) {

        if(id == NotificationController.didReceiveLocation){

            Double latitude = (Double) args[0];
            Double longitude = (Double) args[1];
            Logger.d("Latitude %f" , latitude);
            Logger.d("Longitude %f" , longitude);

            if(mLocationService != null){
                mLocationService.stopLocationListener();
            }
            NotificationController.getInstance().removeObserver(this,NotificationController.didReceiveLocation);
        }
    }
}
