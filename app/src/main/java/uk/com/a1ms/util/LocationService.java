package uk.com.a1ms.util;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import uk.com.a1ms.A1MSApplication;

/**
 * Created by priju.jacobpaul on 29/06/16.
 */
public class LocationService extends Service {

    private final Context mContext;

    static double latitude; // latitude
    static double longitude; // longitude

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    // Declaring a Location Manager
    protected GetFromLocationManager locationManagerProvider;

    Activity mActivity;

    private GooglePlayLocationProvider playLocationProvider;

    public LocationService(Context context) {
        this.mContext = context;
        mActivity = ((A1MSApplication) (A1MSApplication.applicationContext)).getCurrentActivity();

        playLocationProvider = new GooglePlayLocationProvider(A1MSApplication.applicationContext);
        locationManagerProvider = new GetFromLocationManager(A1MSApplication.applicationContext);

        locationManagerProvider.getLocation();
        playLocationProvider.getLastKnownLocation();

    }

    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app
     */
    public void stopLocationListener() {

        stopSelf();

        if (!PermissionRequestManager.checkPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) ||
                !PermissionRequestManager.checkPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            return;
        }

        try {
            if (locationManagerProvider != null) {
                locationManagerProvider.stopLocationManager();
            }

            if (playLocationProvider != null) {
                playLocationProvider.stopApiClient();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Function to get latitude
     */
    public static double getLatitude() {

        // return latitude
        return latitude;
    }

    /**
     * Function to get longitude
     */
    public static double getLongitude() {

        // return longitude
        return longitude;
    }

//        /**
//         * Function to show settings alert dialog
//         * On pressing Settings button will lauch Settings Options
//         * */
//        public void showSettingsAlert(){
//            AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
//
//            // Setting Dialog Title
//            alertDialog.setTitle("GPS is settings");
//
//            // Setting Dialog Message
//            alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
//
//            // On pressing Settings button
//            alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog,int which) {
//                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                    mContext.startActivity(intent);
//                }
//            });
//
//            // on pressing cancel button
//            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.cancel();
//                }
//            });
//
//            // Showing Alert Message
//            alertDialog.show();
//        }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    private boolean isPermissionAvailable() {

        if (!PermissionRequestManager.checkPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) ||
                !PermissionRequestManager.checkPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            return false;
        }
        return true;
    }

    private class GetFromLocationManager implements LocationListener {

        // flag for GPS status
        boolean isGPSEnabled = false;

        // flag for network status
        boolean isNetworkEnabled = false;

        // flag for GPS status
        boolean canGetLocation = false;

        Context mContext;

        LocationManager locationManager;

        Location location; // location

        public GetFromLocationManager(Context context) {
            mContext = context;
        }

        @Override
        public void onLocationChanged(Location location) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        private void populateLastKnownDetails() {

            if (!isPermissionAvailable()) {
                return;
            }

            location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (location == null) {
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
            if (location == null) {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }
        }

        public void stopLocationManager() {

            if (!isPermissionAvailable()) {
                return;
            }

            if (locationManager != null) {
                locationManager.removeUpdates(GetFromLocationManager.this);
            }
        }

        /**
         * Function to check GPS/wifi enabled
         *
         * @return boolean
         */
        public boolean canGetLocation() {
            return this.canGetLocation;
        }

        public Location getLocation() {

            if (!PermissionRequestManager.checkPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) ||
                    !PermissionRequestManager.checkPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                return null;
            }

            try {
                locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

                populateLastKnownDetails();

                // getting GPS status
                isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

                // getting network status
                isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER);

                if (!isGPSEnabled && !isNetworkEnabled) {
                    // no network provider is enabled
                } else {
                        this.canGetLocation = true;
                        // First get location from Network Provider
                        if (isNetworkEnabled) {

                            locationManager.requestLocationUpdates(
                                    LocationManager.PASSIVE_PROVIDER,
                                    MIN_TIME_BW_UPDATES,
                                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                            if (locationManager != null) {
                                location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                                if (location != null) {
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                    NotificationController.getInstance().postNotificationName(NotificationController.didReceiveLocation, latitude, longitude);
                                }
                            }
                        }
                        // if GPS Enabled get lat/long using GPS Services
                        else if (isGPSEnabled) {
                            if (location == null) {
                                locationManager.requestLocationUpdates(
                                        LocationManager.GPS_PROVIDER,
                                        MIN_TIME_BW_UPDATES,
                                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                                if (locationManager != null) {
                                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                    if (location != null) {
                                        latitude = location.getLatitude();
                                        longitude = location.getLongitude();
                                        NotificationController.getInstance().postNotificationName(NotificationController.didReceiveLocation, latitude, longitude);
                                    }
                                }
                            }
                        }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return location;
        }

    }

    private class GooglePlayLocationProvider implements GoogleApiClient.ConnectionCallbacks,
            GoogleApiClient.OnConnectionFailedListener

    {

        private GoogleApiClient mGoogleApiClient;
        private Context mContext;
        private Location mLastLocation;

        public GooglePlayLocationProvider(Context context) {
            mContext = context;
            setupApiClient();
        }

        private void setupApiClient() {
            mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        public void getLastKnownLocation() {
            mGoogleApiClient.connect();

        }

        public void stopApiClient() {

            if (mGoogleApiClient != null) {
                mGoogleApiClient.disconnect();
            }
        }

        @Override
        public void onConnected(@Nullable Bundle bundle) {

            if (!isPermissionAvailable()) {
                return;
            }

            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if(mLastLocation != null) {
                latitude = mLastLocation.getLatitude();
                longitude = mLastLocation.getLongitude();
                NotificationController.getInstance().postNotificationName(NotificationController.didReceiveLocation, latitude, longitude);
            }
        }

        @Override
        public void onConnectionSuspended(int i) {

        }

        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        }
    }

}
