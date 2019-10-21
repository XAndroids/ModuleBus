package com.cangwang.core.cwmodule.ex;

import android.content.res.Configuration;
import android.support.v4.util.ArrayMap;

import com.cangwang.core.util.ModuleUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 模块管理类，保存了模块现相关的信息，分发相关模块事件
 */
public class ModuleExManager {
    //模块名字
    private List<String> modules = new ArrayList<>();
    //模块实体
    protected ArrayMap<String,ELAbsExModule> allModules = new ArrayMap<>();

    public List<String> getModuleNames(){
        return modules;
    }

    public void moduleConfig(List<String> modules) {
        this.modules = modules;
    }

    public ELAbsExModule getModuleByNames(String name){
        if (!ModuleUtil.empty(allModules))
            return allModules.get(name);
        return null;
    }

    public void remove(String name){
        if (!ModuleUtil.empty(allModules)){
            allModules.remove(name);
        }
    }

    public void putModule(String name,ELAbsExModule module){
        allModules.put(name,module);
    }

    public void onResume(){
        for (ELAbsExModule module:allModules.values())
            if (module !=null)
                module.onResume();
    }

    public void onPause(){
        for (ELAbsExModule module:allModules.values())
            if (module !=null)
                module.onPause();
    }

    public void onStop(){
        for (ELAbsExModule module:allModules.values())
            if (module !=null)
                module.onStop();
    }

    public void onConfigurationChanged(Configuration newConfig){
        for (ELAbsExModule module:allModules.values())
            if (module!=null)
                module.onOrientationChanges(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE);
    }

    public void onDestroy(){
        for (ELAbsExModule module:allModules.values())
            if (module !=null){
                module.onDestroy();
            }
    }
}
