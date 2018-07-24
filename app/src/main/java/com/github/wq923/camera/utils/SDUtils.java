package com.github.wq923.camera.utils;

/**
 * Created by 13521838583@163.com on 2018-7-24.
 */

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.text.format.Formatter;
import android.util.Log;
import java.io.File;

/**
 * Created by wq on 2018/7/2.
 *
 */

public class SDUtils {

    private static final String TAG = "SDUtils";

    //检测SD卡是否可用
    public static boolean getSDCardAvailable() {
        String sdCardState = Environment.getExternalStorageState();
        if (sdCardState.equals(Environment.MEDIA_MOUNTED)) {
            Log.d(TAG, "instance initializer!");
            return true;
        }

        Log.d(TAG, "SD Card is unavailable!");
        return false;
    }

    /**
     * 获取SD卡的总空间大小和可用空间
     */
    public static String getSDCardSize(Context context) {

        File file = Environment.getExternalStorageDirectory();
        long totalSpace = file.getTotalSpace();
        long usableSpace = file.getUsableSpace();
        String formatTotalSpace = Formatter.formatFileSize(context, totalSpace);
        String formatUsableSpace = Formatter.formatFileSize(context, usableSpace);

        Log.d(TAG, "getSDCardSize: total size = " + formatTotalSpace + ", unusable = " + formatUsableSpace);

        return "total=" + formatTotalSpace + "，available=" + formatUsableSpace;
    }

    //获取外部存储器路径

    /**
     * android的官方文档上说，采用Enviroment.getExternalStorageDirectory()方法可以得到android设备的外置存
     * 储(即外插SDCARD)，如果android设备有外插SDCARD的话就返回外插SDCARD的根目录路径，如果android设备没有外
     * 插SDCARD的话就返回android设备的内置SDCARD的路径。这套方案很快就被否决了，因为Enviroment类的这个方法里
     * 面的路径也是写死的，只有原生的android系统才使用这套方案，被更改过的anroid体统很多设备的路径都改了。
     */
    public static String getStorageDir() {
        //一般手机文件管理 根路径 /storage/emulated/0/
        return Environment.getExternalStorageDirectory().getPath();
    }

    //获取 当前APP 文件路径
    public static String getAppDir(Context context) {
        return context.getFilesDir().getPath();
    }

    //获取SD卡Picture路径
    public static String getPath() {
        //获取SD卡Picture路径
        File picDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        Log.d(TAG, "getPath: " + picDir.getPath() + "\n" + picDir.getAbsolutePath());
        return picDir.getPath();
    }

    //同步指定文件到图库
    //如果可以在文件管理器中看到，无法在图库中看到的，可能需要更新图库，需要发送一个广播让系统来扫描SD卡。
    //有时文件管理器也更新不及时，估计也是没有收到更新通知。
    public static void scanFileAsync(Context ctx, String filePath) {
        Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        scanIntent.setData(Uri.fromFile(new File(filePath)));
        ctx.sendBroadcast(scanIntent);
    }

    public static String createSDCardDir(String relativeDir) {

        // 创建一个文件夹对象，赋值为外部存储器的目录
        File sdcardDir = Environment.getExternalStorageDirectory();
        //得到一个路径，内容是 SDCard 的文件夹路径和名字，relativeDir 为相对路径，例如:"/wq923"
        File path = new File(sdcardDir.getPath() + relativeDir);
        if (!path.exists()) {
            //若不存在，创建目录，可以在应用启动的时候创建
            path.mkdirs();
        }

        Log.d(TAG, "createSDCardDir: " + path.getAbsolutePath());
        //注意，这里会删除最后一个 "/"，所以，不要写成"/wq923/networkimg/"，这样会删除最后的/，变成
        //"/wq923/networkimg"，导致文件名和路径连在一起，正确的做法是，将"/"单独写
        return path.getAbsolutePath();
    }

    //获取 SD 卡的所有信息
    public static String getSDCardInfo(Context c) {

        return "是否可用：" + getSDCardAvailable() + "\n" + "外部存储：" + getStorageDir() + "\n" +
                "APP 目录：" + getAppDir(c) + "\n" + "SD卡容量：" + getSDCardSize(c);
    }

}
