package uk.com.a1ms.network.handlers;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.com.a1ms.network.BaseNetwork;
import uk.com.a1ms.network.NetworkConstants;
import uk.com.a1ms.network.NetworkServices;
import uk.com.a1ms.network.dto.UserDetails;

/**
 * Created by priju.jacobpaul on 24/06/16.
 */
public class UserActivationNetworkHandler extends BaseNetwork {

    public interface UserActivationListener{
        void onUserActivationResponse(UserDetails userDetails);
        void onUserActivationError();
    }

    private String bearerToken;
    private String activationCode;
    private String mobileNumber;
    private String latitude;
    private String longitude;
    private String locale;
    private String imei;
    private String countryCode;
    private String androidVersion;
    private String manufacture;
    private String language;
    private String country;

    @Deprecated
    private String name;
    private String password;

    private String userId;

    private UserActivationListener userActivationListener;

    public static class UserActivationNetworkHandlerBuilder{

        private String bearerToken;
        private String activationCode;
        private String mobileNumber;
        private String name;
        private String password;
        private String userID;

        private String latitude;
        private String longitude;
        private String locale;
        private String imei;
        private String countryCode;
        private String androidVersion;
        private String manufacture;
        private String language;
        private String country;

        public  UserActivationNetworkHandlerBuilder setBearerToken(String bearerToken ){
            this.bearerToken = bearerToken;
            return this;
        }

        public UserActivationNetworkHandlerBuilder setActivationCode(String activationCode){
            this.activationCode = activationCode;
            return this;
        }

        public UserActivationNetworkHandlerBuilder setMobileNumber(String mobileNumber){
            this.mobileNumber = mobileNumber;
            return this;
        }

        public UserActivationNetworkHandlerBuilder setPassword(String password){
            this.password = password;
            return this;
        }

        public UserActivationNetworkHandlerBuilder setName(String name){
            this.name = name;
            return this;
        }

        public UserActivationNetworkHandlerBuilder setUserID(String userID){
            this.userID = userID;
            return this;
        }

        public UserActivationNetworkHandlerBuilder setLatitude(String latitude){
            this.latitude = latitude;
            return this;
        }

        public UserActivationNetworkHandlerBuilder setLongitude(String longitude){
            this.longitude = longitude;
            return this;
        }

        public UserActivationNetworkHandlerBuilder setLocale(String locale){
            this.locale = locale;
            return this;
        }

        public UserActivationNetworkHandlerBuilder setIMEI(String imei){
            this.imei = imei;
            return this;
        }

        public UserActivationNetworkHandlerBuilder setCountryCode(String countryCode){
            this.countryCode = countryCode;
            return this;
        }

        public UserActivationNetworkHandlerBuilder setAndroidVersion(String androidVersion){
            this.androidVersion = androidVersion;
            return this;
        }

        public UserActivationNetworkHandlerBuilder setManufacture(String manufacture){
            this.manufacture = manufacture;
            return this;
        }

        public UserActivationNetworkHandlerBuilder setLanguage(String language){
            this.language = language;
            return this;
        }

        public UserActivationNetworkHandlerBuilder setCountry(String country){
            this.country = country;
            return this;
        }


        public UserActivationNetworkHandler build(){

            UserActivationNetworkHandler userActivationNetworkHandler= new UserActivationNetworkHandler();
            userActivationNetworkHandler.setActivationBearerToken(bearerToken);
            userActivationNetworkHandler.setActivationCode(activationCode);
            userActivationNetworkHandler.setMobileNumber(mobileNumber);
            userActivationNetworkHandler.setPassword(password);
            userActivationNetworkHandler.setName(name);
            userActivationNetworkHandler.setUserId(userID);
            userActivationNetworkHandler.setLatitude(latitude);
            userActivationNetworkHandler.setLongitude(longitude);
            userActivationNetworkHandler.setImei(imei);
            userActivationNetworkHandler.setAndroidVersion(androidVersion);
            userActivationNetworkHandler.setLocale(locale);
            userActivationNetworkHandler.setCountryCode(countryCode);
            userActivationNetworkHandler.setManufacture(manufacture);
            userActivationNetworkHandler.setLanguage(language);
            userActivationNetworkHandler.setCountry(country);
            userActivationNetworkHandler.init();
            return userActivationNetworkHandler;
        }
    }

