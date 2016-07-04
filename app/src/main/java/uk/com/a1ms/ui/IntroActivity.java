package uk.com.a1ms.ui;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;

import java.util.ArrayList;

import uk.com.a1ms.R;
import uk.com.a1ms.adapters.IntroPagerAdapter;
import uk.com.a1ms.util.SharedPreferenceManager;

public class IntroActivity extends BaseActivity implements IntroPagerAdapter.IntroPageListener {

    private ViewPager mViewPager;
    private ImageView mImageView[] = new ImageView[3];



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        SharedPreferenceManager.setFirstTimeLaunch(true,this);

        if(SharedPreferenceManager.isFirstTimeLaunch(this)) {
            initPages();
        }
        else {
            startContactsGroupsActivity(savedInstanceState,true);
        }

    }


    private void initPages(){
        ImageView img1 = (ImageView)findViewById(R.id.imageview_circle1);
        ImageView img2 = (ImageView)findViewById(R.id.imageview_circle2);
        ImageView img3 = (ImageView)findViewById(R.id.imageview_circle3);

        mImageView[0] = img1;
        mImageView[1] = img2;
        mImageView[2] = img3;

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(new IntroPagerAdapter(this,this));

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mImageView[position].setBackground(getResources().getDrawable(R.drawable.circle));
                int next = position + 1;
                int previous = position - 1;

                if(next > 2){
                    next = 0;
                }

                if(previous < 0) {
                    previous = 2;
                }

                if(next <=2 && previous >= 0){
                    mImageView[next].setBackground(getResources().getDrawable(R.drawable.circle_blue));
                    mImageView[previous].setBackground(getResources().getDrawable(R.drawable.circle_blue));

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    @Override
    public void registerButtonPressed() {

        ArrayList<String> permissions = new ArrayList<>();
        permissions.add(Manifest.permission.RECEIVE_SMS);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.READ_CONTACTS);

        String[] items = permissions.toArray(new String[permissions.size()]);
        ActivityCompat.requestPermissions(this,items,1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1){
            startRegistrationActivity(null,true);
        }
    }
}
