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
import com.cangwang.core.ModuleEvent;
import com.cangwang.core.R;
import com.cangwang.core.cwmodule.ELModuleContext;
import com.cangwang.core.info.ModuleInfo;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 增加了动态添加和删除模块的逻辑
 */
public abstract class ModuleManageExActivity extends AppCompatActivity{
    private final String TAG = "ModuleManageExActivity";

    private ViewGroup mTopViewGroup;
    private ViewGroup mBottomViewGroup;
    private ViewGroup pluginViewGroup;

    private ModuleExManager moduleManager;
    private ELModuleContext moduleContext;

    public abstract List<String> moduleConfig();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //布局由父类固定
        setContentView(R.layout.module_rank_layout);

        ModuleBus.getInstance().register(this);

        //三层视图都传递给每个模块
        mTopViewGroup = (ViewGroup) findViewById(R.id.layout_top);
        mBottomViewGroup = (ViewGroup) findViewById(R.id.layout_bottom);
        pluginViewGroup = (ViewGroup) findViewById(R.id.layout_plugincenter);

        //初始化模块管理者
        moduleManager = new ModuleExManager();
        moduleManager.moduleConfig(moduleConfig());

        //初始化模块
        initView(savedInstanceState);
    }

    public void initView(Bundle mSavedInstanceState){
        moduleContext = new ELModuleContext();
        moduleContext.setActivity(this);
        moduleContext.setSaveInstance(mSavedInstanceState);
        //关联视图
        SparseArrayCompat<ViewGroup> sVerticalViews = new SparseArrayCompat<>();
        sVerticalViews.put(ELModuleContext.TOP_VIEW_GROUP, mTopViewGroup);
        sVerticalViews.put(ELModuleContext.BOTTOM_VIEW_GROUP, mBottomViewGroup);
        sVerticalViews.put(ELModuleContext.PLUGIN_CENTER_VIEW, pluginViewGroup);
        moduleContext.setViewGroups(sVerticalViews);

        //使用RxJava实现异步创建模块
        Observable.fromIterable(moduleManager.getModuleNames())
                .map(new Function<String, ModuleInfo>() {
                    @Override
                    public ModuleInfo apply(@NonNull String s){
                        return new ModuleInfo(s, ELModuleExFactory.newModuleInstance(s));
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ModuleInfo>() {
                    @Override
                    public void accept(@NonNull ModuleInfo elAbsModule){
                        try {
                            if(elAbsModule!=null){
                                long before = System.currentTimeMillis();
                                //调用Init初始化模块，动态的向页面中添加布局
                                elAbsModule.module.init(moduleContext, null);
                                Log.d(TAG, "modulename: " + elAbsModule.getClass().getSimpleName() + " init time = " + (System.currentTimeMillis() - before) + "ms");
                                moduleManager.putModule(elAbsModule.name, elAbsModule.module);
                            }
                        }catch (Exception ex){
                            Log.e(TAG,ex.toString());
                        }
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        moduleManager.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        moduleManager.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        moduleManager.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        moduleManager.onDestroy();
        ModuleBus.getInstance().unregister(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        moduleManager.onConfigurationChanged(newConfig);
    }

    /**
     * 添加模块，该分支增加动态增加和删除模块的功能
     */
    @ModuleEvent(coreClientClass = IBaseClient.class)
    public void addModule(String moduleName,Bundle extend){
        addModule(moduleName,extend,null);
    }

    public void addModule(String moduleName,Bundle extend,ModuleLoadListener listener){
        if (moduleName !=null && !moduleName.isEmpty()){
            if (moduleManager.allModules.containsKey(moduleName))//模块不重复添加
                return;
            ELAbsExModule module = moduleManager.getModuleByNames(moduleName);
            if (module == null){
                module = ELModuleExFactory.newModuleInstance(moduleName);
            }
            if (moduleContext !=null &&module!=null){
                boolean result = module.init(moduleContext,extend);
                if (listener!=null)
                    listener.laodResult(result);  //监听回调
                if (result)
                    moduleManager.putModule(moduleName,module);
            }
        }
    }

    /**
     * 移除模块，该分支增加动态增加和删除模块的功能
     */
    @ModuleEvent(coreClientClass = IBaseClient.class)
    public void removeModule(String moduleName){
        if (moduleName!=null &&!moduleName.isEmpty()) {
            ELAbsExModule module = moduleManager.getModuleByNames(moduleName);
            if (module != null) {
                module.detachView();  //先移除界面，再销毁
                module.onDestroy();
                moduleManager.remove(moduleName);
            }
        }
    }

    @ModuleEvent(coreClientClass = IBaseClient.class)
    public void moduleVisible(String moduleName,boolean isVisible){
        if (moduleName !=null && !moduleName.isEmpty()){
            ELAbsExModule module = moduleManager.getModuleByNames(moduleName);
            if (module !=null){
                module.setVisible(isVisible);
            }
        }
    }
}
