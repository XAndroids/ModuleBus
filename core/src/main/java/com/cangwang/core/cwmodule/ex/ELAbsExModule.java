package com.cangwang.core.cwmodule.ex;

import android.os.Bundle;

import com.cangwang.core.cwmodule.ELModuleContext;

/**
 * 模块抽象接口，每个模块都需要实现的接口回调方法
 */
public abstract class ELAbsExModule {

    public abstract boolean init(ELModuleContext moduleContext,Bundle extend);

    public abstract void onSaveInstanceState(Bundle outState);

    public abstract void onResume();

    public abstract void onPause();

    public abstract void onStop();

    public abstract void onOrientationChanges(boolean isLandscape);

    public abstract void onDestroy();

    public abstract void detachView();

    public abstract void setVisible(boolean visible);
}
