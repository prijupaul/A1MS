package uk.com.a1ms.network.handlers;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.com.a1ms.network.BaseNetwork;
import uk.com.a1ms.network.NetworkServices;

/**
 * Created by priju.jacobpaul on 24/06/16.
 */
public class UserDetailsNetworkHandler extends BaseNetwork {

    public interface UserDetailsNetworkListener{
        void onUserDetailsResponse();
        void onUserDetailsError();
    }


    public static class UserDetailsNetworkBuilder{
        private String bearerToken;

        public UserDetailsNetworkBuilder setBearerToken(String bearerToken) {
            this.bearerToken = bearerToken;
            return this;
        }

        public UserDetailsNetworkHandler build(){
            UserDetailsNetworkHandler userDetailsNetworkHandler = new UserDetailsNetworkHandler();
            userDetailsNetworkHandler.setBearerToken(bearerToken);
            userDetailsNetworkHandler.init();
            return userDetailsNetworkHandler;
        }

    }

    public void getUserDetails(final UserDetailsNetworkListener listener){

        NetworkServices userMessageSendService = getRetrofit().create(NetworkServices.class);
        final Call<Object> call = userMessageSendService.getUserDetails();

        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                listener.onUserDetailsResponse();
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                listener.onUserDetailsError();
            }
        });
    }




}
