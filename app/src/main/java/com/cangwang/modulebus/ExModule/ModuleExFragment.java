package com.cangwang.modulebus.ExModule;

import android.os.Bundle;

import com.cangwang.core.cwmodule.ex.ModuleManageExFragment;

import java.util.List;

public class ModuleExFragment extends ModuleManageExFragment {
    public static ModuleExFragment newInstance(){
        ModuleExFragment fragment = new ModuleExFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static ModuleExFragment newInstance(List<String> modules){
        PageExConfig.moduleList = modules;
        ModuleExFragment fragment = new ModuleExFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public String moduleConfig() {
        return "normal";
    }
}
