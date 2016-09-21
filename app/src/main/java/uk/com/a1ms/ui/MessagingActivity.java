package uk.com.a1ms.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.ArrayList;

import uk.com.a1ms.A1MSApplication;
import uk.com.a1ms.R;
import uk.com.a1ms.db.A1MSGroupsFieldsDataSource;
import uk.com.a1ms.db.A1MSUsersFieldsDataSource;
import uk.com.a1ms.db.dto.A1MSGroup;
import uk.com.a1ms.db.dto.A1MSUser;
import uk.com.a1ms.network.NetworkConstants;
import uk.com.a1ms.network.dto.GroupDetails;
import uk.com.a1ms.network.handlers.UserGroupsNetworkHandler;
import uk.com.a1ms.ui.fragments.GroupAddUserFragment;
import uk.com.a1ms.ui.fragments.GroupInfoFragment;
import uk.com.a1ms.ui.fragments.GroupNameChangeFragment;
import uk.com.a1ms.ui.fragments.MessagingFragment;
import uk.com.a1ms.ui.uiutil.ProgressView;
import uk.com.a1ms.util.NotificationController;
import uk.com.a1ms.util.SharedPreferenceManager;

/**
 * Created by priju.jacobpaul on 17/06/16.
 */
public class MessagingActivity extends BaseActivity implements
        MessagingFragment.MessagingFragmentListener,
        NotificationController.NotificationListener,
        GroupInfoFragment.GroupInfoFragmentListener,
        GroupNameChangeFragment.GroupNameChangeFragmentListener,
        GroupAddUserFragment.GroupAddUserFragmentListener

{

    private MessagingFragment mMessagingFragment;
    private A1MSUser mCurrentUser;
    private A1MSGroup mCurrentGroup;
    private A1MSGroupsFieldsDataSource mGroupsDataSource;
    private A1MSUsersFieldsDataSource mUsersFieldsDataSource;
    private FrameLayout mFrameLayoutHolder;

    private String TAG = MessagingActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.message_activity);
        mFrameLayoutHolder = (FrameLayout)findViewById(R.id.message_framelayout_holder);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setCollapsible(false);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        A1MSUser a1MSUser = getIntent().getParcelableExtra("user");
        mCurrentUser = a1MSUser;
        getSupportActionBar().setTitle(a1MSUser.getName());

        if(mCurrentUser.isGroup()){
            mGroupsDataSource = new A1MSGroupsFieldsDataSource(this);
            mGroupsDataSource.open();

            mUsersFieldsDataSource = new A1MSUsersFieldsDataSource(this);
            mUsersFieldsDataSource.open();

            mCurrentGroup = mGroupsDataSource.getDetailsOfGroups(mCurrentUser.getUserId());
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        A1MSUser user = getIntent().getParcelableExtra("user");
        launchMessagingFragment(user);
    }

    @Override
    public void onBackPressed() {

        FragmentManager fm = getSupportFragmentManager();
        Fragment currentFragment = fm.findFragmentById(R.id.message_framelayout_holder);
        if (currentFragment instanceof MessagingFragment) {
            if(mMessagingFragment != null){
                mMessagingFragment.onBackPressed();
            }
        }

        super.onBackPressed();

    }

    private void launchMessagingFragment(A1MSUser user){
        FragmentManager fm = getSupportFragmentManager();
        Fragment currentFragment = fm.findFragmentById(R.id.message_framelayout_holder);
        if (currentFragment instanceof MessagingFragment) {
            return;
        }

        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        mMessagingFragment = MessagingFragment.newInstance(this);
        fragmentTransaction.replace(R.id.message_framelayout_holder, mMessagingFragment);
        fragmentTransaction.commit();
    }

    public A1MSUser getCurrentUser(){
        return mCurrentUser;
    }

    public A1MSGroup getCurrentGroup(){
        return mCurrentGroup;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mGroupsDataSource != null) {
            mGroupsDataSource.close();
        }
    }

    @Override
    public void onNotificationReceived(int id, Object... args) {

    }

    @Override
    public void onExitGroup() {

        UserGroupsNetworkHandler userGroupsNetworkHandler = new UserGroupsNetworkHandler();
        userGroupsNetworkHandler.setBearerToken(SharedPreferenceManager.getUserToken(A1MSApplication.applicationContext));
        userGroupsNetworkHandler.setUserId(SharedPreferenceManager.getUserId(A1MSApplication.applicationContext));
        userGroupsNetworkHandler.setGroupId(mCurrentGroup.getGroupId());

        userGroupsNetworkHandler.exitGroup(new UserGroupsNetworkHandler.UserExitGroupNetworkListener() {
            @Override
            public void onExitGroup(GroupDetails groupDetails) {
                Log.d(TAG,groupDetails.getResponseCode());
                if(groupDetails != null && Integer.valueOf(groupDetails.getResponseCode()) == NetworkConstants.RESPONSE_CODE_SUCCESS){

                    mGroupsDataSource.deleteA1MSUserFromGroup(SharedPreferenceManager.getUserId(getApplicationContext()),
                            mCurrentGroup);
                    Toast.makeText(MessagingActivity.this,"Exit Success",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(MessagingActivity.this,"Error during exit",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onExitGroupError() {
                Toast.makeText(MessagingActivity.this,"Exit error",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onNameChange(final String groupName) {
        UserGroupsNetworkHandler userGroupsNetworkHandler = new UserGroupsNetworkHandler();
        userGroupsNetworkHandler.setBearerToken(SharedPreferenceManager.getUserToken(this));
        userGroupsNetworkHandler.setGroupName(groupName);
        userGroupsNetworkHandler.setGroupId(mCurrentGroup.getGroupId());
        userGroupsNetworkHandler.setMembersIDs(mCurrentGroup.getMembersList());

        ProgressView.addProgressView(mFrameLayoutHolder,getString(R.string.renaming_group));
        userGroupsNetworkHandler.editGroupDetails(new UserGroupsNetworkHandler.editGroupDetailsListener() {
            @Override
            public void onEditGroupSuccess(GroupDetails groupDetails) {

                ProgressView.removeProgressView(mFrameLayoutHolder);
                if(groupDetails != null && Integer.valueOf(groupDetails.getResponseCode()) == NetworkConstants.RESPONSE_CODE_SUCCESS) {

                    Toast.makeText(MessagingActivity.this, "Group name successfully changed.", Toast.LENGTH_SHORT).show();
                    mCurrentGroup.setGroupName(groupName);
                    mGroupsDataSource.updateGroupDetails(mCurrentGroup);
                    mCurrentUser.setName(groupName);
                    mUsersFieldsDataSource.updateUserDetails(mCurrentUser);

                    FragmentManager fm = getSupportFragmentManager();
                    fm.popBackStack();

                    NotificationController.getInstance().postNotificationName(NotificationController.userDatabaseChanged,null);
                }
                else {
                    Toast.makeText(MessagingActivity.this, "Error during changing group name", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onEditGroupError() {
                ProgressView.removeProgressView(mFrameLayoutHolder);
                Toast.makeText(MessagingActivity.this, "Error during changing group name", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void usersAdded(ArrayList<A1MSUser> users) {

    }
}
