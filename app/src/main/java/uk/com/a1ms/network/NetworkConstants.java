package uk.com.a1ms.network;

/**
 * Created by priju.jacobpaul on 23/06/16.
 */
public class NetworkConstants {

    public static final String USER_REGISTRATION = "/api/user";
    public static final String USER_LOGIN = "/api/login";
    public static final String USER_ACTIVATION_CODE_RESEND = "/api/user/resend";
    public static final String USER_DETAILS = "/api/user";
    public static final String USER_ACTIVATION = "/api/user/activate";
    public static final String USER_MESSAGE = "/api/message/{idUser}";
    public static final String USER_INVITE = "/api/invite";
    public static final String USER_CREATE_GROUP = "/api/group";
    public static final String USER_GET_GROUP = "/api/group";



    public static final String BASE_URL = "http://163.172.137.155:8000";

    public static final boolean IS_MOCK = false;
}
