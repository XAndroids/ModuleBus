package com.cangwang.core;

import android.content.Context;
import android.support.v4.util.ArrayMap;

import com.cangwang.core.info.MethodInfo;

import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ModuleBus {
    //ModuleBus单例对象
    private static ModuleBus instance;

    private static ArrayMap<Object, ArrayMap<String, MethodInfo>> moduleEventMethods = new ArrayMap<>();
    private static ArrayMap<Class<?>, ArrayMap<String, ArrayList<Object>>> moduleMethodClient = new ArrayMap<>();

    /**
     * 单例初始化获取实例方法
     */
    public static ModuleBus getInstance() {
        if (instance == null) {
            synchronized (ModuleBus.class) {
                if (instance == null) {
                    instance = new ModuleBus();
                }
            }
        }
        return instance;
    }

    /**
     * 从本地获取模块配置
     */
    public static void init(Context context) {
        ModuleCenter.init(context);
    }

    /**
     * 从网络获取模块配置
     */
    public static void init(Context context, JSONObject object) {
        ModuleCenter.init(context, object, true);
    }


    public void register(Object client) {
        if (client == null) return;

        Class<?> orginalClass = client.getClass();
        if (orginalClass == null) return;


        Method[] methods = orginalClass.getMethods();

        for (Method method : methods) {
            ModuleEvent event = method.getAnnotation(ModuleEvent.class);
            if (event != null) {
                Class<?> clientClass = event.coreClientClass();
                addClient(clientClass, client, method);
                addEventMethod(client, method, event.single());
            }
        }
    }

    public void unregister(Object client) {
        if (client == null) return;

        Class<?> orginalClass = client.getClass();
        if (orginalClass == null) return;


        Method[] methods = orginalClass.getMethods();
        for (Method method : methods) {
            ModuleEvent event = method.getAnnotation(ModuleEvent.class);
            if (event != null) {
                Class<?> clientClass = event.coreClientClass();
                if (moduleEventMethods.get(clientClass) == null) return;
                moduleEventMethods.get(clientClass).remove(method);  //移除方法
                if (moduleEventMethods.get(clientClass).isEmpty())   //如果此类中已经无方法，移除此类
                    moduleMethodClient.remove(clientClass);
            }
        }
    }

    private void addClient(Class<?> clientClass, Object client, Method m) {
        ArrayMap<String, ArrayList<Object>> clientMethodList = moduleMethodClient.get(clientClass);

        if (clientMethodList == null) {
            clientMethodList = new ArrayMap<>();
        }

        ArrayList<Object> clientList = clientMethodList.get(m.getName());
        if (clientList == null) {
            clientList = new ArrayList<>();
        }

        clientList.add(client);
        clientMethodList.put(m.getName(), clientList);
        moduleMethodClient.put(clientClass, clientMethodList);
    }

    private void addEventMethod(Object client, Method m, boolean single) {
        ArrayMap<String, MethodInfo> methods = moduleEventMethods.get(client);
        if (methods == null) {
            methods = new ArrayMap<>();
            moduleEventMethods.put(client, methods);
        }
        methods.put(m.getName(), new MethodInfo(m.getName(), m, single));
    }

    public List<String> getModuleList(String templet) {
        return ModuleCenter.getModuleList(templet);
    }

}