    public UserActivationNetworkHandler(){
        super();
        setBaseUrl(NetworkConstants.BASE_URL);
    }


    public void setActivationBearerToken(String bearerToken){
        this.bearerToken = bearerToken;
        setBearerToken(this.bearerToken);
    }

    public void setActivationCode(String activationCode){
        this.activationCode = activationCode;
    }

    public void setMobileNumber(String mobileNumber){
        this.mobileNumber = mobileNumber;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setUserId(String userId){
        this.userId = userId;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public void setAndroidVersion(String androidVersion) {
        this.androidVersion = androidVersion;
    }

    public void setManufacture(String manufacture) {
        this.manufacture = manufacture;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void doActivateUserWithCode(UserActivationListener listener){

        this.userActivationListener = listener;

        NetworkServices userActivation = getRetrofit().create(NetworkServices.class);
        final Call<UserDetails> call = userActivation.doActivatePhoneNumber(activationCode,userId);
        call.enqueue(new Callback<UserDetails>() {
            @Override
            public void onResponse(Call<UserDetails> call, Response<UserDetails> response) {
                userActivationListener.onUserActivationResponse(response.body());
            }

            @Override
            public void onFailure(Call<UserDetails> call, Throwable t) {
                userActivationListener.onUserActivationError();
            }
        });

    }

    public void doResendActivationCode(UserActivationListener listener){

        this.userActivationListener = listener;

        NetworkServices userActivation = getRetrofit().create(NetworkServices.class);
        final Call<UserDetails> call = userActivation.doResendActivationCode(userId);
        call.enqueue(new Callback<UserDetails>() {
            @Override
            public void onResponse(Call<UserDetails> call, Response<UserDetails> response) {
                userActivationListener.onUserActivationResponse(response.body());
            }

            @Override
            public void onFailure(Call<UserDetails> call, Throwable t) {
                userActivationListener.onUserActivationError();
            }
        });

    }

    /**
     * This is the last step of two step login.
     * 1. Registration
     * 2. Activation
     * 3. Login
     * @param listener
     */
    public void doUserLogin(UserActivationListener listener){

        this.userActivationListener = listener;

        NetworkServices userActivation = getRetrofit().create(NetworkServices.class);
        final Call<UserDetails> call = userActivation.doUserLogin(mobileNumber,password,latitude,longitude,
                locale,imei,countryCode,androidVersion,manufacture,language,country);
        call.enqueue(new Callback<UserDetails>() {
            @Override
            public void onResponse(Call<UserDetails> call, Response<UserDetails> response) {
                userActivationListener.onUserActivationResponse(response.body());
            }

            @Override
            public void onFailure(Call<UserDetails> call, Throwable t) {
                userActivationListener.onUserActivationError();
            }
        });

    }


    public void doRegisterUserWithMobileNumber(UserActivationListener listener){

        this.userActivationListener = listener;

        NetworkServices userActivation = getRetrofit().create(NetworkServices.class);
        final Call<UserDetails> call = userActivation.doRegisterPhoneNumber(mobileNumber,password,name);
        call.enqueue(new Callback<UserDetails>() {
            @Override
            public void onResponse(Call<UserDetails> call, Response<UserDetails> response) {
                userActivationListener.onUserActivationResponse(response.body());
            }

            @Override
            public void onFailure(Call<UserDetails> call, Throwable t) {
                userActivationListener.onUserActivationError();
            }
        });
    }

    public static void cancelNetworkOperation(){

        if ((getCurrentRetrofitCall() != null) && !getCurrentRetrofitCall().isCanceled()){
            getCurrentRetrofitCall().cancel();
        }
    }

}
