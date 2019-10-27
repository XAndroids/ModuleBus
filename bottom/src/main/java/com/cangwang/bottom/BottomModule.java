package com.cangwang.bottom;

import android.os.Bundle;
import android.view.View;

import com.cangwang.annotation.ModuleGroup;
import com.cangwang.annotation.ModuleUnit;
import com.cangwang.base.api.BottomApi;
import com.cangwang.base.api.GiftApi;
import com.cangwang.base.api.SlideApi;
import com.cangwang.base.ui.CircleImageView;
import com.cangwang.core.ModuleApiManager;
import com.cangwang.core.cwmodule.CWModuleContext;
import com.cangwang.core.cwmodule.ex.CWBasicExModule;
import com.cangwang.enums.LayoutLevel;

/**
 * 底部栏
 */
@ModuleGroup({
        @ModuleUnit(templet = "top",layoutlevel = LayoutLevel.LOW),
})
public class BottomModule extends CWBasicExModule implements BottomApi{
    private CircleImageView moreBtn;
    private CircleImageView chatBtn;
    private CircleImageView giftBtn;
    private View bottomLayout;
    private InputModule ipM;

    @Override
    public boolean onCreate(CWModuleContext moduleContext, Bundle extend) {
        super.onCreate(moduleContext, extend);
        initView();
        registerMApi(BottomApi.class,this);
        return true;
    }

    public void initView(){
        setContentView(R.layout.bottom_layout);
        bottomLayout = findViewById(R.id.bottom_layout);
        bottomLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        chatBtn = findViewById(R.id.bottom_chat);
        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomLayout.setVisibility(View.GONE);
                if (ipM ==null){
                    ipM = new InputModule();
                    ipM.onCreate(moduleContext,null);
                }else {
                    ipM.setVisible(true);
                }

            }
        });
        moreBtn = findViewById(R.id.bottom_more_btn);
        moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ModuleApiManager.getInstance().getApi(SlideApi.class).show();
            }
        });
        giftBtn = findViewById(R.id.bottom_gift);
        giftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModuleApiManager.getInstance().getApi(GiftApi.class).show();
            }
        });
    }


    @Override
    public void onDestroy() {
        ipM =null;
        super.onDestroy();
    }

    @Override
    public void show() {
        if (bottomLayout.getVisibility() == View.GONE)
            bottomLayout.setVisibility(View.VISIBLE);
    }
}
