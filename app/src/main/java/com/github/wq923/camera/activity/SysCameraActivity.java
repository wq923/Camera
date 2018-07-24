package com.github.wq923.camera.activity;

/**
 * Created by 13521838583@163.com on 2018-7-24.
 *
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.github.wq923.camera.R;
import com.github.wq923.camera.utils.SDUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SysCameraActivity extends Activity {

    /**
     * 直接使用系统相机
     * <p>
     * 1、获取拍照缩略图（直接通过调用系统相机，系统相机将拍到的照片数据先压缩，再回传到 APP）
     * <p>
     * 2、获取拍照原图：系统相机将传入的参数 uri，作为原图的保存地址（直接调用系统相机，系统相机将拍到的
     * 照片原始数据保存到本地目录中，当前 APP 会读取该文件进行显示）
     * <p>
     * 参考：https://www.jb51.net/article/98678.htm
     * <p>
     * 在我们Android开发中经常需要做这个一个功能，调用系统相机拍照，然后获取拍摄的照片。
     * 下面是介绍两种方法获取拍摄之后的照片，一种是通过Bundle来获取压缩过的照片，一种是通过SD卡获取的原图。
     */

    private int REQUEST_COMPRESSED = 0;
    private int REQUEST_ORIGINAL = 1;

    private String sdPath;      //SD卡的路径
    private String picPath;     //图片存储路径

    private ImageView mImg;

    private static final String TAG = "SysCameraActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sys_camera);

        printSDCardInfo();

        mImg = findViewById(R.id.imageView1);
        Button smallImage = findViewById(R.id.id_get_compressed_img);
        Button originalImage = findViewById(R.id.id_get_original_img);

        sdPath = Environment.getExternalStorageDirectory().getPath();           //获取SD卡的路径

        //点击获取缩略图
        smallImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // 启动相机
                startActivityForResult(intent1, REQUEST_COMPRESSED);
            }
        });

        //点击获取原图
        originalImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //picPath = sdPath + "/" + getCurrentTime() + "_temp" + ".png";
                picPath = SDUtils.createSDCardDir("/wangq923") + "/temp_" + getCurrentTime() + ".jpg";

                Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri uri = Uri.fromFile(new File(picPath));
                //为拍摄的图片指定一个存储的路径
                intent2.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent2, REQUEST_ORIGINAL);
            }
        });
    }

    private void printSDCardInfo() {
        Log.d(TAG, "onCreate: " + SDUtils.getSDCardAvailable());
        Log.d(TAG, "onCreate: " + SDUtils.getAppDir(getApplicationContext()));
        Log.d(TAG, "onCreate: " + SDUtils.getStorageDir());
        Log.d(TAG, "onCreate: " + SDUtils.getSDCardSize(getApplicationContext()));
        Log.d(TAG, "onCreate: " + SDUtils.getPath());
        Log.d(TAG, "onCreate: " + SDUtils.createSDCardDir("/wq923"));
    }

    private String getCurrentTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式在android中，创建文件时，文件名中不能包含“：”冒号
        return df.format(new Date());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_COMPRESSED) {
                /**
                 * 通过这种方法取出的拍摄会默认压缩，因为如果相机的像素比较高拍摄出来的图会比较高清，
                 * 如果图太大会造成内存溢出（OOM），因此，此种方法会默认给图片进行压缩
                 */
                Bundle bundle = data.getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data");
                mImg.setImageBitmap(bitmap);

            } else if (requestCode == REQUEST_ORIGINAL) {
                /**
                 * 这种方法是通过内存卡的路径进行读取图片，所以的到的图片是拍摄的原图
                 */
                FileInputStream fis = null;
                try {
                    Log.e("sdPath2", picPath);
                    //把图片转化为字节流
                    fis = new FileInputStream(picPath);
                    Log.d(TAG, "onActivityResult:" + fis);
                    //把流转化图片
                    Bitmap bitmap = BitmapFactory.decodeStream(fis);
                    Log.d(TAG, "onActivityResult:" + bitmap);
                    mImg.setImageBitmap(bitmap);
                    Log.d(TAG, "onActivityResult: mImg not null");

                    SDUtils.scanFileAsync(getApplicationContext(), picPath);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        fis.close();//关闭流
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
