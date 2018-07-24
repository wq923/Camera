package com.github.wq923.camera.activity;

/**
 * Created by 13521838583@163.com on 2018-7-24.
 * 
 */

import android.app.Activity;
import android.graphics.Color;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.github.wq923.camera.R;
import com.github.wq923.camera.utils.CameraUtils;

/**
 * Created by wq on 2018/7/24.
 *
 */

public class SilentCameraActivity extends Activity {

    private Camera mCamera;
    private static final String TAG = "SilentCameraActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_silent_camera);

        final TextView cameraState = findViewById(R.id.id_camera_state);
        Button getCamera = findViewById(R.id.id_get_camera);
        Button silentOpenCamera = findViewById(R.id.id_silent_open_camera);
        Button silentCloseCamera = findViewById(R.id.id_silent_close_camera);
        Button faceDetect = findViewById(R.id.id_start_face_detect);
        Button stopFaceDetect = findViewById(R.id.id_stop_face_detect);
        Button silentReleaseCamera = findViewById(R.id.id_silent_release_camera);


        getCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mCamera = CameraUtils.getCamera(1); //最好异步进行
                    if (mCamera != null) {
                        cameraState.setTextColor(Color.GREEN);
                        cameraState.setText("获取相机实例成功！");
                    }
                } catch (Exception e) {
                    cameraState.setTextColor(Color.RED);
                    cameraState.setText("获取相机实例失败！");
                    e.printStackTrace();
                }
            }
        });

        silentOpenCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mCamera.startPreview();
                    if (mCamera != null) {
                        cameraState.setTextColor(Color.GREEN);
                        cameraState.setText("相机静默打开成功！");
                    }
                } catch (Exception e) {
                    cameraState.setTextColor(Color.RED);
                    cameraState.setText("请先获取相机实例！");
                    e.printStackTrace();
                }

            }
        });

        silentCloseCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mCamera.stopPreview();
                    cameraState.setTextColor(Color.GREEN);
                    cameraState.setText("关闭相机成功！");
                } catch (Exception e) {
                    cameraState.setTextColor(Color.RED);
                    cameraState.setText("请先获取相机实例！");
                    e.printStackTrace();
                }
            }
        });

        faceDetect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mCamera.setFaceDetectionListener(new Camera.FaceDetectionListener() {
                        @Override
                        public void onFaceDetection(Camera.Face[] faces, Camera camera) {
                            Log.d(TAG, "onFaceDetection: ");
                            if (faces != null && faces.length > 0) {
                                Log.d(TAG, "onFaceDetection: 检测到人脸 " + faces.length + " 张！");
                                cameraState.setTextColor(Color.GREEN);
                                cameraState.setText("检测到 " + faces.length + " 张人脸！");
                            }
                        }
                    });

                    if (mCamera.getParameters().getMaxNumDetectedFaces() > 0) {
                        Log.d(TAG, "相机支持人脸检测 API！");
                        mCamera.startFaceDetection();   //相机开始检测人脸，需要在 startPreview 之后调用
                    }
                    cameraState.setTextColor(Color.GREEN);
                    cameraState.setText("正在检测人脸...");
                } catch (Exception e) {
                    cameraState.setTextColor(Color.RED);
                    cameraState.setText("问题原因：" + '\n'
                            + "1、请先获取相机实例！" +
                            "2、或已获取实例，但未打开相机！" +
                            "3、重复启动人脸检测！");
                    e.printStackTrace();
                }
            }
        });

        stopFaceDetect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mCamera.stopFaceDetection();
                    cameraState.setTextColor(Color.GREEN);
                    cameraState.setText("停止人脸检测");
                } catch (Exception e) {
                    cameraState.setTextColor(Color.RED);
                    cameraState.setText("请先获取相机实例 或 人脸检测未开始");
                    e.printStackTrace();
                }
            }
        });

        silentReleaseCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    mCamera.release();
                    mCamera = null;
                    cameraState.setTextColor(Color.GREEN);
                    cameraState.setText("相机释放成功！");
                } catch (Exception e) {
                    cameraState.setTextColor(Color.RED);
                    cameraState.setText("请先获取相机实例！");
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    protected void onDestroy() {

        try {
            mCamera.stopFaceDetection();
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        } catch (Exception e) {
            Log.d(TAG, "onDestroy: ");
            e.printStackTrace();
        }

        super.onDestroy();
    }
}

//参考：
//https://www.cnblogs.com/monodin/archive/2013/05/25/3099169.html
