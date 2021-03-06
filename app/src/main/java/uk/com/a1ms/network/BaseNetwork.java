package uk.com.a1ms.network;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uk.com.a1ms.BuildConfig;
import uk.com.a1ms.network.mock.MockInterceptor;

/**
 * Created by priju.jacobpaul on 23/06/16.
 */
public class BaseNetwork {

    private String baseUrl;
    private String bearerToken;
    private Retrofit retrofit;
    protected static Call<Object> mCurrentRetrofitCall;

    protected String getBaseUrl() {
        return baseUrl;
    }

    protected void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    protected String getBearerToken() {
        return bearerToken;
    }

    protected void setBearerToken(String bearerToken) {
        this.bearerToken = bearerToken;
    }



    protected void init(){

        // Logging
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        if(BuildConfig.DEBUG) {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        }
        else {
            interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        }
        OkHttpClient httpClient;

        if(NetworkConstants.IS_MOCK){
            httpClient = new OkHttpClient.Builder().addInterceptor(new MockInterceptor()).build();
        }
        else {
             httpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request;
                    Request original = chain.request();
                    request = original;


                    if (getBearerToken() != null) {
                        // Request customization: add request headers
                        Request.Builder requestBuilder = original.newBuilder()
                                .header("Authorization", "Bearer " + getBearerToken())
                                .method(original.method(), original.body());

                        request = requestBuilder.build();

                    }
                    return chain.proceed(request);

                }
            })
                    .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).build();
        }

        retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(getBaseUrl()).client(httpClient).build();


    }


    protected Retrofit getRetrofit(){
        return retrofit;
    }

    protected static Call<Object> getCurrentRetrofitCall(){
        return mCurrentRetrofitCall;
    }
}
