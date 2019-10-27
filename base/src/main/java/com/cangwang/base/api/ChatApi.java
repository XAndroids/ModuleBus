package com.cangwang.base.api;

import com.cangwang.core.MBaseApi;

public interface ChatApi extends MBaseApi{
    boolean addChatMsg(String user,String text);
}
