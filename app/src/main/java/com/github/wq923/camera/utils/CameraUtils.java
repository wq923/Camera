package com.github.wq923.camera.utils;

/**
 * Created by 13521838583@163.com on 2018-7-24.
 */

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.util.Log;

/**
 * Created by wq on 2018/7/10.
 *
 */

public class CameraUtils {

    private static final String TAG = "CameraUtils";

    /**
     * Check if this device has a camera
     */
    public static boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Log.d(TAG, "this device has a camera.");
            return true;
        } else {
            Log.d(TAG, "no camera on this device.");
            return false;
        }
    }

    /**
     * A safe way to get an instance of the Camera object.
     */
    public static Camera getCamera(int i) {
        Camera camera = null;
        try {
            camera = Camera.open(i);
        } catch (Exception e) {
            if (i == 0) {
                Log.d(TAG, "Rear camera is not available (in use or does not exist).");
            } else if (i == 1) {
                Log.d(TAG, "Secondary camera is not available (in use or does not exist).");
            } else {
                Log.d(TAG, i + " camera is not available (in use or does not exist).");
            }

            e.printStackTrace();
        }
        return camera;
    }


    /**
     * get current camera info
     *
     * @param cameraId current camera id
     * @return camera info
     */
    public static Camera.CameraInfo getCameraInfo(int cameraId) {
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, cameraInfo);
        return cameraInfo;
    }

    /**
     * 获取摄像头数量
     */
    public static int getNumberOfCamera() {
        return Camera.getNumberOfCameras();
    }
}
