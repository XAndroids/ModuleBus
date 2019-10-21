package com.cangwang.modulebus.ExModule;

import com.cangwang.core.cwmodule.ex.ModuleManageExActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 模块主Activity，配置和提供相关模块配置
 */
public class ModuleMainExActivity extends ModuleManageExActivity{
    @Override
    public List<String> moduleConfig() {
        List<String> moduleList= new ArrayList<>();
        moduleList.add(PageExConfig.MODULE_PAGE_NAME);
        moduleList.add(PageExConfig.MODULE_BODY_NAME);
        return moduleList;
    }
}
