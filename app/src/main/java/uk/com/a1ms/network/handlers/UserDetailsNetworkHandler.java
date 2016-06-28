package uk.com.a1ms.network.handlers;

import uk.com.a1ms.network.BaseNetwork;

/**
 * Created by priju.jacobpaul on 24/06/16.
 */
public class UserDetailsNetworkHandler extends BaseNetwork {

    public interface UserRegistrationListener{
        void onUserRegistrationResponse();
        void onUserRegistrationError();
    }
}