package uk.com.a1ms.listeners;

import android.support.design.widget.TabLayout;

/**
 * Created by priju.jacobpaul on 4/06/16.
 */
public class CustomTabLayoutListener extends TabLayout.TabLayoutOnPageChangeListener {


    public CustomTabLayoutListener(TabLayout tabLayout){
        super(tabLayout);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        super.onPageScrollStateChanged(state);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        super.onPageScrolled(position, positionOffset, positionOffsetPixels);
    }

    @Override
    public void onPageSelected(int position) {
        super.onPageSelected(position);
    }
}
