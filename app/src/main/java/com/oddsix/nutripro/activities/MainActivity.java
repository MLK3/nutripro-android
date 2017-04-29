package com.oddsix.nutripro.activities;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.oddsix.nutripro.BaseActivity;
import com.oddsix.nutripro.R;
import com.oddsix.nutripro.fragments.AnalysedPictureFragment;
import com.oddsix.nutripro.fragments.DayResumeFragment;
import com.oddsix.nutripro.fragments.ProfileFragment;
import com.oddsix.nutripro.utils.Constants;
import com.oddsix.nutripro.utils.helpers.SharedPreferencesHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;

/**
 * Created by Filippe on 21/10/16.
 */

public class MainActivity extends BaseActivity {
    private static final int DAY_RESUME_TAB_POSITION = 0;

    private ViewPager mViewPager;
    private String[] mTabTitles;
    private TabLayout mTabLayout;
    private Realm mRealm;
    private AnalysedPictureFragment mPictureFragment;
    private DayResumeFragment mDayResumeFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!SharedPreferencesHelper.getInstance().isLogged()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {
            setContentView(R.layout.activity_main);

            mRealm = Realm.getDefaultInstance();

            setToolbar(false);

            setViewPager();

            setTabLayout();
        }
    }

    private void setViewPager() {
        mTabTitles = getResources().getStringArray(R.array.tab_titles);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mDayResumeFragment = new DayResumeFragment();
        mPictureFragment = new AnalysedPictureFragment();
        sectionsPagerAdapter.addFragment(mDayResumeFragment);
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
                        startCameraActivity();
//                        mUpdatePhotoHelper.initiate(false, Constants.PIC_UPLOAD_MAX_SIZE);
                        break;
                }
                //Enable chart icon only at first tab
                enabledChart(position == DAY_RESUME_TAB_POSITION);
                setTitle(mTabTitles[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void enabledChart(boolean enable) {
        try {
            mChartItem.setVisible(enable);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void startCameraActivity() {
        Intent intent = new Intent(this, CameraActivity.class);
        startActivityForResult(intent, Constants.REQ_PICTURE);
    }

    private void setTabLayout() {
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mTabLayout.setupWithViewPager(mViewPager);
        setTitle(mTabTitles[mTabLayout.getSelectedTabPosition()]);
        setupTabIcons(mTabLayout);
    }

    public void resetTabIndex() {
        mTabLayout.getTabAt(DAY_RESUME_TAB_POSITION).select();
    }

    private void setupTabIcons(TabLayout tabLayout) {
        TypedArray tabIcons = getResources().obtainTypedArray(R.array.tab_icons);

        for (int i = 0; i < tabIcons.length(); i++) {
            tabLayout.getTabAt(i).setIcon(tabIcons.getResourceId(i, 0));
        }
    }


    private MenuItem mChartItem;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mChartItem = menu.findItem(R.id.action_week_chart);
        //Need this when the menu just created
        enabledChart(mTabLayout.getSelectedTabPosition() == DAY_RESUME_TAB_POSITION);
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
            case R.id.action_week_chart:
                Intent chartIntent = new Intent(this, WeekResumeActivity.class);
                startActivity(chartIntent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Bitmap loadPrescaledBitmap(String filename) throws IOException {
        // Facebook image size
        int mPhotoMaxSize = 600;

        File file = null;
        FileInputStream fis;

        BitmapFactory.Options opts;
        int resizeScale;
        Bitmap bmp;

        file = new File(filename);

        // This bit determines only the width/height of the bitmap without loading the contents
        opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        fis = new FileInputStream(file);
        BitmapFactory.decodeStream(fis, null, opts);
        fis.close();

        // Find the correct scale value. It should be a power of 2
        resizeScale = 1;

        if (opts.outHeight > mPhotoMaxSize || opts.outWidth > mPhotoMaxSize) {
            resizeScale = (int) Math.pow(2, (int) Math.round(Math.log(mPhotoMaxSize / (double) Math.max(opts.outHeight, opts.outWidth)) / Math.log(0.5)));
        }

        // Load pre-scaled bitmap
        opts = new BitmapFactory.Options();
        opts.inSampleSize = resizeScale;
        fis = new FileInputStream(file);
        bmp = BitmapFactory.decodeStream(fis, null, opts);

        fis.close();

        return bmp;
    }

    public Bitmap rotateImage(String photoPath) throws IOException {
        int rotate = 0;
        Bitmap bm;
        File imageFile = new File(photoPath);
        ExifInterface exif = new ExifInterface(
                imageFile.getAbsolutePath());
        int orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_270:
                rotate = 270;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                rotate = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                rotate = 90;
                break;
        }
        Matrix matrix = new Matrix();
        matrix.postRotate(rotate);

//        Bitmap bpm = BitmapFactory.decodeStream(new FileInputStream(imageFile), null, null);
        Bitmap bpm = loadPrescaledBitmap(photoPath);
        bm = Bitmap.createBitmap(bpm, 0, 0, bpm.getWidth(), bpm.getHeight(), matrix, true);
        return bm;
    }

    public void refreshDayResume() {
        if(mDayResumeFragment != null) {
            mDayResumeFragment.refreshDayMeal();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQ_PICTURE) {
            if (resultCode == RESULT_OK) {
//                Bitmap bm;
//                try {
//                    bm = rotateImage(data.getStringExtra(Constants.EXTRA_FILE_PATH));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    bm = BitmapFactory.decodeFile(data.getStringExtra(Constants.EXTRA_FILE_PATH));
//                }
//
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//                byte[] b = baos.toByteArray();
//
                showProgressdialog();
                Async async = new Async(data.getStringExtra(Constants.EXTRA_FILE_PATH));
                async.execute();
            } else {
                mTabLayout.getTabAt(0).select();
            }
        }
    }

    /**
     * Async task to send updateImageRequest
     */
    private class Async extends AsyncTask {
        String mPhotoPath;
        String mNewPhotoPath;

        String mPhoto64;

        public Async(String photoPath) {
            mPhotoPath = photoPath;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            try {
                Bitmap bm = rotateImage(mPhotoPath);
                mPhoto64 = getPhoto64(bm);
                mNewPhotoPath = storeImage(bm);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        private String getPhoto64(Bitmap bm) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] b = baos.toByteArray();
            return Base64.encodeToString(b, Base64.DEFAULT);
        }

        private String storeImage(Bitmap image) {
            File pictureFile = getOutputMediaFile();
            if (pictureFile == null) {
                Log.d("PHOTOSTORAGE",
                        "Error creating media file, check storage permissions: ");// e.getMessage());
                return "";
            }
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                image.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.close();
                return pictureFile.getAbsolutePath();
            } catch (FileNotFoundException e) {
                Log.d("PHOTOSTORAGE", "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d("PHOTOSTORAGE", "Error accessing file: " + e.getMessage());
            }
            return "";
        }

        private  File getOutputMediaFile(){
            // To be safe, you should check that the SDCard is mounted
            // using Environment.getExternalStorageState() before doing this.
            File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                    + "/Android/data/"
                    + getApplicationContext().getPackageName()
                    + "/Files");

            // This location works best if you want the created images to be shared
            // between applications and persist after your app has been uninstalled.

            // Create the storage directory if it does not exist
            if (! mediaStorageDir.exists()){
                if (! mediaStorageDir.mkdirs()){
                    return null;
                }
            }
            // Create a media file name
            String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
            File mediaFile;
            String mImageName="MI_"+ timeStamp +".jpg";
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
            return mediaFile;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
//            mOnRequestReady.updateImage();
            dismissProgressDialog();
            if(!mNewPhotoPath.isEmpty()) {
                mPictureFragment.setImage(mPhoto64, mNewPhotoPath);
            } else {
                showToast("Erro ao formatar imagem.");
            }
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
