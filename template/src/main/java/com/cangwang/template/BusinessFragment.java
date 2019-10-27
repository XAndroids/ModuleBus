package com.cangwang.template;

import com.cangwang.core.cwmodule.ex.ModuleManageExFragment;

/**
 * 实际业务Fragment，负责分发Fragmnet
 */
public class BusinessFragment extends ModuleManageExFragment {
    @Override
    public String moduleConfig() {
        //内层Fragment分发top组件
        return "top";
    }
}
