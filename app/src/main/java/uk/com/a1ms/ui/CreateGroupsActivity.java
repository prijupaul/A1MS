package uk.com.a1ms.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import uk.com.a1ms.A1MSApplication;
import uk.com.a1ms.R;
import uk.com.a1ms.db.A1MSDbHelper;
import uk.com.a1ms.db.A1MSGroupsFieldsDataSource;
import uk.com.a1ms.db.A1MSUsersFieldsDataSource;
import uk.com.a1ms.db.dto.A1MSGroup;
import uk.com.a1ms.db.dto.A1MSUser;
import uk.com.a1ms.dialogutil.DialogCallBackListener;
import uk.com.a1ms.dialogutil.DialogUtil;
import uk.com.a1ms.network.NetworkConstants;
import uk.com.a1ms.network.dto.GroupDetails;
import uk.com.a1ms.network.dto.MemberGroupDetails;
import uk.com.a1ms.network.handlers.UserGroupsNetworkHandler;
import uk.com.a1ms.ui.fragments.CreateGroupsFragment;
import uk.com.a1ms.ui.fragments.CreateGroupsPreviewFragment;
import uk.com.a1ms.util.NotificationController;
import uk.com.a1ms.util.SharedPreferenceManager;

/**
 * Created by priju.jacobpaul on 11/07/16.
 */
public class CreateGroupsActivity extends BaseActivity implements NotificationController.NotificationListener ,
        CreateGroupsFragment.CreateGroupsFragmentListener, CreateGroupsPreviewFragment.GroupPreviewListener {
    private CreateGroupsFragment mCreateGroupsFragment;
    private Uri fileUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creategroups_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setCollapsible(false);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("New Group");

    }

    @Override
    protected void onResume() {
        super.onResume();
        launchCreateGroupsFragment();

    }

    @Override
    public void onNotificationReceived(int id, Object... args) {

    }


    private void launchCreateGroupsFragment() {

        FragmentManager fm = getSupportFragmentManager();
        Fragment currentFragment = fm.findFragmentById(R.id.creategroup_framelayout_holder);
        if ((currentFragment instanceof CreateGroupsFragment) ||
                (currentFragment instanceof CreateGroupsPreviewFragment)) {
            return;
        }

        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        mCreateGroupsFragment = CreateGroupsFragment.newInstance(this);
        fragmentTransaction.replace(R.id.creategroup_framelayout_holder, mCreateGroupsFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onGroupMembersSelected(List<A1MSUser> selectedA1MSUsers) {
        CreateGroupsPreviewFragment previewFragment = CreateGroupsPreviewFragment.newInstance(selectedA1MSUsers, this);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.creategroup_framelayout_holder, previewFragment);
        transaction.hide(mCreateGroupsFragment);
        transaction.addToBackStack(CreateGroupsFragment.class.getSimpleName());
        transaction.commit();
    }

    @Override
    public void onGroupCreated(String groupName, List<A1MSUser> a1MSUsers) {

        // TODO: Here the network should support taking in paramters like name, contact users, and group image
        // This part is to be worked on.

        ArrayList<String> memberIDs = new ArrayList<>();
        for(A1MSUser user:a1MSUsers){
            memberIDs.add(user.getUserId());
        }

        UserGroupsNetworkHandler userGroupsNetworkHandler = new UserGroupsNetworkHandler();
        userGroupsNetworkHandler.setBearerToken(SharedPreferenceManager.getUserToken(this));
        userGroupsNetworkHandler.setGroupName(groupName);
        userGroupsNetworkHandler.setMembersIDs(memberIDs);
        userGroupsNetworkHandler.createGroup(new UserGroupsNetworkHandler.UserGroupsNetworkListener() {
            @Override
            public void onGroupCreated(GroupDetails groupDetails) {
                // TODO: The create group details should be inserted to the database
                // and this should should be shown to the user.
                // A toast should be shown that the group was created. ?
                if(groupDetails != null) {
                    int responseCode = Integer.valueOf(groupDetails.getResponseCode());
                    if(responseCode == NetworkConstants.RESPONSE_CODE_SUCCESS) {
                        addgroupDetailsToUserDb(groupDetails);
                        finish();
                        NotificationController.getInstance().postNotificationName(NotificationController.userDatabaseChanged,null);
                    }
                    else {
                        Toast.makeText(CreateGroupsActivity.this,groupDetails.getErrorMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onMembershipGroupDetails(MemberGroupDetails memberGroupDetails) {

            }

            @Override
            public void onCreateGroupError() {
                Toast.makeText(CreateGroupsActivity.this,"Error while creating groups. ",Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1001) {
            boolean isRequestedPermissionGranted = true;
            for (int a = 0; a < permissions.length; a++) {

                if (grantResults[a] != PackageManager.PERMISSION_GRANTED) {
                    isRequestedPermissionGranted = false;
                    break;
                }
            }

            if (!isRequestedPermissionGranted) {
                DialogUtil.showYESNODialog(this,
                        getString(R.string.permission_title),
                        getString(R.string.permission_denied_message),
                        getString(android.R.string.ok),
                        getString(android.R.string.cancel),
                        new DialogCallBackListener() {
                            @Override
                            public void run() {
                                try {
                                    Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    intent.setData(Uri.parse("package:" + A1MSApplication.applicationContext.getPackageName()));
                                    startActivity(intent);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        null, false);
            } else {
                Fragment previewFragment = getSupportFragmentManager().findFragmentById(R.id.creategroup_framelayout_holder);
                if (previewFragment instanceof CreateGroupsPreviewFragment) {
                    ((CreateGroupsPreviewFragment) previewFragment).showBottomSheets();
                }
            }

        }
    }

    private void addgroupDetailsToUserDb(GroupDetails details){

        A1MSDbHelper a1MSDbHelper = A1MSDbHelper.getInstance(A1MSApplication.applicationContext);
        A1MSUsersFieldsDataSource dataSource =  new A1MSUsersFieldsDataSource(A1MSApplication.applicationContext);
        A1MSUser a1msuser = constructA1MSUser(details);
        dataSource.insertA1MSUser(a1MSDbHelper.getWritableDatabase(),a1msuser);

        A1MSGroupsFieldsDataSource groupsDataSource = new A1MSGroupsFieldsDataSource(A1MSApplication.applicationContext);
        A1MSGroup a1MSGroup = constructA1MSGroup(details);
        groupsDataSource.insertA1MSGroup(a1MSDbHelper.getWritableDatabase(),a1MSGroup);
    }

    private A1MSUser constructA1MSUser(GroupDetails details){
        A1MSUser a1MSUser = new A1MSUser();
        a1MSUser.setGroup(true);
        a1MSUser.setEditable(true);
        a1MSUser.setName(details.getData().getName());
        a1MSUser.setUserId(details.getData().getId());
        return a1MSUser;
    }

    private A1MSGroup constructA1MSGroup(GroupDetails details){

        A1MSGroup a1MSGroup = new A1MSGroup();
        a1MSGroup.setActivate(Boolean.valueOf(details.getData().getIsActive()));
        a1MSGroup.setGroupId(details.getData().getId());
        a1MSGroup.setAdminId(details.getData().getIdUser());
        a1MSGroup.setGroupName(details.getData().getName());
        a1MSGroup.setMembersList(details.getData().getMembers());
        a1MSGroup.setAvatar("");
        return a1MSGroup;
    }
}
