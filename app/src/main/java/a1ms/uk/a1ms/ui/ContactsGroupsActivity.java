package a1ms.uk.a1ms.ui;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import a1ms.uk.a1ms.A1MSApplication;
import a1ms.uk.a1ms.R;
import a1ms.uk.a1ms.adapters.ContactsGroupsPagerAdapter;
import a1ms.uk.a1ms.contacts.FetchContactsHandler;
import a1ms.uk.a1ms.dialogutil.DialogCallBackListener;
import a1ms.uk.a1ms.dialogutil.DialogUtil;
import a1ms.uk.a1ms.listeners.CustomTabLayoutListener;
import a1ms.uk.a1ms.ui.fragments.BaseFragment;
import a1ms.uk.a1ms.ui.fragments.ContactsGroupsA1MSFragment;
import a1ms.uk.a1ms.ui.fragments.ContactsGroupsInviteFragment;

/**
 * Created by priju.jacobpaul on 28/05/16.
 */
public class ContactsGroupsActivity extends BaseActivity{

    private ContactsGroupsPagerAdapter mAdapter;
    private ViewPager mViewPager;
    private FloatingActionButton mFab;
    private boolean isPermissionAsked = false;
    private String TAG = ContactsGroupsActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contactsgroups_activity);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setCollapsible(false);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("Groups & Contacts");


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("A1MS"));
        tabLayout.addTab(tabLayout.newTab().setText("Invites"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        toolbar.setVisibility(View.VISIBLE);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mAdapter = new ContactsGroupsPagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());

        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(new CustomTabLayoutListener(tabLayout){

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if(positionOffset != 0 && positionOffsetPixels != 0){
                    mFab.hide();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                onPageScrollChange(state);
            }
        });


        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
                BaseFragment fragment = (BaseFragment)mAdapter.getRegisteredFragment(mViewPager.getCurrentItem());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mFab = (FloatingActionButton)findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ContactsGroupsActivity.this,"touched",Toast.LENGTH_SHORT).show();
            }
        });


    }



    @Override
    protected void onResume() {
        super.onResume();
        askForPermissons();
    }


    @TargetApi(Build.VERSION_CODES.M)
    private void askForPermissons() {
        if(isPermissionAsked) {
            return;
        }

        ArrayList<String> permissons = new ArrayList<>();
        if (checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            permissons.add(Manifest.permission.READ_CONTACTS);
            permissons.add(Manifest.permission.WRITE_CONTACTS);
            permissons.add(Manifest.permission.GET_ACCOUNTS);
            isPermissionAsked = true;
            String[] items = permissons.toArray(new String[permissons.size()]);
            requestPermissions(items, 1);
        }
//        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            permissons.add(Manifest.permission.READ_EXTERNAL_STORAGE);
//            permissons.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        }

    }
    @Override
    public void updateUi(Object object) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            for (int a = 0; a < permissions.length; a++) {
                if (grantResults.length <= a || grantResults[a] != PackageManager.PERMISSION_GRANTED) {
                    continue;
                }
                switch (permissions[a]) {
                    case Manifest.permission.READ_CONTACTS:
                        FetchContactsHandler.getInstance(A1MSApplication.applicationContext).getContactsWithSMSPhone(null);
                        break;
                    case Manifest.permission.WRITE_EXTERNAL_STORAGE:

                        break;
                }
            }
        }

        if(requestCode == 1) {
            if( (grantResults.length > 1) &&  (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                FetchContactsHandler.getInstance(getApplicationContext()).getContactsWithSMSPhone(null);
            }
            else {
                DialogUtil.showOKDialog(this,
                        getString(R.string.permission_title),
                        getString(R.string.permission_message_contacts),
                        getString(android.R.string.ok),
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
                        }, false);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_groups_contacts,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_select_all:{
                item.setChecked(!item.isChecked());
                if(item.isChecked()){
                    item.setIcon(getResources().getDrawable(android.R.drawable.checkbox_on_background));
                }
                else {
                    item.setIcon(getResources().getDrawable(android.R.drawable.checkbox_off_background));
                }

                BaseFragment fragment = (BaseFragment)mAdapter.getRegisteredFragment(mViewPager.getCurrentItem());
                if(fragment instanceof ContactsGroupsA1MSFragment){
                    ((ContactsGroupsA1MSFragment)fragment).setGlobalCheckBoxStatusChange(item.isChecked());
                }

                return true;
            }
            case R.id.action_add_contact:{
                Intent intent = new Intent(Intent.ACTION_INSERT,ContactsContract.Contacts.CONTENT_URI);
                startActivity(intent);
                return true;
            }
            case android.R.id.home:{
                onBackPressed();
                return true;
            }

            case R.id.action_delete:{
                BaseFragment fragment = (BaseFragment)mAdapter.getRegisteredFragment(mViewPager.getCurrentItem());
                if(fragment instanceof ContactsGroupsA1MSFragment){
                    ((ContactsGroupsA1MSFragment)fragment).deleteSelectedItems();
                }
                return true;
            }
        }
        return false;
    }

    public void onPageScrollChange(int state) {

        switch (state) {
            case ViewPager.SCROLL_STATE_SETTLING:
                displayFloatingActionButtonIfNeeded(mViewPager.getCurrentItem());
                break;

            case ViewPager.SCROLL_STATE_IDLE:
                displayFloatingActionButtonIfNeeded(mViewPager.getCurrentItem());
                break;
        }
    }

    private void displayFloatingActionButtonIfNeeded(int position) {

        if (mAdapter.getItem(position) instanceof ContactsGroupsA1MSFragment) {
            mFab.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_send));
            mFab.show();

            mFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
        else if(mAdapter.getItem(position) instanceof ContactsGroupsInviteFragment) {

            mFab.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_share));
            mFab.show();

            mFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT,getString(R.string.a1ms_message_invite));
                    sendIntent.setType("text/plain");
                    startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.app_name)));
                }
            });
        }
    }

    @Override
    public void onBackPressed() {

        BaseFragment fragment = (BaseFragment)mAdapter.getRegisteredFragment(mViewPager.getCurrentItem());
        if(fragment != null) {
            if(fragment.onBackPressed()){
                return;
            }
        }
        super.onBackPressed();
    }
}
