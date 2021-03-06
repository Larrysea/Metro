package com.accelerator.metro.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.util.Log;

/**
 * Created by Nicholas on 2016/7/15.
 */
public class CameraUtil {

    public static final int INVALID_CAMERA_ID = -1;

    private CameraUtil() {
        throw new UnsupportedOperationException("Do not need instantiate!");
    }

    public static boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            return true;
        } else {
            return false;
        }
    }

    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return c;
    }

    /**
     * 获取前置摄像头id
     * @return
     */
    public static int findFrontFacingCamera() {
        int cameraId = INVALID_CAMERA_ID;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                Log.d("CameraUtil", "Camera found");
                cameraId = i;
                break;
            }
        }
        return cameraId;
    }

    /**
     * 获取后置摄像头id
     * @return
     */
    public static int findBackFacingCamera() {
        int cameraId = INVALID_CAMERA_ID;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                Log.d("CameraUtil", "Camera found");
                cameraId = i;
                break;
            }
        }
        return cameraId;
    }

    public static boolean isCameraIdValid(int cameraId) {
        return cameraId != INVALID_CAMERA_ID;
    }
}
