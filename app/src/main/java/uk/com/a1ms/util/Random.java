package uk.com.a1ms.util;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by priju.jacobpaul on 6/07/16.
 */
public class Random {


    public static String getRandomString(){
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }
}
