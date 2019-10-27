package com.cangwang.anchor;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cangwang.anchor.dialog.AnchorDialog;
import com.cangwang.annotation.ModuleGroup;
import com.cangwang.annotation.ModuleUnit;
import com.cangwang.base.api.AnchorApi;
import com.cangwang.core.ModuleApiManager;
import com.cangwang.core.cwmodule.CWModuleContext;
import com.cangwang.core.cwmodule.ex.CWBasicExModule;
import com.cangwang.enums.LayoutLevel;

/**
 * 信息页
 */
@ModuleGroup({
        @ModuleUnit(templet = "top",layoutlevel = LayoutLevel.HIGHT),
})
public class AnchorModule extends CWBasicExModule implements AnchorApi{
    private View anchorLayout;
    private ImageView anchorImg;
    private TextView anchorName;
    private ImageView anchorCareBtn;

    @Override
    public boolean onCreate(CWModuleContext moduleContext, Bundle extend) {
        super.onCreate(moduleContext, extend);
        initView();

        //注册AnchorApi，用于其它组件跨组件调用
        ModuleApiManager.getInstance().putApi(AnchorApi.class,this);

        return true;
    }

    private void initView(){
        //通过自身的RelativeLayout控制布局在页面中的位置
        setContentView(R.layout.anchor_title_layout,parentPlugin);
        anchorImg = findViewById(R.id.anchor_img);
        anchorName = findViewById(R.id.anchor_name);
        anchorCareBtn = findViewById(R.id.anchor_care_btn);

        anchorCareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(context.getSupportFragmentManager());
            }
        });
    }

    private void showDialog(FragmentManager manager) {
        if (!AnchorDialog.isAnchorDialogShow) {
            AnchorDialog.isAnchorDialogShow = true;
            AnchorDialog anchorDialog = AnchorDialog.newInstance();
            anchorDialog.show(manager, AnchorDialog.TAG);
        }
    }

    @Override
    public void showAnchor(FragmentActivity context, String user, String url) {
        showDialog(context.getSupportFragmentManager());
    }
}
