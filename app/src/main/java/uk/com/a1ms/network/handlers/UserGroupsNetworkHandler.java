package uk.com.a1ms.network.handlers;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.com.a1ms.network.BaseNetwork;
import uk.com.a1ms.network.NetworkConstants;
import uk.com.a1ms.network.NetworkServices;
import uk.com.a1ms.network.dto.GroupDetails;
import uk.com.a1ms.network.dto.MemberGroupDetails;

/**
 * Created by priju.jacobpaul on 7/07/16.
 */
public class UserGroupsNetworkHandler extends BaseNetwork {

    public interface UserGroupsNetworkListener{
        void onGroupCreated(GroupDetails groupDetails);
        void onMembershipGroupDetails(MemberGroupDetails memberGroupDetails);
        void onCreateGroupError();

    }

    private String bearerToken;
    private String groupName;

    public UserGroupsNetworkHandler(){
        super();
        setBaseUrl(NetworkConstants.USER_CREATE_GROUP);
    }

    @Override
    public String getBearerToken() {
        return bearerToken;
    }

    @Override
    public void setBearerToken(String bearerToken) {
        this.bearerToken = bearerToken;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void createGroup(final UserGroupsNetworkListener listener){

        init();

        NetworkServices userInviteService = getRetrofit().create(NetworkServices.class);
        final Call<GroupDetails> call = userInviteService.doCreateGroup(getGroupName());
        call.enqueue(new Callback<GroupDetails>() {
            @Override
            public void onResponse(Call<GroupDetails> call, Response<GroupDetails> response) {
                listener.onGroupCreated(response.body());
            }

            @Override
            public void onFailure(Call<GroupDetails> call, Throwable t) {
                listener.onCreateGroupError();
            }
        });

    }

    public void getMembershipGroupDetails(final UserGroupsNetworkListener listener){

        init();

        NetworkServices userInviteService = getRetrofit().create(NetworkServices.class);
        final Call<MemberGroupDetails> call = userInviteService.getMembershipGroups();
        call.enqueue(new Callback<MemberGroupDetails>() {
            @Override
            public void onResponse(Call<MemberGroupDetails> call, Response<MemberGroupDetails> response) {
                listener.onMembershipGroupDetails(response.body());
            }

            @Override
            public void onFailure(Call<MemberGroupDetails> call, Throwable t) {
                listener.onCreateGroupError();
            }
        });
    }
}
