package com.cangwang.core;

import android.content.Context;
import android.util.Log;

import com.cangwang.bean.ModuleUnitBean;
import com.cangwang.core.util.ModuleUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ModuleCenter {
    private final static String TAG = "ModuleCenter";

    //从center.json解析的模块配置集合
    private static Map<String, List<ModuleUnitBean>> templetList = new HashMap<>();
    //是否是从网络获取模块配置
    public static boolean isFromNetWork = false;

    /**
     * 初始化
     */
    public synchronized static void init(Context context) {
        //从集成的center.json中，获取模块配置（从本地加载）
        init(context, ModuleUtil.getAssetJsonObject(context, "center.json"), false);
    }

    /**
     * 初始化模块
     */
    public synchronized static void init(Context context, JSONObject object, boolean isFromNet) {
        isFromNetWork = isFromNet;
        if (object == null) return;
        Log.e(TAG, "templet = " + object.toString());

        //遍历解析模块配置
        try {
            Iterator iterator = object.keys();
            while (iterator.hasNext()) {
                String key = iterator.next().toString();
                JSONArray array = object.getJSONArray(key);
                List<ModuleUnitBean> list = new ArrayList<>();
                int length = array.length();
                for (int i = 0; i < length; i++) {
                    JSONObject o = array.getJSONObject(i);
                    ModuleUnitBean bean = new ModuleUnitBean(o.getString("path"),
                            o.getString("templet"),
                            o.getString("title"),
                            o.getInt("layoutLevel"),
                            o.getInt("extraLevel"));
                    list.add(bean);
                }
                templetList.put(key, list);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static List<String> getModuleList(String templet) {
        if (templetList.isEmpty()) return null;
        List<String> moduleList = new ArrayList<>();
        for (ModuleUnitBean bean : templetList.get(templet)) {
            if (bean.templet.equals(templet)) {
                moduleList.add(bean.path);
            }
        }
        return moduleList;
    }
}
