package uk.com.a1ms.network;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import uk.com.a1ms.network.dto.GroupDetails;
import uk.com.a1ms.network.dto.MemberGroupDetails;
import uk.com.a1ms.network.dto.UserDetails;

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
    Call<UserDetails> doActivatePhoneNumber(@Field("code") String code,
                                            @Field("id") String id);

    @FormUrlEncoded
    @POST(NetworkConstants.USER_LOGIN)
    Call<UserDetails> doUserLogin(@Field("username") String username,
                                            @Field("password") String password);

    @FormUrlEncoded
    @POST(NetworkConstants.USER_ACTIVATION_CODE_RESEND)
    Call<UserDetails> doResendActivationCode(@Field("id") String id);


    @FormUrlEncoded
    @POST(NetworkConstants.USER_INVITE)
    Call<UserDetails> doInviteUser(@Field("email") String email,
                                   @Field("number") String number);



    @FormUrlEncoded
    @POST(NetworkConstants.USER_MESSAGE)
    Call<Object> doSendUserMessage(@Path("idUser")
                                @Field("shortMessage") String shortMessage,
                                @Field("message")String message,
                                @Field("idToUser")String idToUser,
                                @Field("idUser") String idUser);

    @FormUrlEncoded
    @POST(NetworkConstants.USER_CREATE_GROUP)
    Call<GroupDetails> doCreateGroup(@Field("name") String name);


    @GET(NetworkConstants.USER_CREATE_GROUP)
    Call<MemberGroupDetails> getMembershipGroups();

    @GET(NetworkConstants.USER_DETAILS)
    Call<Object> getUserDetails();
}

