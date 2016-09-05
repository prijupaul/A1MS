package uk.com.a1ms.network;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import uk.com.a1ms.network.dto.GroupDetails;
import uk.com.a1ms.network.dto.MemberGroupDetails;
import uk.com.a1ms.network.dto.UserDetails;
import uk.com.a1ms.network.dto.loginDetails;

/**
 * Created by priju.jacobpaul on 23/06/16.
 */
public interface NetworkServices {

    @FormUrlEncoded
    @POST(NetworkConstants.USER_REGISTRATION)
    Call<loginDetails> doRegisterPhoneNumber(
            @Field("username") String phoneNumber,
            @Field("password") String password,
            @Field("name") String name);


    @FormUrlEncoded
    @POST(NetworkConstants.USER_ACTIVATION)
    Call<loginDetails> doActivatePhoneNumber(@Field("code") String code,
                                            @Field("id") String id);

    @FormUrlEncoded
    @POST(NetworkConstants.USER_LOGIN)
    Call<UserDetails> doUserLogin(@Field("username") String username,
                                  @Field("password") String password,
                                  @Field("latitude")String latitude,
                                  @Field("longitude")String longitude,
                                  @Field("locale")String locale,
                                  @Field("imei")String imei,
                                  @Field("macAddress")String macAddress,
                                  @Field("countryCode")String countryCode,
                                  @Field("androidVersion")String androidVersion,
                                  @Field("manufacture")String manufacture,
                                  @Field("language")String language,
                                  @Field("country")String country);


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
                                @Field("idUser") String idUser,
                                @Field("messageId")String guid);

    @FormUrlEncoded
    @POST(NetworkConstants.USER_CREATE_GROUP)
    Call<GroupDetails> doCreateGroup(@Field("name") String name,
                                     @Field("members") ArrayList<String>members);


    @GET(NetworkConstants.USER_CREATE_GROUP)
    Call<MemberGroupDetails> getMembershipGroups();

    @GET(NetworkConstants.USER_DETAILS)
    Call<Object> getUserDetails();
}

