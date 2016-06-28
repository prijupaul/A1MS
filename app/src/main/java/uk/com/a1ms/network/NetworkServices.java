package uk.com.a1ms.network;

import uk.com.a1ms.network.dto.UserDetails;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by priju.jacobpaul on 23/06/16.
 */
public interface NetworkServices {

    @FormUrlEncoded
    @POST(NetworkConstants.USER_REGISTRATION)
    Call<UserDetails> doRegisterPhoneNumber(
            @Field("username") String phoneNumber,
            @Field("password") String password,
            @Field("name") String name);


    @FormUrlEncoded
    @POST(NetworkConstants.USER_ACTIVATION)
    Call<UserDetails> doActivatePhoneNumber(@Field("username") String phoneNumber,
                                            @Field("password") String password);

}

