package com.cangwang.model;

import java.util.List;

public interface IModuleFactory {
    List<ICWModule> getTempleList(String templet);
}
