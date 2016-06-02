package a1ms.uk.a1ms.ui;

import android.content.Intent;
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

import a1ms.uk.a1ms.R;
import a1ms.uk.a1ms.adapters.ContactsGroupsPagerAdapter;
import a1ms.uk.a1ms.ui.fragments.BaseFragment;
import a1ms.uk.a1ms.ui.fragments.ContactsGroupsA1MSFragment;
import a1ms.uk.a1ms.util.PermissionRequestManager;

/**
 * Created by priju.jacobpaul on 28/05/16.
 */
public class ContactsGroupsActivity extends BaseActivity{

    private ContactsGroupsPagerAdapter mAdapter;
    private ViewPager mViewPager;
    private FloatingActionButton mFab;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contactsgroups_activity);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_actionbar);
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
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
                BaseFragment fragment = (BaseFragment)mAdapter.getRegisteredFragment(mViewPager.getCurrentItem());
//                fragment.updateUi(Forecast.getInstance());
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

        PermissionRequestManager.checkAndRequestPermission(this,
        android.Manifest.permission.READ_CONTACTS,
        getResources().getInteger(R.integer.MY_PERMISSIONS_REQUEST_READ_CONTACTS));

    }


    @Override
    public void updateUi(Object object) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == getResources().getInteger(R.integer.MY_PERMISSIONS_REQUEST_READ_CONTACTS)) {
                BaseFragment fragment = (BaseFragment)mAdapter.getRegisteredFragment(mViewPager.getCurrentItem());
                if(fragment instanceof ContactsGroupsA1MSFragment){
                    ((ContactsGroupsA1MSFragment)fragment).fetchContacts();
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



        }
        return false;
    }
}
