package com.oddsix.nutripro.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.oddsix.nutripro.BaseActivity;
import com.oddsix.nutripro.R;
import com.oddsix.nutripro.fragments.AnalysedPictureFragment;
import com.oddsix.nutripro.fragments.DayResumeFragment;
import com.oddsix.nutripro.fragments.ProfileFragment;
import com.oddsix.nutripro.utils.Constants;
import com.oddsix.nutripro.utils.helpers.UpdatePhotoHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Filippe on 21/10/16.
 */

public class MainActivity extends BaseActivity {
    private ViewPager mViewPager;
    private String[] mTabTitles;
    private TabLayout mTabLayout;
    private UpdatePhotoHelper mUpdatePhotoHelper;
    private AnalysedPictureFragment mPictureFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!getSharedPreferences(Constants.SHARED_PREF_NAME, MODE_PRIVATE).getBoolean(Constants.PREF_IS_LOGGED, false)) {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }

        setContentView(R.layout.activity_main);

        mUpdatePhotoHelper = new UpdatePhotoHelper(this, mOnRequestReady);

        setToolbar(false);

        setViewPager();

        setTabLayout();
    }

    private UpdatePhotoHelper.OnRequestReady mOnRequestReady = new UpdatePhotoHelper.OnRequestReady() {
        @Override
        public void photoReady(String photo_64, Bitmap photo) {
            mPictureFragment.setImage(photo);
        }

        @Override
        public void photoFailed() {
            mTabLayout.getTabAt(0).select();
        }
    };

    private void setViewPager() {
        mTabTitles = getResources().getStringArray(R.array.tab_titles);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        sectionsPagerAdapter.addFragment(new DayResumeFragment());
        mPictureFragment = new AnalysedPictureFragment();
        sectionsPagerAdapter.addFragment(mPictureFragment);
        sectionsPagerAdapter.addFragment(new ProfileFragment());
        mViewPager.setAdapter(sectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 1:
                        mUpdatePhotoHelper.initiate(false, Constants.PIC_UPLOAD_MAX_SIZE);
                        break;
                }
                setTitle(mTabTitles[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void setTabLayout() {
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mTabLayout.setupWithViewPager(mViewPager);
        setTitle(mTabTitles[mTabLayout.getSelectedTabPosition()]);
        setupTabIcons(mTabLayout);
    }

    private void setupTabIcons(TabLayout tabLayout) {
        TypedArray tabIcons = getResources().obtainTypedArray(R.array.tab_icons);

        for (int i = 0; i < tabIcons.length(); i++) {
            tabLayout.getTabAt(i).setIcon(tabIcons.getResourceId(i, 0));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mUpdatePhotoHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mUpdatePhotoHelper.onActivityResult(requestCode, resultCode, data);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }

        public void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);
        }
    }
}
