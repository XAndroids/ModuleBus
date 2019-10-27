package com.cangwang.core;

import java.util.HashMap;

/**
 * Module提供Api管理类机制：提供跨组件的，模块Api的调用方式（和提供Service方案相比具有更通用性）
 */
public class ModuleApiManager {
    HashMap<Class<? extends MBaseApi>, MBaseApi> aMap;

    //饿汉单例方法
    static ModuleApiManager instance = new ModuleApiManager();

    private ModuleApiManager() {
        aMap = new HashMap<>();
    }

    public static ModuleApiManager getInstance() {
        return instance;
    }

    public <T extends MBaseApi> T getApi(Class<T> clazz) {
        return (T) aMap.get(clazz);
    }

    public void putApi(Class<? extends MBaseApi> key, MBaseApi value) {
        aMap.put(key, value);
    }

    public void removeApi(Class<? extends MBaseApi> key) {
        aMap.remove(key);
    }
}
