package uk.com.a1ms.util;

import android.Manifest;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import java.util.Locale;

import uk.com.a1ms.A1MSApplication;
import uk.com.a1ms.R;

/**
 * Created by priju.jacobpaul on 17/06/16.
 */
public class PhoneConfigUtils {

    public static String getCountryCode(){
        return Locale.getDefault().getCountry();
    }
    public static String getCountryLocale(){

        String countryCode = getCountryCode();
        Locale locale = new Locale("",countryCode);
        return Locale.getDefault().getDisplayCountry(locale);
    }

    public static String getCountryPhoneCode(Context context){

        String CountryID="";
        String CountryPhoneCode="";

        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        //getNetworkCountryIso
        CountryID= manager.getSimCountryIso().toUpperCase();
        if(CountryID.isEmpty()) {
            CountryID = Locale.getDefault().getCountry();
        }


        String[] rl=context.getResources().getStringArray(R.array.CountryCodes);
        for(int i=0;i<rl.length;i++){
            String[] g=rl[i].split(",");
            if(g[1].trim().equals(CountryID.trim())){
                CountryPhoneCode=g[0];
                break;
            }
        }
        return CountryPhoneCode;
    }

    public static String getMacAddress(){

        WifiManager m_wm = (WifiManager)A1MSApplication.applicationContext.getSystemService(Context.WIFI_SERVICE);
        String m_wlanMacAdd = m_wm.getConnectionInfo().getMacAddress();
        return m_wlanMacAdd;

    }
    public static String getIMEI(){

        if(!PermissionRequestManager.checkPermission(A1MSApplication.applicationContext, Manifest.permission.READ_PHONE_STATE)){
            return "";
        }

        TelephonyManager telephonyManager = (TelephonyManager) A1MSApplication.applicationContext.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    public static String getAndroidSecureId(){

        final String androidId = Settings.Secure.getString(
                A1MSApplication.applicationContext.getContentResolver(), Settings.Secure.ANDROID_ID);
        return androidId;
    }

    public static int getAndroidVersion(){
        return Build.VERSION.SDK_INT;
    }

    public static String getDisplayLanguage(){
        return Locale.getDefault().getDisplayLanguage(Locale.US);
    }

    public static String getDisplayCountry(){
        return Locale.getDefault().getDisplayCountry(Locale.US);
    }

    public static String getManufacture(){
        return Build.MANUFACTURER;
    }


}

