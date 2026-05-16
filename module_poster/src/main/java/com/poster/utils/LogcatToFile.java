package com.poster.utils;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogcatToFile {
    private static LogcatToFile instance = new LogcatToFile();
    private static String PROJECT_NAME = "AC00511-04";
    private static String LOG_FILE_PRE = "LOGCAT";

    private static String APK_DIR_PATH = "/storage/emulated/0/Android/data/com.poster/files/Pictures";
    private static String USB_DIR_PATH = "";
    private static boolean isStart = false;

    public static LogcatToFile getInstance() {
        return instance;
    }

    /**
     * 启动电子表、安装完成打开 或 切换语言，都会调用该方法，也就是调用HomeApplication.onCreate()
     * 每次调用该方法，之前的logcat文件流停止，生成新的txt文件记录logcat。
     */
    public static void start() {
        if (isStart) {
            return;
        }
        isStart = true;

        // log to file
        // 没有限制文件大小，会挂掉。1小时 约 3MB
        new Thread(() -> {
            try {
                String PATH = APK_DIR_PATH;
                // LOGCAT_AC00511-04_20210826-140555.txt
                String NAME = LOG_FILE_PRE + "_" + PROJECT_NAME + "_" + new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date(System.currentTimeMillis())) + ".txt";
                Log.i("LocatToFile","开启日志记录-----------------------" + NAME);

                File file = new File(PATH, NAME);
                if (!file.exists()) {
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                }
                String cmd = "logcat -f " + file.getAbsolutePath();
                Runtime.getRuntime().exec(cmd);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * 日志文件清理
     * 删除文件名包含 LOG_FILE_PRE 的文件
     */
    public static void clearFile() {
        File dir = new File(APK_DIR_PATH);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }

        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.getName().contains(LOG_FILE_PRE)) {
                file.delete();
            }
        }
    }

    /**
     * 复制文件
     * @param oldPath
     * @param newPath
     */
    private static void copyFile(String oldPath, String newPath) {
        File temp = new File(oldPath);
        try (
                FileInputStream fileInputStream = new FileInputStream(temp);
                FileOutputStream fileOutputStream = new FileOutputStream(newPath);
        ) {
            byte[] buffer = new byte[1024];
            int byteRead;
            // !=-1 也可以写成！=null,意思是读取的数据不为负数或者null就说明还没有读取完毕
            while ((byteRead = fileInputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, byteRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 检测文件是否存在
     *
     * @param filepath
     * @return
     */
    private static boolean isCheckExist(String filepath) {
        try {
            if (null == filepath || filepath.isEmpty()) {
                return false;
            }
            File file = new File(filepath);
            return file.exists();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
}