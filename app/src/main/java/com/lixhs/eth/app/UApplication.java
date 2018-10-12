package com.lixhs.eth.app;

import com.lixh.BuildConfig;
import com.lixh.app.BaseApplication;
import com.lixh.utils.ULog;

/**
 * APPLICATION
 */
public class UApplication extends BaseApplication {

    public static UApplication instance;

    @Override
    public void init() {
        instance = this;
        //初始化logger
        ULog.logInit(BuildConfig.LOG_DEBUG);

    }
}
