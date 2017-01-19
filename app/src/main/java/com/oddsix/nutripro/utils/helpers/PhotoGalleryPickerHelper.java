package com.oddsix.nutripro.utils.helpers;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.util.Base64;

import com.oddsix.nutripro.utils.PhotoUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * Created by azul on 23/12/16.
 */

public class PhotoGalleryPickerHelper {

    private static final int REQUEST_PERMISSION_READ_EXTERNAL_STORAGE = 201;
    public static final int REQUEST_IMAGE_SELECTOR = 2;
    private static final String[] STORAGE_PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private OnImageResultListener mOnImageResultListener;
    private Activity mActivity;
    private Fragment mFragment;
    private Context mContext;

    private int mPhotoMaxSize = 400;

    public PhotoGalleryPickerHelper(OnImageResultListener onImageResultListener, Activity activity) {
        mOnImageResultListener = onImageResultListener;
        mActivity = activity;
        mContext = activity;
    }

    public PhotoGalleryPickerHelper(OnImageResultListener onImageResultListener, Fragment fragment) {
        mOnImageResultListener = onImageResultListener;
        mFragment = fragment;
        mContext = mFragment.getActivity();
    }

    public void setPhotoMaxSize(int photoMaxSize) {
        mPhotoMaxSize = photoMaxSize;
    }

    public void startAlbum() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkStoragePermission()) {
                dispatchPhotoSelectionIntent();
            } else {
                if (mActivity != null) {
                    mActivity.requestPermissions(STORAGE_PERMISSIONS, REQUEST_PERMISSION_READ_EXTERNAL_STORAGE);
                } else {
                    mFragment.requestPermissions(STORAGE_PERMISSIONS, REQUEST_PERMISSION_READ_EXTERNAL_STORAGE);
                }
            }
        } else dispatchPhotoSelectionIntent();
    }

    /**
     * Opens Gallery to select image.
     */
    private void dispatchPhotoSelectionIntent() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (mActivity != null) {
            mActivity.startActivityForResult(galleryIntent, REQUEST_IMAGE_SELECTOR);
        } else {
            mFragment.startActivityForResult(galleryIntent, REQUEST_IMAGE_SELECTOR);
        }
    }

    /**
     * Checks storage permissions
     *
     * @return true if granted
     */
    private boolean checkStoragePermission() {
        int StoragePermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE);

        return StoragePermission == PackageManager.PERMISSION_GRANTED;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        File currentPhoto = null;
        switch (requestCode) {
            case REQUEST_IMAGE_SELECTOR:
                if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = mContext.getContentResolver().query(data.getData(), filePathColumn, null, null, null);
                    if (cursor == null || cursor.getCount() < 1) {
                        currentPhoto = null;
                        break;
                    }
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    if (columnIndex < 0) { // no column index
                        currentPhoto = null;
                        break;
                    }
                    currentPhoto = new File(cursor.getString(columnIndex));
                    cursor.close();
                } else {
                    currentPhoto = null;
                    mOnImageResultListener.onImageFailed();
                }
                break;
        }
        if (currentPhoto != null) {
            mOnImageResultListener.onImageReady(currentPhoto);
//            new Async(currentPhoto).execute();
        } else {
            mOnImageResultListener.onImageFailed();
        }
    }

    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults) {
        boolean storageAccepted;
        switch (permsRequestCode) {
            case REQUEST_PERMISSION_READ_EXTERNAL_STORAGE:
                storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (storageAccepted) dispatchPhotoSelectionIntent();
                break;
        }
    }

    /**
     * Async task to send updateImageRequest
     */
    private class Async extends AsyncTask {
        private File mPhoto;

        public Async(File photo) {
            mPhoto = photo;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            String mCurrentPhotoPath = mPhoto.getAbsolutePath();
            if (mCurrentPhotoPath != null && !mPhoto.getAbsolutePath().isEmpty()) {
                Bitmap bm;
                try {
                    bm = PhotoUtil.loadPrescaledBitmap(mCurrentPhotoPath, 400);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] b = baos.toByteArray();
                    String photo64 = Base64.encodeToString(b, Base64.DEFAULT);
//                    mOnImageResultListener.onImageReady(photo64, bm);
                } catch (IOException e) {
                    mOnImageResultListener.onImageFailed();
                    e.printStackTrace();
                }
            } else {
                mOnImageResultListener.onImageFailed();
            }
            return null;
        }
    }

    public interface OnImageResultListener {
        void onImageReady(File file);

        void onImageFailed();
    }
}
