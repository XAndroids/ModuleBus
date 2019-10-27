package com.cangwang.modulebus;

import android.app.Application;
import android.content.Context;

import com.cangwang.core.ModuleBus;

public class ModuleApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //从本地初始化模块
        //本地-从各个模块配置合并的json文件，读取配置，然后调用APT生成的相关源码初始化模块
        //远程-？？
        ModuleBus.init(base);
    }
}
