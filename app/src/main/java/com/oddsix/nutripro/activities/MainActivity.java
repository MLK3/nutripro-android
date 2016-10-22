package com.oddsix.nutripro.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.oddsix.nutripro.BaseActivity;
import com.oddsix.nutripro.R;
import com.oddsix.nutripro.fragments.AnalysedPictureFragment;
import com.oddsix.nutripro.fragments.DayResumeFragment;
import com.oddsix.nutripro.fragments.ProfileFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Filippe on 21/10/16.
 */

public class MainActivity extends BaseActivity {
    private ViewPager mViewPager;
    private String[] mTabTitles;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setToolbar(false);

        setViewPager();

        setTabLayout();
    }

    private void setViewPager() {
        mTabTitles = getResources().getStringArray(R.array.tab_titles);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        sectionsPagerAdapter.addFragment(new DayResumeFragment());
        sectionsPagerAdapter.addFragment(new AnalysedPictureFragment());
        sectionsPagerAdapter.addFragment(new ProfileFragment());
        mViewPager.setAdapter(sectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                setTitle(mTabTitles[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    private void setTabLayout() {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mViewPager);
        setTitle(mTabTitles[tabLayout.getSelectedTabPosition()]);
        setupTabIcons(tabLayout);
    }

    private void setupTabIcons(TabLayout tabLayout) {
        TypedArray tabIcons = getResources().obtainTypedArray(R.array.tab_icons);

        for (int i = 0; i < tabIcons.length(); i++) {
            tabLayout.getTabAt(i).setIcon(tabIcons.getResourceId(i, 0));
        }
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
