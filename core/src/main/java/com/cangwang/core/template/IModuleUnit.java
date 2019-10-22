package com.cangwang.core.template;

import com.cangwang.model.ModuleMeta;

import java.util.List;

public interface IModuleUnit {
    void loadInto(List<ModuleMeta> metaSet);
}
