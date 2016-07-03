package uk.com.a1ms.network.handlers;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.com.a1ms.network.BaseNetwork;
import uk.com.a1ms.network.NetworkConstants;
import uk.com.a1ms.network.NetworkServices;

/**
 * Created by priju.jacobpaul on 3/07/16.
 */
public class UserMessageNetworkHandler extends BaseNetwork {

    public interface UserMessageNetworkListener{
        void onMessageSendResponse();
        void onMessageSendError();
    }

    private String shortMessage;
    private String message;
    private String idToUser;
    private String idUser;
    private String bearerToken;

    public String getShortMessage() {
        return shortMessage;
    }

    public void setShortMessage(String shortMessage) {
        this.shortMessage = shortMessage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getIdToUser() {
        return idToUser;
    }

    public void setIdToUser(String idToUser) {
        this.idToUser = idToUser;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    @Override
    public String getBearerToken() {
        return bearerToken;
    }

    @Override
    public void setBearerToken(String bearerToken) {
        this.bearerToken = bearerToken;
        super.setBearerToken(this.bearerToken);
    }

    public static class UserMessageNetworkBuilder{

        private String shortMessage;
        private String message;
        private String idToUser;
        private String idUser;
        private String bearerToken;

        public UserMessageNetworkBuilder setShortMessage(String shortMessage){
            this.shortMessage = shortMessage;
            return this;
        }

        public UserMessageNetworkBuilder setMessage(String message){
            this.message = message;
            return this;
        }

        public UserMessageNetworkBuilder setIdToUser(String idToUser){
            this.idToUser = idToUser;
            return this;
        }

        public UserMessageNetworkBuilder setUserId(String userId){
            this.idUser = userId;
            return this;
        }

        public UserMessageNetworkBuilder setBearerToken(String bearerToken){
            this.bearerToken = bearerToken;
            return this;
        }

        public UserMessageNetworkHandler build(){
            UserMessageNetworkHandler userMessageNetworkHandler = new UserMessageNetworkHandler();
            userMessageNetworkHandler.setShortMessage(shortMessage);
            userMessageNetworkHandler.setMessage(message);
            userMessageNetworkHandler.setIdToUser(idToUser);
            userMessageNetworkHandler.setIdUser(idUser);
            userMessageNetworkHandler.setBearerToken(bearerToken);
            userMessageNetworkHandler.init();
            return userMessageNetworkHandler;
        }

    }

    public UserMessageNetworkHandler(){
        super();
        setBaseUrl(NetworkConstants.BASE_URL);
    }

    public void sendMessageToUser(final UserMessageNetworkListener messageNetworkListener){

        NetworkServices userMessageSendService = getRetrofit().create(NetworkServices.class);
        final Call<Object> call = userMessageSendService.doSendUserMessage(shortMessage,
                message,idToUser,idUser);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                messageNetworkListener.onMessageSendResponse();
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                messageNetworkListener.onMessageSendError();
            }
        });
    }

}
