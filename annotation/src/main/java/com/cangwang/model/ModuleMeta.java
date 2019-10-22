package com.cangwang.model;

import com.cangwang.annotation.ModuleUnit;
import com.cangwang.enums.LayoutLevel;

/**
 * 注解的模块信息要转化成模块信息实体
 */
public class ModuleMeta {
    public String templet;
    public String moduleName;
    public String title;
    public LayoutLevel layoutlevel;
    public int extralevel;

    //提供给编译时注解中使用
    public ModuleMeta(String templet,String moduleName,String title,int layoutlevel,int extralevel){
        this.templet = templet;
        this.moduleName = moduleName;
        this.title = title;
        if (layoutlevel == 500){
            this.layoutlevel = LayoutLevel.VERY_LOW;
        }else if (layoutlevel == 400){
            this.layoutlevel = LayoutLevel.LOW;
        }else if (layoutlevel == 300){
            this.layoutlevel = LayoutLevel.NORMAL;
        }else if (layoutlevel == 200){
            this.layoutlevel = LayoutLevel.HIGHT;
        }else if (layoutlevel == 100){
            this.layoutlevel = LayoutLevel.VERY_HIGHT;
        }
        this.extralevel = extralevel;
    }

    public ModuleMeta(ModuleUnit unit,String moduleName){
        this.moduleName = moduleName;
        this.templet = unit.templet();
        this.layoutlevel = unit.layoutlevel();
        this.extralevel = unit.extralevel();
        this.title = unit.title();
    }

    @Override
    public String toString() {
        return "ModuleMeta{" +
                "templet='" + templet + '\'' +
                ", moduleName='" + moduleName + '\'' +
                ", title='" + title + '\'' +
                ", layoutlevel=" + layoutlevel +
                ", extralevel=" + extralevel +
                '}';
    }
}
