package com.cangwang.modulebus.ExModule;

import java.util.ArrayList;
import java.util.List;

public class PageExConfig {

    public static final String BODY_CREATE ="com.cangwang.page_body.BodyCreate";
    public static final String NAME_CREATE ="com.cangwang.page_name.NameCreate";

    public static List<String> moduleList = new ArrayList<>();

    public static final String[] moduleCreate = {
            BODY_CREATE,
            NAME_CREATE
    };

}
