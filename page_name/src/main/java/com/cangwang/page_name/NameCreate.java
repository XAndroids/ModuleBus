package com.cangwang.page_name;

import android.app.Application;
import android.util.Log;

import com.cangwang.core.util.ModuleImpl;

public class NameCreate implements ModuleImpl{
    @Override
    public void onLoad(Application app) {
        for (int i=0;i<5;i++){
            Log.v("NameCreate","NameCreate onLoad");
        }
    }
}
