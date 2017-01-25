package com.oddsix.nutripro.fragments;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.oddsix.nutripro.R;
import com.oddsix.nutripro.activities.CameraActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

/**
 * Created by azul on 23/12/16.
 */
@SuppressWarnings("deprecation")
public class CameraFragment extends Fragment implements Camera.PictureCallback {
    private Camera mCamera;
    private ImageSurfaceView mImageSurfaceView;
    private FrameLayout mCameraPreviewLayout;

    private OnCameraListener mOnCameraListener;

    public static final int FLASH_AUTO = 0;
    public static final int FLASH_ON = 1;
    public static final int FLASH_OFF = 2;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);
        mCameraPreviewLayout = (FrameLayout) view;
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mCamera = getCameraInstance();
        mImageSurfaceView = new ImageSurfaceView(getContext(), mCamera);
        mCameraPreviewLayout.addView(mImageSurfaceView);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mOnCameraListener = (CameraActivity) context;
    }

    public void takePicture() {
        mCamera.takePicture(null, null, this);
    }

    public void startCameraAgain() {
        mCamera.startPreview();
    }


    public void setFlash(int flashMode) {
        String flashModeParam = Camera.Parameters.FLASH_MODE_AUTO;
        switch (flashMode) {
            case FLASH_AUTO:
                flashModeParam = Camera.Parameters.FLASH_MODE_AUTO;
                break;
            case FLASH_OFF:
                flashModeParam = Camera.Parameters.FLASH_MODE_OFF;
                break;
            case FLASH_ON:
                flashModeParam = Camera.Parameters.FLASH_MODE_ON;
                break;
        }
        Camera.Parameters p = mCamera.getParameters();
        p.setFlashMode(flashModeParam);
        mCamera.setParameters(p);
    }

    public boolean isFlashSupported() {
        if (mCamera == null) {
            return false;
        }

        Camera.Parameters parameters = mCamera.getParameters();

        if (parameters.getFlashMode() == null) {
            return false;
        }

        List<String> supportedFlashModes = parameters.getSupportedFlashModes();
        if (supportedFlashModes == null || supportedFlashModes.isEmpty() || supportedFlashModes.size() == 1 && supportedFlashModes.get(0).equals(Camera.Parameters.FLASH_MODE_OFF)) {
            return false;
        }

        return true;
    }

    private Camera getCameraInstance() {
        Camera camera = null;
        try {
            camera = Camera.open();
            Camera.Parameters params = camera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            camera.setParameters(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return camera;
    }

    @Override
    public void onPictureTaken(byte[] bytes, Camera camera) {
        File file = new File(getActivity().getExternalFilesDir(null), "pic.jpg");
        FileOutputStream output = null;
        try {
            output = new FileOutputStream(file);
            output.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != output) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        mOnCameraListener.onPictureTaken(file);
    }

    private static class ImageSaver implements Runnable {

        /**
         * The JPEG image
         */
        private final byte[] mBytes;
        /**
         * The file we save the image into.
         */
        private final File mFile;

        public ImageSaver(byte[] bytes, File file) {
            mFile = file;
            mBytes = bytes;
        }

        @Override
        public void run() {
            FileOutputStream output = null;
            try {
                output = new FileOutputStream(mFile);
                output.write(mBytes);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (null != output) {
                    try {
                        output.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }



    }

    class ImageSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

        private Camera camera;
        private SurfaceHolder surfaceHolder;

        public ImageSurfaceView(Context context, Camera camera) {
            super(context);
            this.camera = camera;
            this.camera.setDisplayOrientation(90);
            this.camera.autoFocus(new Camera.AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean b, Camera camera) {

                }
            });
            this.surfaceHolder = getHolder();
            this.surfaceHolder.addCallback(this);
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                this.camera.setPreviewDisplay(holder);
                this.camera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            this.getHolder().removeCallback(this);
            this.camera.stopPreview();
            this.camera.release();
            this.camera = null;
        }
    }
}
