package uk.com.a1ms.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import uk.com.a1ms.ui.fragments.ContactsGroupsA1MSFragment;
import uk.com.a1ms.ui.fragments.ContactsGroupsInviteFragment;

/**
 * Created by priju.jacobpaul on 24/04/16.
 */
public class ContactsGroupsPagerAdapter extends FragmentStatePagerAdapter {

    SparseArray<Fragment> registeredFragments = new SparseArray<>();
    int mNumOfTabs;

    public ContactsGroupsPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                ContactsGroupsA1MSFragment a1MSFragment = new ContactsGroupsA1MSFragment();
                return a1MSFragment;
            case 1:
                ContactsGroupsInviteFragment inviteFragment = new ContactsGroupsInviteFragment();
                return inviteFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }
}
