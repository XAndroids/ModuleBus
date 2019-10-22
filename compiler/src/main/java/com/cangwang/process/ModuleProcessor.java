package com.cangwang.process;

import com.cangwang.annotation.ModuleGroup;
import com.cangwang.annotation.ModuleUnit;
import com.cangwang.utils.Logger;
import com.google.auto.service.AutoService;

import org.apache.commons.collections4.CollectionUtils;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

//使用Google的AutoService，通过编译时注解添加javax.annotation.processing.Processor文件
//在根目录下创建resources文件夹，监理META-INF.service文件夹，然后定义javax.annotation.processing.Process文件
//在文件中添加运行javapeot代码的包名和地址
//配置此入口索引，Javapjoet框架才能在程序编译时先运行processor

@AutoService(Processor.class)//自动编译运行时注解文件
public class ModuleProcessor extends AbstractProcessor {
    //文件工具，写入class文件到硬盘
    private Filer mFiler;
    //日志打印工具
    private Logger logger;
    private Types types;
    //文件环境信息
    private Elements elements;

    /**
     * 初始化调用
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        // Generate class.
        mFiler = processingEnv.getFiler();
        // Package the log utils.
        logger = new Logger(processingEnv.getMessager());
        // Get type utils.
        types = processingEnv.getTypeUtils();
        // Get class meta.
        elements = processingEnv.getElementUtils();
        System.out.println("------------------------------");
        System.out.println("ModuleProcess init");
    }

    /**
     * 声明需要支持的注解类型
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new LinkedHashSet<>();
        annotations.add(ModuleUnit.class.getCanonicalName());
        annotations.add(ModuleGroup.class.getCanonicalName());
        return annotations;
    }

    /**
     * 使用的Java版本
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    /**
     * 编译时注解运行
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if(CollectionUtils.isNotEmpty(set)){
            //获取ModuleUnit和ModuleGroup注解对象
            Set<? extends Element> moduleUnitElements = roundEnvironment.getElementsAnnotatedWith(ModuleUnit.class);
            Set<? extends Element> moduleGroupElements = roundEnvironment.getElementsAnnotatedWith(ModuleGroup.class);
            try {
                logger.info(">>> Found moduleUnit, start... <<<");
                //解析注解对象信息，并编写Java文件
                ModuleUnitProcessor.parseModules(moduleUnitElements,logger,mFiler,elements);
                logger.info(">>> Found moduleGroup, start... <<<");
                ModuleGroupProcessor.parseModulesGroup(moduleGroupElements,logger,mFiler,elements);
            } catch (Exception e) {
                logger.error(e);
            }
        }
        return true;
    }
}
