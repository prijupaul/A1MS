package uk.com.a1ms.network.handlers;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.com.a1ms.network.BaseNetwork;
import uk.com.a1ms.network.NetworkConstants;
import uk.com.a1ms.network.NetworkServices;
import uk.com.a1ms.network.dto.UserDetails;

/**
 * Created by priju.jacobpaul on 6/07/16.
 */
public class UserInviteNetworkHandler extends BaseNetwork {

    public interface UserInviteNetworkListener{
        void onInviteResponse();
        void onInviteError();
    }

    private String bearerToken;
    private String mobileNumber;
    private String userEmail;


    public UserInviteNetworkHandler(){
        super();
        setBaseUrl(NetworkConstants.BASE_URL);
    }

    @Override
    public String getBearerToken() {
        return bearerToken;
    }

    @Override
    public void setBearerToken(String bearerToken) {
        this.bearerToken = bearerToken;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }


    public static class UserInviteNetworkBuilder{

        private String bearerToken;
        private String mobileNumber;
        private String userEmail;

        public UserInviteNetworkBuilder setBearerToken(String bearerToken){
            this.bearerToken = bearerToken;
            return this;
        }

        public UserInviteNetworkBuilder setMobileNumber(String mobileNumber){
            this.mobileNumber = mobileNumber;
            return this;
        }

        public UserInviteNetworkBuilder setUserEmail(String userEmail){
            this.userEmail = userEmail;
            return this;
        }

        public UserInviteNetworkHandler build(){
            UserInviteNetworkHandler userInviteNetworkHandler = new UserInviteNetworkHandler();
            userInviteNetworkHandler.setUserEmail(userEmail);
            userInviteNetworkHandler.setMobileNumber(mobileNumber);
            userInviteNetworkHandler.setBearerToken(bearerToken);
            userInviteNetworkHandler.init();
            return userInviteNetworkHandler;
        }
    }

    public void sendInviteToUser(final UserInviteNetworkListener inviteNetworkListener){

        NetworkServices userInviteService = getRetrofit().create(NetworkServices.class);
        final Call<UserDetails> call = userInviteService.doInviteUser(getUserEmail(),getMobileNumber());
        call.enqueue(new Callback<UserDetails>() {
            @Override
            public void onResponse(Call<UserDetails> call, Response<UserDetails> response) {
                inviteNetworkListener.onInviteResponse();
            }

            @Override
            public void onFailure(Call<UserDetails> call, Throwable t) {
                inviteNetworkListener.onInviteError();
            }
        });
    }
}
