package com.cangwang.core.cwmodule;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.util.SparseArrayCompat;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * 模块上下文
 */
public class CWModuleContext {
    //上层视图标识
    public static final int TOP_VIEW_GROUP = 0;
    //下层视图标识
    public static final int BOTTOM_VIEW_GROUP = 1;
    //中间插件视图标识
    public static final int PLUGIN_CENTER_VIEW = 2;

    //分发Activity上下文
    private FragmentActivity context;
    //分发ActivityBundle
    private Bundle saveInstance;
    //分发LayoutInflater
    private LayoutInflater inflater;

    //分发ViewGroup容器
    private SparseArrayCompat<ViewGroup> viewGroups = new SparseArrayCompat<>();

    private String templateName;

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public LayoutInflater getInflater() {
        if(context!=null)
            inflater = LayoutInflater.from(context);
        return inflater;
    }

    public FragmentActivity getActivity(){
        return context;
    }

    public void setActivity(FragmentActivity component){
        this.context = component;
        inflater = LayoutInflater.from(context);
    }

    public void setSaveInstance(Bundle saveInstance){
        this.saveInstance = saveInstance;
    }

    public ViewGroup getView(int key){
        return viewGroups.get(key);
    }

    public void setViewGroups(SparseArrayCompat<ViewGroup> viewGroups){
        this.viewGroups = viewGroups;
    }
}
