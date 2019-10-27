package com.cangwang.modulebus.ExModule;

import com.cangwang.core.ModuleBus;
import com.cangwang.core.cwmodule.ex.ModuleManageExActivity;

import java.util.List;

public class ModuleMainExActivity extends ModuleManageExActivity{

    @Override
    public List<String> moduleConfig() {
        //从app初始化后的group中取指定的Module列表
        return ModuleBus.getInstance().getModuleList("top");
    }
}
