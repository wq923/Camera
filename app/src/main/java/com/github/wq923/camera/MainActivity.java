package com.github.wq923.camera;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.github.wq923.camera.activity.CustomCameraActivity;
import com.github.wq923.camera.activity.SysCameraActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnSysCamera = (Button) findViewById(R.id.id_sys_camera);
        Button btnCustomCamera = (Button) findViewById(R.id.id_custom_camera);

        btnSysCamera.setOnClickListener(this);
        btnCustomCamera.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.id_sys_camera:
            {
                //系统相机使用demo（使用Android系统自带的相机应用进行拍照）
                startActivity(new Intent(MainActivity.this, SysCameraActivity.class));
                break;
            }
            case R.id.id_custom_camera:
            {
                //用户使用 Camera API 使用相机
                startActivity(new Intent(MainActivity.this, CustomCameraActivity.class));
                break;
            }
        }
    }
}
