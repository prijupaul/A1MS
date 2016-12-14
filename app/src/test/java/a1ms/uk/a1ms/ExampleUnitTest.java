package a1ms.uk.a1ms;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testPhoneNumber(){
        String swissNumberStr = "044 668 18 00";
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {


            Phonenumber.PhoneNumber swissNumberProto = phoneUtil.parse(swissNumberStr, "AU");

            if(phoneUtil.isValidNumber(swissNumberProto)){
                System.out.println("number is not valid");
                return;
            }


            System.out.println(swissNumberProto.getCountryCode());
            System.out.println(swissNumberProto.getNationalNumber());
            System.err.println(phoneUtil.format(swissNumberProto, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL));

        } catch (NumberParseException e) {
            System.err.println("NumberParseException was thrown: " + e.toString());
        }
    }

    @Test
    public void testPhoneNumberWithLeadingZero(){
        String swissNumberStr = "+61(0)43576111";
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber swissNumberProto = phoneUtil.parse(swissNumberStr, "gb");

            if(!phoneUtil.isValidNumber(swissNumberProto)){
                System.out.println("number is not valid");
                return;
            }

            System.out.println(swissNumberProto.getCountryCode());
            System.out.println(swissNumberProto.getNationalNumber());
            System.err.println(phoneUtil.format(swissNumberProto, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL));

        } catch (NumberParseException e) {
            System.err.println("NumberParseException was thrown: " + e.toString());
        }
    }

    @Test
    public void testPhoneNumberWithLeadingCountryCode(){
        String swissNumberStr = "0091446681800";
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

        try {
            Phonenumber.PhoneNumber swissNumberProto = phoneUtil.parse(swissNumberStr, "AU");

            if(phoneUtil.isValidNumber(swissNumberProto)){
                System.out.println("number is not valid");
                return;
            }

            System.out.println(swissNumberProto.getCountryCode());
            System.out.println(swissNumberProto.getNationalNumber());

            System.err.println(phoneUtil.format(swissNumberProto, PhoneNumberUtil.PhoneNumberFormat.NATIONAL));

        } catch (NumberParseException e) {
            System.err.println("NumberParseException was thrown: " + e.toString());
        }
    }

    @Test
    public void checkcountryCode() {
        System.out.println(Locale.getDefault().getCountry());
    }
}