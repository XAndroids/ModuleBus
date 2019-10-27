package com.cangwang.core.cwmodule.ex;

import android.content.res.Configuration;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.cangwang.core.cwmodule.api.BackPressStack;
import com.cangwang.core.util.ModuleUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 模块管理类，负责管理模块行管信息，分发模块生命周期等
 */
public class ModuleExManager {
    //模块实体
    protected ArrayMap<String, CWAbsExModule> allModules = new ArrayMap<>();
    //模板名称
    private String template;

    //工具Handler
    private Handler handler;
    //工具pool
    private ExecutorService pool;

    public void moduleConfig(String template) {
        this.template = template;
    }

    public String getTemplate() {
        return template;
    }

    public Handler getHandler() {
        if (handler == null) {
            handler = new Handler(Looper.getMainLooper());
        }
        return handler;
    }

    public ExecutorService getPool() {
        if (pool == null) {
            pool = Executors.newSingleThreadExecutor();
        }
        return pool;
    }

    //allModules操作方法
    public CWAbsExModule getModuleByNames(String name) {
        if (!ModuleUtil.empty(allModules))
            return allModules.get(name);
        return null;
    }

    public void remove(String name) {
        if (!ModuleUtil.empty(allModules)) {
            allModules.remove(name);
        }
    }

    public void putModule(String name, CWAbsExModule module) {
        allModules.put(name, module);
    }


    //生命周期分发方法
    public void onResume() {
        for (CWAbsExModule module : allModules.values())
            if (module != null)
                module.onResume();
    }

    public void onPause() {
        for (CWAbsExModule module : allModules.values())
            if (module != null)
                module.onPause();
    }

    public void onStop() {
        for (CWAbsExModule module : allModules.values())
            if (module != null)
                module.onStop();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        for (CWAbsExModule module : allModules.values())
            if (module != null)
                module.onOrientationChanges(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE);
    }

    public void onDestroy() {
        handler = null;
        pool = null;
        for (CWAbsExModule module : allModules.values()) {
            if (module != null) {
                module.onDestroy();
            }
        }
    }

    public boolean onBackPressed() {
        boolean hasCallback = false;
        if (BackPressStack.getInstance().getStack().size() > 0) {
            Log.e("ModuleExManager", "peek " + BackPressStack.getInstance().getStack().toString());
            hasCallback = BackPressStack.getInstance().getStack().peek().onBackPress();
        }
        return hasCallback;
    }
}
