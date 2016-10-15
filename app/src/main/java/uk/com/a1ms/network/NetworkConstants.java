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
    public static final String USER_GROUP = "/api/group";
    public static final String USER_REMOVE_GROUP = "/api/group/userRemove";
    public static final String USER_GET_GROUP = "/api/group";
    public static final String ADMIN_CHANGE_ADMIN = "/api/admin/changeAdmin";



    public static final String BASE_URL = "http://chat.voltric.io:8000"; // http://163.172.137.155:8000";

    public static final boolean IS_MOCK = false;

    public static final int RESPONSE_CODE_SUCCESS = 200;

}
