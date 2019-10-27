package com.cangwang.annotation;

import com.cangwang.enums.LayoutLevel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Module单元注解
 */
//Java元注解-Retention：
//  SOURCE
//  CLASS：默认配置，存在于源码，且编译后也存在.class中，但信息不会添加到JVM虚拟机中
//  RUNTIME：源码、class文件、虚拟机都存在
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface ModuleUnit {
    //模块分组名称
    String templet() default "normal";

    //模块的标题
    String title() default "CangWang";

    //模块的级别
    LayoutLevel layoutlevel() default LayoutLevel.NORMAL;

    int extralevel() default 0;
}
