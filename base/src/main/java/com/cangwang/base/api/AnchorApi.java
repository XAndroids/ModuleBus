package com.cangwang.base.api;

import android.support.v4.app.FragmentActivity;

import com.cangwang.core.MBaseApi;

public interface AnchorApi extends MBaseApi{
    void showAnchor(FragmentActivity context, String user, String url);
}
