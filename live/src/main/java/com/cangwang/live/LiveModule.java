package com.cangwang.live;

import android.os.Bundle;

import com.cangwang.annotation.ModuleUnit;
import com.cangwang.core.cwmodule.CWModuleContext;
import com.cangwang.core.cwmodule.ex.CWBasicExModule;
import com.cangwang.enums.LayoutLevel;

/**
 * 视频模块
 */
@ModuleUnit(templet = "video",layoutlevel = LayoutLevel.VERY_LOW)
public class LiveModule extends CWBasicExModule{

    @Override
    public boolean onCreate(CWModuleContext moduleContext, Bundle extend) {
        super.onCreate(moduleContext, extend);
        initView();
        return true;
    }

    @Override
    public void onOrientationChanges(boolean isLandscape) {
        super.onOrientationChanges(isLandscape);
    }

    private void initView(){
        setContentView(R.layout.live_layout,parentBottom);
    }
}
