package com.zhangyf.bidirectio.net;

import android.os.Environment;

import java.io.File;

/**
 * Created on 2018-03-09 09:33.
 *
 * @author zyf
 */
public class AppConfig {

    /**
     * sd卡存储文件夹名称
     */
    public static final String YF_BI_CACHE_DIR = "YF_Bidirecotio_Cache";

    // 默认超时时间
    public static final int TIME_OUT = 20;

    /**
     * okhttp缓存空间
     */
    public static final int OK_HTTP_CACHE_SIZE = 400 * 1024 * 1024;

    /**
     * 缓存路径
     */
    public static final String DEFAULT_CACHE_PATH = Environment.getExternalStorageDirectory() + File.separator
            + YF_BI_CACHE_DIR + File.separator;
    /**
     * okhttp 存储地址
     */
    public static final String DEFAULT_SAVE_OK_HTTP_PATH = DEFAULT_CACHE_PATH + "okhttp" + File.separator;
}
