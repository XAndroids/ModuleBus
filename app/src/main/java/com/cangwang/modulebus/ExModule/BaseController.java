package com.cangwang.modulebus.ExModule;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

//FIXME 没有作用
public class BaseController implements IView{
    private Context context;

    public void onAttach(Activity activity){
        context= activity;
    }

    public void create(Bundle saveInstanceState, Activity activity,ViewGroup container){

    }

    @Override
    public View getView() {
        return null;
    }

    @Override
    public void hide() {

    }

    @Override
    public void show() {

    }
}
