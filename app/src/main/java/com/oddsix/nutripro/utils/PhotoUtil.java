package com.oddsix.nutripro.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by filippecl on 19/01/17.
 */

public class PhotoUtil {

    public static Bitmap loadPrescaledBitmap(String filename, int photoMaxSize) throws IOException {
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

        if (opts.outHeight > photoMaxSize || opts.outWidth > photoMaxSize) {
            resizeScale = (int) Math.pow(2, (int) Math.round(Math.log(photoMaxSize / (double) Math.max(opts.outHeight, opts.outWidth)) / Math.log(0.5)));
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
