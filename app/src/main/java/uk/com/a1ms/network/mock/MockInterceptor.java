package uk.com.a1ms.network.mock;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import uk.com.a1ms.network.NetworkConstants;

/**
 * Created by priju.jacobpaul on 5/07/16.
 */
public class MockInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        HttpUrl url = original.url();
        String encoded = url.encodedPath();

        if (encoded.equals(NetworkConstants.USER_REGISTRATION)) {
            return mockUserRegistrationResponse(chain);
        }
        else if(encoded.equals(NetworkConstants.USER_LOGIN)) {
            return mockUserLogin(chain);
        }
        else if(encoded.equals(NetworkConstants.USER_ACTIVATION)){
            return mockUserActivation(chain);
        }

        // nothing to handle. proceed with the original request
        return chain.proceed(original);
    }

    private Response mockUserRegistrationResponse(Chain chain) {

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String jsonResponse = "{\"user\":{\"username\":\"00447811396\",\"name\":\"Geffy\",\"dateCreated\":\"2016-07-04T19:59:59.820Z\",\"isActive\":true,\"isAdmin\":true,\"isRetailer\":false,\"code\":\"53170\",\"isDeleted\":false,\"id\":\"ad390cfd-ed3d-49a5-894f-799c857e974a\"},\"token\":\"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc0FkbWluIjp0cnVlLCJpc1JldGFpbGVyIjpmYWxzZSwiaXNBY3RpdmUiOnRydWUsImlhdCI6MTQ2NzY2MjM5OSwiZXhwIjoxNDY3NjcxMzk5fQ.QAZCEMWAYwvtgAq9sM9aynUMcELbYRqmEcEXzxhLomU\"}";

        return new Response.Builder()
                .code(200)
                .message("MockResponse")
                .protocol(Protocol.HTTP_1_1)
                .request(chain.request())
                .body(ResponseBody.create(MediaType.parse("application/json"), jsonResponse))
                .build();
    }



    private Response mockUserLogin(Chain chain){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String jsonResponse = "{\"user\":{\"code\":\"9973\",\"dateCreated\":\"2016-07-05T20:11:09.717Z\",\"id\":\"161da82f-8f36-4723-929f-8ef03de8de00\",\"ids\":\"f1289d6e-cfbe-53c4-a674-116d5421974f\",\"isActiSent\":false,\"isActive\":true,\"isAdmin\":false,\"isDeleted\":false,\"isRetailer\":false,\"name\":\"deprecated\",\"username\":\"2145236589\"},\"token\":\"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6IjE2MWRhODJmLThmMzYtNDcyMy05MjlmLThlZjAzZGU4ZGUwMCIsImlzQWRtaW4iOmZhbHNlLCJpc1JldGFpbGVyIjpmYWxzZSwiaXNBY3RpdmUiOnRydWUsImlhdCI6MTQ2Nzc0OTQ5MSwiZXhwIjoxNDY3NzU4NDkxfQ.gxIVp8I6EJ0JC32U7GqOn2mkagUbW8KpNxlw5FDJ-00\"}";

        return new Response.Builder()
                .code(200)
                .message("MockResponse")
                .protocol(Protocol.HTTP_1_1)
                .request(chain.request())
                .body(ResponseBody.create(MediaType.parse("application/json"), jsonResponse))
                .build();
    }

    private Response mockUserActivation(Chain chain){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String jsonResponse = "{\"code\":\"805\",\"Error\":\"User Activated!\"}";

        return new Response.Builder()
                .code(200)
                .message("MockResponse")
                .protocol(Protocol.HTTP_1_1)
                .request(chain.request())
                .body(ResponseBody.create(MediaType.parse("application/json"), jsonResponse))
                .build();
    }
}

