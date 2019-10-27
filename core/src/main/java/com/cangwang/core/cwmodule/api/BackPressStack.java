package com.cangwang.core.cwmodule.api;

import java.util.Stack;

/**
 * 返回键堆栈机制：用于处理各个模块（不是页面处理返回键）的返回键处理事件
 */
public class BackPressStack {
    //返回键堆栈
    private volatile Stack<ModuleBackpress> stack;
    //单例对象
    private static BackPressStack backPressStack;

    /**
     * 单例获取实例方法
     */
    public static BackPressStack getInstance() {
        if (backPressStack == null) {
            synchronized (BackPressStack.class) {
                if (backPressStack == null) {
                    backPressStack = new BackPressStack();
                }
            }
        }
        return backPressStack;
    }

    private BackPressStack() {
        stack = new Stack<>();
    }


    public Stack<ModuleBackpress> getStack() {
        return stack;
    }
}
