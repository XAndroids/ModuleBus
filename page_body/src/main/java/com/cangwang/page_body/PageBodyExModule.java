package com.cangwang.page_body;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cangwang.annotation.ModuleUnit;
import com.cangwang.core.IBaseClient;
import com.cangwang.core.ModuleBus;
import com.cangwang.core.cwmodule.CWModuleContext;
import com.cangwang.core.cwmodule.ex.CWBasicExModule;
import com.cangwang.core.util.ModuleImpl;
import com.cangwang.enums.LayoutLevel;

@ModuleUnit(templet = "top",layoutlevel = LayoutLevel.LOW,extralevel = 11)
public class PageBodyExModule extends CWBasicExModule implements ModuleImpl{
    private View pageBodyView_fi;
    private View pageBodyView_se;
    private TextView pageBodyTop;
    private TextView pageBodyBottom;
    private Button changeNameBtn;
    private Button addTitle;
    private Button removeTitle;

    @Override
    public boolean init(CWModuleContext moduleContext, Bundle extend) {
        super.init(moduleContext, extend);
        this.moduleContext = moduleContext;
        initView();
        return true;
    }

    private void initView(){
        //直接添加布局到父布局
        pageBodyView_fi = LayoutInflater.from(context).inflate(R.layout.page_body_fi,parentTop,true);
        pageBodyTop = (TextView) pageBodyView_fi.findViewById(R.id.page_body_top);
        //动态添加布局
        pageBodyView_se = LayoutInflater.from(context).inflate(R.layout.page_body_se,null);
        RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        if (parentBottom!=null)
            parentBottom.addView(pageBodyView_se,rl);

        removeTitle = genericFindViewById(R.id.remove_title);
        addTitle = genericFindViewById(R.id.add_title);

        removeTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModuleBus.getInstance().post(IBaseClient.class,"removeModule","com.cangwang.page_name.PageNameExModule");
            }
        });

        addTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle =new Bundle();
                ModuleBus.getInstance().post(IBaseClient.class,"addModule","com.cangwang.page_name.PageNameExModule",bundle);
            }
        });

        changeNameBtn = (Button) pageBodyView_se.findViewById(R.id.change_page_Name);
        changeNameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ModuleBus.getInstance().post(IBaseClient.class,"changeNameTxt","Cang_Wang");
                context.startActivity(new Intent("com.cangwang.moduleExFg"));
            }
        });
    }

    @Override
    public void onLoad(Application app) {
        for (int i=0;i<5;i++){
            Log.v("PageBodyModule","PageBodyModule onLoad");
        }
    }
}

