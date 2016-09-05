package uk.com.a1ms.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;

import uk.com.a1ms.R;
import uk.com.a1ms.db.dto.A1MSUser;
import uk.com.a1ms.ui.fragments.MessagingFragment;

/**
 * Created by priju.jacobpaul on 17/06/16.
 */
public class MessagingActivity extends BaseActivity implements MessagingFragment.MessagingFragmentListener {

    private MessagingFragment mMessagingFragment;
    private A1MSUser mCurrentUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.message_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setCollapsible(false);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        A1MSUser a1MSUser = getIntent().getParcelableExtra("user");
        mCurrentUser = a1MSUser;
        getSupportActionBar().setTitle(a1MSUser.getName());
    }

    @Override
    protected void onResume() {
        super.onResume();
        A1MSUser user = getIntent().getParcelableExtra("user");
        launchMessagingFragment(user);
    }

    @Override
    public void onBackPressed() {
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

}
