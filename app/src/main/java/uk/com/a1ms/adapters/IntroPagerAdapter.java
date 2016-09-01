package uk.com.a1ms.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import uk.com.a1ms.R;

/**
 * Created by priju.jacobpaul on 30/06/16.
 */
public class IntroPagerAdapter extends PagerAdapter {

    private Context mContext;
    private Button mRegister;
    private IntroPageListener mIntroPageListener;

    public interface IntroPageListener{
        void registerButtonPressed();
    }

    public IntroPagerAdapter(Context context,IntroPageListener introPageListener) {
        mContext = context;
        mIntroPageListener = introPageListener;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        IntroPagerEnum customPagerEnum = IntroPagerEnum.values()[position];
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(customPagerEnum.getLayoutResId(), collection, false);
        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return IntroPagerEnum.values().length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        IntroPagerEnum customPagerEnum = IntroPagerEnum.values()[position];
        return mContext.getString(customPagerEnum.getTitleResId());
    }

    private enum IntroPagerEnum {

        RED(R.string.intro_one_details, R.layout.view_intro_one),
        BLUE(R.string.intro_two_details, R.layout.view_intro_two),
        ORANGE(R.string.intro_three_details, R.layout.view_intro_three),
        PURPLE(R.string.intro_four_details,R.layout.view_intro_four),
        BLACK(R.string.intro_five_details,R.layout.view_intro_five),
        WHITE(R.string.intro_six_details,R.layout.view_intro_six);


        private int mTitleResId;
        private int mLayoutResId;

        IntroPagerEnum(int titleResId, int layoutResId) {
            mTitleResId = titleResId;
            mLayoutResId = layoutResId;
        }

        public int getTitleResId() {
            return mTitleResId;
        }

        public int getLayoutResId() {
            return mLayoutResId;
        }

    }


}
