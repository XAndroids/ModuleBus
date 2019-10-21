package com.cangwang.core.cwmodule.ex;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.SparseArrayCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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
 * 模块分发Fragment
 */
public abstract class ModuleManageExFragment extends Fragment{

    private final String TAG = "ModuleManageExFragment";
    private ViewGroup mTopViewGroup;
    private ViewGroup mBottomViewGroup;
    private ViewGroup pluginViewGroup;
    private View rootView;

    private ModuleExManager moduleManager;
    private ELModuleContext moduleContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView =  inflater.inflate(R.layout.module_rank_layout,container,false);
        mTopViewGroup = (ViewGroup) rootView.findViewById(R.id.layout_top);
        mBottomViewGroup = (ViewGroup) rootView.findViewById(R.id.layout_bottom);
        pluginViewGroup = (ViewGroup) rootView.findViewById(R.id.layout_plugincenter);

        moduleManager = new ModuleExManager();
        moduleManager.moduleConfig(moduleConfig());

        ModuleBus.getInstance().register(this);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        moduleContext = new ELModuleContext();
        moduleContext.setActivity(getActivity());
        moduleContext.setSaveInstance(savedInstanceState);
        //关联视图
        SparseArrayCompat<ViewGroup> sVerticalViews = new SparseArrayCompat<>();
        sVerticalViews.put(ELModuleContext.TOP_VIEW_GROUP, mTopViewGroup);
        sVerticalViews.put(ELModuleContext.BOTTOM_VIEW_GROUP, mBottomViewGroup);
        sVerticalViews.put(ELModuleContext.PLUGIN_CENTER_VIEW, pluginViewGroup);
        moduleContext.setViewGroups(sVerticalViews);

        Observable.fromIterable(moduleManager.getModuleNames())
                .map(new Function<String, ModuleInfo>() {
                    @Override
                    public ModuleInfo apply(@NonNull String s){
                        return new ModuleInfo(s, ELModuleExFactory.newModuleInstance(s));
                    }
                })
//              .delay(10, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ModuleInfo>() {
                    @Override
                    public void accept(@NonNull ModuleInfo elAbsModule){
                        try {
                            if(elAbsModule!=null){
                                long before = System.currentTimeMillis();
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
    public abstract List<String> moduleConfig();

    @Override
    public void onResume() {
        super.onResume();
        if (moduleManager !=null)
            moduleManager.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (moduleManager !=null)
            moduleManager.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ModuleBus.getInstance().unregister(this);
        if (moduleManager !=null)
            moduleManager.onDestroy();
        if (moduleContext !=null)
            moduleContext =null;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (moduleManager !=null)
            moduleManager.onConfigurationChanged(newConfig);
    }
    /**
     * 添加模块
     * @param moduleName
     * @param extend
     */
    @ModuleEvent(coreClientClass = IBaseClient.class)
    public void addModule(String moduleName,Bundle extend){
        addModule(moduleName,extend,null);
    }

    public void addModule(String moduleName,Bundle extend,ModuleLoadListener listener){
        if (moduleName !=null && !moduleName.isEmpty()){
            if (moduleManager.allModules.containsKey(moduleName))  //模块不重复添加
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
     * 移除模块
     * @param moduleName
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
