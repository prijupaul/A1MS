package uk.com.a1ms.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;

import java.util.List;

import uk.com.a1ms.R;
import uk.com.a1ms.db.dto.A1MSUser;
import uk.com.a1ms.ui.fragments.CreateGroupsFragment;
import uk.com.a1ms.ui.fragments.CreateGroupsPreviewFragment;
import uk.com.a1ms.util.NotificationController;

/**
 * Created by priju.jacobpaul on 11/07/16.
 */
public class CreateGroupsActivity extends BaseActivity implements NotificationController.NotificationListener ,
        CreateGroupsFragment.CreateGroupsFragmentListener, CreateGroupsPreviewFragment.GroupPreviewListener
{
    private CreateGroupsFragment mCreateGroupsFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creategroups_activity);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setCollapsible(false);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("New Group");

        NotificationController.getInstance().addObserver(this,NotificationController.contactsDidLoaded);

    }

    @Override
    protected void onResume() {
        super.onResume();
        launchCreateGroupsFragment();

    }

    @Override
    public void onNotificationReceived(int id, Object... args) {

        if(id == NotificationController.contactsDidLoaded){

        }
    }


    private void launchCreateGroupsFragment() {

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        mCreateGroupsFragment = CreateGroupsFragment.newInstance(this);
        fragmentTransaction.replace(R.id.creategroup_framelayout_holder, mCreateGroupsFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onGroupMembersSelected(List<A1MSUser> selectedA1MSUsers) {
        CreateGroupsPreviewFragment previewFragment =  CreateGroupsPreviewFragment.newInstance(selectedA1MSUsers,this);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.creategroup_framelayout_holder,previewFragment);
        transaction.hide(mCreateGroupsFragment);
        transaction.addToBackStack(CreateGroupsFragment.class.getSimpleName());
        transaction.commit();
    }

    @Override
    public void onGroupCreated(String groupName, List<A1MSUser> a1MSUsers) {

    }
}
