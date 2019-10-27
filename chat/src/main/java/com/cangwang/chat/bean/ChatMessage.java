package com.cangwang.chat.bean;

import com.cangwang.base.util.ColorUtil;
import com.cangwang.annotation.ModuleBean;

/**
 * 发言条目
 */
@ModuleBean
public class ChatMessage {
    public String user;
    public String text;
    public int color;

    public ChatMessage(String user,String text){
        this.user = user;
        this.text = text;
        color = ColorUtil.getRandomColor();
    }
}
