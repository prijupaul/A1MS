package uk.com.a1ms.network;

import retrofit2.http.GET;
import retrofit2.http.Path;
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
    @POST(NetworkConstants.USER_ACTIVATION_2)
    Call<UserDetails> doActivatePhoneNumber(@Field("code") String code,
                                            @Field("id") String id);

    @FormUrlEncoded
    @POST(NetworkConstants.USER_ACTIVATION_CODE_RESEND)
    Call<UserDetails> doResendActivationCode(@Field("id") String id);

    @FormUrlEncoded
    @POST(NetworkConstants.USER_MESSAGE)
    Call<Object> doSendUserMessage(@Path("idUser")
                                @Field("shortMessage") String shortMessage,
                                @Field("message")String message,
                                @Field("idToUser")String idToUser,
                                @Field("idUser") String idUser);


    @GET(NetworkConstants.USER_DETAILS)
    Call<Object> getUserDetails();
}

