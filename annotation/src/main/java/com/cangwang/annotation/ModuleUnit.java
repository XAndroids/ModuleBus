package com.cangwang.annotation;

import com.cangwang.enums.LayoutLevel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Module单元注解：记录每个Module配置相关信息
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface ModuleUnit {
    String templet() default "normal"; //设定模板的名字
    String title() default "CangWang"; //业务模板名字
    LayoutLevel layoutlevel() default LayoutLevel.NORMAL;//层次编排
    int extralevel() default 0;//层级内排序
}
