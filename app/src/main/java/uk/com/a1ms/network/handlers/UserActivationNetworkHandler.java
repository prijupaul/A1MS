package uk.com.a1ms.network.handlers;


import uk.com.a1ms.network.BaseNetwork;
import uk.com.a1ms.network.NetworkConstants;
import uk.com.a1ms.network.NetworkServices;
import uk.com.a1ms.network.dto.UserDetails;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    @Deprecated
    private String name;
    @Deprecated
    private String password;

    private String userId;

    private UserActivationListener userActivationListener;

    public static class UserActivationNetworkHandlerBuilder{

        private  String bearerToken;
        private String activationCode;
        private String mobileNumber;
        private String name;
        private String password;
        private String userID;

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


        public UserActivationNetworkHandler build(){

            UserActivationNetworkHandler userActivationNetworkHandler= new UserActivationNetworkHandler();
            userActivationNetworkHandler.setActivationBearerToken(bearerToken);
            userActivationNetworkHandler.setActivationCode(activationCode);
            userActivationNetworkHandler.setMobileNumber(mobileNumber);
            userActivationNetworkHandler.setPassword(password);
            userActivationNetworkHandler.setName(name);
            userActivationNetworkHandler.setUserId(userID);
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
