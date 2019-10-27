package com.cangwang.base.api;

import com.cangwang.core.MBaseApi;

public interface WebApi extends MBaseApi{
    void loadWeb(String url,String title);
    void removeWeb();
}
