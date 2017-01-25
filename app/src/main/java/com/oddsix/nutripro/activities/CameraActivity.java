package com.oddsix.nutripro.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.oddsix.nutripro.BaseActivity;
import com.oddsix.nutripro.R;
import com.oddsix.nutripro.fragments.Camera2Fragment;
import com.oddsix.nutripro.fragments.CameraFragment;
import com.oddsix.nutripro.fragments.OnCameraListener;
import com.oddsix.nutripro.utils.Constants;
import com.oddsix.nutripro.utils.helpers.PhotoGalleryPickerHelper;

import java.io.File;

/**
 * Created by azul on 23/12/16.
 */

public class CameraActivity extends BaseActivity implements PhotoGalleryPickerHelper.OnImageResultListener, OnCameraListener {

    private CameraFragment mCamera;
    private Camera2Fragment mCamera2;
    private File mFile;
    private PhotoGalleryPickerHelper mPhotoGalleryPickerHelper;
    private boolean mUsesCamera2 = false;

    private FloatingActionButton mFab;

    private ImageView mCloseIv;

    private ImageView mCheckIv;

    private ImageView mAlbumIv;


    private int mCurrentFlash;
    private static final int[] FLASH_ICONS = {
            R.drawable.ic_flash_auto,
            R.drawable.ic_flash_off,
            R.drawable.ic_flash_on,
    };

    private static final int[] FLASH_TITLES = {
            R.string.flash_auto,
            R.string.flash_off,
            R.string.flash_on,
    };

    private static final int[] FLASH_OPTIONS = {
            CameraFragment.FLASH_AUTO,
            CameraFragment.FLASH_ON,
            CameraFragment.FLASH_OFF
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_camera);
        findViews();

        mPhotoGalleryPickerHelper = new PhotoGalleryPickerHelper(this, this);

        addCameraFragment();
    }


    private void findViews() {
        mFab = (FloatingActionButton) findViewById(R.id.camera_take_pic);
        mCloseIv = (ImageView) findViewById(R.id.camera_close);
        mCheckIv = (ImageView) findViewById(R.id.camera_check);
        mAlbumIv = (ImageView) findViewById(R.id.camera_album);
    }

    private void addCameraFragment() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mCamera2 = new Camera2Fragment();
            mUsesCamera2 = true;
            getSupportFragmentManager().beginTransaction().add(R.id.camera_fragment, mCamera2).commit();
        } else {
            mCamera = new CameraFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.camera_fragment, mCamera).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_camera, menu);
        if (mUsesCamera2) {
            menu.findItem(R.id.switch_flash).setVisible(mCamera2.isFlashSupported());
        } else {
            menu.findItem(R.id.switch_flash).setVisible(mCamera.isFlashSupported());
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.switch_flash:
                mCurrentFlash = (mCurrentFlash + 1) % FLASH_OPTIONS.length;
                item.setTitle(FLASH_TITLES[mCurrentFlash]);
                item.setIcon(FLASH_ICONS[mCurrentFlash]);
                setFlash(FLASH_OPTIONS[mCurrentFlash]);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setFlash(int flashOption) {
        if (mUsesCamera2) {
            if (mCamera2.isAdded()) {
                mCamera2.setFlash(flashOption);
            }
        } else {
            if (mCamera.isAdded()) {
                mCamera.setFlash(flashOption);
            }
        }
    }


    public static Intent createIntent(Context context) {
        return new Intent(context, CameraActivity.class);
    }

    public void onTakePicClicked(View view) {
        if (mUsesCamera2) {
            mCamera2.takePicture();
        } else {
            mCamera.takePicture();
        }
    }

    public void onAlbumClicked(View view) {
        mPhotoGalleryPickerHelper.startAlbum();
    }

    public void onCheckClicked(View view) {
        Intent dataIntent = new Intent();
        dataIntent.putExtra(Constants.EXTRA_FILE_PATH, mFile.getAbsolutePath());
        setResult(RESULT_OK, dataIntent);
        finish();
    }

    public void onCloseClicked(View view) {
        showPreviewControls(false);
        if (mUsesCamera2) {
            mCamera2.unlockFocus();
        } else {
            mCamera.startCameraAgain();
        }
    }

    @Override
    public void onImageReady(File file) {
        mFile = file;
        Intent dataIntent = new Intent();
        dataIntent.putExtra(Constants.EXTRA_FILE_PATH, mFile.getAbsolutePath());
        setResult(RESULT_OK, dataIntent);
        finish();
    }

    @Override
    public void onImageFailed() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPhotoGalleryPickerHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPhotoGalleryPickerHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Override
    public void onCameraNotPresent() {
        finish();
    }

    @Override
    public void onPictureTaken(File file) {
        mFile = file;
        showPreviewControls(true);
    }

    private void showPreviewControls(boolean show) {
        try {
            if (show) {
                mFab.hide();
                mCheckIv.setVisibility(View.VISIBLE);
                mCloseIv.setVisibility(View.VISIBLE);
                mAlbumIv.setVisibility(View.GONE);
            } else {
                mFab.show();
                mAlbumIv.setVisibility(View.VISIBLE);
                mCheckIv.setVisibility(View.GONE);
                mCloseIv.setVisibility(View.GONE);
            }
        } catch (NullPointerException e) {

        }
    }

    @Override
    public void onPictureError() {

    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showPreviewControls(false);
    }
}
