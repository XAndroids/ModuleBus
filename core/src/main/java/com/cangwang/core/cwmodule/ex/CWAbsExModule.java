package com.cangwang.core.cwmodule.ex;

import android.os.Bundle;

import com.cangwang.core.MBaseApi;
import com.cangwang.core.cwmodule.CWModuleContext;
import com.cangwang.model.ICWModule;

/**
 * 抽象模块类
 */
public abstract class CWAbsExModule implements ICWModule{

    public abstract boolean onCreate(CWModuleContext moduleContext, Bundle extend);

    public abstract void onSaveInstanceState(Bundle outState);

    public abstract void onStart();

    public abstract void onResume();

    public abstract void onPause();

    public abstract void onStop();

    public abstract void onOrientationChanges(boolean isLandscape);

    public abstract void onDestroy();

    public abstract void detachView();

    public abstract void setVisible(boolean visible);

    public abstract void registerMApi(Class<?extends MBaseApi> key, MBaseApi value);

    public abstract void unregisterMApi(Class<?extends MBaseApi> key);
}
