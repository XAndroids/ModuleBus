package com.cangwang.core.cwmodule.ex;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cangwang.core.MBaseApi;
import com.cangwang.core.ModuleApiManager;
import com.cangwang.core.cwmodule.CWModuleContext;
import com.cangwang.core.cwmodule.api.BackPressStack;
import com.cangwang.core.cwmodule.api.ModuleBackpress;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 模块基类
 */
public class CWBasicExModule extends CWAbsExModule {
    //Activity上下文
    public FragmentActivity context;
    //模块上下文
    public CWModuleContext moduleContext;

    //布局解析器
    private LayoutInflater inflater;
    public Handler handler;

    //上层视图组
    protected ViewGroup parentTop;
    //底层视图组
    protected ViewGroup parentBottom;
    //父视图组
    protected ViewGroup parentPlugin;
    //模块根视图
    private View rootView;

    public String templateName;

    //模块中视图集合
    private List<View> viewList;
    //返回按键事件队列
    private Stack<ModuleBackpress> stack;

    private boolean isShow = false;

    @CallSuper
    @Override
    public boolean onCreate(CWModuleContext moduleContext, Bundle extend) {
        this.moduleContext = moduleContext;
        context = moduleContext.getActivity();
        inflater = moduleContext.getInflater();
        handler = new Handler();

        parentTop = moduleContext.getView(CWModuleContext.TOP_VIEW_GROUP);
        parentBottom = moduleContext.getView(CWModuleContext.BOTTOM_VIEW_GROUP);
        parentPlugin = moduleContext.getView(CWModuleContext.PLUGIN_CENTER_VIEW);

        templateName = moduleContext.getTemplateName();

        viewList = new ArrayList<>();

        //初始化返回事件队列
        stack = BackPressStack.getInstance().getStack();
        return true;
    }

    public void setContentView(@LayoutRes int layoutResID) {
        //默认设置内容到中间的Plugin
        setContentView(layoutResID, parentPlugin);
    }

    public void setContentView(@LayoutRes int layoutResID, ViewGroup viewGroup) {
        setContentView(layoutResID, viewGroup, false);
    }

    public void setContentView(@LayoutRes int layoutResID, ViewGroup viewGroup, boolean attachToRoot) {
        //解析内容布局
        rootView = inflater.inflate(layoutResID, viewGroup, attachToRoot);
        if (rootView != null && viewGroup != null)
            //添加到插件容器中
            viewGroup.addView(rootView);

        isShow = true;

        //如果给模块支持ModuleBackpress，则添加到堆栈中
        if (this instanceof ModuleBackpress) {
            stack.push((ModuleBackpress) this);
            Log.e("BasicExModule", "push " + BackPressStack.getInstance().getStack().toString());
        }
    }

    public <T extends View> T findViewById(int id) {
        //return返回view时,加上泛型T
        if (context == null) return null;
        T view = context.findViewById(id);
        if (viewList != null && view != null)
            viewList.add(view);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onOrientationChanges(boolean isLandscape) {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    public Resources getResources() {
        return context.getResources();
    }

    public Activity getActivity() {
        return context;
    }

    @CallSuper
    @Override
    public void detachView() {
        //移除模块相关内容
        ViewGroup viewGroup;
        if (viewList != null && viewList.size() > 0) {
            //后去视图父视图，然后移除View
            viewGroup = (ViewGroup) viewList.get(0).getParent();
            if (viewGroup != null)
                viewGroup.removeAllViewsInLayout();
        }
    }

    @CallSuper
    @Override
    public void onDestroy() {
        context = null;
        moduleContext = null;
        handler = null;
        parentTop = null;
        parentBottom = null;
        parentPlugin = null;
        rootView = null;
        viewList = null;
        isShow = false;

        //移除返回事件堆栈
        if (this instanceof ModuleBackpress && stack.contains(this)) {
            stack.remove(this);
        }
    }

    @Override
    public void setVisible(boolean visible) {
        ViewGroup viewGroup;
        if (viewList != null && viewList.size() > 0) {
            viewGroup = (ViewGroup) viewList.get(0).getParent();
            viewGroup.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
        setRootVisbile(visible ? View.VISIBLE : View.GONE);
    }

    private void setRootVisbile(int visbile) {
        isShow = visbile == View.VISIBLE;
        if (this instanceof ModuleBackpress) {
            if (isShow && !stack.contains(this)) {
                stack.push((ModuleBackpress) this);
                Log.e("BasicExModule", "push " + BackPressStack.getInstance().getStack().toString());
            } else if (!isShow && stack.contains(this)) {
                Log.e("BasicExModule", "pop " + BackPressStack.getInstance().getStack().toString());
                stack.pop();
            }
        }
    }

    public boolean isVisible() {
        return isShow;
    }

    public void showModule() {
        if (rootView != null)
            rootView.setVisibility(View.VISIBLE);
        setRootVisbile(View.VISIBLE);
    }

    public void hideModule() {
        if (rootView != null)
            rootView.setVisibility(View.GONE);
        if (BackPressStack.getInstance().getStack().contains(this)) {
            BackPressStack.getInstance().getStack().pop();
        }
        setRootVisbile(View.GONE);
    }

    @Override
    public void registerMApi(Class<? extends MBaseApi> key, MBaseApi value) {
        ModuleApiManager.getInstance().putApi(key, value);
    }

    @Override
    public void unregisterMApi(Class<? extends MBaseApi> key) {
        ModuleApiManager.getInstance().removeApi(key);
    }

    public void setOnClickListener(View.OnClickListener listener) {
        rootView.setOnClickListener(listener);
    }
}
