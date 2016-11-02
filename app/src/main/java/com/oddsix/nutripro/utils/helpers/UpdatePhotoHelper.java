package com.oddsix.nutripro.utils.helpers;

/**
 * Created by filippecl on 30/10/16.
 */

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.util.Base64;

import com.oddsix.nutripro.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mobile2you on 20/07/16.
 */
public class UpdatePhotoHelper {

    public interface OnRequestReady {
        void photoReady(String photo_64, Bitmap photo);
        void photoFailed();
    }

    private static final int PERMISSION_CODE_CAMERA = 200;
    private static final int PERMISSION_CODE_ALBUM = 201;
    public static final int REQUEST_IMAGE_SELECTOR = 2;
    public static final int REQUEST_TAKE_PHOTO = 1;

    private String[] perms = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.CAMERA"};
    private String[] permsStorage = {"android.permission.READ_EXTERNAL_STORAGE"};

    private File mCurrentPhoto;
    public String mCurrentPhotoPath = "";

    private Activity mContext;

    private DialogHelper mDialogHelper;

    private OnRequestReady mOnRequestReady;

    private int mPhotoMaxSize;


    public UpdatePhotoHelper(Activity context, OnRequestReady onRequestReady) {
        mOnRequestReady = onRequestReady;
        mContext = context;
    }

    public void initiate(boolean isWithRemoveOption, int maxSize) {
        mDialogHelper = new DialogHelper(mContext);
        mPhotoMaxSize = maxSize;
        String[] arrayOptions = new String[0];

        if(isWithRemoveOption){
            arrayOptions = mContext.getResources().getStringArray(R.array.update_image_options);
        } else arrayOptions = mContext.getResources().getStringArray(R.array.upload_image_options);


        mDialogHelper.showListDialog(mContext.getString(R.string.dialog_title_select_an_option),
                arrayOptions,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            //OPEN CAMERA
                            case 0:
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    if (checkCameraPermission()) {
                                        dispatchTakePictureIntent();
                                    } else {
                                        mContext.requestPermissions(perms, PERMISSION_CODE_CAMERA);
                                    }
                                } else dispatchTakePictureIntent();
                                break;
                            //OPEN ALBUM
                            case 1:
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    if (checkStoragePermission()) {
                                        dispatchPhotoSelectionIntent();
                                    } else {
                                        mContext.requestPermissions(permsStorage, PERMISSION_CODE_ALBUM);
                                    }
                                } else dispatchPhotoSelectionIntent();
                                break;
                            //REMOVE CURRENT IMAGE
                            case 2:
                                mOnRequestReady.photoReady(null, null);
                                mDialogHelper.dismissProgressDialog();
                                break;
                        }
                    }
                }, new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        mOnRequestReady.photoFailed();
                    }
                });
    }

    /**
     * Checks camera permissions
     *
     * @return true if all granted
     */
    private boolean checkCameraPermission() {
        int cameraPermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA);
        int StoragePermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE);

        return cameraPermission == PackageManager.PERMISSION_GRANTED && StoragePermission == PackageManager.PERMISSION_GRANTED;
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


    String mPhoto64;
    Bitmap mPhotoBitmap;
    /**
     * Async task to send updateImageRequest
     */
    private class Async extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] params) {
            if (mCurrentPhotoPath != null && !mCurrentPhotoPath.isEmpty()) {
                Bitmap bm;
                try {
                    bm = rotateImage(mCurrentPhotoPath);
                } catch (Exception e) {
                    e.printStackTrace();
                    bm = BitmapFactory.decodeFile(mCurrentPhotoPath);
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] b = baos.toByteArray();
                mPhotoBitmap = bm;
                mPhoto64 = Base64.encodeToString(b, Base64.DEFAULT);
            } else {
                mPhoto64 = null;
                mPhotoBitmap = null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
//            mOnRequestReady.updateImage();
            mOnRequestReady.photoReady(mPhoto64, mPhotoBitmap);
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    /**
     * Opens Gallery to select image.
     */
    private void dispatchPhotoSelectionIntent() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        mContext.startActivityForResult(galleryIntent, REQUEST_IMAGE_SELECTOR);
    }

    /**
     * Opens system camera application to capture a photo.
     */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(mContext.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                mContext.startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults) {
        boolean storageAccepted;
        switch (permsRequestCode) {
            case PERMISSION_CODE_CAMERA:
                storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                if (storageAccepted && cameraAccepted) dispatchTakePictureIntent();
                break;
            case PERMISSION_CODE_ALBUM:
                storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (storageAccepted) dispatchPhotoSelectionIntent();
                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_IMAGE_SELECTOR:
                if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = mContext.getContentResolver().query(data.getData(), filePathColumn, null, null, null);
                    if (cursor == null || cursor.getCount() < 1) {
                        mCurrentPhoto = null;
                        break;
                    }
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    if (columnIndex < 0) { // no column index
                        mCurrentPhoto = null;
                        break;
                    }
                    mCurrentPhoto = new File(cursor.getString(columnIndex));
                    mCurrentPhotoPath = cursor.getString(columnIndex);
                    cursor.close();
                } else {
                    mCurrentPhoto = null;
                    mOnRequestReady.photoFailed();
                }
                break;
            case REQUEST_TAKE_PHOTO:
                if (resultCode != Activity.RESULT_OK) {
                    mCurrentPhoto = null;
                    mOnRequestReady.photoFailed();
                } else {
                    mCurrentPhoto = new File(mCurrentPhotoPath);
                }
                break;
        }
        if (mCurrentPhoto != null) {
            new Async().execute();
        }
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

    private Bitmap loadPrescaledBitmap(String filename) throws IOException {
        // Facebook image size
        mPhotoMaxSize = 400;

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
            resizeScale = (int)Math.pow(2, (int) Math.round(Math.log(mPhotoMaxSize / (double) Math.max(opts.outHeight, opts.outWidth)) / Math.log(0.5)));
        }

        // Load pre-scaled bitmap
        opts = new BitmapFactory.Options();
        opts.inSampleSize = resizeScale;
        fis = new FileInputStream(file);
        bmp = BitmapFactory.decodeStream(fis, null, opts);

        fis.close();

        return bmp;
    }
}
