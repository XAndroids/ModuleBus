package com.cangwang.core;

import android.content.Context;
import android.util.Log;

import com.cangwang.core.template.IModuleUnit;
import com.cangwang.core.util.ModuleUtil;
import com.cangwang.core.util.ClassUtils;
import com.cangwang.model.ModuleMeta;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 将所有模块的信息保存在模块列表中
 */
public class ModuleCenter {
    private final static String TAG = "ModuleCenter";

    private static List<ModuleMeta> group= new ArrayList<>();
    private static Map<String,List<ModuleMeta>> sortgroup = new HashMap<>();

    public synchronized static void init(Context context){
        try {
            //获取指定ModuleUnit$$的类名的文件，将APT解析注解的信息转化到group中
            List<String> classFileNames = ClassUtils.getFileNameByPackageName(context, ModuleUtil.NAME_OF_MODULEUNIT);
            for (String className:classFileNames){
                if (className.startsWith(ModuleUtil.ADDRESS_OF_MODULEUNIT)){
                    IModuleUnit iModuleUnit = (IModuleUnit)(Class.forName(className).getConstructor().newInstance());
                    iModuleUnit.loadInto(group);  //加载列表
                }
            }
            Log.i(TAG,"group ="+group.toString());
            //并对group的模块进行优先级调序
            sortTemplate(group);

        }catch (Exception e){
            Log.e(TAG,e.toString());
        }
    }

    /**
     * 排列Module列表
     */
    private static void sortTemplate(List<ModuleMeta> group){
        ModuleMeta exitMeta;
        for (ModuleMeta meta:group){
            Log.i(TAG,"meta ="+meta.toString());
            List<ModuleMeta> metaList = new ArrayList<>();
            if (sortgroup.get(meta.templet) != null) {
                metaList = sortgroup.get(meta.templet);
            }

            int index = 0;
            for (int i = 0; i < metaList.size(); i++) {
                exitMeta = metaList.get(i);
                if (meta.layoutlevel.getValue() < exitMeta.layoutlevel.getValue()) {  //比较层级参数,值越低,越先被加载
                    index=i;
                } else if (meta.layoutlevel.getValue() == exitMeta.layoutlevel.getValue()) {   //层级相同
                    if (meta.extralevel >= exitMeta.extralevel) {          //比较额外层级参数
                       index=i;
                    }
                }
            }

            if (index == 0){  //如果遍历后不符合条件，会添加到尾部
                metaList.add(meta);
            }else {
                metaList.add(index,meta);
            }

            sortgroup.put(meta.templet, metaList);
        }
        Log.i(TAG,"sortgroup ="+sortgroup.toString());
    }


    public static List<String> getModuleList(String templet){
        //从sortgroup中获取指定module配置
        List<String> list = new ArrayList<>();
        if (sortgroup.containsKey(templet)) {
            for (ModuleMeta meta : sortgroup.get(templet)) {
                list.add(meta.moduleName);
            }
        }else {
            Log.i(TAG,"do not have key "+ templet);
        }
        Log.i(TAG,"list ="+list.toString());

        return list;
    }
}
