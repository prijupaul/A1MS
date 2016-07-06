package uk.com.a1ms.util;

import android.content.Context;
import android.telephony.TelephonyManager;

import java.util.Locale;

import uk.com.a1ms.A1MSApplication;
import uk.com.a1ms.R;

/**
 * Created by priju.jacobpaul on 17/06/16.
 */
public class PhoneConfigUtils {

    public static String getCountryLocale(){

        String countryCode = Locale.getDefault().getCountry();
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

    public static String getIMEI(){

        TelephonyManager telephonyManager = (TelephonyManager) A1MSApplication.applicationContext.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }
}

