package com.github.wq923.camera.activity;

/**
 * Created by 13521838583@163.com on 2018-7-24.
 *
 */


import android.content.Intent;
import android.graphics.Color;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.github.wq923.camera.R;
import com.github.wq923.camera.utils.CameraUtils;

public class CustomCameraActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_camera);

        TextView mPrompt = (TextView) findViewById(R.id.id_support_camera_or_not);
        TextView mSilent = (Button) findViewById(R.id.id_silent_camera);
        TextView mPreview = (Button) findViewById(R.id.id_preview_camera);

        if (CameraUtils.checkCameraHardware(getApplicationContext())) {

            StringBuffer cameraInfo = new StringBuffer();
            int num = CameraUtils.getNumberOfCamera();
            for (int j = 0; j < num; j++) {
                if (CameraUtils.getCameraInfo(j).facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    cameraInfo.append("  后置摄像头").append(j).append("，相机角度：").append(CameraUtils.getCameraInfo(j).orientation).append('\n');
                }
                if (CameraUtils.getCameraInfo(j).facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    cameraInfo.append("  前置摄像头").append(j).append("，相机角度：").append(CameraUtils.getCameraInfo(j).orientation).append('\n');
                }
            }

            mPrompt.setTextColor(Color.GREEN);
            mPrompt.setText(" 该设备共有 " + num + " 个摄像头！" + '\n' + cameraInfo);
        } else {
            mPrompt.setTextColor(Color.RED);
            mPrompt.setText(" 该设备不支持相机设备！");
        }

        mSilent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CustomCameraActivity.this, SilentCameraActivity.class));
            }
        });

        mPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
