package com.cangwang.core.cwmodule.ex;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;

import com.cangwang.core.IBaseClient;
import com.cangwang.core.ModuleBus;
import com.cangwang.core.ModuleCenter;
import com.cangwang.core.ModuleEvent;
import com.cangwang.core.R;
import com.cangwang.core.cwmodule.CWModuleContext;
import com.cangwang.core.cwmodule.api.BackPressStack;
import com.cangwang.model.ICWModule;

import java.util.List;

public abstract class ModuleManageExActivity extends AppCompatActivity {
    private final String TAG = "ModuleManageExActivity";

    //上层布局
    private ViewGroup mTopViewGroup;
    //下层布局
    private ViewGroup mBottomViewGroup;
    //插件布局
    private ViewGroup pluginViewGroup;
    //根布局
    private ViewGroup rootLayout;

    //模块管理器
    private ModuleExManager moduleManager;
    //模块上下文
    private CWModuleContext moduleContext;

    //模块配置
    public abstract String moduleConfig();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_rank_layout);

        //注册EventBus
        ModuleBus.getInstance().register(this);

        //初始化top,bootom等视图
        rootLayout = (ViewGroup) findViewById(R.id.root_layout);
        mTopViewGroup = (ViewGroup) findViewById(R.id.layout_top);
        mBottomViewGroup = (ViewGroup) findViewById(R.id.layout_bottom);
        pluginViewGroup = (ViewGroup) findViewById(R.id.layout_plugincenter);

        //初始化ModuleManager
        moduleManager = new ModuleExManager();
        moduleManager.moduleConfig(moduleConfig());

        initView(savedInstanceState);
    }

    public void initView(Bundle mSavedInstanceState) {
        //创建模块分发上下文
        moduleContext = new CWModuleContext();
        moduleContext.setActivity(this);
        moduleContext.setSaveInstance(mSavedInstanceState);
        //关联视图
        SparseArrayCompat<ViewGroup> sVerticalViews = new SparseArrayCompat<>();
        sVerticalViews.put(CWModuleContext.TOP_VIEW_GROUP, mTopViewGroup);
        sVerticalViews.put(CWModuleContext.BOTTOM_VIEW_GROUP, mBottomViewGroup);
        sVerticalViews.put(CWModuleContext.PLUGIN_CENTER_VIEW, pluginViewGroup);
        moduleContext.setViewGroups(sVerticalViews);
        moduleContext.setTemplateName(moduleConfig());

        if (ModuleCenter.isFromNetWork) {
            //在线加载
            for (final String moduleName : ModuleBus.getInstance().getModuleList(moduleManager.getTemplate())) {
                moduleManager.getPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        final CWAbsExModule module = CWModuleExFactory.newModuleInstance(moduleName);
                        if (module != null) {
                            moduleManager.getHandler().post(new Runnable() {
                                @Override
                                public void run() {
                                    long before = System.currentTimeMillis();
                                    module.onCreate(moduleContext, null);
                                    Log.d(TAG, "modulename: " + moduleName + " init time = " + (System.currentTimeMillis() - before) + "ms");
                                    moduleManager.putModule(moduleName, module);
                                }
                            });
                        }
                    }
                });
            }
        } else {
            //本地缓存加载
            //通过APT生成了模块初始化源代码，通过json配置文件获取相关配置，调用初始化源代码
            CWAbsExModule module;
            List<ICWModule> moduleList = CWModuleExFactory.getInstance().getTempleList(moduleManager.getTemplate());
            if (moduleList == null || moduleList.isEmpty()) return;
            for (ICWModule moduleIn : moduleList) {
                module = (CWAbsExModule) moduleIn;
                long before = System.currentTimeMillis();
                module.onCreate(moduleContext, null);
                Log.d(TAG, "modulename: " + moduleIn.getClass().getCanonicalName() + " init time = " + (System.currentTimeMillis() - before) + "ms");
                moduleManager.putModule(moduleIn.getClass().getCanonicalName(), module);
            }
        }
    }

    //设置背景颜色
    public void setBackGroundResouce(int color) {
        rootLayout.setBackgroundResource(color);
    }

    //声明周期分发
    @Override
    protected void onResume() {
        super.onResume();
        if (moduleManager != null)
            moduleManager.onResume();
    }

    @Override
    protected void onPause() {
        if (moduleManager != null)
            moduleManager.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        if (moduleManager != null)
            moduleManager.onStop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (moduleManager != null)
            moduleManager.onDestroy();
        ModuleBus.getInstance().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        moduleManager.onConfigurationChanged(newConfig);
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (moduleManager.onBackPressed()) {
            return;
        }
        if (BackPressStack.getInstance().getStack().size() > 0) return;
        super.onBackPressed();
    }

    /**
     * 添加模块
     */
    @ModuleEvent(coreClientClass = IBaseClient.class)
    public void addModule(String moduleName, Bundle extend) {
        addModule(moduleName, extend, null);
    }

    public void addModule(String moduleName, Bundle extend, ModuleLoadListener listener) {
        if (moduleName != null && !moduleName.isEmpty()) {
            if (moduleManager.allModules.containsKey(moduleName))  //模块不重复添加
                return;
            CWAbsExModule module = moduleManager.getModuleByNames(moduleName);
            if (module == null) {
                module = CWModuleExFactory.newModuleInstance(moduleName);
            }
            if (moduleContext != null && module != null) {
                boolean result = module.onCreate(moduleContext, extend);
                if (listener != null)
                    listener.laodResult(result);  //监听回调
                if (result)
                    moduleManager.putModule(moduleName, module);
            }
        }
    }

    /**
     * 移除模块
     */
    @ModuleEvent(coreClientClass = IBaseClient.class)
    public void removeModule(String moduleName) {
        if (moduleName != null && !moduleName.isEmpty()) {
            CWAbsExModule module = moduleManager.getModuleByNames(moduleName);
            if (module != null) {
                module.detachView();  //先移除界面，再销毁
                module.onDestroy();
                moduleManager.remove(moduleName);
            }
        }
    }

    @ModuleEvent(coreClientClass = IBaseClient.class)
    public void moduleVisible(String moduleName, boolean isVisible) {
        if (moduleName != null && !moduleName.isEmpty()) {
            CWAbsExModule module = moduleManager.getModuleByNames(moduleName);
            if (module != null) {
                module.setVisible(isVisible);
            }
        }
    }
}
