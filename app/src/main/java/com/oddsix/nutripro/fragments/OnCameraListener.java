package com.oddsix.nutripro.fragments;

import android.graphics.Bitmap;

import java.io.File;

/**
 * Created by filippecl on 26/12/16.
 */


public interface OnCameraListener {
    void onCameraNotPresent();

    void onPictureTaken(File file);

    void onPictureError();
}
