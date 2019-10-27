package com.cangwang.core.cwmodule.ex;

import com.cangwang.model.ICWModule;
import com.cangwang.model.IModuleFactory;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CWFacotry implements IModuleFactory{
    public static Map<String,List<ICWModule>> moduleMap = new HashMap<>();

    static {
        List<ICWModule> list = new LinkedList<>();
//        list.add();
//        moduleMap.put("normal",);
        list.clear();
    }

    @Override
    public List<ICWModule> getTempleList(String templet) {
        return moduleMap.get(templet);
    }
}
