package com.bins.biffhelps.utils;

import android.os.Environment;

import java.io.File;

public class KConstant {

    /*****************存储路径 path*****************/
    //SD卡根目录[/storage/sdcard0]
    public final static String SD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;

    //应用缓存目录[/data/data/应用包名/cache]
    public final static String CACHE_PATH = UIKit.getContext().getCacheDir().getAbsolutePath() + File.separator;

    // 应用根目录名称 sd/0/biff_helps
    public static String SD_ROOT_NAME = "biff_helps";

    // /data/data/应用包名/cache/biff_helps
    public static String CACHE_ROOT_NAME = "biff_helps";

    // sp 默认文件名
    public static String SP_FILE_NAME = "com_biff_helps_common";

}
