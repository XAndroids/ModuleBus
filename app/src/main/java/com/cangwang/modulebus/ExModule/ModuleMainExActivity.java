package com.cangwang.modulebus.ExModule;

import com.cangwang.core.ModuleBus;
import com.cangwang.core.cwmodule.ex.ModuleManageExActivity;

import java.util.List;

public class ModuleMainExActivity extends ModuleManageExActivity{

    @Override
    public List<String> moduleConfig() {
        return ModuleBus.getInstance().getModuleList("top");
    }
}
