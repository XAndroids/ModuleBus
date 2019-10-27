package com.cangwang.modulebus.ExModule;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;

import com.cangwang.base.util.ViewUtil;
import com.cangwang.core.cwmodule.ex.ModuleManageExActivity;
import com.cangwang.modulebus.R;
import com.cangwang.template.TemplateFragment;

public class ModuleMainExActivity extends ModuleManageExActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (Configuration.ORIENTATION_LANDSCAPE == getResources().getConfiguration().orientation) {
            //横屏屏幕设置
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            getWindow().setAttributes(lp);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        } else if (Configuration.ORIENTATION_PORTRAIT == getResources().getConfiguration().orientation) {
            //竖屏屏幕设置
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setAttributes(lp);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        super.onCreate(savedInstanceState);
        setBackGroundResouce(R.color.black);
        ViewUtil.replaceFragment(this, R.id.layout_plugincenter, getSupportFragmentManager(), null, TemplateFragment.class, TemplateFragment.TAG);
    }

    @Override
    public String moduleConfig() {
        //在外层Activity分发video组件
        return "video";
    }
}
